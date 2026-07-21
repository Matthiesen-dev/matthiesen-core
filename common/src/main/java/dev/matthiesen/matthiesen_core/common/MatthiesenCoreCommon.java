package dev.matthiesen.matthiesen_core.common;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderEventsListeners;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.common.core.permissions.PermissionsManager;
import dev.matthiesen.matthiesen_core.common.core.registry.CommandsRegistryManager;
import dev.matthiesen.matthiesen_core.common.core.registry.PlayerEventsManager;
import dev.matthiesen.matthiesen_core.common.core.registry.ServerEventsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

@SuppressWarnings("unused")
public final class MatthiesenCoreCommon {
    /**
     * The unique identifier for the mod, used for registration and identification purposes. This constant is used throughout
     * the application to refer to the mod in a consistent manner.
     */
    public static final String MOD_ID = "matthiesen_lib";

    /**
     * The name of the mod, used for logging and identification purposes. This constant is used throughout the application
     * to refer to the mod in a consistent manner.
     */
    public static final String MOD_NAME = "Matthiesen Lib";

    private static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    private static final CommonLoaderUtils COMMON_UTILS =
            ServiceLoader.load(CommonLoaderUtils.class).findFirst().orElseThrow();
    private static final CommonLoaderRegistry COMMON_REGISTRY =
            ServiceLoader.load(CommonLoaderRegistry.class).findFirst().orElseThrow();
    private static final CommonLoaderEventsListeners COMMON_EVENTS_LISTENERS =
            ServiceLoader.load(CommonLoaderEventsListeners.class).findFirst().orElseThrow();

    /**
     * Singleton instance of the MatthiesenLibCommon. This instance is used to manage the common utilities and registry across the application.
     * It is initialized lazily and is thread-safe, ensuring that only one instance exists throughout the lifecycle of the application.
     */
    public static final MatthiesenCoreCommon INSTANCE = new MatthiesenCoreCommon();

    private boolean initialized;

    private MatthiesenCoreCommon() {}

    /**
     * Initializes the MatthiesenLibCommon instance. This method sets up the PermissionsManager and marks the instance as initialized.
     */
    public void initialize() {
        if (initialized) return;

        PermissionsManager.INSTANCE.initialize(COMMON_REGISTRY);
        CommandsRegistryManager.INSTANCE.initialize(COMMON_REGISTRY);
        PlayerEventsManager.INSTANCE.initialize(COMMON_EVENTS_LISTENERS);
        ServerEventsManager.INSTANCE.initialize(COMMON_EVENTS_LISTENERS);

        initialized = true;
        createInfoLog("Initialized Common");
    }

    /**
     * Creates an informational log message using the internal logger. This method is used to log messages that are intended
     * informational purposes, such as status updates or general information about the application's state.
     * @param message The message to be logged. This should be a concise and clear description of the information being conveyed.
     */
    public void createInfoLog(String message) {
        LOGGER.info(message);
    }

    /**
     * Creates a warning log message using the internal logger. This method is used to log messages that indicate potential
     * issues or situations that may require attention, but do not necessarily indicate an error.
     * @param message The message to be logged. This should describe the warning or potential issue in a clear and concise manner.
     */
    public void createWarnLog(String message) {
        LOGGER.warn(message);
    }

    /**
     * Creates a debug log message using the internal logger. This method is used to log messages that are intended for
     * debugging purposes, providing detailed information about the application's state and behavior for developers.
     * @param message The message to be logged. This should provide detailed information that can help in diagnosing issues
     *                or understanding the application's flow during development and debugging.
     */
    public void createDebugLog(String message) {
        LOGGER.debug(message);
    }

    /**
     * Creates an error log message using the internal logger. This method is used to log messages that indicate errors or
     * exceptions that have occurred within the application, providing information about the nature of the error and any relevant context.
     * @param message The message to be logged. This should describe the error or exception in a clear and concise manner,
     *                providing enough context to understand the issue.
     */
    public void createErrorLog(String message) {
        LOGGER.error(message);
    }

    /**
     * Creates an error log message with an associated throwable using the internal logger. This method is used to log messages
     * that indicate errors or exceptions that have occurred within the application, along with the stack trace of the throwable for debugging purposes.
     * @param message The message to be logged. This should describe the error or exception in a clear and concise manner,
     *                providing enough context to understand the issue.
     * @param throwable The throwable associated with the error, which provides the stack trace and additional context for
     *                  debugging. This allows developers to trace the source of the error and understand
     */
    public void createErrorLog(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }

    /**
     * Retrieves the PermissionsManager instance. The PermissionsManager is responsible for managing permissions within the application,
     * including checking and validating permissions for various actions and commands.
     * @return The singleton instance of the PermissionsManager, which can be used to manage and validate permissions throughout the application.
     */
    public PermissionsManager getPermissionsManager() {
        return PermissionsManager.INSTANCE;
    }

    /**
     * Retrieves the CommandsRegistryManager instance. The CommandsRegistryManager is responsible for managing the registration of commands
     * within the application, ensuring that commands are properly registered and available for use.
     * @return The singleton instance of the CommandsRegistryManager, which can be used to register and manage commands throughout the application.
     */
    public CommandsRegistryManager getCommandsRegistryManager() {
        return CommandsRegistryManager.INSTANCE;
    }

    /**
     * Retrieves the PlayerEventsManager instance. The PlayerEventsManager is responsible for managing player event listeners within the application,
     * @return The singleton instance of the PlayerEventsManager, which can be used to register and manage player event listeners throughout the application.
     */
    public PlayerEventsManager getPlayerEventsManager() {
        return PlayerEventsManager.INSTANCE;
    }

    /**
     * Retrieves the ServerEventsManager instance. The ServerEventsManager is responsible for managing server event listeners within the application,
     * @return The singleton instance of the ServerEventsManager, which can be used to register and manage server event listeners throughout the application.
     */
    public ServerEventsManager getServerEventsManager() {
        return ServerEventsManager.INSTANCE;
    }
}
