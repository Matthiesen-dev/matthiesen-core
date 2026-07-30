package dev.matthiesen.matthiesen_core.common.api.platform;

import dev.matthiesen.matthiesen_core.common.core.client.EntityRendererManager;
import dev.matthiesen.matthiesen_core.common.core.client.KeybindingsManager;
import dev.matthiesen.matthiesen_core.common.core.client.ScreenManager;

/**
 * Interface representing a common client mod. This interface extends the CommonMod interface and provides methods for accessing
 * client-specific services and managers, including screen management, entity renderer management, and keybinding management.
 * Mods implementing this interface can leverage these services to enhance their client-side capabilities and integrate
 * with the Matthiesen Core framework.
 */
public interface CommonClientMod extends CommonMod {

    /**
     * Returns the ScreenManager instance for managing menu screen registrations and platform callbacks.
     * @return the ScreenManager instance
     */
    ScreenManager getScreenManager();

    /**
     * Returns the EntityRendererManager instance for managing entity and block entity renderer registrations.
     * @return the EntityRendererManager instance
     */
    EntityRendererManager getEntityRendererManager();

    /**
     * Returns the KeybindingsManager instance for managing keybinding registrations and tick callbacks.
     * @return the KeybindingsManager instance
     */
    KeybindingsManager getKeybindingsManager();
}
