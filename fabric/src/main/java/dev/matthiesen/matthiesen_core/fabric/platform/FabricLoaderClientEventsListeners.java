package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.client.*;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The FabricLoaderClientEventsListeners class implements the CommonLoaderClientEventsListeners interface and provides client-side event handling for the Fabric mod loader.
 */
public final class FabricLoaderClientEventsListeners implements CommonLoaderClientEventsListeners {
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
    public void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar) {
        registrar.accept(KeyBindingHelper::registerKeyBinding);
    }
}
