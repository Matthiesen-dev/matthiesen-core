package dev.matthiesen.matthiesen_core.common.core.client;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.client.HudOrdering;
import dev.matthiesen.matthiesen_core.common.api.client.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.NeoForgeVanillaGuiLayers;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Manages custom HUD layer registrations in common code with cross-platform ordering support.
 *
 * <p>On loaders with native layer insertion, registrations are forwarded to platform APIs. On loaders without full
 * ordering APIs, this manager can resolve and render custom layers in a deterministic order.</p>
 */
@SuppressWarnings("unused")
public final class HudManager {
	private static final List<HudLayerRegistration> REGISTERED_HUD_LAYERS = new CopyOnWriteArrayList<>();
	private static final Set<ResourceLocation> WARNED_MISSING_ORDER_TARGETS = new HashSet<>();
	private static final List<ResourceLocation> VANILLA_LAYER_ORDER = List.of(
			NeoForgeVanillaGuiLayers.CAMERA_OVERLAYS,
			NeoForgeVanillaGuiLayers.CROSSHAIR,
			NeoForgeVanillaGuiLayers.HOTBAR,
			NeoForgeVanillaGuiLayers.JUMP_METER,
			NeoForgeVanillaGuiLayers.EXPERIENCE_BAR,
			NeoForgeVanillaGuiLayers.PLAYER_HEALTH,
			NeoForgeVanillaGuiLayers.ARMOR_LEVEL,
			NeoForgeVanillaGuiLayers.FOOD_LEVEL,
			NeoForgeVanillaGuiLayers.VEHICLE_HEALTH,
			NeoForgeVanillaGuiLayers.AIR_LEVEL,
			NeoForgeVanillaGuiLayers.SELECTED_ITEM_NAME,
			NeoForgeVanillaGuiLayers.SPECTATOR_TOOLTIP,
			NeoForgeVanillaGuiLayers.EXPERIENCE_LEVEL,
			NeoForgeVanillaGuiLayers.EFFECTS,
			NeoForgeVanillaGuiLayers.BOSS_OVERLAY,
			NeoForgeVanillaGuiLayers.SLEEP_OVERLAY,
			NeoForgeVanillaGuiLayers.DEMO_OVERLAY,
			NeoForgeVanillaGuiLayers.DEBUG_OVERLAY,
			NeoForgeVanillaGuiLayers.SCOREBOARD_SIDEBAR,
			NeoForgeVanillaGuiLayers.OVERLAY_MESSAGE,
			NeoForgeVanillaGuiLayers.TITLE,
			NeoForgeVanillaGuiLayers.CHAT,
			NeoForgeVanillaGuiLayers.TAB_LIST,
			NeoForgeVanillaGuiLayers.SUBTITLE_OVERLAY,
			NeoForgeVanillaGuiLayers.SAVING_INDICATOR
	);

	/**
	 * Shared singleton instance used by common client bootstrap.
	 */
	public static final HudManager INSTANCE = new HudManager();

	private volatile HudRegistrar activeRegistrar;
	private int appliedRegistrations;
	private boolean initialized;

	private HudManager() {}

	/**
	 * Installs the platform HUD registration callback once.
	 *
	 * @param clientEventsListeners platform event listener bridge
	 */
	public synchronized void initialize(CommonLoaderClientEventsListeners clientEventsListeners) {
		if (initialized) {
			return;
		}
		initialized = true;
		clientEventsListeners.applyHudRegistrations(this::applyHudLayerRegistrations);
		MatthiesenCoreCommon.INSTANCE.createInfoLog("Initialized HudManager");
	}

	/**
	 * Registers multiple HUD layers using a registrar callback.
	 *
	 * @param registrarConsumer callback that receives a HUD registrar helper
	 */
	public void registerHudLayers(Consumer<HudRegistrar> registrarConsumer) {
		registrarConsumer.accept(this::registerHudLayer);
	}

	/**
	 * Queues a HUD layer at the top of the stack.
	 *
	 * @param key unique id for the layer
	 * @param layer layer renderer implementation
	 */
	public void registerHudLayer(ResourceLocation key, LayeredDraw.Layer layer) {
		registerHudLayer(HudOrdering.AFTER, null, key, layer);
	}

