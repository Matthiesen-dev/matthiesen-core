package dev.matthiesen.matthiesen_core.common.core.discord;

import dev.matthiesen.matthiesen_core.common.api.discord.WebhookClient;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierInstance;

public final class DiscordNotifierInstance implements WebhookNotifierInstance {
    private final String WEBHOOK_URL;

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
