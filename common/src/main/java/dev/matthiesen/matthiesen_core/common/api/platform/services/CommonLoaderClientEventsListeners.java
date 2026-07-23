package dev.matthiesen.matthiesen_core.common.api.platform.services;

import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.ScreenRegistrar;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The CommonLoaderClientEventsListeners interface defines a set of methods for registering client-side event listeners and handlers
 * in a modding environment. It provides mechanisms for executing code during specific client events, such as when the client is
 * stopping, at the end of each client tick, and for registering custom screens, entity renderers, HUD layers, key bindings, and
 * block highlight overrides. This interface allows mod developers to integrate their custom functionalities seamlessly into the
 * Minecraft client environment by providing hooks for various client-side events and registrations.
 */
public interface CommonLoaderClientEventsListeners {
    /**
     * Registers a runnable to be executed when the client is stopping. This method allows the mod to perform cleanup or finalization
     * tasks before the client shuts down. The provided Runnable will be called when the client is stopping, enabling the mod to release
     * resources, save data, or perform any necessary actions to ensure a smooth shutdown process.
     * @param runnable A Runnable that will be executed when the client is stopping. This allows the mod to perform cleanup or finalization
     *                 tasks before the client shuts down, ensuring that resources are released and data is saved properly.
     */
    void onClientStopping(Runnable runnable);

    /**
     * Registers a runnable to be executed at the end of each client tick. This method allows the mod to perform actions or updates that
     * need to occur after all other client-side processing for the tick has completed. The provided Runnable will be called at the end of
     * each client tick, enabling the mod to implement custom logic or behavior that should be executed in sync with the game's tick cycle.
     * @param runnable A Runnable that will be executed at the end of each client tick. This allows the mod to perform actions or updates
     *                 that need to occur after all other client-side processing for the tick has completed, enabling custom logic or behavior
     *                 to be implemented in sync with the game's tick cycle.
     */
    void endClientTick(Runnable runnable);

    /**
     * Registers a HUD render event callback to be invoked during each client frame.
     *
     * <p>The provided Consumer receives a {@link ClientEvent.HudRender}
     * event on each frame, allowing platform-specific HUD rendering to be emitted through {@link dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents}.</p>
     *
     * @param hudRenderEventConsumer A Consumer that receives HUD render events
     */
    void onHudRender(Consumer<ClientEvent.HudRender> hudRenderEventConsumer);

    /**
     * Registers screen registrations for the mod. This method allows the mod to define custom screens that will be displayed in the game.
     * The provided Consumer takes a ScreenRegistrar, which is used to register these screens. The registrar is registered with the mod's
     * unique identifier (MOD_ID) to associate it with this specific mod, ensuring that the screens are properly integrated into the Minecraft client environment.
     * @param screenRegistrarConsumer A Consumer that takes a ScreenRegistrar, which is used to register custom screens for the mod. The
     *                                registrar allows the mod to define and manage screens, enabling the display of custom user interfaces
     *                                within the Minecraft client environment.
     */
    void applyScreenRegistrations(Consumer<ScreenRegistrar> screenRegistrarConsumer);

    /**
     * Registers entity and block entity renderers for the mod. This method allows the mod to define custom renderers for entities and
     * block entities, enabling unique visual representations in the game. The provided BiConsumer takes two parameters: one for registering
     * entity renderers and another for registering block entity renderers. The mod can use these parameters to specify how different entities
     * and block entities should be rendered, allowing for enhanced visual customization and immersion within the Minecraft client environment.
     * @param entityRendererConsumer A BiConsumer that takes two parameters: one for registering entity renderers and another for registering
     *                               block entity renderers. The mod can use these parameters to define custom renderers for entities and block
     *                               entities, enabling unique visual representations in the game.
     */
    @SuppressWarnings("rawtypes")
    void applyEntityRendererRegistrations(BiConsumer<
            BiConsumer<EntityType<? extends Entity>, EntityRendererProvider>,
            BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider>
            > entityRendererConsumer);

    /**
     * Registers key bindings for the mod. This method allows the mod to define custom key bindings that players can use to interact with the mod's features.
     * The provided KeyMappingRegistrar is used to register the key bindings, enabling players to customize their controls and access mod-specific functionalities
     * through keyboard shortcuts. The registrar is registered with the mod's unique identifier (MOD_ID) to associate it with this specific mod, ensuring that the
     * key bindings are properly integrated into the Minecraft client environment.
     * @param registrar The KeyMappingRegistrar used to register custom key bindings for the mod. This registrar allows the mod to define and manage key bindings,
     *                  enabling players to customize their controls and access mod-specific features through keyboard shortcuts.
     */
    void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar);

    /**
     * Registers a block highlight override event callback to be invoked before block outlines are rendered.
     *
     * <p>The provided Consumer receives a {@link ClientEvent.BlockHighlight}
     * event and returns a boolean: {@code true} to continue with default rendering, or {@code false} to cancel it.
     * This allows platform-specific block highlight rendering to be emitted through {@link dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents}.</p>
     *
     * @param blockHighlightEventConsumer A Consumer that receives block highlight events and returns success/failure
     */
    void applyBlockHighlightOverrides(Consumer<ClientEvent.BlockHighlight> blockHighlightEventConsumer);
}
