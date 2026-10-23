package dev.matthiesen.matthiesen_core.common;

import dev.faststats.ErrorTracker;
import dev.faststats.Metrics;
import dev.faststats.SimpleContext;
import dev.faststats.Token;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.api.faststats.CommonContextService;
import dev.matthiesen.matthiesen_core.common.api.platform.CommonServerMod;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.economy.EconomyManager;
import dev.matthiesen.matthiesen_core.common.core.faststats.MatthiesenCoreMetrics;
import dev.matthiesen.matthiesen_core.common.core.network.NetworkingManager;
import dev.matthiesen.matthiesen_core.common.core.registry.PermissionsManager;
import dev.matthiesen.matthiesen_core.common.core.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

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
    private final SimpleContext metricContext;
    private final RegistryBuilder registryBuilder;

    /**
     * The Metric's Token from FastStats.dev for the Mod/Plugin
     * @return The Token as a String, or null if Metrics are not used
     */
    public abstract @Nullable @Token String getMetricsToken();

    /**
     * Determines whether metrics collection requires user consent. This method can be overridden by subclasses to specify
     * whether metrics collection should be enabled only with user consent. By default, this method returns false, indicating
     * that metrics collection does not require user consent.
     * @return true if metrics collection requires user consent, false otherwise.
     */
    public boolean metricsRequiresConsent() {
        return false;
    }

    /**
     * Retrieves a function that creates a Metrics instance using the provided Metrics.Factory. This function is used to create
     * metrics instances for the mod, allowing for customization of metrics collection and reporting. By default, this method
     * returns a function that creates a Metrics instance using the Metrics.Factory's create method.
     * @return A function that takes a Metrics.Factory and returns a Metrics instance.
     */
    public Function<Metrics.Factory, Metrics> getMetricsFunction() {
        return Metrics.Factory::create;
    }

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
        if (metricsToken != null) {
            CommonContextService contextService = CommonContextService.getInstance();
            errorTracker = contextService.makeAnonymizedErrorTracker();
            metricContext = contextService.buildMetricContext(MOD_ID, metricsToken, errorTracker, getMetricsFunction(), metricsRequiresConsent());
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
    public SimpleContext getMetricContext() {
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
