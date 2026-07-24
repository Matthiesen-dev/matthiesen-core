package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.client.*;
import dev.matthiesen.matthiesen_core.common.api.client.BlockOutlineContext;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The FabricLoaderClientEventsListeners class implements the CommonLoaderClientEventsListeners interface and provides client-side event handling for the Fabric mod loader.
 */
public final class FabricLoaderClientEventsListeners implements CommonLoaderClientEventsListeners {
    @Override
    public void onClientStopping(Runnable runnable) {
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> runnable.run());
    }

    @Override
    public void endClientTick(Runnable runnable) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                runnable.run();
            }
        });
    }

    @Override
    public void applyScreenRegistrations(Consumer<ScreenRegistrar> screenRegistrarConsumer) {
        screenRegistrarConsumer.accept(MenuScreens::register);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void applyEntityRendererRegistrations(BiConsumer<BiConsumer<EntityType<? extends Entity>, EntityRendererProvider>, BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider>> entityRendererConsumer) {
        entityRendererConsumer.accept(EntityRendererRegistry::register, BlockEntityRenderers::register);
    }

    @Override
    public void applyHudRegistrations(Consumer<HudRegistrar> hudRegistrarConsumer) {
        HudRenderCallback.EVENT.register(PlatformClientEvents::renderHudLayers);
    }

    @Override
    public void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar) {
        registrar.accept(KeyBindingHelper::registerKeyBinding);
    }

    @Override
    public void applyBlockHighlightOverrides(Function<ClientEvent.BlockHighlight, InteractionResult> blockHighlightEventHandler) {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((worldContext, hitResult) -> {
            if (!(hitResult instanceof BlockHitResult blockHitResult)) {
                return false;
            }

            BlockOutlineContext context = new BlockOutlineContext(
                    worldContext.world(),
                    blockHitResult,
                    worldContext.matrixStack(),
                    worldContext.camera(),
                    worldContext.consumers()
            );

            InteractionResult result = blockHighlightEventHandler.apply(new ClientEvent.BlockHighlight(context));
            // Fabric callback uses true to cancel outline rendering.
            return result == InteractionResult.FAIL;
        });
    }

    @Override
    public void applyResourcePackRegistrations(Consumer<ResourcePackRegistrar> resourcePackRegistrarConsumer) {
        resourcePackRegistrarConsumer.accept((packDef) -> {
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
