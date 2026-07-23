package dev.matthiesen.matthiesen_core.common.api.discord;

import dev.matthiesen.matthiesen_core.common.core.discord.model.WebhookMessage;
import dev.matthiesen.matthiesen_core.common.core.discord.model.WebhookMessageBuilder;
import dev.matthiesen.matthiesen_core.common.api.exceptions.DiscordWebhookException;

import java.util.function.Consumer;

/**
 * Interface for sending messages to Discord webhooks.
 */
@SuppressWarnings("unused")
public interface WebhookClient {
    /**
     * Sends a message to the specified Discord webhook URL.
     * @param webhookUrl The URL of the Discord webhook to send the message to.
     * @param message The message to be sent, encapsulated in a WebhookMessage object.
     * @throws DiscordWebhookException If an error occurs during message sending, such as network issues or invalid webhook URL.
     */
    void sendMessage(String webhookUrl, WebhookMessage message) throws DiscordWebhookException;

    /**
     * Sends a message to the specified Discord webhook URL using a builder pattern.
     * @param webhookUrl The URL of the Discord webhook to send the message to.
     * @param messageBuilderConsumer A consumer that accepts a WebhookMessageBuilder to build the message to be sent.
     * @throws DiscordWebhookException If an error occurs during message sending, such as network issues or invalid webhook URL.
     */
    default void sendMessage(String webhookUrl, Consumer<WebhookMessageBuilder> messageBuilderConsumer) throws DiscordWebhookException {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        messageBuilderConsumer.accept(builder);
        sendMessage(webhookUrl, builder.build());
    }
}

