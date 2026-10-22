package dev.matthiesen.matthiesen_core.neoforge.api.faststats;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.SimpleContext;
import dev.faststats.Token;
import dev.faststats.neoforge.NeoForgeContext;
import dev.matthiesen.matthiesen_core.common.api.faststats.CommonContextService;

import java.util.function.Function;

public final class NeoForgeContextServiceProvider implements CommonContextService {
    private NeoForgeContext.Factory getBaseFactory(String modId, @Token String token, boolean requireConsent) {
        var factory = new NeoForgeContext.Factory(modId, token);
        if (requireConsent) {
            factory.requireConsent();
        }
        return factory;
    }

    @Override
    public SimpleContext buildMetricContext(String modId, @Token String token, boolean requireConsent) {
        return getBaseFactory(modId, token, requireConsent).create();
    }

    @Override
    public SimpleContext buildMetricContext(String modId, @Token String token, ErrorTracker errorTracker, boolean requireConsent) {
        return getBaseFactory(modId, token, requireConsent).errorTrackerService(errorTracker).create();
    }

    @Override
    public SimpleContext buildMetricContext(String modId, @Token String token, ErrorTracker errorTracker, Function<Metrics.Factory, Metrics> metrics, boolean requireConsent) {
        return getBaseFactory(modId, token, requireConsent).errorTrackerService(errorTracker).metrics(metrics).create();
    }
}
