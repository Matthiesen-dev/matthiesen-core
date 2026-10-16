/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button.moveable;

import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;

/**
 * Represents a button that can be moved within a server menu.
 * This interface extends the Button interface and adds methods for handling pickup and drop actions.
 */
public interface Movable extends Button {
    /**
     * Called when the button is picked up by a player.
     *
     * @param action The MovableButtonAction containing information about the pickup event.
     */
    void onPickup(MovableButtonAction action);
    /**
     * Called when the button is dropped by a player.
     *
     * @param action The MovableButtonAction containing information about the drop event.
     */
    void onDrop(MovableButtonAction action);
}
