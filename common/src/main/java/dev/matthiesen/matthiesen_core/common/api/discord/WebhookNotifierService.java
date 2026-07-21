package dev.matthiesen.matthiesen_core.common.api.discord;

/**
 * This interface defines the contract for a WebhookNotifierService, which is responsible for creating instances of WebhookNotifierInstance
 * that can send notifications to webhooks (e.g., Discord webhooks). Implementations of this interface can vary based on the type of webhook service
 * being used (e.g., Discord, Slack, etc.). The service can be a no-operation (NOOP) implementation that does not perform any actual notifications,
 * or it can be a fully functional implementation that sends notifications to the specified webhook URLs.
 * The WebhookNotifierService provides methods to create instances of WebhookNotifierInstance for specific webhook URLs and to check if the service is
 * available for use. The type of the service can be determined using the type() method, which returns an enum indicating the implementation type (e.g., NOOP, DISCORD).
 */
@SuppressWarnings("unused")
public interface WebhookNotifierService {
    /**
     * Returns the type of the WebhookNotifierService implementation. This can be used to determine the capabilities of the service and to handle different implementations accordingly.
     * @return The type of the WebhookNotifierService, which can be NOOP (no operation) or DISCORD (for Discord webhook notifications).
     */
    TYPE type();

    /**
     * Creates a new instance of a WebhookNotifierInstance for the specified webhook URL. This instance can be used to send notifications to the given webhook.
     * @param webhookUrl The URL of the webhook to which notifications will be sent. This should be a valid webhook URL for the service being used (e.g., Discord).
     * @return A new instance of WebhookNotifierInstance for the specified webhook URL.
     */
    WebhookNotifierInstance makeInstance(String webhookUrl);

    /**
     * Checks if the WebhookNotifierService is available for use. This method returns true if the service is not a NOOP implementation, indicating that it can
     * perform actual webhook notifications.
     * @return true if the service is available for use, false if it is a NOOP implementation.
     */
    default boolean isAvailable() {
        return type() != TYPE.NOOP;
    }

    /**
     * Returns the type of the WebhookNotifierService, which indicates the implementation being used (e.g., NOOP, DISCORD).
     * This can be useful for determining the capabilities of the service and for conditional logic based on the type.
     */
    enum TYPE {
        /**
         * Indicates that the WebhookNotifierService is a no-operation implementation, meaning it does not perform any actual webhook notifications.
         */
        NOOP,

        /**
         * Indicates that the WebhookNotifierService is implemented for Discord, allowing for sending notifications to Discord webhooks.
         */
        DISCORD
    }
}
