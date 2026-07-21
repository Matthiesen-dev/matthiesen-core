package dev.matthiesen.matthiesen_core.common.api.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * The CommonLoaderEventsListeners interface provides a unified way to register event listeners for various game events across
 * different mod loaders. It allows developers to handle player interactions, server lifecycle events, and other game-related
 * events in a consistent manner, regardless of the underlying mod loader being used.
 */
public interface CommonLoaderEventsListeners {
    /**
     * Called when a player uses an item. This method allows you to register a listener that will be called when a player interacts with an item.
     */
    @FunctionalInterface
    interface PlayerUseItemResultListener {
        /**
         * Called when a player uses an item. This method allows you to handle the result of the player's interaction with an item.
         * @param player The player who is using the item.
         * @param level The level in which the interaction is taking place.
         * @param interactionHand The hand in which the player is holding the item.
         * @return The result of the interaction.
         */
        InteractionResult onPlayerUseItemResult(ServerPlayer player, Level level, InteractionHand interactionHand);
    }

    /**
     * Called when a player uses an item on a block. This method allows you to handle the result of the player's interaction with a block using an item.
     */
    @FunctionalInterface
    interface PlayerUseBlockResultListener {
        /**
         * Called when a player uses an item on a block. This method allows you to handle the result of the player's interaction with a block using an item.
         * @param player The player who is using the item.
         * @param level The level in which the interaction is taking place.
         * @param hand The hand in which the player is holding the item.
         * @param pos The position of the block being interacted with.
         * @return The result of the interaction.
         */
        InteractionResult onPlayerUseBlockResult(ServerPlayer player, Level level, InteractionHand hand, BlockPos pos);
    }

    /**
     * Called when the server is starting. This method allows you to register a consumer that will be called when the server
     * starts, providing access to the MinecraftServer instance.
     * @param serverConsumer The consumer that will be called when the server is starting. It receives the MinecraftServer
     *                       instance, allowing you to perform initialization or setup tasks before the server fully starts.
     */
    void onServerStarting(Consumer<MinecraftServer> serverConsumer);

    /**
     * Called when the server is stopping. This method allows you to register a consumer that will be called when the server
     * is shutting down, providing access to the MinecraftServer instance.
     * @param serverConsumer The consumer that will be called when the server is stopping. It receives the MinecraftServer
     *                       instance, allowing you to perform cleanup or save operations before the server shuts down.
     */
    void onServerStopping(Consumer<MinecraftServer> serverConsumer);

    /**
     * Called when the server starts a tick. This method allows you to register a consumer that will be called at the start
     * of each server tick, providing access to the MinecraftServer instance.
     * @param serverConsumer The consumer that will be called at the start of each server tick. It receives the MinecraftServer
     *                       instance, allowing you to perform actions or checks at the start of each tick.
     */
    void onServerStartTick(Consumer<MinecraftServer> serverConsumer);

    /**
     * Called when the server ends a tick. This method allows you to register a consumer that will be called at the end of
     * each server tick, providing access to the MinecraftServer instance.
     * @param serverConsumer The consumer that will be called at the end of each server tick. It receives the MinecraftServer
     *                       instance, allowing you to perform actions or checks at the end of each tick.
     */
    void onServerEndTick(Consumer<MinecraftServer> serverConsumer);

    /**
     * Called when the server is reloaded. This method allows you to register a runnable that will be executed when the server
     * reloads its data packs or configuration.
     * @param runnable The runnable that will be executed when the server reloads. This can be used to perform custom actions
     *                 or reinitialize certain aspects of your mod when the server reloads.
     */
    void onServerReload(Runnable runnable);

    /**
     * Called when a player joins the game. This method allows for custom handling of player connection events, such as
     * sending welcome messages or initializing player data.
     * @param playerConsumer The consumer that will be called when a player joins the game. It receives the ServerPlayer
     *                       instance representing the player who joined.
     */
    void onPlayerJoin(Consumer<ServerPlayer> playerConsumer);

    /**
     * Called when a player leaves the game. This method allows for custom handling of player disconnection events, such
     * as saving player data or performing cleanup tasks.
     * @param playerConsumer The consumer that will be called when a player leaves the game. It receives the ServerPlayer
     *                       instance representing the player who left.
     */
    void onPlayerLeave(Consumer<ServerPlayer> playerConsumer);

    /**
     * Called when a player uses an item. This event allows you to handle the result of the player's interaction with an item.
     * @param listener The listener that will be called when a player uses an item. It should return an InteractionResult
     *                 indicating the outcome of the interaction.
     */
    void onPlayerUseItemResult(PlayerUseItemResultListener listener);

    /**
     * Called when a player uses an item on a block. This event allows you to handle the result of the player's
     * interaction with a block using an item.
     * @param listener The listener that will be called when a player uses an item on a block. It should return an
     *                 InteractionResult indicating the outcome of the interaction.
     */
    void onPlayerUseBlockResult(PlayerUseBlockResultListener listener);
}
