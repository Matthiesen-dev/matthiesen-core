package dev.matthiesen.matthiesen_core.common.core.metric.impl;

import com.google.gson.JsonObject;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.platform.ModContainer;
import net.minecraft.client.Minecraft;

/**
 * Client-specific implementation of the UniversalMetrics. This class extends the base UniversalMetricsImpl and provides client-specific data collection and submission logic. It overrides the appendDefaultData method to include a client-specific property in the metrics JSON object, indicating that this is a client metrics submission. The actual submission logic is handled by the superclass, while this subclass focuses on adding client-specific data to the metrics before submission.
 * The client-specific property added in the appendDefaultData method is "client_no_op" with a value of 1. This is a placeholder property that can be used to identify client metrics submissions in the analysis of the collected data. The universal data fields defined in the parent class are also included in the metrics submission, ensuring that both client and server metrics contain consistent information about the mod version and platform.
 */
@SuppressWarnings("UnstableApiUsage")
public final class UniversalMetricsClient extends AbstractUniversalMetric {
    /**
     * Constructs a new UniversalMetricsClientImpl instance with the given factory and mod container. This constructor calls the superclass constructor to initialize the common functionality of the metrics implementation, and then allows for any client-specific initialization if needed. The actual submission logic is handled by the superclass, while this subclass focuses on adding client-specific data to the metrics before submission.
     * @param factory the factory used to create this metrics instance. This is passed to the superclass constructor to initialize the context and other necessary components for metrics collection and submission.
     * @param mod the mod container associated with this metrics instance. This provides access to the mod's information such as version and platform, which can be included in the metrics data.
     * @throws IllegalStateException if there is an issue with initializing the metrics instance, such as invalid configuration or missing dependencies. The actual conditions for throwing this exception depend on the implementation of the superclass and the context initialization.
     */
    public UniversalMetricsClient(final Factory factory, final ModContainer mod) throws IllegalStateException {
        super(factory, mod);
    }

    /**
     * Appends client-specific data to the metrics JSON object. This method is called by the parent class to add common data fields to the metrics before submission. In addition to the universal data added by the parent class, this implementation adds a client-specific property "client_no_op" with a value of 1. This is a placeholder property that can be used to identify client metrics submissions in the analysis of the collected data. The parent class's appendUniversalData method is also called to ensure that universal data fields are included in the metrics submission.
     * @param metrics the JsonObject representing the metrics data that will be submitted. This object is modified by adding a property "client_no_op" with a value of 1, which serves as an identifier for client metrics submissions. The parent class's appendUniversalData method is also called to ensure that universal data fields such as mod version and platform are included in the metrics submission, providing consistent information across both client and server metrics.
     */
    @Override
    protected void appendDefaultData(JsonObject metrics) {
        metrics.addProperty("online_mode", isOnlineMode());
        metrics.addProperty("player_count", getPlayerCount());
        appendUniversalData(metrics, modContainer.getPlatformData().getLabel() + " (client)");
    }

    /**
     * Determines if the client is currently in online mode. This method checks the client's session information to determine if the player is authenticated and connected to a server. The result is used to populate the "online_mode" property in the metrics JSON object, which can be useful for analyzing the distribution of online vs offline players in the collected metrics data.
     * @return true if the client is in online mode (authenticated and connected to a server), false otherwise. This information is derived from the client's session data, and it provides insight into the player's connection status, which can be relevant for understanding player behavior and engagement in the collected metrics.
     */
    private boolean isOnlineMode() {
        if (MatthiesenCoreCommon.INSTANCE.getCommonUtils().isDevelopmentEnvironment())
            return true;
        return Minecraft.getInstance().getUser().getXuid().isPresent();
    }

    /**
     * Retrieves the player count for the client environment. This method checks if the client is currently connected to a server and retrieves the player count accordingly. If the client is connected to a multiplayer server, it retrieves the online player count from the server connection. If the client is running in singleplayer mode, it retrieves the player count from the integrated server. If neither of these conditions are met, it checks if the client player instance is null (indicating that the player has not fully initialized) and returns 0 if it is null, or 1 if it is not null (indicating that there is one player instance present). This method provides an accurate player count for both singleplayer and multiplayer environments on the client side.
     * @return the player count for the client environment, which can be 0 if the player instance is not initialized, 1 if the client is in singleplayer mode with a player instance, or the online player count from the server connection if the client is connected to a multiplayer server.
     */
    private int getPlayerCount() {
        final var connection = Minecraft.getInstance().getConnection();
        if (connection != null) return connection.getOnlinePlayers().size();

        final var server = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) return server.getPlayerCount();

        return Minecraft.getInstance().player == null ? 0 : 1;
    }
}
