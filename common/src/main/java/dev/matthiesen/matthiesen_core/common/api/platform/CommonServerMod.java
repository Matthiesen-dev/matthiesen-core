package dev.matthiesen.matthiesen_core.common.api.platform;

import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.common.core.economy.EconomyManager;
import dev.matthiesen.matthiesen_core.common.core.metric.MatthiesenCoreMetrics;
import dev.matthiesen.matthiesen_core.common.core.network.NetworkingManager;
import dev.matthiesen.matthiesen_core.common.core.registry.*;

/**
 * Represents a common server mod interface that extends the CommonMod interface. This interface provides methods for accessing
 * various services and managers related to the server-side functionality of the mod, including registry management, permissions
 * management, command registration, webhook notifications, text parsing, metrics collection, networking, creative mode
 * augmentations, and economy management. Mods implementing this interface can leverage these services to enhance their
 * server-side capabilities and integrate with the Matthiesen Core framework.
 */
public interface CommonServerMod extends CommonMod {

    /**
     * Gets the registry builder for this mod. This is used to register various components to the API.
     * @return The registry builder for this mod.
     */
    RegistryBuilder getRegistryBuilder();

    /**
     * Retrieves the PermissionsManager instance. The PermissionsManager is responsible for managing permissions within the application,
     * including checking and validating permissions for various actions and commands.
     * @return The singleton instance of the PermissionsManager, which can be used to manage and validate permissions throughout the application.
     */
    PermissionsManager getPermissionsManager();

    /**
     * Retrieves the CommandsRegistryManager instance. The CommandsRegistryManager is responsible for managing the registration of commands
     * within the application, ensuring that commands are properly registered and available for use.
     * @return The singleton instance of the CommandsRegistryManager, which can be used to register and manage commands throughout the application.
     */
    CommandsRegistryManager getCommandsRegistryManager();

    /**
     * Retrieves the loaded Webhook notifier service. This service is responsible for sending notifications to Discord webhooks.
     * If no implementation is found, a no-op implementation is returned, which does not perform any actions.
     * @return The loaded Webhook notifier service, or a no-op implementation if none is found. This service can be used to send
     * notifications to Discord webhooks, or to perform other actions related to webhook notifications. The no-op implementation
     * is provided to ensure that the application can function without a webhook notifier service, and will not throw any exceptions
     * or errors if the service is not available.
     */
    WebhookNotifierService getWebhookService();

    /**
     * Retrieves the common utilities instance. This instance provides access to various utility methods and services that are used throughout
     * the application, such as server access, configuration management, and other common tasks.
     * @return The common utilities instance, which provides access to various utility methods and services that are used throughout the application.
     */
    CommonLoaderUtils getCommonUtils();

    /**
     * Retrieves the common registry instance. This instance provides access to various registries that are used throughout
     * the application, such as command registries, event registries, and other common registries. The common registry is
     * responsible for managing the registration and retrieval of various components and services within the application,
     * ensuring that they are properly initialized and available for use.
     * @return The common registry instance, which provides access to various registries that are used throughout the application.
     * This instance can be used to register and retrieve various components and services, ensuring that they are properly initialized and available for use.
     */
    CommonLoaderRegistry getCommonRegistry();

    /**
     * Retrieves the text parser manager instance. This instance is responsible for managing text parsers within the application,
     * allowing for the registration and retrieval of text parsers based on their type.
     * @return The singleton instance of the TextParserRegistryManager, which can be used to register and manage text parsers throughout the application.
     */
    TextParserRegistryManager getTextParserManager();

    /**
     * Retrieves the core metrics instance. This instance is responsible for collecting and reporting metrics related to
     * the application's performance,
     * usage, and other relevant data. It provides methods for tracking errors, logging events, and sending metrics to
     * external services for analysis and monitoring.
     * @return The singleton instance of the MatthiesenCoreMetrics, which can be used to collect and report metrics related
     * to the application's performance, usage, and other relevant data.
     */
    MatthiesenCoreMetrics getCoreMetrics();

    /**
     * Retrieves the NetworkingManager instance. This instance is responsible for managing network communications within the application,
     * allowing for the registration of packet types, sending and receiving packets, and handling network events.
     * @return The singleton instance of the NetworkingManager, which can be used to register and manage network communications throughout the application.
     */
    NetworkingManager getNetworkingManager();

    /**
     * Retrieves the CreativeModeAugmentsManager instance. This instance is responsible for managing augmentations (item additions) to creative mode tabs.
     *
     * @return The singleton instance of the CreativeModeAugmentsManager, which can be used to register and retrieve creative tab item augmentations.
     */
    CreativeModeAugmentsManager getCreativeModeAugmentsManager();

    /**
     * Retrieves the Economy Manager instance. This instance is responsible for managing the in-game economy, including currency providers, balance management,
     * deposits, withdrawals, and checking if a player has sufficient funds.
     *
     * @return The singleton instance of the EconomyManager, which can be used to register and retrieve economy providers and manage in-game currency.
     */
    EconomyManager getEconomyManager();
}
