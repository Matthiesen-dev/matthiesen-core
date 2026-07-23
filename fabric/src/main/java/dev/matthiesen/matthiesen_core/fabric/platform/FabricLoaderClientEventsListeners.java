package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.client.*;
import dev.matthiesen.matthiesen_core.common.api.client.block_outline.BlockOutlineContext;
import dev.matthiesen.matthiesen_core.common.api.client.block_outline.BlockOutlineListener;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import dev.matthiesen.matthiesen_core.common.core.client.HudManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    public void applyHudRegistrations(Consumer<HudRegistrar> registrar) {
        HudRenderCallback.EVENT.register(HudManager.INSTANCE::renderHudLayers);
        // Fabric does not expose native layer insertion by id, so common HudManager handles ordering and rendering.
        registrar.accept((ordering, other, key, layer) -> {});
    }

    @Override
    public void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar) {
        registrar.accept(KeyBindingHelper::registerKeyBinding);
    }

    @Override
    public void applyBlockHighlightOverrides(BlockOutlineListener listener) {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((worldContext, hitResult) -> {
            if (!(hitResult instanceof BlockHitResult blockHitResult)) return true;
            BlockOutlineContext context = new BlockOutlineContext(
                    worldContext.world(),
                    blockHitResult,
                    worldContext.matrixStack(),
                    worldContext.camera(),
                    worldContext.consumers()
            );
            return listener.onBlockOutline(context);
        });
    }
}
