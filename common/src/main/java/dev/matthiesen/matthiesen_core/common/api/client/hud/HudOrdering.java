package dev.matthiesen.matthiesen_core.common.api.client.hud;

/**
 * Relative insertion order for HUD layers.
 */
public enum HudOrdering {
    /**
     * Render the new layer before the specified existing layer, or at the bottom of the stack if {@code other} is null.
     */
    BEFORE,
    /**
     * Render the new layer after the specified existing layer, or at the top of the stack if {@code other} is null.
     */
    AFTER
}

