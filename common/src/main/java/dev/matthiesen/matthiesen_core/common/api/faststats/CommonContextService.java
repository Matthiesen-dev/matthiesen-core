package dev.matthiesen.matthiesen_core.common.api.faststats;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.SimpleContext;
import dev.faststats.Token;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.AccessDeniedException;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * CommonContextService is an interface that provides methods for building metric contexts and retrieving error trackers.
 * It serves as a common API for different platforms (e.g., Fabric, NeoForge) to implement their own context service providers.
 * The interface includes methods to build metric contexts with various configurations, including mod ID, token, error tracker,
 * and custom metrics functions. It also provides default methods to retrieve context-aware and context-unaware error trackers.
 */
@SuppressWarnings("unused")
public interface CommonContextService {
    /**
     * Retrieves an instance of the CommonContextService using the ServiceLoader mechanism.
     * If no implementation is found, an IllegalStateException is thrown.
     *
     * @return An instance of CommonContextService
     * @throws IllegalStateException if no implementation of CommonContextService is found
     */
    static CommonContextService getInstance() {
        return ServiceLoader.load(CommonContextService.class).findFirst().orElseThrow(() -> new IllegalStateException("No implementation of CommonContextService found"));
    }

    /**
     * Retrieves a context-aware ErrorTracker instance.
     *
     * @return A context-aware ErrorTracker
     */
    default ErrorTracker getContextAwareErrorTracker() {
        return ErrorTracker.contextAware();
    }

    /**
     * Retrieves a context-aware ErrorTracker instance with the specified ClassLoader.
     *
     * @param classLoader The ClassLoader to use for context awareness, or null for the default
     * @return A context-aware ErrorTracker
     */
    default ErrorTracker getContextAwareErrorTracker(@Nullable final ClassLoader classLoader) {
        return ErrorTracker.contextAware(classLoader);
    }

    /**
     * Retrieves a context-unaware ErrorTracker instance.
     *
     * @return A context-unaware ErrorTracker
     */
    default ErrorTracker getContextUnawareErrorTracker() {
        return ErrorTracker.contextUnaware();
    }

    /**
     * Creates a context-unaware ErrorTracker instance with default anonymization and error ignoring settings.
     * This method is useful for creating an ErrorTracker that does not rely on any specific context and can be used
     * in situations where context information is not available or not needed.
     *
     * @return A context-unaware ErrorTracker with default settings
     */
    default ErrorTracker makeAnonymizedErrorTracker() {
        return ErrorTracker.contextUnaware()
                .ignoreError(InvocationTargetException.class, "Expected .* but got .*")
                .ignoreError(AccessDeniedException.class)
                .anonymize("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "[email hidden]")
                .anonymize("Bearer [A-Za-z0-9._~+/=-]+", "Bearer [token hidden]")
                .anonymize("AKIA[0-9A-Z]{16}", "[aws-key hidden]")
                .anonymize("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", "[uuid hidden]")
                .anonymize("([?&](?:api_?key|token|secret)=)[^&\\s]+", "$1[redacted]");
    }

    /**
     * Builds a SimpleContext for metrics with the specified modId and token.
     *
     * @param modId The mod ID
     * @param token The token
     * @return A SimpleContext for metrics
     */
    default SimpleContext buildMetricContext(
            String modId,
            @Token String token
    ) {
        return buildMetricContext(modId, token, false);
    }

    /**
     * Builds a SimpleContext for metrics with the specified modId and token, requiring user consent.
     *
     * @param modId The mod ID
     * @param token The token
     * @return A SimpleContext for metrics that requires user consent
     */
    SimpleContext buildMetricContext(
            String modId,
            @Token String token,
            boolean requireConsent
    );

    /**
     * Builds a SimpleContext for metrics with the specified modId, token, and ErrorTracker.
     *
     * @param modId The mod ID
     * @param token The token
     * @param errorTracker The ErrorTracker to use
     * @return A SimpleContext for metrics
     */
    default SimpleContext buildMetricContext(
            String modId,
            @Token String token,
            ErrorTracker errorTracker
    ) {
        return buildMetricContext(modId, token, errorTracker, false);
    }

    /**
     * Builds a SimpleContext for metrics with the specified modId, token, and ErrorTracker, requiring user consent.
     *
     * @param modId The mod ID
     * @param token The token
     * @param errorTracker The ErrorTracker to use
     * @return A SimpleContext for metrics that requires user consent
     */
    SimpleContext buildMetricContext(
            String modId,
            @Token String token,
            ErrorTracker errorTracker,
            boolean requireConsent
    );

    /**
     * Builds a SimpleContext for metrics with the specified modId, token, ErrorTracker, and custom Metrics function.
     *
     * @param modId The mod ID
     * @param token The token
     * @param errorTracker The ErrorTracker to use
     * @param metrics A function to customize the Metrics instance
     * @return A SimpleContext for metrics
     */
    default SimpleContext buildMetricContext(
            String modId,
            @Token String token,
            ErrorTracker errorTracker,
            Function<Metrics.Factory, Metrics> metrics
    ) {
        return buildMetricContext(modId, token, errorTracker, metrics, false);
    }

    /**
     * Builds a SimpleContext for metrics with the specified modId, token, ErrorTracker, and custom Metrics function, requiring user consent.
     *
     * @param modId The mod ID
     * @param token The token
     * @param errorTracker The ErrorTracker to use
     * @param metrics A function to customize the Metrics instance
     * @return A SimpleContext for metrics that requires user consent
     */
    SimpleContext buildMetricContext(
            String modId,
            @Token String token,
            ErrorTracker errorTracker,
            Function<Metrics.Factory, Metrics> metrics,
            boolean requireConsent
    );
}
