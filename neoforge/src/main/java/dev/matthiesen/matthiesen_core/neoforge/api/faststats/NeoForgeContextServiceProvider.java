package dev.matthiesen.matthiesen_core.neoforge.api.faststats;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.SimpleContext;
import dev.faststats.Token;
import dev.faststats.neoforge.NeoForgeContext;
import dev.matthiesen.matthiesen_core.common.api.faststats.CommonContextService;

import java.util.function.Function;

public final class NeoForgeContextServiceProvider implements CommonContextService {
    private NeoForgeContext.Factory getBaseFactory(String modId, @Token String token) {
        return new NeoForgeContext.Factory(modId, token);
    }

    @Override
    public SimpleContext buildMetricContext(String modId, @Token String token) {
        return getBaseFactory(modId, token).create();
    }

    @Override
    public SimpleContext buildMetricContext(String modId, @Token String token, ErrorTracker errorTracker) {
        return getBaseFactory(modId, token).errorTrackerService(errorTracker).create();
    }

    @Override
    public SimpleContext buildMetricContext(String modId, @Token String token, ErrorTracker errorTracker, Function<Metrics.Factory, Metrics> metrics) {
        return getBaseFactory(modId, token).errorTrackerService(errorTracker).metrics(metrics).create();
    }
}
