package dev.matthiesen.matthiesen_core.common.core.discord;

import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierInstance;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;

import java.util.HashMap;
import java.util.Map;

/**
 * A service for managing Discord webhook notifier instances. This service allows for the creation and retrieval of notifier instances
 * that can send messages to specified Discord webhooks. It maintains a mapping of webhook URLs to their corresponding notifier instances,
 * ensuring that each unique webhook URL has a single associated instance.
 */
public final class DiscordNotifierService implements WebhookNotifierService {
    /**
     * Constructs a new DiscordNotifierService. This constructor initializes the service and prepares it for managing webhook notifier instances.
     */
    public DiscordNotifierService() {
        // Constructor for DiscordNotifierService
    }

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
        return webhookNotifierInstances.computeIfAbsent(webhookUrl, DiscordNotifierInstance::new);
    }
}
