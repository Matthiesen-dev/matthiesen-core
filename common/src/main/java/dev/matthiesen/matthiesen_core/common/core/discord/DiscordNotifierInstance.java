package dev.matthiesen.matthiesen_core.common.core.discord;

import dev.matthiesen.matthiesen_core.common.api.discord.WebhookClient;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierInstance;

/**
 * Represents an instance of a Discord webhook notifier. This class implements the WebhookNotifierInstance interface and provides
 * the necessary methods to send messages to a specific Discord webhook URL. It encapsulates the webhook URL and provides access
 * to a WebhookClient for sending messages.
 */
public final class DiscordNotifierInstance implements WebhookNotifierInstance {
    private final String WEBHOOK_URL;

    /**
     * Constructs a new DiscordNotifierInstance with the specified webhook URL.
     *
     * @param webhookUrl The URL of the Discord webhook to which messages will be sent.
     */
    public DiscordNotifierInstance(String webhookUrl) {
        WEBHOOK_URL = webhookUrl;
    }

    @Override
    public WebhookClient client() {
        return new DiscordWebhookClient();
    }

    @Override
    public String webhookUrl() {
        return WEBHOOK_URL;
    }
}
