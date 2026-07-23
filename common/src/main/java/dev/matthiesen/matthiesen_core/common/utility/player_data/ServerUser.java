package dev.matthiesen.matthiesen_core.common.utility.player_data;

import com.mojang.authlib.GameProfile;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.data.SavedPlayerData;
import dev.matthiesen.matthiesen_core.common.core.data.fakeplayer.FakePlayerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.UUID;

/**
 * Represents a user on the server, which can be either online or offline.
 */
@SuppressWarnings("unused")
public final class ServerUser {
    private final UUID uuid;
    private Player offlinePlayer;

    /**
     * Creates a new ServerUser instance for the given player.
     * @param player The player to create the ServerUser for. Can be either online or offline.
     */
    public ServerUser(ServerPlayer player) {
        this.uuid = player.getUUID();
        this.offlinePlayer = player;
    }

    /**
     * Creates a new ServerUser instance for the given player.
     * @param player The player to create the ServerUser for. Can be either online or offline.
     */
    public ServerUser(Player player) {
        this.uuid = player.getUUID();
        this.offlinePlayer = player;
    }

    /**
     * Creates a new ServerUser instance for the given UUID.
     * @param uuid The UUID of the player to create the ServerUser for. Can be either online or offline.
     */
    public ServerUser(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Creates a new ServerUser instance for the given player name.
     * @param playerName The name of the player to create the ServerUser for. Can be either online or offline.
     */
    public ServerUser(String playerName) {
        this.uuid = SavedPlayerData.findPlayerByUsername(playerName);
    }

    /**
     * Gets the online player associated with this ServerUser, if they are currently online.
     * @return The online player, or null if they are not online.
     */
    public ServerPlayer getOnlinePlayer() {
        return MatthiesenCoreCommon.INSTANCE.getCommonUtils().getServer().getPlayerList().getPlayer(this.uuid);
    }

    /**
     * Checks if the player associated with this ServerUser is currently online.
     * @return True if the player is online, false otherwise.
     */
    public boolean isOnline() {
        return getOnlinePlayer() != null;
    }

    /**
     * Gets the offline player associated with this ServerUser, if they are currently offline.
     * @return The offline player, or null if there was an error
     */
    public Player getOfflinePlayer() {
        Player player = this.getOnlinePlayer();
        if (player != null) return player;
        if (this.offlinePlayer != null) return this.offlinePlayer;
        MinecraftServer server = MatthiesenCoreCommon.INSTANCE.getCommonUtils().getServer();
        if (server == null) return null;
        this.offlinePlayer = server.getPlayerList().getPlayer(this.uuid);
        if (this.offlinePlayer != null) return this.offlinePlayer;
        GameProfileCache profileCache = server.getProfileCache();
        if (profileCache == null) return null;
        GameProfile gameProfile = profileCache.get(this.uuid).orElse(null);
        if (gameProfile == null) return null;
        this.offlinePlayer = FakePlayerFactory.get(server.overworld(), gameProfile);
        return this.offlinePlayer;
    }

    /**
     * Gets the username of the player associated with this ServerUser.
     * @return The username of the player, or null if they are not found.
     */
    public String getUsername() {
        Player player = getOnlinePlayer();
        if (player != null) return player.getName().getString();
        player = getOfflinePlayer();
        if (player != null) return player.getName().getString();
        return SavedPlayerData.findPlayerNameByUUID(this.uuid);
    }

    /**
     * Gets the list of aliases associated with the player.
     * @return A list of aliases for the player, or an empty list if none are found.
     */
    public List<String> getAliases() {
        return SavedPlayerData.getPlayerAliases(this.uuid);
    }

    /**
     * Gets the UUID of the player associated with this ServerUser.
     * @return The UUID of the player.
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Gets the string representation of the UUID of the player associated with this ServerUser.
     * @return The string representation of the UUID of the player.
     */
    public String getStringUUID() {
        return this.uuid.toString();
    }
}
