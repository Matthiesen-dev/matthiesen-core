package dev.matthiesen.matthiesen_core.common.api.events;

import dev.matthiesen.matthiesen_core.common.api.client.ResourcePackRegistrar;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackDef;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackActivationBehaviour;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudOrdering;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.hud.NeoForgeVanillaGuiLayers;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central registry of all client-side platform events provided by Matthiesen Core.
 */
@SuppressWarnings("unused")
public final class PlatformClientEvents {
    private static final List<HudLayerRegistration> REGISTERED_HUD_LAYERS = new CopyOnWriteArrayList<>();
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

    private static final List<ResourcePackDef> REGISTERED_RESOURCE_PACKS = new CopyOnWriteArrayList<>();

    private static volatile HudRegistrar activeRegistrar;
    private static boolean hudRegistrationEventDispatched;

    private static volatile ResourcePackRegistrar activeResourcePackRegistrar;
    private static boolean resourcePackRegistrationEventDispatched;

    private PlatformClientEvents() {}

    /**
     * Fired when the client is stopping.
     *
     * <p>Void event. Listeners are dispatched in priority order; exceptions are logged and suppressed.</p>
     */
    public static final EventObservable<ClientEvent.Stopping> CLIENT_STOPPING = new EventObservable<>();

    /**
     * Fired at the end of each client tick.
     *
     * <p>Void event. Listeners are dispatched in priority order; exceptions are logged and suppressed.</p>
     */
    public static final EventObservable<ClientEvent.EndTick> CLIENT_END_TICK = new EventObservable<>();

    /**
     * Fired when HUD layers should be registered with explicit ordering and resource IDs.
     */
    public static final EventObservable<ClientEvent.HudRegistration> HUD_REGISTRATION = new EventObservable<>();

    /**
     * Fired when a block outline highlight is about to be rendered.
     *
     * <p>Result-based event returning {@link InteractionResult}. First listener returning FAIL stops dispatch
     * and cancels rendering. All other listeners must return PASS. Exceptions are logged and suppressed.</p>
     */
    public static final ResultEventObservable<ClientEvent.BlockHighlight> BLOCK_HIGHLIGHT = new ResultEventObservable<>();

    /**
     * Fired when resource packs are ready to be registered.
     *
     * <p>Listeners should register their resource packs using the provided resource pack definition.</p>
     */
    public static final EventObservable<ClientEvent.ResourcePackRegistration> RESOURCE_PACK_REGISTRATION = new EventObservable<>();

    /**
     * Initializes the client-side event system by wiring platform-specific event callbacks to the observables.
     *
     * <p>Called once during client setup by {@link dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient#initialize()}.</p>
     */
    public static void initialize() {
    }

    /**
     * Registers a HUD layer above all others.
     */
    public static void registerHudLayer(ResourceLocation key, LayeredDraw.Layer layer) {
        registerHudLayer(HudOrdering.AFTER, null, key, layer);
    }

    /**
     * Registers a HUD layer with explicit ordering metadata.
     */
    public static void registerHudLayer(HudOrdering ordering, ResourceLocation other, ResourceLocation key, LayeredDraw.Layer layer) {
        registerHudLayerInternal(new HudLayerRegistration(ordering, other, key, layer));
    }

    /**
     * Registers a built-in resource pack using the resource-pack definition record.
     *
     * <p>If the platform registrar is already active, the pack is applied immediately.</p>
     *
     * @param resourcePackDef resource pack metadata
     */
    public static void registerResourcePack(ResourcePackDef resourcePackDef) {
        registerResourcePackInternal(Objects.requireNonNull(resourcePackDef, "resourcePackDef"));
    }

    /**
     * Registers a built-in resource pack using a literal display name.
     *
     * @param modId owning mod id
     * @param id unique pack id within the mod
     * @param displayName pack display name
     * @param activationBehaviour activation mode for the pack
     */
    public static void registerResourcePack(
            String modId,
            String id,
            String displayName,
            ResourcePackActivationBehaviour activationBehaviour
    ) {
        registerResourcePack(new ResourcePackDef(modId, id, displayName, activationBehaviour));
    }

    /**
     * Registers a built-in resource pack using a component display name.
     *
     * @param modId owning mod id
     * @param id unique pack id within the mod
     * @param displayName pack display name
     * @param activationBehaviour activation mode for the pack
     */
    public static void registerResourcePack(
            String modId,
            String id,
            Component displayName,
            ResourcePackActivationBehaviour activationBehaviour
    ) {
        registerResourcePackInternal(new ResourcePackDef(modId, id, displayName, activationBehaviour));
    }

