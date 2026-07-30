package dev.matthiesen.matthiesen_core.common;

import dev.matthiesen.matthiesen_core.common.api.platform.CommonClientMod;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.common.core.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract class representing a common client mod. This class provides basic functionality for initializing the client-side of the mod,
 * tracking errors, and managing client-specific services. Mods should extend this class to leverage the common client functionality.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommonClientMod implements CommonClientMod {
    private final String MOD_ID;
    private final String MOD_NAME;
    private final Logger LOGGER;
    private final AbstractCommonMod COMMON_MOD;

    /**
     * Constructor for the AbstractCommonClientMod class.
     *
     * @param commonMod The Server-side mod instance
     */
    public AbstractCommonClientMod(AbstractCommonMod commonMod) {
        this.COMMON_MOD = commonMod;
        this.MOD_ID = commonMod.getModId();
        this.MOD_NAME = commonMod.getModName() + " (client)";
        this.LOGGER = LogManager.getLogger(MOD_NAME);
    }

    /**
     * Initializer for the client mod. This method is called during the mod initialization phase.
     */
    public abstract void initialize();

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
        COMMON_MOD.trackError(throwable);
    }

    @Override
    public ScreenManager getScreenManager() {
        return MatthiesenCoreCommonClient.INSTANCE.getScreenManager();
    }

    @Override
    public EntityRendererManager getEntityRendererManager() {
        return MatthiesenCoreCommonClient.INSTANCE.getEntityRendererManager();
    }

    @Override
    public KeybindingsManager getKeybindingsManager() {
        return MatthiesenCoreCommonClient.INSTANCE.getKeybindingsManager();
    }
}
