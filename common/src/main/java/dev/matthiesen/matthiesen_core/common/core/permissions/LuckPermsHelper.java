package dev.matthiesen.matthiesen_core.common.core.permissions;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.utility.player_data.ServerUser;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;
import java.util.function.Predicate;

/**
 * The LuckPermsHelper class provides utility methods for interacting with the LuckPerms API. It allows for the retrieval and
 * management of user permissions within the LuckPerms system.
 * This class is designed to be a singleton, ensuring that only one instance exists throughout the application. It provides
 * methods to initialize the LuckPerms API, retrieve user data, save user data, and check for specific permission nodes.
 * The class also handles the loading of the LuckPerms API when the server starts, ensuring that the API is available for use
 * when needed. If the LuckPerms API is not available, appropriate error logging is performed to notify the user of the issue.
 */
@SuppressWarnings("unused")
public final class LuckPermsHelper {
    /**
     * Singleton instance of the LuckPermsHelper class. This instance is used to access LuckPerms functionalities throughout the mod.
     */
    public static final LuckPermsHelper INSTANCE = new LuckPermsHelper();

    private volatile LuckPerms LP_INSTANCE;
    private boolean initialized;

    private LuckPermsHelper() {}

    /**
     * Initializes the LuckPermsHelper instance. This method sets up the necessary event subscriptions and prepares the helper for use.
     * It ensures that the LuckPerms API is loaded when the server starts, allowing for seamless integration with the LuckPerms permission system.
     * If the helper has already been initialized, this method does nothing.
     */
    public void initialize() {
        if (initialized) return;
        initialized = true;

        PlatformEvents.SERVER_STARTED.subscribe(event -> getLuckPerms());
        MatthiesenCoreCommon.INSTANCE.createInfoLog("LuckPermsHelper initialized. LuckPerms API will be loaded when the server starts.");
    }

    /**
     * Retrieves the LuckPerms API instance. If the instance is already loaded, it returns the existing instance. If not, it
     * attempts to load the LuckPerms API using the LuckPermsProvider.
     * @return The LuckPerms API instance if available, or null if the API is not available. If the API is not available, an error log will be created.
     */
    public LuckPerms getLuckPerms() {
        if (LP_INSTANCE != null) return LP_INSTANCE;
        try {
            LP_INSTANCE = LuckPermsProvider.get();
            MatthiesenCoreCommon.INSTANCE.createInfoLog("LuckPerms API loaded successfully");
            return LP_INSTANCE;
        } catch (RuntimeException e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("LuckPerms not available", e);
            return null;
        }
    }

    /**
     * Retrieves the LuckPerms user associated with the provided player UUID. This method attempts to load the user data from the LuckPerms API.
     * @param playerUUID The UUID of the player whose LuckPerms user data is to be retrieved. This UUID is used to identify the specific player in the LuckPerms system.
     * @return The LuckPerms User object corresponding to the provided player UUID. If the LuckPerms API is not available or the user cannot be retrieved, this method will return null.
     */
    public User getUser(UUID playerUUID) {
        try {
            LuckPerms lp = getLuckPerms();
            if (lp == null) return null;
            UserManager userManager = lp.getUserManager();
            return userManager.loadUser(playerUUID).join();
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to load user", e);
            return null;
        }
    }

    /**
     * Retrieves the LuckPerms user associated with the provided ServerPlayer instance. This method extracts the player's UUID and calls the getUser(UUID) method to retrieve the corresponding LuckPerms user data.
     * @param player The ServerPlayer instance representing the player whose LuckPerms user data is to be retrieved. This player object contains the necessary information to identify the specific player in the LuckPerms system.
     * @return The LuckPerms User object corresponding to the provided ServerPlayer instance. If the LuckPerms API is not available or the user cannot be retrieved, this method will return null.
     */
    public User getUser(ServerPlayer player) {
        return getUser(player.getUUID());
    }

    /**
     * Retrieves the LuckPerms user associated with the provided ServerUser instance. This method extracts the player's UUID and calls the getUser(UUID) method to retrieve the corresponding LuckPerms user data.
     * @param player The ServerUser instance representing the player whose LuckPerms user data is to be retrieved. This player object contains the necessary information to identify the specific player in the LuckPerms system.
     * @return The LuckPerms User object corresponding to the provided ServerUser instance. If the LuckPerms API is not available or the user cannot be retrieved, this method will return null.
     */
    public User getUser(ServerUser player) {
        return getUser(player.getUUID());
    }

    /**
     * Checks if a player has a specific permission node using the LuckPerms API. This method retrieves the LuckPerms user
     * associated with the provided player UUID and checks if they have the specified permission node.
     * @param playerUUID The UUID of the player whose permissions are being checked. This is used to retrieve the corresponding LuckPerms user.
     * @param node The permission node to check for the player. This is a string representing the specific permission being queried.
     * @return true if the player has the specified permission node, false otherwise. If the LuckPerms API is not available or the
     * user cannot be retrieved, this method will return false.
     */
    public boolean hasPermissionNode(UUID playerUUID, String node) {
        try {
            User user = getUser(playerUUID);
            if (user == null) return false;
            return user.getCachedData().getPermissionData().checkPermission(node).asBoolean();
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to check permission", e);
            return false;
        }
    }

