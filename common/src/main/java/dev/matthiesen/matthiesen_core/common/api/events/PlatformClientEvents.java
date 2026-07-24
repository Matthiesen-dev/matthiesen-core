package dev.matthiesen.matthiesen_core.common.api.events;

import dev.matthiesen.matthiesen_core.common.api.client.ResourcePackRegistrar;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackDef;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackActivationBehaviour;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
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
     *
     * @param loader the platform-specific client event listener bridge
     */
    public static void initialize(CommonLoaderClientEventsListeners loader) {
        loader.onClientStopping(() -> CLIENT_STOPPING.emit(new ClientEvent.Stopping()));
        loader.endClientTick(() -> CLIENT_END_TICK.emit(new ClientEvent.EndTick()));
        loader.applyHudRegistrations(PlatformClientEvents::applyHudLayerRegistrations);
        loader.applyBlockHighlightOverrides(PlatformClientEvents::emitBlockHighlight);
        loader.applyResourcePackRegistrations(PlatformClientEvents::applyResourcePackRegistrations);
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

    private static synchronized void applyHudLayerRegistrations(HudRegistrar registrar) {
        activeRegistrar = registrar;

        for (HudLayerRegistration registration : REGISTERED_HUD_LAYERS) {
            registration.apply(registrar);
        }

        if (!hudRegistrationEventDispatched) {
            hudRegistrationEventDispatched = true;
            HUD_REGISTRATION.emit(new ClientEvent.HudRegistration(PlatformClientEvents::registerHudLayer));
        }
    }

    private static synchronized void applyResourcePackRegistrations(ResourcePackRegistrar registrar) {
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

    private static InteractionResult emitBlockHighlight(ClientEvent.BlockHighlight event) {
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
