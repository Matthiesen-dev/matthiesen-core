package dev.matthiesen.matthiesen_core.fabric.events;

import dev.matthiesen.matthiesen_core.common.api.client.BlockOutlineContext;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import dev.matthiesen.matthiesen_core.fabric.MatthiesenCoreFabricClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;

/**
 * The PlatformClientEventsBusListener class is responsible for wiring Fabric-specific loader callbacks into the common event observables.
 */
public final class PlatformClientEventsBusListener {
    /**
     * Wires Fabric-specific loader callbacks into the common event observables.
     */
    public static void initialize() {
        // Run common setup first
        MatthiesenCoreFabricClient.INSTANCE.onClientSetup();

        // ========================================================
        // Lifecycle events
        // ========================================================

        ClientLifecycleEvents.CLIENT_STOPPING.register(client ->
                PlatformClientEvents.CLIENT_STOPPING.emit(new ClientEvent.Stopping()));

        // ========================================================
        // Tick events
        // ========================================================

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                PlatformClientEvents.CLIENT_END_TICK.emit(new ClientEvent.EndTick());
            }
        });

        // ========================================================
        // Rendering events
        // ========================================================

        HudRenderCallback.EVENT.register(PlatformClientEvents::renderHudLayers);

        WorldRenderEvents.BLOCK_OUTLINE.register((worldContext, hitResult) -> {
            if (!(hitResult instanceof BlockHitResult blockHitResult)) {
                return true;
            }

            BlockOutlineContext context = new BlockOutlineContext(
                    worldContext.world(),
                    blockHitResult,
                    worldContext.matrixStack(),
                    worldContext.camera(),
                    worldContext.consumers()
            );

            InteractionResult result = PlatformClientEvents.emitBlockHighlight(new ClientEvent.BlockHighlight(context));
            // Fabric callback uses false to cancel outline rendering.
            return result != InteractionResult.FAIL;
        });

        // ========================================================
        // Built-in Resource Pack additions
        // ========================================================

        PlatformClientEvents.applyResourcePackRegistrations((packDef) -> {
            var modContainer = FabricLoader.getInstance().getModContainer(packDef.modId())
                    .orElseThrow(() -> new IllegalArgumentException("Mod ID " + packDef.modId() + " not found in Fabric mod list."));

            var activationType = switch (packDef.activationBehaviour()) {
                case NORMAL -> ResourcePackActivationType.NORMAL;
                case DEFAULT_ENABLED -> ResourcePackActivationType.DEFAULT_ENABLED;
                case ALWAYS_ENABLED -> ResourcePackActivationType.ALWAYS_ENABLED;
            };

            var packId = ResourceLocation.fromNamespaceAndPath(packDef.modId(), packDef.id());
            boolean registered = ResourceManagerHelper.registerBuiltinResourcePack(
                    packId,
                    modContainer,
                    packDef.displayName(),
                    activationType
            );

            if (!registered) {
                throw new IllegalStateException("Failed to register built-in resource pack " + packId + " for mod " + packDef.modId());
            }
        });
    }
}
