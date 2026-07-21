package dev.matthiesen.matthiesen_lib.common.api.permissions;

import net.minecraft.resources.ResourceLocation;

/**
 * Interface representing a permission that can be checked against a player's permissions. This is used for command permissions and other permission checks throughout the mod.
 * The Permission interface provides a standardized way to define permissions that can be checked against a player's permissions. Each Permission has a unique identifier,
 * a literal string representation, and a permission level. The identifier is used for registration and lookup, while the literal string is used for checking against a
 * player's permissions.
 *
 * <p>
 *     Based on Cobblemon's Permission system
 * </p>
 */
@SuppressWarnings("unused")
public interface Permission {

    /**
     * Gets the unique identifier for this permission. This identifier is used for registration and lookup of the permission within the permission system.
     * @return The unique identifier for this permission, which is used for registration and lookup.
     */
    ResourceLocation getIdentifier();

    /**
     * Gets the literal string representation of this permission. This is the string that is used for checking against a player's
     * permissions. The literal string should be in a format that is compatible with the permission system being used (e.g., "modid.permission_name").
     * @return The literal string representation of this permission, which is used for checking against a player's permissions.
     */
    String getLiteral();

    /**
     * Gets the permission level for this permission. The permission level determines how this permission is checked against a player's
     * permissions. For example, in Minecraft's built-in permission system, a permission level of 0 means that the player must be an operator to have
     * the permission, while a permission level of 1 or higher means that the player must have a certain permission level to have the permission. The specific
     * meaning of the permission level may vary depending on the permission system being used, but it generally indicates the required level of permission
     * a player must have to be granted this permission.
     * @return The level of this permission, which determines how it is checked against a player's permissions.
     */
    PermissionLevel getLevel();
}

