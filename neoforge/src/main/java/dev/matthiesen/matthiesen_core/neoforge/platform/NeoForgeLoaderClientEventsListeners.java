package dev.matthiesen.matthiesen_core.neoforge.platform;

import dev.matthiesen.matthiesen_core.common.api.client.ResourcePackRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.ScreenRegistrar;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import dev.matthiesen.matthiesen_core.neoforge.platform.helpers.NeoForgeClientRegistryHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The NeoForgeLoaderClientEventsListeners class implements the CommonLoaderClientEventsListeners interface and provides
 * client-side event handling functionalities for the NeoForge mod loader. It allows for the registration of various client-side
 * components such as screens, entity renderers, HUD elements, and key bindings, as well as handling client tick events and block highlight overrides.
 */
public final class NeoForgeLoaderClientEventsListeners implements CommonLoaderClientEventsListeners {
    /**
     * A list of Runnables that will be executed at the end of each client tick. This allows for scheduling tasks that need to be performed after the main client tick processing is complete.
     */
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
    public void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar) {
        NeoForgeClientRegistryHelper.applyKeyBindingRegistrations(registrar);
    }

    @Override
    public void applyHudRegistrations(Consumer<HudRegistrar> hudRegistrarConsumer) {
        NeoForgeClientRegistryHelper.applyHudRegistrations(hudRegistrarConsumer);
    }

    @Override
    public void applyBlockHighlightOverrides(Function<ClientEvent.BlockHighlight, InteractionResult> blockHighlightEventHandler) {
        NeoForgeClientRegistryHelper.applyBlockHighlightOverrides(blockHighlightEventHandler);
    }

    @Override
    public void applyResourcePackRegistrations(Consumer<ResourcePackRegistrar> resourcePackRegistrarConsumer) {
        NeoForgeClientRegistryHelper.applyResourcePackRegistrations(resourcePackRegistrarConsumer);
    }
}
