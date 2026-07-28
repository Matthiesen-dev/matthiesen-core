package dev.matthiesen.matthiesen_core.common.api.events.definitions;

import dev.matthiesen.matthiesen_core.common.utility.player_data.ServerUser;
import net.minecraft.server.level.ServerPlayer;

/**
 * An interface that defines a contract for handling player-related events on the server side. Implementing classes must
 * provide a method to retrieve the associated {@link ServerPlayer} instance. Additionally, this interface provides a default
 * method to convert the {@link ServerPlayer} to a {@link ServerUser} for easier access to player data.
 */
@SuppressWarnings("unused")
public interface PlayerEventHandlerDef {
    /**
     * Returns the {@link ServerPlayer} associated with this event.
     * @return the {@link ServerPlayer} associated with this event
     */
    ServerPlayer player();

    /**
     * Converts the {@link ServerPlayer} to a {@link ServerUser} for easier access to player data.
     * @return a {@link ServerUser} instance representing the player
     */
    default ServerUser serverUser() {
        return new ServerUser(player());
    }
}
