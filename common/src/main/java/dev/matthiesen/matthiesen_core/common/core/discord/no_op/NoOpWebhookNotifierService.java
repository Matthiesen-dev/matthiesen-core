package dev.matthiesen.matthiesen_core.common.core.discord.no_op;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierInstance;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;

/**
 * A no-operation implementation of the {@link WebhookNotifierService} interface. This service does not perform any actual
 * webhook notifications and is intended for use in environments where webhook functionality is not required or available.
 */
public final class NoOpWebhookNotifierService implements WebhookNotifierService {
    /**
     * Creates a new instance of the NoOpWebhookNotifierService.
     */
    public NoOpWebhookNotifierService() {}

    @Override
    public void initialize() {
        MatthiesenCoreCommon.INSTANCE.createInfoLog("NoOpWebhookNotifierService initialized. Webhook notifications will not be sent.");
    }

    @Override
    public TYPE type() {
        return TYPE.NOOP;
    }

    @Override
    public WebhookNotifierInstance makeInstance(String webhookUrl) {
        return new NoOpWebhookNotifierInstance(webhookUrl);
    }
}
