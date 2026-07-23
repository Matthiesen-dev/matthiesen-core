package dev.matthiesen.matthiesen_core.common.api.platform.services;

import dev.matthiesen.matthiesen_core.common.api.client.block_outline.BlockOutlineListener;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.ScreenRegistrar;
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
     * Registers HUD layers for the mod. This method allows the mod to define custom HUD elements that will be rendered on the player's screen.
     * The provided HudRegistrar is used to register these HUD layers, enabling the mod to display additional information or visual elements during
     * gameplay. The registrar is registered with the mod's unique identifier (MOD_ID) to associate it with this specific mod, ensuring that the
     * HUD layers are properly integrated into the Minecraft client environment.
     * @param registrar The HudRegistrar used to register custom HUD layers for the mod. This registrar allows the mod to define and manage HUD elements,
     *                  enabling the display of additional information or visual elements on the player's screen during gameplay.
     */
    void applyHudRegistrations(Consumer<HudRegistrar> registrar);

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
     * Registers block highlight overrides for the mod. This method allows the mod to customize the appearance of block outlines in the game. The provided
     * BlockOutlineListener is used to define how block outlines should be rendered, enabling features such as custom colors, shapes, or effects for specific
     * blocks. The listener is registered with the mod's unique identifier (MOD_ID) to associate it with this specific mod, ensuring that the customizations
     * are applied correctly within the Minecraft client environment.
     * @param registrar The BlockOutlineListener used to define custom block outline rendering behavior for the mod. This listener allows the mod to specify
     *                  how block outlines should be displayed, enabling visual enhancements or modifications to the default block highlighting behavior in the game.
     */
    void applyBlockHighlightOverrides(BlockOutlineListener registrar);
}
