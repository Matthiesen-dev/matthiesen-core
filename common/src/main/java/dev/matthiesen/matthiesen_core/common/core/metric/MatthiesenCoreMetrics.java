package dev.matthiesen.matthiesen_core.common.core.metric;

import dev.matthiesen.libs.faststats.ErrorTracker;
import dev.matthiesen.libs.faststats.Metrics;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.libs.faststats.data.Metric;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.platform.ModContainer;
import dev.matthiesen.matthiesen_core.common.core.metric.impl.UniversalMetricContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * MatthiesenCoreMetrics is a singleton class that extends AbstractMetricsProvider and provides metrics collection and error
 * tracking functionality for the mod. It manages a list of registered mods, tracks errors, and provides methods to register
 * mods to the metrics system. The class uses a token for authentication with the metrics service and initializes an error
 * tracker and a universal metric context for reporting purposes.
 */
@SuppressWarnings("unused")
public final class MatthiesenCoreMetrics extends AbstractMetricsProvider {
    private static final @Token String TOKEN = "41cdbbe3ecea001225a5ee5aec374db7";

    /**
     * Singleton instance of MatthiesenCoreMetrics. This instance is used to manage metrics collection and error tracking
     * for the mod. It provides methods to register mods, track errors, and retrieve registered mods for reporting purposes.
     */
    public static final MatthiesenCoreMetrics INSTANCE = new MatthiesenCoreMetrics(MatthiesenCoreCommon.MOD_ID);

    private MatthiesenCoreMetrics(String modId) {
        super(modId, TOKEN);
    }

    /**
     * Initializes the metrics system for the mod. This method sets up the necessary components for tracking and reporting
     * metrics, including error tracking and metric context creation. It should be called during the mod's initialization
     * phase to ensure that metrics collection is properly configured.
     */
    public static void initialize() {}

    private static final ErrorTracker ERROR_TRACKER;
    private static final UniversalMetricContext METRIC_CONTEXT;
    private static final List<String> REGISTERED_MODS = new CopyOnWriteArrayList<>();

    static {
        ERROR_TRACKER = INSTANCE.makeErrorTracker();
        METRIC_CONTEXT = INSTANCE.getBaseMetricsFactory()
                .metrics(Metrics.Factory::create)
                .metrics(factory -> factory
                        .addMetric(Metric.stringArray("registered_mods", INSTANCE::getRegisteredMods))
                        .create()
                )
                .create();
    }

    private String[] getRegisteredMods() {
        return REGISTERED_MODS.toArray(new String[0]);
    }

    /**
     * Tracks an error using the error tracker. This method allows for centralized error tracking and reporting, enabling
     * developers to monitor and respond to issues that arise during the execution of the application.
     * @param throwable The Throwable instance representing the error to be tracked. This can be an exception or any other
     *                  error that needs to be logged and monitored.
     */
    public void trackError(Throwable throwable) {
        ERROR_TRACKER.trackError(throwable);
    }

    /**
     * Registers a mod to the metrics system. This method checks if the mod is already registered and logs a warning if it
     * is. If the mod is not registered, it adds the mod's metric ID to the list of registered mods.
     * @param modId The ID of the mod to register to the metrics system. This should be the mod's unique identifier.
     */
    public void registerModToMetrics(String modId) {
        ModContainer modContainer = MatthiesenCoreCommon.INSTANCE.getCommonUtils().getModContainer(modId);
        if (modContainer == null) {
            MatthiesenCoreCommon.INSTANCE.createWarnLog("Attempted to register mod '" + modId + "' to metrics, but the mod container could not be found. Ensure the mod ID is correct.");
            return;
        }

        String modMetricId = modContainer.getModMetricId();

        if (REGISTERED_MODS.contains(modMetricId)) {
            MatthiesenCoreCommon.INSTANCE.createWarnLog("Mod '" + modMetricId + "' is already registered to metrics. Skipping registration.");
            return;
        }
        REGISTERED_MODS.add(modMetricId);
    }
}
