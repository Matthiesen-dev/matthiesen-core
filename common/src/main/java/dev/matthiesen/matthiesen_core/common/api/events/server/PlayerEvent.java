package dev.matthiesen.matthiesen_core.common.api.events.server;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.ResultEventObservable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

/**
 * Typed event records for server-side player events used with {@link PlatformEvents}.
 *
 * <p>Interaction events ({@link UseItem}, {@link UseBlock}) are dispatched through a
 * {@link ResultEventObservable}, allowing any listener to return {@link InteractionResult#FAIL}
 * to immediately cancel the action.</p>
 *
 * @see PlatformEvents#PLAYER_JOIN
 * @see PlatformEvents#PLAYER_LEAVE
 * @see PlatformEvents#PLAYER_USE_ITEM
 * @see PlatformEvents#PLAYER_USE_BLOCK
 */
@SuppressWarnings("unused")
public final class PlayerEvent {

    private PlayerEvent() {}

    /**
     * Fired when a player successfully joins the server.
     *
     * @param player the {@link ServerPlayer} who joined
     */
    public record Join(ServerPlayer player) {}

    /**
     * Fired when a player disconnects from the server for any reason.
     *
     * @param player the {@link ServerPlayer} who left
     */
    public record Leave(ServerPlayer player) {}

    /**
     * Fired when a server-side player uses an item. Subscribed via
     * {@link PlatformEvents#PLAYER_USE_ITEM} ({@link ResultEventObservable}).
     *
     * <p>Return {@link InteractionResult#FAIL} from your listener to cancel the action
     * and stop further listeners from being called.</p>
     *
     * @param player the {@link ServerPlayer} using the item
     * @param level  the level in which the interaction is taking place
     * @param hand   the hand being used ({@link InteractionHand#MAIN_HAND} or {@link InteractionHand#OFF_HAND})
     */
    public record UseItem(ServerPlayer player, Level level, InteractionHand hand) {}

    /**
     * Fired when a server-side player right-clicks a block. Subscribed via
     * {@link PlatformEvents#PLAYER_USE_BLOCK} ({@link ResultEventObservable}).
     *
     * <p>Return {@link InteractionResult#FAIL} from your listener to cancel the action
     * and stop further listeners from being called.</p>
     *
     * @param player the {@link ServerPlayer} interacting with the block
     * @param level  the level in which the interaction is taking place
     * @param hand   the hand being used
     * @param pos    the position of the block being interacted with
     */
    public record UseBlock(ServerPlayer player, Level level, InteractionHand hand, BlockPos pos) {}
}

