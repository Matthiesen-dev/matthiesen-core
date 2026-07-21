package dev.matthiesen.matthiesen_lib.common.api.permissions;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Interface for validating permissions for players and command sources. This is used to allow mods to implement their own permission systems,
 * such as integrating with a permissions mod or using a custom permission system.
 * The PermissionValidator is used by the CommandManager to validate permissions for commands. When a command is executed, the CommandManager will call
 * the appropriate hasPermission method on the PermissionValidator to determine if the player or command source has the required permission to execute the
 * command. The PermissionValidator can be implemented to check permissions based on a variety of factors, such as the player's UUID, their permission level,
 * or any other criteria that the mod developer chooses to implement. This allows for a high degree of flexibility in how permissions are handled, and allows
 * mods to integrate with existing permission systems or create their own custom permission systems as needed.
 *
 * <p>
 *     Based on Cobblemon's Permission system
 * </p>
 */
@SuppressWarnings("unused")
public interface PermissionValidator {
    /**
     * Invoked when the validator replaces the existing one.
     */
    void initialize();

    /**
     * Validates a permission for ServerPlayer.
     *
     * @param player The target ServerPlayer.
     * @param permission The Permission being queried.
     * @return If the player has the permission.
     */
    boolean hasPermission(ServerPlayer player, Permission permission);

    /**
     * Validates a permission for ServerPlayer based only on a permission string and a permission level.
     *
     * @param player The target ServerPlayer.
     * @param permission The permission string being queried such.
     * @param level The permission level being queried. 4 is generally used for cheats.
     * @return If the player has the permission.
     */
    boolean hasPermission(ServerPlayer player, String permission, int level);

    /**
     * Validates a permission for CommandSourceStack. This is used for validating permissions for command sources
     * that are not players, such as the console or command blocks.
     *
     * @param source The CommandSourceStack representing the source of the command. This can be used to determine if the source is a player, console, command block, etc.
     * @param permission The Permission being queried.
     * @return If the source has the permission. For non-player sources, this should generally return true if the permission level is sufficient, as non-player sources
     * are often considered to have all permissions. However, this can be customized based on the needs of the mod.
     */
    boolean hasPermission(CommandSourceStack source, Permission permission);

    /**
     * Validates a permission for CommandSourceStack based only on a permission string and a permission level. This is used for validating permissions for command sources
     *
     * @param source The CommandSourceStack representing the source of the command. This can be used to determine if the source is a player, console, command block, etc.
     * @param permission The permission string being queried.
     * @param level The permission level being queried. 4 is generally used for cheats.
     * @return If the source has the permission. For non-player sources, this should generally return true if the permission level is sufficient, as non-player sources
     * are often considered to have all permissions. However, this can be customized based on the needs of the mod.
     */
    boolean hasPermission(CommandSourceStack source, String permission, int level);
}
