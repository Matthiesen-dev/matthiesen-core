package dev.matthiesen.matthiesen_core.common.core.metric.impl;

import dev.matthiesen.libs.faststats.Metrics;
import dev.matthiesen.libs.faststats.SimpleContext;
import dev.matthiesen.libs.faststats.SimpleMetrics;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.libs.faststats.config.SimpleConfig;
import dev.matthiesen.libs.faststats.internal.PlatformLoggerFactory;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.common.api.events.ServerEventListener;
import dev.matthiesen.matthiesen_core.common.api.platform.ModContainer;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonMetricsCompatLayer;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Contract;

import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.*;

/**
 * UniversalMetricContext is a specialized context for collecting and submitting metrics data for a specific mod. It extends the SimpleContext from the FastStats library and provides mod-specific information such as the mod version and platform. This context is designed to be used with the UniversalMetrics implementation, which handles the actual collection and submission of metrics data. The UniversalMetricContext initializes the mod container based on the provided mod ID and ensures that the necessary services for metrics collection are set up correctly. It also overrides the getProjectName method to return a unique identifier for the mod, which is used in the metrics submission process to associate the collected data with the correct mod.
 * The Factory inner class provides a convenient way to create instances of UniversalMetricContext by accepting the mod ID and token as parameters. This allows for easy integration of metrics collection into mods by simply creating a new Factory instance with the appropriate mod ID and token, and then calling the create method to obtain a UniversalMetricContext instance ready for use with the UniversalMetrics implementation.
 */
