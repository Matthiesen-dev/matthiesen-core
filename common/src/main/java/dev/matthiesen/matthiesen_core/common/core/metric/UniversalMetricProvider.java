package dev.matthiesen.matthiesen_core.common.core.metric;

import dev.matthiesen.libs.faststats.ErrorTracker;
import dev.matthiesen.libs.faststats.Metrics;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.core.metric.impl.UniversalMetricContext;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.AccessDeniedException;

/**
 * A provider for creating UniversalMetricContext instances, which are used for collecting and reporting metrics related to a specific mod.
 * @param modId The unique identifier of the mod for which metrics are being collected. This ID is used to associate metrics with the correct mod.
 * @param token A token used for authenticating and authorizing the collection and reporting of metrics. This token should be kept secure and not exposed publicly.
 */
@SuppressWarnings("unused")
public record UniversalMetricProvider(String modId, @Token String token) {

    /**
     * Builds a UniversalMetricContext for general metrics collection, using the modId and token provided during the instantiation
     * of this AbstractMetricsProvider. This context is configured to collect and report metrics related to the mod identified by modId and token.
     * @return A UniversalMetricContext instance configured for general metrics collection, which can be used to collect and report metrics related to the mod.
     */
    public UniversalMetricContext buildBaseMetricProvider() {
        return getBaseMetricsFactory().metrics(Metrics.Factory::create).create();
    }

    /**
     * Builds a UniversalMetricContext for error tracking, using the provided ErrorTracker instance. This context is configured
     * to collect and report metrics related to errors encountered in the mod identified by modId and token.
     * @param errorTracker The ErrorTracker instance to be used for tracking errors. This instance should be configured with any
     *                     necessary error handling and anonymization settings.
     * @return A UniversalMetricContext instance configured for error tracking, which can be used to collect and report metrics
     * related to errors encountered in the mod.
     */
    public UniversalMetricContext buildErrorTrackingMetricProvider(final ErrorTracker errorTracker) {
        return getBaseMetricsFactory().metrics(Metrics.Factory::create).errorTrackerService(errorTracker).create();
    }

    /**
     * Retrieves a factory for creating UniversalMetricContext instances, which are used for collecting and reporting metrics
     * related to the mod identified by modId and token.
     * @return A UniversalMetricContext.Factory instance configured with the modId and token for creating metric contexts.
     */
    public UniversalMetricContext.Factory getBaseMetricsFactory() {
        return new UniversalMetricContext.Factory(modId, token);
    }

    /**
     * Creates an ErrorTracker instance with default configurations for error tracking. The ErrorTracker is set up to ignore
     * certain expected errors and anonymize sensitive information in error messages.
     * @return An instance of ErrorTracker configured with default settings for error tracking and anonymization.
     */
    public ErrorTracker makeErrorTracker() {
        return ErrorTracker.contextUnaware()
                .ignoreError(InvocationTargetException.class, "Expected .* but got .*")
                .ignoreError(AccessDeniedException.class)
                .anonymize("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "[email hidden]")
                .anonymize("Bearer [A-Za-z0-9._~+/=-]+", "Bearer [token hidden]")
                .anonymize("AKIA[0-9A-Z]{16}", "[aws-key hidden]")
                .anonymize("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "[uuid hidden]")
                .anonymize("([?&](?:api_?key|token|secret)=)[^&\\s]+", "$1[redacted]");
    }
}
