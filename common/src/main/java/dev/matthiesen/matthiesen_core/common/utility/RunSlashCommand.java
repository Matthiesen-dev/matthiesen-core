package dev.matthiesen.matthiesen_core.common.utility;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Utility class for running slash commands on the Minecraft server.
 *
 * <p>The API variant only exposes methods that require an explicit {@link MinecraftServer} instance,
 * making it safe for server-only projects that should not depend on the MatthiesenLib mod facade.</p>
 */
@SuppressWarnings("unused")
public class RunSlashCommand {
    /**
     * Default constructor for RunSlashCommand. This constructor is protected so the common-layer convenience
     * wrapper can extend this class while preventing normal instantiation.
     */
    protected RunSlashCommand() {}

    /**
     * Executes a slash command as the server using a provided MinecraftServer instance. This method allows you to run any
     * command with the server's permissions and context.
     * @param server The MinecraftServer instance on which to execute the command. This allows you to specify a server instance
     *               directly, which can be useful in certain contexts where the server is already available.
     * @param command The command string to execute, without the leading slash. For example, "say Hello world!".
     */
    public static void asServer(MinecraftServer server, String command) {
        try {
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
        } catch (RuntimeException e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("An error occurred while executing the command: " + command, e);
        }
    }

    /**
     * Executes a slash command as a specific player using a provided MinecraftServer instance. This method allows you to run
     * any command with the player's permissions and context.
     * @param server The MinecraftServer instance on which to execute the command. This allows you to specify a server instance
     *               directly, which can be useful in certain contexts where the server is already available.
     * @param player The ServerPlayer instance representing the player as whom the command will be executed. The command will
     *               be executed with this player's permissions and context.
     * @param command The command string to execute, without the leading slash. For example, "say Hello world!".
     */
    public static void asPlayer(MinecraftServer server, ServerPlayer player, String command) {
        try {
            server.getCommands().performPrefixedCommand(player.createCommandSourceStack(), command);
        } catch (RuntimeException e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("An error occurred while executing the command: " + command, e);
        }
    }
}

