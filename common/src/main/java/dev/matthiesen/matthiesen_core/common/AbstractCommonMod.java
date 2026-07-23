package dev.matthiesen.matthiesen_core.common;

import dev.matthiesen.libs.faststats.ErrorTracker;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.metric.MatthiesenCoreMetrics;
import dev.matthiesen.matthiesen_core.common.core.metric.UniversalMetricProvider;
import dev.matthiesen.matthiesen_core.common.core.metric.impl.UniversalMetricContext;
import dev.matthiesen.matthiesen_core.common.core.network.NetworkingManager;
import dev.matthiesen.matthiesen_core.common.core.registry.PermissionsManager;
import dev.matthiesen.matthiesen_core.common.core.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract class representing a common mod. This class provides basic functionality for initializing the mod,
 * tracking errors, and managing metrics. Mods should extend this class to leverage the common functionality.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommonMod {
    private final String MOD_ID;
    private final String MOD_NAME;
    private final Logger LOGGER;
    private final ErrorTracker errorTracker;
    private final UniversalMetricContext metricContext;
    private final RegistryBuilder registryBuilder;

    /**
     * The Metric's Token from FastStats.dev for the Mod/Plugin
     * @return The Token as a String, or null if Metrics are not used
     */
    public abstract @Nullable @Token String getMetricsToken();

    /**
     * Initializes the mod with the given mod ID and mod name.
     * @param MOD_ID The mod ID
     * @param MOD_NAME The mod name
     */
    public AbstractCommonMod(String MOD_ID, String MOD_NAME) {
        this.MOD_ID = MOD_ID;
        this.MOD_NAME = MOD_NAME;
        this.LOGGER = LogManager.getLogger(MOD_NAME);
        this.registryBuilder = new RegistryBuilder(MOD_ID);

        var metricsToken = getMetricsToken();
        UniversalMetricProvider metricsProvider;
        if (metricsToken != null) {
            metricsProvider = new UniversalMetricProvider(MOD_ID, metricsToken);
            errorTracker = metricsProvider.makeErrorTracker();
            metricContext = metricsProvider.buildErrorTrackingMetricProvider(errorTracker);
        } else {
            errorTracker = null;
            metricContext = null;
        }
    }

    /**
     * Initializes the mod with the given mod ID. The mod name will be set to the same value as the mod ID.
     * @param MOD_ID The mod ID
     */
    public AbstractCommonMod(String MOD_ID) {
        this(MOD_ID, MOD_ID);
    }

    /**
     * Initializes the mod. This should be called in the mod's main class during initialization.
     */
    public void initialize() {
        MatthiesenCoreCommon.INSTANCE.registerModToMetrics(MOD_ID);
    }

    /**
     * Tracks an error using the mod's error tracker. If the error tracker is not initialized, this method does nothing.
     * @param throwable The error to be tracked. This should be a Throwable object representing the error that occurred. If the error tracker is not initialized, this method does nothing.
     */
    public void trackError(Throwable throwable) {
        if (errorTracker != null) {
            errorTracker.trackError(throwable);
        }
    }

    /**
     * Get the mod's metric context. This is used to send metrics to FastStats.dev. If the metric context is not initialized, this method returns null.
     * @return The mod's metric context, or null if the metric context is not initialized. The metric context is used to send metrics to FastStats.dev, and
     * is initialized if the mod has a valid metrics token. If the mod does not have a valid metrics token, this method returns null.
     */
    public UniversalMetricContext getMetricContext() {
        return metricContext;
    }

    /**
     * Get the mod's ID
     * @return The mod's ID
     */
    public String getModId() {
        return MOD_ID;
    }

    /**
     * Get the mod's name
     * @return The mod's name
     */
    public String getModName() {
        return MOD_NAME;
    }

    /**
     * Get the mod's logger
     * @return The mod's logger
     */
    public Logger getLogger() {
        return LOGGER;
    }

    /**
     * Send an info log message using the mod's logger.
     * @param message The message to log
     */
    public void createInfoLog(String message) {
        getLogger().info(message);
    }

    /**
     * Send a warning log message using the mod's logger.
     * @param message The message to log
     */
    public void createWarnLog(String message) {
        getLogger().warn(message);
    }

    /**
     * Send an error log message using the mod's logger.
     * @param message The message to log
     */
    public void createErrorLog(String message) {
        getLogger().error(message);
    }

    /**
     * Send an error log message with a throwable using the mod's logger.
     * @param message The message to log
     * @param throwable The throwable to log
     */
    public void createErrorLog(String message, Throwable throwable) {
        getLogger().error(message, throwable);
        trackError(throwable);
    }

    /**
     * Gets the registry builder for this mod. This is used to register various components to the API.
     * @return The registry builder for this mod.
     */
    public RegistryBuilder getRegistryBuilder() {
        return registryBuilder;
    }

    /**
     * Retrieves the PermissionsManager instance. The PermissionsManager is responsible for managing permissions within the application,
     * including checking and validating permissions for various actions and commands.
     * @return The singleton instance of the PermissionsManager, which can be used to manage and validate permissions throughout the application.
     */
    public PermissionsManager getPermissionsManager() {
        return MatthiesenCoreCommon.INSTANCE.getPermissionsManager();
    }

    /**
     * Retrieves the CommandsRegistryManager instance. The CommandsRegistryManager is responsible for managing the registration of commands
     * within the application, ensuring that commands are properly registered and available for use.
     * @return The singleton instance of the CommandsRegistryManager, which can be used to register and manage commands throughout the application.
     */
    public CommandsRegistryManager getCommandsRegistryManager() {
        return MatthiesenCoreCommon.INSTANCE.getCommandsRegistryManager();
    }

    /**
     * Retrieves the loaded Webhook notifier service. This service is responsible for sending notifications to Discord webhooks.
     * If no implementation is found, a no-op implementation is returned, which does not perform any actions.
     * @return The loaded Webhook notifier service, or a no-op implementation if none is found. This service can be used to send
     * notifications to Discord webhooks, or to perform other actions related to webhook notifications. The no-op implementation
     * is provided to ensure that the application can function without a webhook notifier service, and will not throw any exceptions
     * or errors if the service is not available.
     */
    public WebhookNotifierService getWebhookService() {
        return MatthiesenCoreCommon.INSTANCE.getWebhookService();
    }

    /**
     * Retrieves the common utilities instance. This instance provides access to various utility methods and services that are used throughout
     * the application, such as server access, configuration management, and other common tasks.
     * @return The common utilities instance, which provides access to various utility methods and services that are used throughout the application.
     */
    public CommonLoaderUtils getCommonUtils() {
        return MatthiesenCoreCommon.INSTANCE.getCommonUtils();
    }

    /**
     * Retrieves the common registry instance. This instance provides access to various registries that are used throughout
     * the application, such as command registries, event registries, and other common registries. The common registry is
     * responsible for managing the registration and retrieval of various components and services within the application,
     * ensuring that they are properly initialized and available for use.
     * @return The common registry instance, which provides access to various registries that are used throughout the application.
     * This instance can be used to register and retrieve various components and services, ensuring that they are properly initialized and available for use.
     */
    public CommonLoaderRegistry getCommonRegistry() {
        return MatthiesenCoreCommon.INSTANCE.getCommonRegistry();
    }

    /**
     * Retrieves the text parser manager instance. This instance is responsible for managing text parsers within the application,
     * allowing for the registration and retrieval of text parsers based on their type.
     * @return The singleton instance of the TextParserRegistryManager, which can be used to register and manage text parsers throughout the application.
     */
    public TextParserRegistryManager getTextParserManager() {
        return MatthiesenCoreCommon.INSTANCE.getTextParserManager();
    }

    /**
     * Retrieves the core metrics instance. This instance is responsible for collecting and reporting metrics related to
     * the application's performance,
     * usage, and other relevant data. It provides methods for tracking errors, logging events, and sending metrics to
     * external services for analysis and monitoring.
     * @return The singleton instance of the MatthiesenCoreMetrics, which can be used to collect and report metrics related
     * to the application's performance, usage, and other relevant data.
     */
    public MatthiesenCoreMetrics getCoreMetrics() {
        return MatthiesenCoreCommon.INSTANCE.getCoreMetrics();
    }

    /**
     * Retrieves the NetworkingManager instance. This instance is responsible for managing network communications within the application,
     * allowing for the registration of packet types, sending and receiving packets, and handling network events.
     * @return The singleton instance of the NetworkingManager, which can be used to register and manage network communications throughout the application.
     */
    public NetworkingManager getNetworkingManager() {
        return MatthiesenCoreCommon.INSTANCE.getNetworkingManager();
    }

    /**
     * Retrieves the CreativeModeAugmentsManager instance. This instance is responsible for managing augmentations (item additions) to creative mode tabs.
     *
     * @return The singleton instance of the CreativeModeAugmentsManager, which can be used to register and retrieve creative tab item augmentations.
     */
    public CreativeModeAugmentsManager getCreativeModeAugmentsManager() {
        return MatthiesenCoreCommon.INSTANCE.getCreativeModeAugmentsManager();
    }
}
