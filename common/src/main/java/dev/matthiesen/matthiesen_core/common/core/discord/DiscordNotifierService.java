package dev.matthiesen.matthiesen_core.common.core.discord;

import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierInstance;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;

import java.util.HashMap;
import java.util.Map;

public final class DiscordNotifierService implements WebhookNotifierService {
    @Override
    public void initialize() {
        MatthiesenCoreCommon.INSTANCE.createInfoLog("DiscordNotifierService initialized. Webhook notifications will attempt to be sent to Discord.");
    }

    @Override
    public TYPE type() {
        return TYPE.DISCORD;
    }

    private final Map<String, WebhookNotifierInstance> webhookNotifierInstances = new HashMap<>();

    @Override
    public WebhookNotifierInstance makeInstance(String webhookUrl) {
        if (webhookUrl == null || webhookUrl.isBlank()) {
            throw new IllegalArgumentException("Webhook URL must not be blank");
        }
        if (webhookNotifierInstances.containsKey(webhookUrl)) {
            return webhookNotifierInstances.get(webhookUrl);
        }
        WebhookNotifierInstance instance = new DiscordNotifierInstance(webhookUrl);
        webhookNotifierInstances.put(webhookUrl, instance);
        return instance;
    }
}
