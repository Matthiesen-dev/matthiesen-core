package dev.matthiesen.matthiesen_core.common.api.platform.registry;

/**
 * Defines the activation behavior of a resource pack in the modding platform. This enum provides options for how a
 * resource pack should be activated when the game starts, allowing mod developers to specify whether a resource pack
 * should be enabled by default, always enabled, or start disabled. The activation behavior can influence the user's
 * experience and control over resource packs in the game.
 */
public enum ResourcePackActivationBehaviour {

    /**
     * The resource pack will start disabled.
     */
    NORMAL,

    /**
     * The resource pack will start enabled.
     */
    DEFAULT_ENABLED,

    /**
     * The resource pack will always be enabled.
     * The user can reorder it but cannot remove it.
     */
    ALWAYS_ENABLED
}
