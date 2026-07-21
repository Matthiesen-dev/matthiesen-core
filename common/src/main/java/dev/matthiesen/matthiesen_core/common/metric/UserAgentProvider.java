package dev.matthiesen.matthiesen_core.common.metric;

import dev.matthiesen.libs.faststats.SdkInfo;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;

/**
 * Provides a custom user agent for the FastStats SDK, including the mod name and version.
 */
public final class UserAgentProvider {
    /**
     * Utility class to provide a custom user agent for the FastStats SDK, including the mod name and version.
     */
    public UserAgentProvider() {}

    /**
     * Custom user agent provider for FastStats SDK, providing the mod name and version.
     */
    public static final class MatthiesenCoreUniversalAgent implements SdkInfo.UserAgentProvider {
        /**
         * Creates a new instance of the MatthiesenCoreUniversalAgent.
         */
        public MatthiesenCoreUniversalAgent() {}

        @Override
        public String getUserAgent(SdkInfo sdkInfo) {
            return "Matthiesen Core API Metrics " + sdkInfo.getName() + "/" + getVersion() + " https://mods.matthiesen.dev/matthiesen-core/";
        }

        /**
         * Gets the version of the mod from the mod container.
         * @return The version of the mod.
         */
        private String getVersion() {
            return MatthiesenCoreCommon.INSTANCE.getCommonUtils().getModContainer(MatthiesenCoreCommon.MOD_ID).getModVersion();
        }
    }
}
