package dev.matthiesen.matthiesen_core.common.core.permissions;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.utility.player_data.ServerUser;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.track.Track;
import net.luckperms.api.track.TrackManager;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static void loadCompat() {
        INSTANCE.initialize();
    }

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
     * Retrieves the LuckPerms user associated with the provided player UUID. This method attempts to load the user
     * data from the LuckPerms API.
     * @param playerUUID The UUID of the player whose LuckPerms user data is to be retrieved. This UUID is used to identify
     *                   the specific player in the LuckPerms system.
     * @return The LuckPerms User object corresponding to the provided player UUID. If the LuckPerms API is not available or
     * the user cannot be retrieved, this method will return null.
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
     * Retrieves the LuckPerms user associated with the provided ServerPlayer instance. This method extracts the player's
     * UUID and calls the getUser(UUID) method to retrieve the corresponding LuckPerms user data.
     * @param player The ServerPlayer instance representing the player whose LuckPerms user data is to be retrieved. This
     *               player object contains the necessary information to identify the specific player in the LuckPerms system.
     * @return The LuckPerms User object corresponding to the provided ServerPlayer instance. If the LuckPerms API is not
     * available or the user cannot be retrieved, this method will return null.
     */
    public User getUser(ServerPlayer player) {
        return getUser(player.getUUID());
    }

    /**
     * Retrieves the LuckPerms user associated with the provided ServerUser instance. This method extracts the player's UUID and
     * calls the getUser(UUID) method to retrieve the corresponding LuckPerms user data.
     * @param player The ServerUser instance representing the player whose LuckPerms user data is to be retrieved. This player
     *               object contains the necessary information to identify the specific player in the LuckPerms system.
     * @return The LuckPerms User object corresponding to the provided ServerUser instance. If the LuckPerms API is not available
     * or the user cannot be retrieved, this method will return null.
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
     * @param player The ServerPlayer instance representing the player whose permissions are being checked. This player object
     *               contains the necessary information to identify the specific player in the LuckPerms system.
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
     * @param player The ServerUser instance representing the player whose permissions are being checked. This player object
     *               contains the necessary information to identify the specific player in the LuckPerms system.
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
     * Saves the provided LuckPerms track data. This method attempts to save the track's data using the LuckPerms API. If the
     * LuckPerms API is not available or an error occurs during the save operation, an error log will be created.
     * @param track The LuckPerms Track object representing the track whose data is to be saved. This track object should be obtained
     *              from the LuckPerms API and contain the necessary data to be persisted.
     */
    public void saveTrack(Track track) {
        try {
            LuckPerms lp = getLuckPerms();
            if (lp == null) return;
            TrackManager trackManager = lp.getTrackManager();
            trackManager.saveTrack(track);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to save track", e);
        }
    }

    /**
     * Clears the prefix for a LuckPerms user based on a specified predicate. This method allows for selective clearing of prefix nodes
     * based on the provided predicate, enabling more granular control over which prefixes are removed from the user's data. After
     * clearing the prefixes, the user's data is saved to persist the changes.
     * @param user The LuckPerms User object representing the player whose prefix is to be cleared. This user object should be
     *             obtained from the LuckPerms API and contain the necessary data to identify the player's current prefix nodes.
     * @param predicate A predicate that defines the condition to match the prefix node. This allows for flexible comparison of
     *                  the player's prefix against various criteria.
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
     * Clears the prefix for a LuckPerms user based on a specified predicate. This method retrieves the LuckPerms user associated with
     * the provided player UUID and clears their prefix nodes that match the given predicate. After clearing the prefixes, the user's
     * data is saved to persist the changes.
     * @param playerUUID The UUID of the player whose prefix is to be cleared. This is used to retrieve the corresponding LuckPerms user.
     * @param predicate A predicate that defines the condition to match the prefix node. This allows for flexible comparison of the
     *                  player's prefix against various criteria.
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
     * Sets a new prefix for a LuckPerms user with a specified priority and clears any existing prefixes that match the provided predicate.
     * This method retrieves the LuckPerms user associated with the provided player UUID, clears their existing prefixes based on the predicate,
     * and adds a new prefix node with the specified priority. After updating the user's prefix, their data is saved to persist the changes.
     * @param playerUUID The UUID of the player whose prefix is to be set. This is used to retrieve the corresponding LuckPerms user.
     * @param newPrefix The new prefix string to be set for the player. This will be used to create a new PrefixNode in the LuckPerms system.
     * @param priority The priority value for the new prefix node. This determines the order of precedence for the prefix in relation to
     *                 other prefixes the player may have.
     * @param clearPredicate A predicate that defines the condition to match existing prefix nodes that should be cleared before setting
     *                       the new prefix. This allows for selective clearing of prefixes based on specific criteria.
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

    /**
     * Adds a parent group to a LuckPerms user. This method retrieves the LuckPerms user associated with the provided player UUID
     * and adds the specified parent group to their data. After adding the parent group, the user's data is saved to persist the changes.
     * @param user The LuckPerms User object representing the player to whom the parent group is to be added. This user object should be
     *             obtained from the LuckPerms API and contain the necessary data to identify the player's current group memberships.
     * @param parentGroup The name of the parent group to be added to the user's data. This should correspond to a valid group in the LuckPerms system.
     */
    public void addUserParentGroup(User user, String parentGroup) {
        try {
            InheritanceNode node = InheritanceNode.builder(parentGroup).build();
            user.data().add(node);
            saveUser(user);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to add parent group", e);
        }
    }

    /**
     * Adds a parent group to a LuckPerms user based on their UUID. This method retrieves the LuckPerms user associated
     * with the provided player UUID and adds the specified parent group to their data. After adding the parent group,
     * the user's data is saved to persist the changes.
     * @param playerUUID The UUID of the player to whom the parent group is to be added. This is used to retrieve the
     *                   corresponding LuckPerms user.
     * @param parentGroup The name of the parent group to be added to the user's data. This should correspond to a
     *                    valid group in the LuckPerms system.
     */
    public void addUserParentGroup(UUID playerUUID, String parentGroup) {
        try {
            User user = getUser(playerUUID);
            if (user == null) return;
            addUserParentGroup(user, parentGroup);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to add parent group", e);
        }
    }

    /**
     * Adds a parent group to a LuckPerms user based on their ServerPlayer instance. This method retrieves the LuckPerms user
     * associated with the provided ServerPlayer and adds the specified parent group to their data. After adding the parent
     * group, the user's data is saved to persist the changes.
     * @param player The ServerPlayer instance representing the player to whom the parent group is to be added. This player
     *               object contains the necessary information to identify the specific player in the LuckPerms system.
     * @param parentGroup The name of the parent group to be added to the user's data. This should correspond to a valid
     *                    group in the LuckPerms system.
     */
    public void addUserParentGroup(ServerPlayer player, String parentGroup) {
        addUserParentGroup(player.getUUID(), parentGroup);
    }

    /**
     * Adds a parent group to a LuckPerms user based on their ServerUser instance. This method retrieves the LuckPerms user
     * associated with the provided ServerUser and adds the specified parent group to their data. After adding the parent
     * group, the user's data is saved to persist the changes.
     * @param player The ServerUser instance representing the player to whom the parent group is to be added. This player
     *               object contains the necessary information to identify the specific player in the LuckPerms system.
     * @param parentGroup The name of the parent group to be added to the user's data. This should correspond to a valid group
     *                    in the LuckPerms system.
     */
    public void addUserParentGroup(ServerUser player, String parentGroup) {
        addUserParentGroup(player.getUUID(), parentGroup);
    }

    /**
     * Removes a parent group from a LuckPerms user. This method retrieves the LuckPerms user associated with the provided
     * player UUID and removes the specified parent group from their data. After removing the parent group, the user's data is saved to persist the changes.
     * @param user The LuckPerms User object representing the player from whom the parent group is to be removed. This user
     *             object should be obtained from the LuckPerms API and contain the necessary data to identify the player's current group memberships.
     * @param parentGroup The name of the parent group to be removed from the user's data. This should correspond to a
     *                    valid group in the LuckPerms system.
     */
    public void removeUserParentGroup(User user, String parentGroup) {
        try {
            InheritanceNode node = InheritanceNode.builder(parentGroup).build();
            user.data().remove(node);
            saveUser(user);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to remove parent group", e);
        }
    }

    /**
     * Removes a parent group from a LuckPerms user based on their UUID. This method retrieves the LuckPerms user associated
     * with the provided player UUID and removes the specified parent group from their data. After removing the parent group,
     * the user's data is saved to persist the changes.
     * @param playerUUID The UUID of the player from whom the parent group is to be removed. This is used to retrieve the
     *                   corresponding LuckPerms user.
     * @param parentGroup The name of the parent group to be removed from the user's data. This should correspond to a
     *                    valid group in the LuckPerms system.
     */
    public void removeUserParentGroup(UUID playerUUID, String parentGroup) {
        try {
            User user = getUser(playerUUID);
            if (user == null) return;
            removeUserParentGroup(user, parentGroup);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to remove parent group", e);
        }
    }

    /**
     * Removes a parent group from a LuckPerms user based on their ServerPlayer instance. This method retrieves the LuckPerms
     * user associated with the provided ServerPlayer and removes the specified parent group from their data. After removing
     * the parent group, the user's data is saved to persist the changes.
     * @param player The ServerPlayer instance representing the player from whom the parent group is to be removed. This player
     *               object contains the necessary information to identify the specific player in the LuckPerms system.
     * @param parentGroup The name of the parent group to be removed from the user's data. This should correspond to a valid
     *                    group in the LuckPerms system.
     */
    public void removeUserParentGroup(ServerPlayer player, String parentGroup) {
        removeUserParentGroup(player.getUUID(), parentGroup);
    }

    /**
     * Removes a parent group from a LuckPerms user based on their ServerUser instance. This method retrieves the LuckPerms user
     * associated with the provided ServerUser and removes the specified parent group from their data. After removing the parent
     * group, the user's data is saved to persist the changes.
     * @param player The ServerUser instance representing the player from whom the parent group is to be removed. This player
     *               object contains the necessary information to identify the specific player in the LuckPerms system.
     * @param parentGroup The name of the parent group to be removed from the user's data. This should correspond to a
     *                    valid group in the LuckPerms system.
     */
    public void removeUserParentGroup(ServerUser player, String parentGroup) {
        removeUserParentGroup(player.getUUID(), parentGroup);
    }

    /**
     * Retrieves a list of group names that the specified LuckPerms user belongs to. This method extracts the inheritance nodes from
     * the user's data and collects the group names into a list. If an error occurs during the retrieval process, an error log will be
     * created and an empty list will be returned.
     * @param user The LuckPerms User object representing the player whose group memberships are to be retrieved. This user object
     *             should be obtained from the LuckPerms API and contain the necessary data to identify the player's current group memberships.
     * @return A list of group names that the specified user belongs to. If the user has no group memberships or an error occurs,
     * an empty list will be returned.
     */
    public List<String> getUserGroups(User user) {
        try {
            return user.getNodes(NodeType.INHERITANCE).stream()
                    .map(InheritanceNode::getGroupName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to get user groups", e);
            return List.of();
        }
    }

    /**
     * Retrieves a list of group names that the LuckPerms user associated with the specified player UUID belongs to. This method
     * retrieves the LuckPerms user based on the provided UUID and then calls the getUserGroups(User) method to obtain the list of
     * group names. If an error occurs during the retrieval process, an error log will be created and an empty list will be returned.
     * @param playerUUID The UUID of the player whose group memberships are to be retrieved. This is used to retrieve the corresponding LuckPerms user.
     * @return A list of group names that the LuckPerms user associated with the specified player UUID belongs to. If the user has no
     * group memberships or an error occurs, an empty list will be returned.
     */
    public List<String> getUserGroups(UUID playerUUID) {
        try {
            User user = getUser(playerUUID);
            if (user == null) return List.of();
            return getUserGroups(user);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to get user groups", e);
            return List.of();
        }
    }

    /**
     * Retrieves a list of group names that the LuckPerms user associated with the specified ServerPlayer instance belongs to. This
     * method extracts the player's UUID and calls the getUserGroups(UUID) method to obtain the list of group names. If an error occurs
     * during the retrieval process, an error log will be created and an empty list will be returned.
     * @param player The ServerPlayer instance representing the player whose group memberships are to be retrieved. This player object
     *               contains the necessary information to identify the specific player in the LuckPerms system.
     * @return A list of group names that the LuckPerms user associated with the specified ServerPlayer instance belongs to. If the user
     * has no group memberships or an error occurs, an empty list will be returned.
     */
    public List<String> getUserGroups(ServerPlayer player) {
        return getUserGroups(player.getUUID());
    }

    /**
     * Retrieves a list of group names that the LuckPerms user associated with the specified ServerUser instance belongs to. This method
     * extracts the player's UUID and calls the getUserGroups(UUID) method to obtain the list of group names. If an error occurs during
     * the retrieval process, an error log will be created and an empty list will be returned.
     * @param player The ServerUser instance representing the player whose group memberships are to be retrieved. This player object contains
     *               the necessary information to identify the specific player in the LuckPerms system.
     * @return A list of group names that the LuckPerms user associated with the specified ServerUser instance belongs to. If the user has
     * no group memberships or an error occurs, an empty list will be returned.
     */
    public List<String> getUserGroups(ServerUser player) {
        return getUserGroups(player.getUUID());
    }

    /**
     * Checks if a LuckPerms user is on a specific track. This method retrieves the LuckPerms user associated with the provided player
     * UUID and checks if they belong to any of the groups defined in the specified track. If the user is on the track, it returns true;
     * otherwise, it returns false. If an error occurs during the process, an error log will be created and false will be returned.
     * @param playerUUID The UUID of the player whose track membership is to be checked. This is used to retrieve the corresponding LuckPerms user.
     * @param track The name of the track to check for the user's membership. This should correspond to a valid track in the LuckPerms system.
     * @return true if the LuckPerms user associated with the specified player UUID is on the specified track, false otherwise. If the
     * user is not found or an error occurs, false will be returned.
     */
    public boolean isUserOnTrack(UUID playerUUID, String track) {
        try {
            LuckPerms lp = getLuckPerms();
            if (lp == null) return false;
            TrackManager trackManager = lp.getTrackManager();
            Track trackObj = trackManager.getTrack(track);
            if (trackObj == null) return false;

            List<String> trackGroups = trackObj.getGroups();
            List<String> userGroups = getUserGroups(playerUUID);

            if (userGroups.isEmpty()) return false;

            return userGroups.stream().anyMatch(trackGroups::contains);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to check if user is on track", e);
            return false;
        }
    }

    /**
     * Checks if a LuckPerms user is on a specific track based on their ServerPlayer instance. This method extracts the player's UUID
     * and calls the isUserOnTrack(UUID, String) method to determine if they belong to any of the groups defined in the specified track.
     * If the user is on the track, it returns true; otherwise, it returns false. If an error occurs during the process, an error log will
     * be created and false will be returned.
     * @param player The ServerPlayer instance representing the player whose track membership is to be checked. This player object contains
     *               the necessary information to identify the specific player in the LuckPerms system.
     * @param track The name of the track to check for the user's membership. This should correspond to a valid track in the LuckPerms system.
     * @return true if the LuckPerms user associated with the specified ServerPlayer instance is on the specified track, false otherwise.
     * If the user is not found or an error occurs, false will be returned.
     */
    public boolean isUserOnTrack(ServerPlayer player, String track) {
        return isUserOnTrack(player.getUUID(), track);
    }

    /**
     * Checks if a LuckPerms user is on a specific track based on their ServerUser instance. This method extracts the player's UUID and
     * calls the isUserOnTrack(UUID, String) method to determine if they belong to any of the groups defined in the specified track. If
     * the user is on the track, it returns true; otherwise, it returns false. If an error occurs during the process, an error log will
     * be created and false will be returned.
     * @param player The ServerUser instance representing the player whose track membership is to be checked. This player object contains
     *               the necessary information to identify the specific player in the LuckPerms system.
     * @param track The name of the track to check for the user's membership. This should correspond to a valid track in the LuckPerms system.
     * @return true if the LuckPerms user associated with the specified ServerUser instance is on the specified track, false otherwise.
     * If the user is not found or an error occurs, false will be returned.
     */
    public boolean isUserOnTrack(ServerUser player, String track) {
        return isUserOnTrack(player.getUUID(), track);
    }

    /**
     * Clears a specific meta key from a LuckPerms user's data. This method retrieves the LuckPerms user associated with the provided
     * player UUID and removes any meta nodes that match the specified meta key. After clearing the meta key, the user's data is saved to persist the changes.
     * @param user The LuckPerms User object representing the player whose meta key is to be cleared. This user object should be obtained
     *             from the LuckPerms API and contain the necessary data to identify the player's current meta nodes.
     * @param metaKey The meta key to be cleared from the user's data. This is a string representing the specific meta information that
     *                should be removed from the user's LuckPerms data.
     */
    public void clearMetaKey(User user, String metaKey) {
        try {
            var node = NodeType.META.predicate(mn -> mn.getMetaKey().equals(metaKey));
            user.data().clear(node);
            saveUser(user);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to clear meta key", e);
        }
    }

    /**
     * Clears a specific meta key from a LuckPerms user's data based on their UUID. This method retrieves the LuckPerms user associated
     * with the provided player UUID and removes any meta nodes that match the specified meta key. After clearing the meta key, the user's
     * data is saved to persist the changes.
     * @param playerUUID The UUID of the player whose meta key is to be cleared. This is used to retrieve the corresponding LuckPerms user.
     * @param metaKey The meta key to be cleared from the user's data. This is a string representing the specific meta information that
     *                should be removed from the user's LuckPerms data.
     */
    public void clearMetaKey(UUID playerUUID, String metaKey) {
        try {
            User user = getUser(playerUUID);
            if (user == null) return;
            clearMetaKey(user, metaKey);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to clear meta key", e);
        }
    }

    /**
     * Clears a specific meta key from a LuckPerms user's data based on their ServerPlayer instance. This method extracts the player's
     * UUID and calls the clearMetaKey(UUID, String) method to remove any meta nodes that match the specified meta key. After clearing
     * the meta key, the user's data is saved to persist the changes.
     * @param player The ServerPlayer instance representing the player whose meta key is to be cleared. This player object contains the
     *               necessary information to identify the specific player in the LuckPerms system.
     * @param metaKey The meta key to be cleared from the user's data. This is a string representing the specific meta information that
     *                should be removed from the user's LuckPerms data.
     */
    public void clearMetaKey(ServerPlayer player, String metaKey) {
        clearMetaKey(player.getUUID(), metaKey);
    }

    /**
     * Clears a specific meta key from a LuckPerms user's data based on their ServerUser instance. This method extracts the player's
     * UUID and calls the clearMetaKey(UUID, String) method to remove any meta nodes that match the specified meta key. After clearing
     * the meta key, the user's data is saved to persist the changes.
     * @param player The ServerUser instance representing the player whose meta key is to be cleared. This player object contains the
     *               necessary information to identify the specific player in the LuckPerms system.
     * @param metaKey The meta key to be cleared from the user's data. This is a string representing the specific meta information that
     *                should be removed from the user's LuckPerms data.
     */
    public void clearMetaKey(ServerUser player, String metaKey) {
        clearMetaKey(player.getUUID(), metaKey);
    }
}
