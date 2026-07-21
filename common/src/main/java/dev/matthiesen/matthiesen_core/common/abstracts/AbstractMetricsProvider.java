package dev.matthiesen.matthiesen_core.common.abstracts;

import dev.matthiesen.libs.faststats.ErrorTracker;
import dev.matthiesen.libs.faststats.Metrics;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.metric.UniversalMetricContext;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.AccessDeniedException;

/**
 * AbstractMetricsProvider is an abstract class that provides a framework for creating and managing metrics collection and
 * error tracking for a specific mod. It encapsulates the logic for building metric contexts and error trackers, allowing
 * subclasses to easily implement metrics collection and error tracking functionality without having to deal with the
 * underlying implementation details.
 */
@SuppressWarnings("unused")
public abstract class AbstractMetricsProvider {
    private final String modId;
    private final @Token String token;

    /**
     * Constructs an AbstractMetricsProvider with the specified modId and token. The modId is used to identify the mod for
     * which metrics are being collected, and the token is used for authentication and authorization purposes when reporting metrics.
     * @param modId The mod ID for the mod for which metrics are being collected. This should be a unique identifier for the mod.
     * @param token The token used for authentication and authorization when reporting metrics. This should be a secure token
     *              that is unique to the mod and is used to verify the identity of the mod when sending metrics data.
     */
    protected AbstractMetricsProvider(final String modId, final @Token String token) {
        this.modId = modId;
        this.token = token;
    }

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
