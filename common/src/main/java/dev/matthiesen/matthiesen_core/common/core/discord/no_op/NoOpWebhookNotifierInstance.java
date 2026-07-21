package dev.matthiesen.matthiesen_core.common.core.discord.no_op;

import dev.matthiesen.matthiesen_core.common.api.discord.WebhookClient;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierInstance;

/**
 * A no-operation implementation of the WebhookNotifierInstance interface. This class provides a placeholder implementation that
 * does not perform any actual webhook notifications. It is used when a real webhook notifier is not available or needed.
 * @param webhookUrl The URL of the webhook to be used for notifications. In this no-op implementation, this value is not utilized.
 */
public record NoOpWebhookNotifierInstance(String webhookUrl) implements WebhookNotifierInstance {
    @Override
    public WebhookClient client() {
        return new NoOpWebhookClient();
    }
}
