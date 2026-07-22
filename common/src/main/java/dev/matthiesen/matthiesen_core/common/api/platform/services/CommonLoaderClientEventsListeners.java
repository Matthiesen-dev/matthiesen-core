package dev.matthiesen.matthiesen_core.common.api.platform.services;

import dev.matthiesen.matthiesen_core.common.api.client.BlockOutlineListener;
import dev.matthiesen.matthiesen_core.common.api.client.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.ScreenRegistrar;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface CommonLoaderClientEventsListeners {
    void onClientStopping(Runnable runnable);
    void endClientTick(Runnable runnable);

    void applyScreenRegistrations(Consumer<ScreenRegistrar> screenRegistrarConsumer);
    @SuppressWarnings("rawtypes")
    void applyEntityRendererRegistrations(BiConsumer<
            BiConsumer<EntityType<? extends Entity>, EntityRendererProvider>,
            BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider>
            > entityRendererConsumer);
    void applyHudRegistrations(Consumer<HudRegistrar> registrar);
    void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar);
    void applyBlockHighlightOverrides(BlockOutlineListener registrar);
}