    /**
     * Applies all registered HUD layers to the provided registrar. If the registrar is already active, all previously
     * registered HUD layers are applied immediately. If the registrar is not yet active, the HUD layers will be applied
     * when the registrar becomes active. This method is typically called during the HUD registration phase of the client
     * lifecycle to ensure that all registered HUD layers are properly integrated into the game.
     * @param registrar The HudRegistrar used to register the HUD layers. This registrar allows the mod to define and manage
     *                  HUD layers, enabling players to customize their game experience by adding or removing visual elements on the screen.
     */
    public static synchronized void applyHudLayerRegistrations(HudRegistrar registrar) {
        activeRegistrar = registrar;

        for (HudLayerRegistration registration : REGISTERED_HUD_LAYERS) {
            registration.apply(registrar);
        }

        if (!hudRegistrationEventDispatched) {
            hudRegistrationEventDispatched = true;
            HUD_REGISTRATION.emit(new ClientEvent.HudRegistration(PlatformClientEvents::registerHudLayer));
        }
    }

    /**
     * Applies all registered resource packs to the provided registrar. If the registrar is already active, all previously
     * registered resource packs are applied immediately.
     * If the registrar is not yet active, the resource packs will be applied when the registrar becomes active. This method
     * is typically called during the resource pack registration phase of the client lifecycle to ensure that all registered
     * resource packs are properly integrated into the game.
     * @param registrar The ResourcePackRegistrar used to register the resource packs. This registrar allows the mod to define
     *                  and manage resource packs, enabling players to customize their game experience by adding or removing content packs.
     */
    public static synchronized void applyResourcePackRegistrations(ResourcePackRegistrar registrar) {
        activeResourcePackRegistrar = registrar;

        for (ResourcePackDef resourcePackDef : REGISTERED_RESOURCE_PACKS) {
            registrar.register(resourcePackDef);
        }

        if (!resourcePackRegistrationEventDispatched) {
            resourcePackRegistrationEventDispatched = true;
            RESOURCE_PACK_REGISTRATION.emit(new ClientEvent.ResourcePackRegistration(registrar));
        }
    }

    private static synchronized void registerResourcePackInternal(ResourcePackDef resourcePackDef) {
        for (ResourcePackDef existingResourcePack : REGISTERED_RESOURCE_PACKS) {
            if (existingResourcePack.modId().equals(resourcePackDef.modId()) && existingResourcePack.id().equals(resourcePackDef.id())) {
                throw new IllegalArgumentException("Resource pack already registered: " + resourcePackDef.modId() + ":" + resourcePackDef.id());
            }
        }

        REGISTERED_RESOURCE_PACKS.add(resourcePackDef);

        if (activeResourcePackRegistrar != null) {
            activeResourcePackRegistrar.register(resourcePackDef);
        }
    }

    /**
     * Emits a block highlight event to all registered listeners, allowing them to modify or cancel the rendering of the block outline.
     * @param event The BlockHighlight event containing the context for the block outline rendering.
     * @return The InteractionResult returned by the listeners, indicating whether to continue rendering (PASS) or cancel rendering (FAIL).
     */
    public static InteractionResult emitBlockHighlight(ClientEvent.BlockHighlight event) {
        return BLOCK_HIGHLIGHT.emit(event);
    }

    private static synchronized void registerHudLayerInternal(HudLayerRegistration registration) {
        for (HudLayerRegistration existingRegistration : REGISTERED_HUD_LAYERS) {
            if (existingRegistration.key().equals(registration.key())) {
                throw new IllegalArgumentException("Layer already registered: " + registration.key());
            }
        }

        REGISTERED_HUD_LAYERS.add(registration);

        if (activeRegistrar != null) {
            registration.apply(activeRegistrar);
        }
    }

    /**
     * Renders all registered HUD layers in the correct order.
     * (Used internally by the Fabric platform to render HUD layers)
     * @param drawContext the GUI graphics context for rendering
     * @param tickCounter the frame delta tracker for animation
     */
    public static void renderHudLayers(GuiGraphics drawContext, DeltaTracker tickCounter) {
        for (HudLayerRegistration registration : resolveRenderOrder()) {
            try {
                registration.layer().render(drawContext, tickCounter);
            } catch (Throwable ignored) {
                // The observable already handles listener-level failures; keep rendering other layers.
            }
        }
    }

    private static List<HudLayerRegistration> resolveRenderOrder() {
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

    private record HudLayerRegistration(
            HudOrdering ordering,
            ResourceLocation other,
            ResourceLocation key,
            LayeredDraw.Layer layer
    ) {
        void apply(HudRegistrar registrar) {
            registrar.register(ordering, other, key, layer);
        }
    }
}