@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class UniversalMetricContext extends SimpleContext {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
        final var thread = new Thread(runnable, "matthiesen-core-faststats-submitter");
        thread.setDaemon(true);
        return thread;
    });
    private final Set<Future<?>> tasks = new CopyOnWriteArraySet<>();
    private final CommonMetricsCompatLayer compatibilityLayer;
    private final ModContainer mod;

    /**
     * Constructs a new UniversalMetricContext instance with the given factory, mod ID, and token. This constructor initializes the mod container based on the provided mod ID and sets up the necessary services for metrics collection. It also ensures that the mod with the specified ID exists, throwing an IllegalArgumentException if it does not. The context is initialized with a configuration read from the mod's config file, allowing for customizable behavior based on the mod's settings. This context is designed to be used with the UniversalMetrics implementation, which will utilize this context to collect and submit metrics data specific to the mod.
     * @param factory the factory used to create this context instance. This is passed to the superclass constructor to initialize the context and other necessary components for metrics collection and submission.
     * @param modId the unique identifier of the mod for which this metrics context is being created. This is used to retrieve the mod container and ensure that the correct mod information is included in the metrics data.
     * @param token the token used for authentication or identification purposes in the metrics submission process. This is passed to the superclass constructor to initialize the context and may be used in the metrics submission process to authenticate or identify the source of the metrics data.
     */
    private UniversalMetricContext(final Factory factory, final dev.matthiesen.libs.faststats.internal.LoggerFactory loggerFactory, final String modId, @Token final String token) {
        super(factory, loggerFactory, getPlatformConfig(loggerFactory), "Universal", token);
        this.mod = MatthiesenCoreCommon.INSTANCE.getCommonUtils().getModContainer(modId);
        if (mod == null) throw new IllegalArgumentException("Mod with id '" + modId + "' not found");

        this.compatibilityLayer = ServiceLoader.load(CommonMetricsCompatLayer.class).findFirst().orElseThrow();

        initializeServices(factory);
        switch (MatthiesenCoreCommon.INSTANCE.getCommonUtils().getEnvironment()) {
            case CLIENT -> {
                ready();
                MatthiesenCoreCommonClient.INSTANCE.getClientEventsListeners().onClientStopping(this::shutdown);
            }
            case SERVER -> MatthiesenCoreCommon.INSTANCE.getServerEventsManager().registerListener(
                    MatthiesenCoreCommon.MOD_ID + "_metrics_context",
                    new ServerEventListener() {
                        @Override
                        public void onServerStarting(MinecraftServer server) {
                            ready();
                        }

                        @Override
                        public void onServerStopping(MinecraftServer server) {
                            shutdown();
                        }
                    }
            );
        }
    }

    /**
     * Creates and returns a Metrics.Factory instance for this context. This factory is responsible for creating Metrics instances that will be used to collect and submit metrics data for the mod associated with this context. The factory implementation checks the environment type (client or server) and creates the appropriate Metrics implementation (UniversalMetricsClientImpl for client and UniversalMetricsServerImpl for server). This allows for environment-specific handling of metrics collection and submission while still utilizing the common functionality provided by the UniversalMetricsImpl base class.
     * The Metrics instances created by this factory will utilize the mod information from the context to include relevant data in the metrics submissions, ensuring that the collected data is associated with the correct mod and environment. This design allows for flexible and efficient metrics collection tailored to the specific needs of both client and server environments while maintaining a consistent interface for mods to use when integrating metrics collection into their functionality.
     * @return a Metrics.Factory instance for this context, which creates Metrics instances that will be used to collect and submit metrics data for the mod associated with this context. The factory implementation checks the environment type (client or server) and creates the appropriate Metrics implementation (UniversalMetricsClientImpl for client and UniversalMetricsServerImpl for server), allowing for environment-specific handling of metrics collection and submission while still utilizing the common functionality provided by the UniversalMetricsImpl base class.
     */
    @Override
    @Contract(value = " -> new", pure = true)
    protected Metrics.Factory metricsFactory() {
        return new SimpleMetrics.Factory(this) {
            @Override
            public Metrics create() throws IllegalStateException {
                final var mod = ((UniversalMetricContext) context).mod;
                return switch (MatthiesenCoreCommon.INSTANCE.getCommonUtils().getEnvironment()) {
                    case CLIENT -> new UniversalMetricsClient(this, mod, compatibilityLayer);
                    case SERVER -> new UniversalMetricsServer(this, mod, compatibilityLayer);
                };
            }
        };
    }

    /**
     * Retrieves the platform-specific configuration for this metrics context. This method reads the configuration from the mod's config file, allowing for customizable behavior based on the mod's settings. The configuration is expected to be in a properties file format and is read using the SimpleConfig class from the FastStats library. This allows mod developers to define specific settings for their metrics collection and submission process, such as enabling or disabling certain features, adjusting submission intervals, or configuring other aspects of the metrics behavior based on their specific requirements.
     * @return a SimpleConfig instance containing the platform-specific configuration for this metrics context. This configuration is read from the mod's config file and allows for customizable behavior based on the mod's settings, providing flexibility for mod developers to tailor the metrics collection and submission process to their specific needs. The configuration is expected to be in a properties file format and is read using the SimpleConfig class from the FastStats library, ensuring compatibility with the metrics system and allowing for easy integration of custom settings for metrics behavior.
     */
    public static SimpleConfig getPlatformConfig(dev.matthiesen.libs.faststats.internal.LoggerFactory factory) {
        return SimpleConfig.read(MatthiesenCoreCommon.INSTANCE.getCommonUtils().getModConfig(MatthiesenCoreCommon.MOD_ID, "metrics.properties"), factory);
    }

    /**
     * Pre-submission start hook for the metrics context. This method is called before the metrics submission process begins and can be used to perform any necessary setup or initialization before metrics are collected and submitted. In this implementation, it retrieves the pre-submission start value from the context's configuration, allowing for customizable behavior based on the mod's settings. This can be used to enable or disable certain features or perform specific actions before the metrics submission process starts, providing flexibility for mod developers to tailor the metrics collection process to their specific needs.
     * @return a boolean value indicating whether the pre-submission start process should proceed. This value is retrieved from the context's configuration, allowing for customizable behavior based on the mod's settings. If this method returns false, the metrics submission process will not proceed, allowing mod developers to control when metrics collection and submission should occur based on their specific requirements or conditions defined in the configuration.
     */
    @Override
    protected boolean preSubmissionStart() {
        return ((SimpleConfig) getConfig()).preSubmissionStart(this);
    }

    /**
     * Returns the project name for this metrics context, which is the unique identifier of the mod associated with this context. This method is used in the metrics submission process to associate the collected data with the correct mod. The project name is obtained from the mod container's getModMetricId method, ensuring that it is unique and consistent with the mod's information. This allows for accurate tracking and analysis of metrics data for each individual mod using this UniversalMetricContext.
     * @return the project name for this metrics context, which is the unique identifier of the mod associated with this context. This value is used in the metrics submission process to associate the collected data with the correct mod, allowing for accurate tracking and analysis of metrics data for each individual mod using this UniversalMetricContext.
     */
    @Override
    public String getProjectName() {
        return mod.getModMetricId();
    }

    /**
     * Schedules a task to be executed at a fixed rate. This method is used to schedule recurring tasks for metrics collection or submission. The task will be executed after the specified initial delay and then repeatedly with the specified period between executions. The scheduled task is added to a set of tasks that can be canceled when the context is shut down, ensuring that all scheduled tasks are properly managed and do not continue to run after the context is no longer active.
     * @param task the Runnable task to be scheduled for execution at a fixed rate. This task will be executed after the specified initial delay and then repeatedly with the specified period between executions. The task is added to a set of tasks that can be canceled when the context is shut down, ensuring proper management of scheduled tasks.
     * @param initialDelay the initial delay before the task is first executed. This is the time to wait before the first execution of the task, allowing for any necessary setup or initialization before the task starts running.
     * @param period the period between successive executions of the task. This is the time to wait between the end of one execution and the start of the next execution, allowing for consistent scheduling of recurring tasks for metrics collection or submission.
     * @param unit the time unit for the initial delay and period parameters. This specifies the time unit for the initial delay and period, allowing for flexible scheduling of tasks based on different time units such as seconds, minutes, or hours. The scheduled task is added to a set of tasks that can be canceled when the context is shut down, ensuring proper management of scheduled tasks and preventing them from running after the context is no longer active.
     */
    @Override
    public void scheduleAtFixedRate(final Runnable task, final long initialDelay, final long period, final TimeUnit unit) {
        tasks.add(executor.scheduleAtFixedRate(task, initialDelay, period, unit));
    }

    /**
     * Shuts down the metrics context and cancels all scheduled tasks. This method is called when the context is no longer active, such as when the server stops or the client shuts down. It ensures that all scheduled tasks are properly canceled to prevent them from running after the context is shut down, and then it shuts down the executor service to release any resources associated with it. This allows for clean shutdown of the metrics collection process and ensures that no tasks continue to run after the context is no longer active.
     */
    @Override
    public void shutdown() {
        super.shutdown();
        tasks.forEach(task -> task.cancel(true));
        tasks.clear();
        executor.shutdown();
    }

    /**
     * Factory class for creating instances of UniversalMetricContext. This factory accepts the mod ID and token as parameters and provides a create method to instantiate a new UniversalMetricContext with the specified mod information. By using this factory, mods can easily integrate metrics collection by simply providing their mod ID and token, allowing for streamlined creation of the necessary context for metrics collection and submission.
     * The Factory class extends the SimpleContext.Factory, providing the necessary functionality to create instances of UniversalMetricContext while also allowing for customization of the context initialization process if needed. This design promotes ease of use and flexibility for mod developers when integrating metrics collection into their mods using the UniversalMetricContext and UniversalMetrics implementations.
     */
    public static final class Factory extends SimpleContext.Factory<UniversalMetricContext, Factory> {
        private final String modId;
        private final @Token String token;

        /**
         * Constructs a new Factory instance for creating UniversalMetricContext instances. This constructor accepts the mod ID and token as parameters, which are used to initialize the necessary information for creating UniversalMetricContext instances. The mod ID is used to retrieve the mod container and ensure that the correct mod information is included in the metrics data, while the token is used for authentication or identification purposes in the metrics submission process. By providing these parameters in the constructor, this Factory allows for streamlined creation of UniversalMetricContext instances with the necessary mod information for metrics collection and submission.
         * @param modId the unique identifier of the mod for which this metrics context will be created. This is used to retrieve the mod container and ensure that the correct mod information is included in the metrics data.
         * @param token the token used for authentication or identification purposes in the metrics submission process. This is passed to the superclass constructor to initialize the context and may be used in the metrics submission process to authenticate or identify the source of the metrics data.
         */
        public Factory(final String modId, @Token final String token) {
            this.modId = modId;
            this.token = token;
        }

        /**
         * Creates and returns a new UniversalMetricContext instance using the provided mod ID and token. This method utilizes the Factory's constructor parameters to initialize the necessary information for creating the UniversalMetricContext, including retrieving the mod container based on the mod ID and setting up the context with the appropriate configuration and services for metrics collection. By calling this create method, mods can easily obtain a UniversalMetricContext instance that is ready for use with the UniversalMetrics implementation, allowing for efficient integration of metrics collection into their functionality.
         * @return a new UniversalMetricContext instance initialized with the mod information and token provided to this Factory. This context is ready for use with the UniversalMetrics implementation, allowing for efficient integration of metrics collection into the mod's functionality. The created UniversalMetricContext will include the necessary mod information and services for collecting and submitting metrics data specific to the mod associated with this context.
         */
        @Override
        public UniversalMetricContext create() {
            final var logger = org.slf4j.LoggerFactory.getLogger(modId);
            final var loggerFactory = new PlatformLoggerFactory((level, throwable, message) -> {
                switch (level) {
                    case INFO -> {
                        if (throwable == null) logger.info(message);
                        else logger.info(message, throwable);
                    }
                    case WARN -> {
                        if (throwable == null) logger.warn(message);
                        else logger.warn(message, throwable);
                    }
                    case ERROR -> {
                        if (throwable == null) logger.error(message);
                        else logger.error(message, throwable);
                    }
                }
            });
            return new UniversalMetricContext(this, loggerFactory, modId, token);
        }
    }
}
