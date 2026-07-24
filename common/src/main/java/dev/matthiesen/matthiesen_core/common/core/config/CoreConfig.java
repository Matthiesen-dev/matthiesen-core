package dev.matthiesen.matthiesen_core.common.core.config;

import com.google.gson.annotations.SerializedName;

/**
 * The CoreConfig class represents the configuration options for the Matthiesen Core library.
 */
public final class CoreConfig {

    /**
     * Configuration options related to logging for the Matthiesen Core library. This class allows users to enable or disable
     * debug logging, which can be useful for troubleshooting and development purposes.
     */
    @SerializedName("logging")
    public Logging logging = new Logging();

    /**
     * Static class containing configuration options related to logging for the Matthiesen Core library. This class allows
     * users to enable or disable debug logging, which can be useful for troubleshooting and development purposes.
     */
    public static class Logging {

        /**
         * Whether to enable debug logging for the Matthiesen Core library. When enabled, additional debug information will be
         * logged to assist with troubleshooting and development.
         */
        @SerializedName("enableDebugLogging")
        public boolean enableDebugLogging = false;
    }
}
