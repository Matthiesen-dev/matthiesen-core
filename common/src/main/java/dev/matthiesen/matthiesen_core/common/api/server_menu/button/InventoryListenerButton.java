/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a button that listens for inventory interactions in a server menu.
 * This button can be used to handle clicks and other actions performed by players
 * within the inventory interface of the server menu.
 */
public class InventoryListenerButton extends ButtonBase {

    private final Consumer<ButtonAction> onClick;

    /**
     * Constructs an InventoryListenerButton with the specified click action.
     *
     * @param onClick The action to perform when the button is clicked. Can be null if no action is desired.
     */
    public InventoryListenerButton(@Nullable Consumer<ButtonAction> onClick) {
        super(ItemStack.EMPTY);
        this.onClick = onClick;
    }

    @Override
    public void onClick(@NotNull ButtonAction action) {
        if (onClick != null) {
            onClick.accept(action);
        }
    }

}
