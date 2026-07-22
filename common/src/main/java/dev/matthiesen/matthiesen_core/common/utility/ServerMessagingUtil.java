package dev.matthiesen.matthiesen_core.common.utility;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Utility class for sending messages to players and the console on the Minecraft server. This class provides static methods
 * to send messages to all players, the console, or both. It also includes convenience methods that automatically retrieve
 * the server instance from MatthiesenLibApi, allowing you to send messages without needing to pass the server instance explicitly.
 * If the server instance is not available (i.e., null), the methods will simply do nothing, preventing any potential errors from
 * trying to send messages when the server is not initialized.
 */
@SuppressWarnings("unused")
public final class ServerMessagingUtil {
    private static MinecraftServer getServer() {
        return MatthiesenCoreCommon.INSTANCE.getCommonUtils().getServer();
    }

    /**
     * Default constructor for the ServerMessagingUtil class. This constructor is used when creating a new instance of the
     * ServerMessagingUtil class. Since all methods in this class are static, there is no need to create an instance of the
     * class to use its methods, and this constructor is private to prevent instantiation.
     */
    private ServerMessagingUtil() {}

    /**
     * Sends a message to all players on the server. If the server is null, this method will do nothing.
     * @param server The Minecraft server to send the message to. If this is null, the method will do nothing.
     * @param message The message to send to all players. This should be a Component that can contain text, colors, and other formatting.
     */
    public static void sendToAllPlayers(MinecraftServer server, Component message) {
        if (server == null) return;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(message, false);
        }
    }

    /**
     * Convenience method to send a message to all players using the server instance from MatthiesenLibApi. If the server is null, this method will do nothing.
     * @param message The message to send to all players. This should be a Component that can contain text, colors, and other formatting.
     */
    public static void sendToAllPlayers(Component message) {
        MinecraftServer server = getServer();
        sendToAllPlayers(server, message);
    }

    /**
     * Sends a message to the server console. If the server is null, this method will do nothing.
     * @param server The Minecraft server to send the message to. If this is null, the method will do nothing.
     * @param message The message to send to the console. This should be a Component that can contain text, colors, and other formatting.
     */
    public static void sendToConsole(MinecraftServer server, Component message) {
        if (server == null) return;
        server.sendSystemMessage(message);
    }

    /**
     * Convenience method to send a message to the console using the server instance from MatthiesenLibApi. If the server is null, this method will do nothing.
     * @param message The message to send to the console. This should be a Component that can contain text, colors, and other formatting.
     */
    public static void sendToConsole(Component message) {
        MinecraftServer server = getServer();
        sendToConsole(server, message);
    }

    /**
     * Sends a message to all players and the console. If the server is null, this method will do nothing.
     * @param server The Minecraft server to send the message to. If this is null, the method will do nothing.
     * @param message The message to send to all players and the console. This should be a Component that can contain text, colors, and other formatting.
     */
    public static void sendToAllAndConsole(MinecraftServer server, Component message) {
        sendToAllPlayers(server, message);
        sendToConsole(server, message);
    }

    /**
     * Convenience method to send a message to all players and the console using the server instance from MatthiesenLibApi.
     * If the server is null, this method will do nothing.
     * @param message The message to send to all players and the console. This should be a Component that can contain text, colors, and other formatting.
     */
    public static void sendToAllAndConsole(Component message) {
        MinecraftServer server = getServer();
        sendToAllAndConsole(server, message);
    }
}