	/**
	 * Queues a HUD layer with explicit ordering relative to another layer.
	 *
	 * @param ordering relative ordering mode
	 * @param other target layer id, or {@code null} for top/bottom insertion
	 * @param key unique id for the new layer
	 * @param layer layer renderer implementation
	 */
	public void registerHudLayer(HudOrdering ordering, @Nullable ResourceLocation other, ResourceLocation key, LayeredDraw.Layer layer) {
		registerHudLayerInternal(new HudLayerRegistration(ordering, other, key, layer));
	}

	/**
	 * Applies all queued HUD registrations to a platform registrar.
	 *
	 * @param registrar platform HUD registrar
	 */
	public synchronized void applyHudLayerRegistrations(HudRegistrar registrar) {
		activeRegistrar = registrar;
		for (int i = appliedRegistrations; i < REGISTERED_HUD_LAYERS.size(); i++) {
			REGISTERED_HUD_LAYERS.get(i).apply(registrar);
		}
		appliedRegistrations = REGISTERED_HUD_LAYERS.size();
	}

	/**
	 * Renders queued HUD layers using resolved cross-platform ordering.
	 *
	 * @param drawContext GUI graphics context
	 * @param tickCounter frame delta tracker
	 */
	public void renderHudLayers(GuiGraphics drawContext, DeltaTracker tickCounter) {
		for (HudLayerRegistration registration : resolveRenderOrder()) {
			try {
				registration.layer().render(drawContext, tickCounter);
			} catch (Throwable throwable) {
				MatthiesenCoreCommon.INSTANCE.createErrorLog("Exception while rendering HUD layer " + registration.key(), throwable);
			}
		}
	}

	private synchronized void registerHudLayerInternal(HudLayerRegistration registration) {
		for (HudLayerRegistration existingRegistration : REGISTERED_HUD_LAYERS) {
			if (existingRegistration.key().equals(registration.key())) {
				throw new IllegalArgumentException("Layer already registered: " + registration.key());
			}
		}

		REGISTERED_HUD_LAYERS.add(registration);

		if (activeRegistrar != null) {
			registration.apply(activeRegistrar);
			appliedRegistrations = REGISTERED_HUD_LAYERS.size();
		}
	}

	private List<HudLayerRegistration> resolveRenderOrder() {
		List<ResourceLocation> ids = new ArrayList<>(VANILLA_LAYER_ORDER);
		List<HudLayerRegistration> customLayers = new ArrayList<>();

		for (HudLayerRegistration registration : REGISTERED_HUD_LAYERS) {
			int insertPosition;

			if (registration.other() == null) {
				insertPosition = registration.ordering() == HudOrdering.BEFORE ? 0 : ids.size();
			} else {
				int otherIndex = ids.indexOf(registration.other());
				if (otherIndex < 0) {
					insertPosition = registration.ordering() == HudOrdering.BEFORE ? 0 : ids.size();
					warnMissingOrderTarget(registration.other());
				} else {
					insertPosition = otherIndex + (registration.ordering() == HudOrdering.BEFORE ? 0 : 1);
				}
			}

			ids.add(insertPosition, registration.key());
			customLayers.add(registration);
		}

		List<HudLayerRegistration> orderedLayers = new ArrayList<>();
		for (ResourceLocation id : ids) {
			for (HudLayerRegistration registration : customLayers) {
				if (registration.key().equals(id)) {
					orderedLayers.add(registration);
					break;
				}
			}
		}

		return orderedLayers;
	}

	private synchronized void warnMissingOrderTarget(ResourceLocation missingTarget) {
		if (WARNED_MISSING_ORDER_TARGETS.add(missingTarget)) {
			MatthiesenCoreCommon.INSTANCE.createErrorLog("HUD layer ordering target not found: " + missingTarget + ". Rendering at edge of stack instead.");
		}
	}

	private record HudLayerRegistration(
			HudOrdering ordering,
			@Nullable ResourceLocation other,
			ResourceLocation key,
			LayeredDraw.Layer layer
	) {
		void apply(HudRegistrar registrar) {
			registrar.register(ordering, other, key, layer);
		}
	}
}
