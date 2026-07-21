package dev.matthiesen.matthiesen_core.common;

public final class MatthiesenCoreCommonClient {
    public static final MatthiesenCoreCommonClient INSTANCE = new MatthiesenCoreCommonClient();

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
