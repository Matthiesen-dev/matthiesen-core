package dev.matthiesen.matthiesen_core.neoforge.platform.helpers;

import dev.matthiesen.matthiesen_core.common.api.client.*;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The NeoForgeClientRegistryHelper class provides utility methods for registering client-side components such as screens,
 * entity renderers, HUD elements, and key bindings in the NeoForge mod loader environment. It ensures that these registrations
 * are performed at the appropriate time during the mod loading process by utilizing the event bus system provided by NeoForge.
 */
public final class NeoForgeClientRegistryHelper {
    private static volatile IEventBus modBus;

    private NeoForgeClientRegistryHelper() {}

    /**
     * Initializes the NeoForgeClientRegistryHelper with the provided event bus. This method must be called before any client-side registration methods are invoked to ensure that the event bus is available for handling registration events.
     * @param eventBus The event bus to be used for client-side registrations.
     */
    public static void init(IEventBus eventBus) {
        modBus = eventBus;
    }

    /**
     * Applies screen registrations by accepting a consumer that registers screens with the provided ScreenRegistrar.
     * This method listens for the RegisterMenuScreensEvent and invokes the consumer to perform the screen registrations.
     *
     * @param screenRegistrarConsumer A consumer that accepts a ScreenRegistrar for registering screens.
     * @throws IllegalStateException if the NeoForgeClientRegistryHelper has not been initialized with an event bus.
     */
    public static void applyScreenRegistrations(Consumer<ScreenRegistrar> screenRegistrarConsumer) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeClientRegistryHelper has not been initialized.");
        }
        eventBus.addListener(EventPriority.LOWEST, (RegisterMenuScreensEvent event) ->
                screenRegistrarConsumer.accept(event::register));
    }

    /**
     * Applies entity renderer registrations by accepting a bi-consumer that registers entity and block entity renderers.
     * This method listens for the EntityRenderersEvent.RegisterRenderers event and invokes the bi-consumer to perform the renderer registrations.
     *
     * @param entityRendererConsumer A bi-consumer that accepts two consumers: one for registering entity renderers and another for registering block entity renderers.
     * @throws IllegalStateException if the NeoForgeClientRegistryHelper has not been initialized with an event bus.
     */
    @SuppressWarnings("rawtypes")
    public static void applyEntityRendererRegistrations(BiConsumer<
            BiConsumer<EntityType<? extends Entity>, EntityRendererProvider>,
            BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider>
            > entityRendererConsumer) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeClientRegistryHelper has not been initialized.");
        }
        eventBus.addListener(EventPriority.LOWEST, (EntityRenderersEvent.RegisterRenderers event) ->
                entityRendererConsumer.accept(event::registerEntityRenderer, event::registerBlockEntityRenderer));
    }

    /**
     * Applies key binding registrations by accepting a consumer that registers key mappings with the provided KeyMappingRegistrar.
     * This method listens for the RegisterKeyMappingsEvent and invokes the consumer to perform the key binding registrations.
     *
     * @param registrar A consumer that accepts a KeyMappingRegistrar for registering key bindings.
     * @throws IllegalStateException if the NeoForgeClientRegistryHelper has not been initialized with an event bus.
     */
    public static void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeClientRegistryHelper has not been initialized.");
        }
        eventBus.addListener(EventPriority.LOWEST, (RegisterKeyMappingsEvent event) ->
                registrar.accept(event::register));
    }
}
