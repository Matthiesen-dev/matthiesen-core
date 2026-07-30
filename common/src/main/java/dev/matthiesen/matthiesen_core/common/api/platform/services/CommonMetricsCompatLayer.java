package dev.matthiesen.matthiesen_core.common.api.platform.services;

/**
 * Interface representing a common metrics compatibility layer. This interface provides methods for initializing server-side
 * metrics tracking, checking online mode status, retrieving player counts, and obtaining platform information. Mods implementing
 * this interface can leverage these methods to integrate with the Matthiesen Core framework's metrics system.
 */
public interface CommonMetricsCompatLayer {

    /**
     * Initializes the server-side metrics compatibility layer. This method should be called during the server initialization
     * phase to set up any necessary metrics tracking and reporting mechanisms.
     */
    void initServer();

    /**
     * Returns true if the client is running in online mode, false otherwise.
     * @return true if the client is running in online mode, false otherwise
     */
    boolean clientOnlineMode();

    /**
     * Returns the number of players currently connected to the client.
     * @return the number of players currently connected to the client
     */
    int clientPlayerCount();

    /**
     * Returns true if the server is running in online mode, false otherwise.
     * @return true if the server is running in online mode, false otherwise
     */
    boolean serverOnlineMode();

    /**
     * Returns the number of players currently connected to the server.
     * @return the number of players currently connected to the server
     */
    int serverPlayerCount();

    /**
     * Returns the platform label, which is a string that identifies the platform on which the mod is running (e.g., "Forge", "Fabric").
     * @return the platform label
     */
    String platformLabel();
}
