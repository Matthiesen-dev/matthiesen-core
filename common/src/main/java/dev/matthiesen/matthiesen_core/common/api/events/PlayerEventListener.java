package dev.matthiesen.matthiesen_core.common.api.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

/**
 * The PlayerEventListener interface provides a set of methods that can be implemented to listen for and handle various
 * player-related events in the game. This interface allows developers to create custom behavior when players join or leave
 * the game, use items, or interact with blocks. By implementing this interface, developers can extend the functionality of
 * the game and respond to player actions in a flexible manner.
 */
@SuppressWarnings("unused")
public interface PlayerEventListener {
    /**
     * Called when a player joins the game. This method allows for custom handling of player connection events, such as
     * initializing player data, sending welcome messages, or triggering specific actions when a player joins the game.
     * @param player The player who is joining the game. This parameter provides access to the player's state, inventory,
     *               and other relevant information that may be needed for handling the connection event.
     */
    default void onPlayerJoin(ServerPlayer player) {}

    /**
     * Called when a player leaves the game. This method allows for custom handling of player disconnection events, such
     * as saving player data, cleaning up resources, or triggering specific actions when a player leaves the game.
     * @param player The player who is leaving the game. This parameter provides access to the player's state, inventory,
     *               and other relevant information that may be needed for handling the disconnection event.
     */
    default void onPlayerLeave(ServerPlayer player) {}

    /**
     * Called when a player uses an item in the game world. This method allows for custom handling of item usage, such as
     * triggering specific actions or modifying the interaction result based on the player's state or the item's properties.
     * @param player The player who is using the item. This parameter provides access to the player's state, inventory, and
     *               other relevant information that may influence the item usage.
     * @param level The game world in which the item usage is taking place. This parameter allows for querying the state of
     *              the world, such as checking block types, properties, and other environmental factors that may affect the item usage.
     * @param interactionHand The hand (main-hand or off-hand) that the player is using to interact with the item. This parameter
     *                        can be used to differentiate between different types of item usage based on which hand is being used.
     * @return The result of the item usage interaction, which can be used to determine whether the interaction was successful, failed, or should be passed to other handlers.
     */
    default InteractionResult onPlayerUseItemResult(ServerPlayer player, Level level, InteractionHand interactionHand) {
        return InteractionResult.PASS;
    }

    /**
     * Called when a player interacts with a block in the game world. This method allows for custom handling of block interactions,
     * such as triggering specific actions or modifying the interaction result based on the player's state or the block's properties.
     * @param player The player who is interacting with the block. This parameter provides access to the player's state, inventory,
     *               and other relevant information that may influence the interaction.
     * @param level The game world in which the interaction is taking place. This parameter allows for querying the state of the world,
     *              such as checking block types, properties, and other environmental factors that may affect the interaction.
     * @param hand The hand (main-hand or off-hand) that the player is using to interact with the block. This parameter can be used to
     *             differentiate between different types of interactions based on which hand is being used.
     * @param pos The position of the block being interacted with. This parameter provides the exact location of the block in the game
     *            world, allowing for precise handling of interactions based on the block's position and surrounding context.
     * @return The result of the interaction, which can be used to determine whether the interaction was successful, failed, or should be passed to other handlers.
     */
    default InteractionResult onPlayerUseBlockResult(ServerPlayer player, Level level, InteractionHand hand, BlockPos pos) {
        return InteractionResult.PASS;
    }
}
