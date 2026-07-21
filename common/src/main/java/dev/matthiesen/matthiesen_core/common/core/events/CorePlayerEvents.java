package dev.matthiesen.matthiesen_core.common.core.events;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.events.PlayerEventListener;
import dev.matthiesen.matthiesen_core.common.core.data.SavedPlayerData;
import dev.matthiesen.matthiesen_core.common.core.registry.PlayerEventsManager;
import net.minecraft.server.level.ServerPlayer;

/**
 * The CorePlayerEvents class is responsible for managing player-related events within the MatthiesenCore mod. It provides a static method to register event listeners that respond to player actions, such as joining the server. When a player joins, the registered listener verifies the player's saved data to ensure consistency and integrity. This class serves as a centralized point for handling player events, allowing for modular and organized event management in the Minecraft server environment.
 */
public final class CorePlayerEvents {
    private CorePlayerEvents() {}

    /**
     * Registers the player event listeners for the mod. This method sets up a listener for player join events, ensuring that when a player joins the server, their saved player data is verified. The listener is registered with the PlayerEventsManager of the MatthiesenCoreCommon instance, using the mod's unique identifier (MOD_ID) to associate the listener with this specific mod. This allows for modular and organized event handling within the Minecraft server environment.
     */
    public static void register(PlayerEventsManager eventsManager) {
        eventsManager.registerListener(MatthiesenCoreCommon.MOD_ID, new PlayerEventListener() {
            @Override
            public void onPlayerJoin(ServerPlayer player) {
                SavedPlayerData.verifyPlayerData(player);
            }
        });
    }
}
