/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import dev.matthiesen.matthiesen_core.common.api.server_menu.data.Subject;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a button in a server menu, which can be displayed to the player and can respond to click actions.
 * This interface defines the basic behavior of a button, including its display item and click handling.
 */
public interface Button extends Subject<Button> {

    /**
     * Returns the display item for this button.
     *
     * @return The ItemStack representing the display of this button.
     */
    ItemStack getDisplay();

    /**
     * Called when the button is clicked.
     *
     * @param action The action associated with the button click.
     */
    default void onClick(@NotNull ButtonAction action) {
    }

}
