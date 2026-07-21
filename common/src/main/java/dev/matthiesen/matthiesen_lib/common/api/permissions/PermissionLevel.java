package dev.matthiesen.matthiesen_lib.common.api.permissions;

/**
 * Enum representing different permission levels for command execution in Minecraft.
 * These levels are based on the standard Minecraft permission levels, which determine what commands a player can execute based on their permissions.
 * The levels are as follows:
 * <ul>
 *     <li>NONE (0): No permissions. The player cannot execute any commands.</li>
 *     <li>SPAWN_PROTECTION_BYPASS (1): The player can bypass spawn protection, but cannot execute any other commands.</li>
 *     <li>CHEAT_COMMANDS_AND_COMMAND_BLOCKS (2): The player can execute cheat commands and use command blocks, but cannot manage multiplayer or execute all commands.</li>
 *     <li>MULTIPLAYER_MANAGEMENT (3): The player can manage multiplayer features, such as kicking and banning players, but cannot execute all commands.</li>
 *     <li>ALL_COMMANDS (4): The player can execute all commands, including those that require cheats and multiplayer management.</li>
 * </ul>
 * <p>
 *     Based on Cobblemon's Permission system
 * </p>
 */
@SuppressWarnings("unused")
public enum PermissionLevel {
    /**
     * No permissions. The player cannot execute any commands.
     */
    NONE(0),
    /**
     * The player can bypass spawn protection, but cannot execute any other commands.
     */
    SPAWN_PROTECTION_BYPASS(1),
    /**
     * The player can execute cheat commands and use command blocks, but cannot manage multiplayer or execute all commands.
     */
    CHEAT_COMMANDS_AND_COMMAND_BLOCKS(2),
    /**
     * The player can manage multiplayer features, such as kicking and banning players, but cannot execute all commands.
     */
    MULTIPLAYER_MANAGEMENT(3),
    /**
     * The player can execute all commands, including those that require cheats and multiplayer management.
     */
    ALL_COMMANDS(4);

    private final int level;

    /**
     * Constructor for the PermissionLevel enum. This constructor is used to associate a numerical value with each permission level, which is used for comparing permission levels and determining if a player has sufficient permissions to execute a command.
     * @param level The numerical value associated with this permission level, where higher values indicate higher permissions. For example, ALL_COMMANDS has a value of 4, which is the highest permission level, while NONE has a value of 0, which is the lowest permission level.
     */
    PermissionLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the numerical value associated with this permission level. This is used for comparing permission levels and determining if a player has
     * sufficient permissions to execute a command.
     *
     * @return The numerical value of this permission level, where higher values indicate higher permissions. For example, ALL_COMMANDS has a value of 4,
     * which is the highest permission level, while NONE has a value of 0, which is the lowest permission level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the PermissionLevel corresponding to the given numerical value. This is used for converting between numerical permission levels and their
     * corresponding enum values. If the provided value does not correspond to any PermissionLevel, an IllegalArgumentException is thrown.
     *
     * @param value The numerical value of the permission level to retrieve. This should be a value between 0 and 4, where 0 corresponds to NONE and 4
     *              corresponds to ALL_COMMANDS.
     * @return The PermissionLevel corresponding to the given numerical value.
     */
    public static PermissionLevel byLevel(int value) {
        for (PermissionLevel level : PermissionLevel.values()) {
            if (level.level == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("No PermissionLevel with numerical value: " + value);
    }
}