    /**
     * Checks if a player has a specific permission node using the LuckPerms API. This method retrieves the LuckPerms user
     * associated with the provided ServerPlayer instance and checks if they have the specified permission node.
     * @param player The ServerPlayer instance representing the player whose permissions are being checked. This player object contains the necessary information to identify the specific player in the LuckPerms system.
     * @param node The permission node to check for the player. This is a string representing the specific permission being queried.
     * @return true if the player has the specified permission node, false otherwise. If the LuckPerms API is not available or the
     * user cannot be retrieved, this method will return false.
     */
    public boolean hasPermissionNode(ServerPlayer player, String node) {
        return hasPermissionNode(player.getUUID(), node);
    }

    /**
     * Checks if a player has a specific permission node using the LuckPerms API. This method retrieves the LuckPerms user
     * associated with the provided ServerUser instance and checks if they have the specified permission node.
     * @param player The ServerUser instance representing the player whose permissions are being checked. This player object contains the necessary information to identify the specific player in the LuckPerms system.
     * @param node The permission node to check for the player. This is a string representing the specific permission being queried.
     * @return true if the player has the specified permission node, false otherwise. If the LuckPerms API is not available or the
     * user cannot be retrieved, this method will return false.
     */
    public boolean hasPermissionNode(ServerUser player, String node) {
        return hasPermissionNode(player.getUUID(), node);
    }

    /**
     * Saves the provided LuckPerms user data. This method attempts to save the user's data using the LuckPerms API. If the
     * LuckPerms API is not available or an error occurs during the save operation, an error log will be created.
     * @param user The LuckPerms User object representing the player whose data is to be saved. This user object should be obtained
     *             from the LuckPerms API and contain the necessary data to be persisted.
     */
    public void saveUser(User user) {
        try {
            LuckPerms lp = getLuckPerms();
            if (lp == null) return;
            UserManager userManager = lp.getUserManager();
            userManager.saveUser(user);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to save user", e);
        }
    }

    /**
     * Clears the prefix for a LuckPerms user based on a specified predicate. This method allows for selective clearing of prefix nodes
     * based on the provided predicate, enabling more granular control over which prefixes are removed from the user's data. After clearing the prefixes, the user's data is saved to persist the changes.
     * @param user The LuckPerms User object representing the player whose prefix is to be cleared. This user object should be obtained from the LuckPerms API and contain the necessary data to identify the player's current prefix nodes.
     * @param predicate A predicate that defines the condition to match the prefix node. This allows for flexible comparison of the player's prefix against various criteria.
     */
    public void clearUserPrefix(User user, Predicate<PrefixNode> predicate) {
        try {
            user.data().clear(NodeType.PREFIX.predicate(predicate));
            saveUser(user);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to clear prefix", e);
        }
    }

    /**
     * Clears the prefix for a LuckPerms user based on a specified predicate. This method retrieves the LuckPerms user associated with the provided player UUID and clears their prefix nodes that match the given predicate. After clearing the prefixes, the user's data is saved to persist the changes.
     * @param playerUUID The UUID of the player whose prefix is to be cleared. This is used to retrieve the corresponding LuckPerms user.
     * @param predicate A predicate that defines the condition to match the prefix node. This allows for flexible comparison of the player's prefix against various criteria.
     */
    public void clearUserPrefix(UUID playerUUID, Predicate<PrefixNode> predicate) {
        try {
            User user = getUser(playerUUID);
            if (user == null) return;
            clearUserPrefix(user, predicate);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to clear prefix", e);
        }
    }

    /**
     * Sets a new prefix for a LuckPerms user with a specified priority and clears any existing prefixes that match the provided predicate. This method retrieves the LuckPerms user associated with the provided player UUID, clears their existing prefixes based on the predicate, and adds a new prefix node with the specified priority. After updating the user's prefix, their data is saved to persist the changes.
     * @param playerUUID The UUID of the player whose prefix is to be set. This is used to retrieve the corresponding LuckPerms user.
     * @param newPrefix The new prefix string to be set for the player. This will be used to create a new PrefixNode in the LuckPerms system.
     * @param priority The priority value for the new prefix node. This determines the order of precedence for the prefix in relation to other prefixes the player may have.
     * @param clearPredicate A predicate that defines the condition to match existing prefix nodes that should be cleared before setting the new prefix. This allows for selective clearing of prefixes based on specific criteria.
     */
    public void setUserPrefix(UUID playerUUID, String newPrefix, int priority, Predicate<PrefixNode> clearPredicate) {
        try {
            User user = getUser(playerUUID);
            if (user == null) return;
            clearUserPrefix(user, clearPredicate);
            PrefixNode newNode = PrefixNode.builder(newPrefix, priority).build();
            user.data().add(newNode);
            saveUser(user);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to set prefix", e);
        }
    }
}
