package dev.matthiesen.matthiesen_core.common.utility;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.BossEvent;

/**
 * A utility class for creating and managing boss bars in Minecraft.
 * This class provides a convenient interface for creating a boss bar with a specified name, progress, color, and
 * overlay style, and for managing the visibility of the boss bar for players in a PlayerList. It encapsulates the
 * ServerBossEvent and provides methods to add/remove players, update progress, set the name, and manage visibility
 * of the boss bar for players in a PlayerList.
 */
@SuppressWarnings("unused")
public final class BossBar {
    private final Builder builder;

    /**
     * Create a new BossBar instance with the given parameters.
     * @param comp The Component to display as the name of the boss bar.
     * @param initProgress The initial progress value for the boss bar, between 0.0 and 1.0.
     * @param bbColor The color of the boss bar, as a BossEvent.BossBarColor.
     * @param bbOverlay The overlay style of the boss bar, as a BossEvent.BossBarOverlay.
     */
    public BossBar(Component comp, float initProgress, BossEvent.BossBarColor bbColor, BossEvent.BossBarOverlay bbOverlay) {
        this.builder = new Builder(comp, initProgress, bbColor, bbOverlay);
    }

    /**
     * Get the Builder instance for this BossBar, which can be used to manage the underlying ServerBossEvent.
     * @return The Builder instance for this BossBar.
     */
    public Builder getBuilder() {
        return this.builder;
    }

    /**
     * A builder class for creating and managing a ServerBossEvent (boss bar) in Minecraft.
     * This class provides methods to add/remove players, update progress, set the name, and manage visibility of the boss bar for players in a PlayerList.
     * It encapsulates the ServerBossEvent and provides a convenient interface for interacting with it.
     */
    public static class Builder {
        private final ServerBossEvent bossBar;

        /**
         * Create a new Builder for a BossBar with the given parameters.
         * @param component The Component to display as the name of the boss bar.
         * @param initialProgress The initial progress value for the boss bar, between 0.0 and 1.0.
         * @param bossBarColor The color of the boss bar, as a BossEvent.BossBarColor.
         * @param bossBarOverlay The overlay style of the boss bar, as a BossEvent.BossBarOverlay.
         */
        public Builder(Component component, float initialProgress, BossEvent.BossBarColor bossBarColor, BossEvent.BossBarOverlay bossBarOverlay) {
            this.bossBar = new ServerBossEvent(component, bossBarColor, bossBarOverlay);
            this.bossBar.setProgress(initialProgress);
        }

        /**
         * Add the given player to the boss bar, so that they can see it.
         * @param player The ServerPlayer to add to the boss bar.
         */
        public void addPlayer(ServerPlayer player) {
            this.bossBar.addPlayer(player);
        }

        /**
         * Remove the given player from the boss bar, so that they no longer see it.
         * @param player The ServerPlayer to remove from the boss bar.
         */
        public void removePlayer(ServerPlayer player) {
            this.bossBar.removePlayer(player);
        }

        /**
         * Update the progress of the boss bar to the given value.
         * @param progress The new progress value to set for the boss bar. Should be between 0.0 and 1.0.
         */
        public void updateProgress(float progress) {
            this.bossBar.setProgress(progress);
        }

        /**
         * Set the name of the boss bar to the given Component.
         * @param component The Component to set as the name of the boss bar.
         */
        public void setName(Component component) {
            this.bossBar.setName(component);
        }

        /**
         * Verify that all players in the given PlayerList are currently viewing the boss bar, and add any missing players to the boss bar.
         * @param list The PlayerList containing the players to verify.
         */
        public void verifyPlayerList(PlayerList list) {
            for (ServerPlayer sp : list.getPlayers()) {
                if (!this.bossBar.getPlayers().contains(sp)) {
                    this.bossBar.addPlayer(sp);
                }
            }
        }

        /**
         * Hide the boss bar from all players in the given PlayerList.
         * @param list The PlayerList containing the players to hide the boss bar from.
         */
        public void hideBossBarFromPlayerList(PlayerList list) {
            for (ServerPlayer sp : list.getPlayers()) {
                removePlayer(sp);
            }
        }

        /**
         * Show the boss bar to all players in the given PlayerList.
         * @param list The PlayerList containing the players to show the boss bar to.
         */
        public void showBossBarFromPlayerList(PlayerList list) {
            for (ServerPlayer sp : list.getPlayers()) {
                addPlayer(sp);
            }
        }
    }
}
