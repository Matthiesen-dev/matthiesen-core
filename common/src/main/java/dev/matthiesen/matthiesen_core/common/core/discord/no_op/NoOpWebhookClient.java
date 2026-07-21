package dev.matthiesen.matthiesen_core.common.core.discord.no_op;

import dev.matthiesen.matthiesen_core.common.api.discord.WebhookClient;
import dev.matthiesen.matthiesen_core.common.core.discord.model.WebhookMessage;
import dev.matthiesen.matthiesen_core.common.exceptions.DiscordWebhookException;

/**
 * A no-operation implementation of the WebhookClient interface. This class provides a placeholder implementation that
 * does not perform any actual webhook operations. It is used when a real webhook client is not available or needed.
 */
public final class NoOpWebhookClient implements WebhookClient {
    /**
     * Constructs a new instance of the NoOpWebhookClient. This client does not perform any actual webhook operations and serves as a placeholder implementation.
     */
    public NoOpWebhookClient() {}

    @Override
    public void sendMessage(String webhookUrl, WebhookMessage message) throws DiscordWebhookException {
        // No-Op
    }
}
