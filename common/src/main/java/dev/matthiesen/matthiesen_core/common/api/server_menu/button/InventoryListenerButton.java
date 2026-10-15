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

public class InventoryListenerButton extends ButtonBase {

    private final Consumer<ButtonAction> onClick;

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
