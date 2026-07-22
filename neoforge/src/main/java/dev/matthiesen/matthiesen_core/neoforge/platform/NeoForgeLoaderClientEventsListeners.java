package dev.matthiesen.matthiesen_core.neoforge.platform;

import dev.matthiesen.matthiesen_core.common.api.client.BlockOutlineListener;
import dev.matthiesen.matthiesen_core.common.api.client.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.ScreenRegistrar;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import dev.matthiesen.matthiesen_core.neoforge.platform.helpers.NeoForgeClientRegistryHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class NeoForgeLoaderClientEventsListeners implements CommonLoaderClientEventsListeners {
    public static final List<Runnable> END_CLIENT_TICK_RUNNABLES = new ArrayList<>();

    @Override
    public void onClientStopping(Runnable runnable) {

    }

    @Override
    public void endClientTick(Runnable runnable) {
        END_CLIENT_TICK_RUNNABLES.add(runnable);
    }

    @Override
    public void applyScreenRegistrations(Consumer<ScreenRegistrar> screenRegistrarConsumer) {
        NeoForgeClientRegistryHelper.applyScreenRegistrations(screenRegistrarConsumer);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void applyEntityRendererRegistrations(BiConsumer<BiConsumer<EntityType<? extends Entity>, EntityRendererProvider>, BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider>> entityRendererConsumer) {
        NeoForgeClientRegistryHelper.applyEntityRendererRegistrations(entityRendererConsumer);
    }

    @Override
    public void applyHudRegistrations(Consumer<HudRegistrar> registrar) {
        NeoForgeClientRegistryHelper.applyHudRegistrations(registrar);
    }

    @Override
    public void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar) {
        NeoForgeClientRegistryHelper.applyKeyBindingRegistrations(registrar);
    }

    @Override
    public void applyBlockHighlightOverrides(BlockOutlineListener listener) {
        NeoForgeClientRegistryHelper.applyBlockHighlightOverrides(listener);
    }
}
