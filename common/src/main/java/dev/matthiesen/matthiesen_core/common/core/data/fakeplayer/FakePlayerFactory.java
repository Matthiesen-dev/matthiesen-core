package dev.matthiesen.matthiesen_core.common.core.data.fakeplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A factory class for creating and managing FakePlayer instances in a Minecraft server environment. This class provides methods to retrieve or create FakePlayers based on their GameProfile and the ServerLevel they are associated with. It ensures that FakePlayers are unique per dimension and profile, and handles thread safety by using concurrent data structures. The factory also provides methods to unload FakePlayers when a level is unloaded and to clear all cached FakePlayers, which can be useful for testing or resetting the state of the fake player system.
 */
@SuppressWarnings("unused")
public final class FakePlayerFactory {
    private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AFB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
    private static final ConcurrentMap<FakePlayerKey, FakePlayer> fakePlayers = new ConcurrentHashMap<>();
    private static final ConcurrentMap<FakePlayerKey, CompletableFuture<FakePlayer>> pending = new ConcurrentHashMap<>();

    private FakePlayerFactory() {
    }

    /**
     * Retrieves a FakePlayer instance for the specified server level and the default Minecraft game profile. This method is a convenience wrapper around the get(ServerLevel, GameProfile) method, using the predefined MINECRAFT GameProfile. It ensures that a unique FakePlayer is created or retrieved for the given level and the Minecraft profile, allowing for consistent behavior across different dimensions and server contexts.
     * @param level The ServerLevel instance representing the level in which the FakePlayer should exist. This is used to determine the dimension and context for the FakePlayer.
     * @return A FakePlayer instance corresponding to the specified level and the default Minecraft game profile. If a FakePlayer with the Minecraft profile already exists for the given level, it is returned from the cache; otherwise, a new FakePlayer is created and cached for future use.
     */
    public static FakePlayer getMinecraft(ServerLevel level) {
        return get(level, MINECRAFT);
    }

    /**
     * Retrieves a FakePlayer instance for the specified server level and game profile. If a FakePlayer with the given profile already exists in the cache, it is returned. If not, a new FakePlayer is created and cached. This method ensures that FakePlayers are unique per dimension and profile, and handles thread safety by using a ConcurrentMap and CompletableFuture for pending requests.
     * @param level The ServerLevel instance representing the level in which the FakePlayer should exist. This is used to determine the dimension and context for the FakePlayer.
     * @param profile The GameProfile instance representing the player's profile, including their UUID and name. This is used to uniquely identify the FakePlayer and ensure that it is associated with the correct player data.
     * @return A FakePlayer instance corresponding to the specified level and profile. If a FakePlayer with the given profile already exists, it is returned from the cache; otherwise, a new FakePlayer is created and cached for future use.
     */
    public static FakePlayer get(ServerLevel level, GameProfile profile) {
        MinecraftServer server = level.getServer();
        FakePlayerKey key = new FakePlayerKey(level.dimension().location().hashCode(), profile.getId());
        FakePlayer cached = fakePlayers.get(key);
        if (cached != null) {
            return cached;
        } else if (server.isSameThread()) {
            return fakePlayers.computeIfAbsent(key, (k) -> new FakePlayer(level, profile));
        } else {
            CompletableFuture<FakePlayer> future = pending.computeIfAbsent(key, (k) -> {
                CompletableFuture<FakePlayer> f = new CompletableFuture<>();
                server.execute(() -> {
                    try {
                        FakePlayer fp = fakePlayers.computeIfAbsent(key, (kk) -> new FakePlayer(level, profile));
                        f.complete(fp);
                    } finally {
                        pending.remove(key);
                    }

                });
                return f;
            });
            return future.join();
        }
    }

    /**
     * Unloads all fake players associated with the specified server level. This method removes any cached fake players and pending futures that are linked to the given level's dimension. It is typically called when a level is unloaded to ensure that resources are properly cleaned up and that no stale references remain in the factory's internal maps.
     * @param level The ServerLevel instance representing the level to unload. All fake players and pending futures associated with this level's dimension will be removed from the factory's internal caches.
     */
    public static void unloadLevel(ServerLevel level) {
        int dim = level.dimension().location().hashCode();
        fakePlayers.keySet().removeIf((k) -> k.dimensionId == dim);
        pending.keySet().removeIf((k) -> k.dimensionId == dim);
    }

    /**
     * Clears all cached fake players and pending futures. This method is intended for use in testing or when resetting the state of the fake player system. It removes all entries from the fakePlayers and pending maps, effectively resetting the factory to its initial state. Use with caution, as this will remove all existing fake player instances and any pending requests for fake players.
     */
    public static void clearAll() {
        fakePlayers.clear();
        pending.clear();
    }

    private record FakePlayerKey(int dimensionId, UUID uuid) {
    }
}
