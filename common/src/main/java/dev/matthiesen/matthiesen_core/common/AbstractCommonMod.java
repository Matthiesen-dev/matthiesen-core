package dev.matthiesen.matthiesen_core.common;

import dev.matthiesen.libs.faststats.ErrorTracker;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.api.platform.CommonServerMod;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.economy.EconomyManager;
import dev.matthiesen.matthiesen_core.common.core.metric.MatthiesenCoreMetrics;
import dev.matthiesen.matthiesen_core.common.core.metric.UniversalMetricProvider;
import dev.matthiesen.matthiesen_core.common.core.metric.impl.UniversalMetricContext;
import dev.matthiesen.matthiesen_core.common.core.network.NetworkingManager;
import dev.matthiesen.matthiesen_core.common.core.registry.PermissionsManager;
import dev.matthiesen.matthiesen_core.common.core.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract class representing a common mod. This class provides basic functionality for initializing the mod,
 * tracking errors, and managing metrics. Mods should extend this class to leverage the common functionality.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommonMod implements CommonServerMod {
    private final String MOD_ID;
    private final String MOD_NAME;
    private final Logger LOGGER;
    private final ErrorTracker errorTracker;
    private final UniversalMetricContext metricContext;
    private final RegistryBuilder registryBuilder;

    /**
     * The Metric's Token from FastStats.dev for the Mod/Plugin
     * @return The Token as a String, or null if Metrics are not used
     */
    public abstract @Nullable @Token String getMetricsToken();

    /**
     * Initializes the mod with the given mod ID and mod name.
     * @param MOD_ID The mod ID
     * @param MOD_NAME The mod name
     */
    public AbstractCommonMod(String MOD_ID, String MOD_NAME) {
        this.MOD_ID = MOD_ID;
        this.MOD_NAME = MOD_NAME;
        this.LOGGER = LogManager.getLogger(MOD_NAME);
        this.registryBuilder = new RegistryBuilder(MOD_ID);

        var metricsToken = getMetricsToken();
        UniversalMetricProvider metricsProvider;
        if (metricsToken != null) {
            metricsProvider = new UniversalMetricProvider(MOD_ID, metricsToken);
            errorTracker = metricsProvider.makeErrorTracker();
            metricContext = metricsProvider.buildErrorTrackingMetricProvider(errorTracker);
        } else {
            errorTracker = null;
            metricContext = null;
        }
    }

    /**
     * Initializes the mod with the given mod ID. The mod name will be set to the same value as the mod ID.
     * @param MOD_ID The mod ID
     */
    public AbstractCommonMod(String MOD_ID) {
        this(MOD_ID, MOD_ID);
    }

    @Override
    public void initialize() {
        MatthiesenCoreCommon.INSTANCE.registerModToMetrics(MOD_ID);
    }

    /**
     * Get the mod's metric context. This is used to send metrics to FastStats.dev. If the metric context is not initialized, this method returns null.
     * @return The mod's metric context, or null if the metric context is not initialized. The metric context is used to send metrics to FastStats.dev, and
     * is initialized if the mod has a valid metrics token. If the mod does not have a valid metrics token, this method returns null.
     */
    public UniversalMetricContext getMetricContext() {
        return metricContext;
    }

    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public String getModName() {
        return MOD_NAME;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void trackError(Throwable throwable) {
        if (errorTracker != null) {
            errorTracker.trackError(throwable);
        }
    }

    @Override
    public RegistryBuilder getRegistryBuilder() {
        return registryBuilder;
    }

    @Override
    public PermissionsManager getPermissionsManager() {
        return MatthiesenCoreCommon.INSTANCE.getPermissionsManager();
    }

    @Override
    public CommandsRegistryManager getCommandsRegistryManager() {
        return MatthiesenCoreCommon.INSTANCE.getCommandsRegistryManager();
    }

    @Override
    public WebhookNotifierService getWebhookService() {
        return MatthiesenCoreCommon.INSTANCE.getWebhookService();
    }

    @Override
    public CommonLoaderUtils getCommonUtils() {
        return MatthiesenCoreCommon.INSTANCE.getCommonUtils();
    }

    @Override
    public CommonLoaderRegistry getCommonRegistry() {
        return MatthiesenCoreCommon.INSTANCE.getCommonRegistry();
    }

    @Override
    public TextParserRegistryManager getTextParserManager() {
        return MatthiesenCoreCommon.INSTANCE.getTextParserManager();
    }

    @Override
    public MatthiesenCoreMetrics getCoreMetrics() {
        return MatthiesenCoreCommon.INSTANCE.getCoreMetrics();
    }

    @Override
    public NetworkingManager getNetworkingManager() {
        return MatthiesenCoreCommon.INSTANCE.getNetworkingManager();
    }

    @Override
    public CreativeModeAugmentsManager getCreativeModeAugmentsManager() {
        return MatthiesenCoreCommon.INSTANCE.getCreativeModeAugmentsManager();
    }

    @Override
    public EconomyManager getEconomyManager() {
        return MatthiesenCoreCommon.INSTANCE.getEconomyManager();
    }
}
