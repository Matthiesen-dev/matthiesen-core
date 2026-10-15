/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import dev.matthiesen.matthiesen_core.common.api.server_menu.data.UpdateEmitter;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * ButtonBase is an abstract class that serves as a base implementation for buttons in the server menu system.
 * It extends UpdateEmitter to allow for update notifications when the button's state changes.
 * This class provides a display ItemStack that represents the visual representation of the button.
 */
public abstract class ButtonBase extends UpdateEmitter<Button> implements Button {

    private ItemStack display;

    /**
     * Constructs a new ButtonBase with the specified display ItemStack.
     *
     * @param display The ItemStack to be displayed for this button.
     */
    protected ButtonBase(@NotNull ItemStack display) {
        this.display = requireNonNull(display);
    }

    /**
     * Returns the display ItemStack for this button.
     *
     * @return The ItemStack that represents the display of this button.
     */
    public final ItemStack getDisplay() {
        return display;
    }

    /**
     * Sets the display ItemStack for this button and triggers an update.
     *
     * @param display The new ItemStack to be displayed for this button.
     */
    public void setDisplay(@NotNull ItemStack display) {
        this.display = display;
        update();
    }

}
