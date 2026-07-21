package dev.matthiesen.matthiesen_core.common.metric;

import com.google.gson.JsonObject;
import dev.matthiesen.libs.faststats.SimpleMetrics;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.platform.ModContainer;

/**
 * Base implementation for universal metrics, providing common functionality for both client and server metrics.
 * This class handles the initialization of the mod container and provides a method to append universal data to the metrics JSON object.
 * The actual submission logic and environment-specific data collection are implemented in the subclasses.
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractUniversalMetric extends SimpleMetrics {
    /**
     * The mod container associated with this metrics instance. This provides access to the mod's information such as version and platform, which can be included in the metrics data.
     */
    protected final ModContainer modContainer;

    /**
     * Constructs a new UniversalMetricsImpl instance with the given factory and mod container. This constructor is called by the subclasses to initialize the common functionality of the metrics implementation.
     * @param factory the factory used to create this metrics instance. This is passed to the superclass constructor to initialize the context and other necessary components for metrics collection and submission.
     * @param modContainer the mod container associated with this metrics instance. This provides access to the mod's information such as version and platform, which can be included in the metrics data.
     * @throws IllegalStateException if there is an issue with initializing the metrics instance, such as invalid configuration or missing dependencies. The actual conditions for throwing this exception depend on the implementation of the superclass and the context initialization.
     */
    public AbstractUniversalMetric(final Factory factory, final ModContainer modContainer) throws IllegalStateException {
        super(factory);
        this.modContainer = modContainer;
    }

    private String version(final String modId) {
        var container = MatthiesenCoreCommon.INSTANCE.getCommonUtils().getModContainer(modId);
        String version;
        if (container == null) {
            version = "unknown";
        } else {
            version = container.getModVersion();
        }
        return version;
    }

    private String minecraftVersion() {
        return version("minecraft");
    }

    private String getPlatformVersion() {
        return version(modContainer.getPlatformData().getModId());
    }

    /**
     * Appends universal data to the metrics JSON object. This method is called by the subclasses to add common data fields to the metrics before submission. The data added in this method includes the mod version and platform, which are retrieved from the mod container. This allows for consistent inclusion of these fields in both client and server metrics, providing valuable information about the mod's environment and version for analysis.
     * @param metrics the JsonObject representing the metrics data that will be submitted. This object is modified by adding properties for the mod version and platform, which are obtained from the mod container. Subclasses can call this method to ensure that these universal data fields are included in the metrics submission, regardless of whether it's client or server metrics.
     */
    protected void appendUniversalData(final JsonObject metrics) {
        metrics.addProperty("minecraft_version", minecraftVersion());
        metrics.addProperty("platform_version", getPlatformVersion());
        metrics.addProperty("plugin_version", modContainer.getModVersion());
        metrics.addProperty("server_type", modContainer.getPlatformData().getLabel());
    }
}
