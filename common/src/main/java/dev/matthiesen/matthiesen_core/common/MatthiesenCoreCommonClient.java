package dev.matthiesen.matthiesen_core.common;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;

import java.util.ServiceLoader;

public final class MatthiesenCoreCommonClient {
    public static final MatthiesenCoreCommonClient INSTANCE = new MatthiesenCoreCommonClient();

    public static final CommonLoaderClientEventsListeners CLIENT_EVENTS_LISTENERS =
            ServiceLoader.load(CommonLoaderClientEventsListeners.class).findFirst().orElseThrow();

    private final MatthiesenCoreCommon COMMON_MOD;

    private boolean initialized;

    public MatthiesenCoreCommonClient() {
        COMMON_MOD = MatthiesenCoreCommon.INSTANCE;
    }

    public void initialize() {
        if (initialized) return;

        initialized = true;
        COMMON_MOD.createInfoLog("Initialized Common Client");
    }

    public void onClientSetup() {

    }

    public void createInfoLog(String message) {
        COMMON_MOD.createInfoLog(message);
    }
}
