package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.data.SavedPlayerData;

/**
 * Registers internal Matthiesen Core player event listeners using {@link PlatformEvents}.
 */
public final class CorePlayerEvents {
    private CorePlayerEvents() {}

    /**
     * Registers the core player event listeners. Called once during
     * {@link MatthiesenCoreCommon#initialize()}.
     */
    public static void register() {
        PlatformEvents.PLAYER_JOIN.subscribe(event ->
                SavedPlayerData.verifyPlayerData(event.player()));
    }
}
