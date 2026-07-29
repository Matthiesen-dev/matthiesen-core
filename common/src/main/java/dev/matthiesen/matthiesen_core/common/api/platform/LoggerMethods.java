package dev.matthiesen.matthiesen_core.common.api.platform;

import org.apache.logging.log4j.Logger;

/**
 * Interface representing logging methods for a mod. This interface provides methods for logging messages at different levels
 * (info, warn, error) and tracking errors.
 */
public interface LoggerMethods {
    /**
     * Get the mod's logger
     * @return The mod's logger
     */
    Logger getLogger();

    /**
     * Track an error using the mod's error tracking system.
     * @param throwable The throwable to track
     */
    void trackError(Throwable throwable);

    /**
     * Send an info log message using the mod's logger.
     * @param message The message to log
     */
    default void createInfoLog(String message) {
        getLogger().info(message);
    }

    /**
     * Send a debug log message using the mod's logger.
     * @param message The message to log
     */
    default void createDebugLog(String message) {
        getLogger().debug(message);
    }

    /**
     * Send a warning log message using the mod's logger.
     * @param message The message to log
     */
    default void createWarnLog(String message) {
        getLogger().warn(message);
    }

    /**
     * Send an error log message using the mod's logger.
     * @param message The message to log
     */
    default void createErrorLog(String message) {
        getLogger().error(message);
    }

    /**
     * Send an error log message with a throwable using the mod's logger.
     * @param message The message to log
     * @param throwable The throwable to log
     */
    default void createErrorLog(String message, Throwable throwable) {
        getLogger().error(message, throwable);
        trackError(throwable);
    }
}
