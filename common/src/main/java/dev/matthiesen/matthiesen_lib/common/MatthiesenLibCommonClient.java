package dev.matthiesen.matthiesen_lib.common;

public final class MatthiesenLibCommonClient {
    public static final MatthiesenLibCommonClient INSTANCE = new MatthiesenLibCommonClient();

    private final MatthiesenLibCommon COMMON_MOD;

    private boolean initialized;

    public MatthiesenLibCommonClient() {
        COMMON_MOD = MatthiesenLibCommon.INSTANCE;
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
