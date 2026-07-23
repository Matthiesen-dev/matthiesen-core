package dev.matthiesen.matthiesen_core.common.api.discord;

import dev.matthiesen.matthiesen_core.common.core.discord.model.WebhookMessage;
import dev.matthiesen.matthiesen_core.common.core.discord.model.WebhookMessageBuilder;
import dev.matthiesen.matthiesen_core.common.api.exceptions.DiscordWebhookException;

import java.util.function.Consumer;

/**
 * This interface defines the contract for a notifier that sends messages to Discord webhooks. Implementations of this interface
 * are responsible for providing a WebhookClient instance, the webhook URL, and methods to send messages to the configured Discord webhook.
 * The interface also provides default methods to check if the webhook is configured and to send messages using either a WebhookMessage or
 * a builder pattern. Implementations should handle any potential errors that may arise during the process of sending messages to Discord's webhook API.
 */
@SuppressWarnings("unused")
public interface WebhookNotifierInstance {
    /**
     * Returns the WebhookClient instance used to send messages to Discord webhooks. This client is responsible for handling the actual communication with Discord's webhook API.
     * @return the WebhookClient instance used for sending messages. Implementations of this interface should provide a concrete instance of WebhookClient.
     */
    WebhookClient client();

    /**
     * Returns the Discord webhook URL to which messages will be sent. This URL is used by the WebhookClient to send messages to the appropriate Discord channel.
     * @return the Discord webhook URL as a String. If the webhook is not configured, this method may return null or an empty string.
     */
    String webhookUrl();

    /**
     * Checks if the webhook is configured. A webhook is considered configured if the webhook URL is not null and not empty.
     * @return true if the webhook is configured, false otherwise.
     */
    default boolean isConfigured() {
        return webhookUrl() != null && !webhookUrl().isEmpty();
    }

    /**
     * Checks if the webhook is not configured. A webhook is considered not configured if the webhook URL is null or empty.
     * @return true if the webhook is not configured, false otherwise.
     */
    default boolean isNotConfigured() {
        return webhookUrl() == null || webhookUrl().isEmpty();
    }

    /**
     * Sends a message to the Discord webhook. If the webhook is not configured, the method will return without sending a message.
     * @param message The message to be sent to the Discord webhook.
     * @throws DiscordWebhookException If there is an error while sending the message to the Discord webhook.
     */
    default void sendMessage(WebhookMessage message) throws DiscordWebhookException {
        if (isNotConfigured()) return;
        client().sendMessage(webhookUrl(), message);
    }

    /**
     * Sends a message to the Discord webhook using a builder pattern. The provided consumer is used to configure the message
     * before it is sent. If the webhook is not configured, the method will return without sending a message.
     * @param messageBuilderConsumer A consumer that accepts a WebhookMessageBuilder to configure the message to be sent.
     * @throws DiscordWebhookException If there is an error while sending the message to the Discord webhook.
     */
    default void sendMessage(Consumer<WebhookMessageBuilder> messageBuilderConsumer) throws DiscordWebhookException {
        if (isNotConfigured()) return;
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        messageBuilderConsumer.accept(builder);
        sendMessage(builder.build());
    }
}
