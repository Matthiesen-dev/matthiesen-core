/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

/**
 * Represents the type of click that can be performed on a button within a server menu.
 * This enum defines various click types, including left click, right click, middle click,
 * shift left click, shift right click, throw, and other types of clicks.
 */
public enum ButtonClick {
    /**
     * Represents a left click action on a button.
     */
    LEFT_CLICK,
    /**
     * Represents a right click action on a button.
     */
    RIGHT_CLICK,
    /**
     * Represents a middle click action on a button.
     */
    MIDDLE_CLICK,
    /**
     * Represents a shift left click action on a button.
     */
    SHIFT_LEFT_CLICK,
    /**
     * Represents a shift right click action on a button.
     */
    SHIFT_RIGHT_CLICK,
    /**
     * Represents a throw action on a button.
     */
    THROW,
    /**
     * Represents any other type of click action on a button.
     */
    OTHER
}
