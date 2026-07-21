package dev.matthiesen.matthiesen_core.common.metric;

import com.google.gson.JsonObject;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.events.ServerEventListener;
import dev.matthiesen.matthiesen_core.common.api.platform.ModContainer;
import net.minecraft.server.MinecraftServer;

/**
 * This class implements the UniversalMetrics interface for the server environment. It listens for server start and stop events to manage the lifecycle of the metrics collection.
 * When the server starts, it initializes the metrics collection and starts submitting data. When the server stops, it shuts down the metrics collection to ensure that all data is properly sent before the server fully shuts down. The metrics collected include server-specific information such as Minecraft version, online mode status, and player count, in addition to any universal data defined in the parent class.
 */
@SuppressWarnings("UnstableApiUsage")
public final class UniversalMetricsServer extends AbstractUniversalMetric {
    private volatile MinecraftServer SERVER;

    /**
     * Constructs a new UniversalMetricsServerImpl instance with the given factory and mod container. This constructor registers a server event handler to listen for server start and stop events, allowing it to manage the lifecycle of the metrics collection based on these events. When the server starts, it initializes the metrics collection and starts submitting data. When the server stops, it shuts down the metrics collection to ensure that all data is properly sent before the server fully shuts down.
     * @param factory the factory used to create this metrics instance. This is passed to the superclass constructor to initialize the context and other necessary components for metrics collection and submission.
     * @param mod the mod container associated with this metrics instance. This provides access to the mod's information such as version and platform, which can be included in the metrics data.
     * @throws IllegalStateException if there is an issue with initializing the metrics instance, such as invalid configuration or missing dependencies. The actual conditions for throwing this exception depend on the implementation of the superclass and the context initialization.
     */
    public UniversalMetricsServer(final Factory factory, final ModContainer mod) throws IllegalStateException {
        super(factory, mod);
        MatthiesenCoreCommon.INSTANCE.getServerEventsManager().registerListener(MatthiesenCoreCommon.MOD_ID + "_metrics_server", new ServerEventListener() {
            @Override
            public void onServerStarting(MinecraftServer server) {
                SERVER = server;
            }

            @Override
            public void onServerStopping(MinecraftServer server) {
                shutdown();
            }
        });
    }

    /**
     * Appends server-specific data to the metrics JSON object. This method is called by the parent class to add common data fields to the metrics before submission. In addition to the universal data added by the parent class, this implementation adds server-specific information such as the Minecraft version, online mode status, and player count. This allows for more detailed analysis of the server environment in which the mod is running, providing valuable insights for both mod developers and users.
     * @param metrics the JsonObject representing the metrics data that will be submitted. This object is modified by adding properties for the Minecraft version, online mode status, and player count, which are obtained from the SERVER instance. The parent class's appendUniversalData method is also called to ensure that universal data fields are included in the metrics submission.
     */
    @Override
    protected void appendDefaultData(final JsonObject metrics) {
        if (SERVER == null) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Attempted to append server metrics data before server was initialized");
            return;
        }
        metrics.addProperty("online_mode", isOnlineMode());
        metrics.addProperty("player_count", SERVER.getPlayerCount());
        appendUniversalData(metrics);
    }

    /**
     * Determines whether the server is running in online mode. This method checks if the server is in a development environment, in which case it assumes online mode is enabled for testing purposes. If not in a development environment, it checks the server's authentication settings to determine if online mode is enabled. This information is important for metrics collection as it can affect player behavior and server performance, providing valuable insights for mod developers and users.
     * @return true if the server is running in online mode, false otherwise. The method first checks if the server is in a development environment, returning true if it is. If not in a development environment, it checks the server's authentication settings using the SERVER.usesAuthentication() method to determine if online mode is enabled, returning the result accordingly.
     */
    private boolean isOnlineMode() {
        if (MatthiesenCoreCommon.INSTANCE.getCommonUtils().isDevelopmentEnvironment())
            return true;
        return SERVER.usesAuthentication();
    }
}
