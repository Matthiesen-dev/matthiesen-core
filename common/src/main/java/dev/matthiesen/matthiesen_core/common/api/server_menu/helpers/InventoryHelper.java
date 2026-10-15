/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.helpers;

import dev.matthiesen.matthiesen_core.common.api.server_menu.container.ServerContainer;
import dev.matthiesen.matthiesen_core.common.api.server_menu.tasks.UITask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class InventoryHelper {

    public static void setToInventorySlot(@NotNull ServerPlayer player, int inventorySlot, @Nullable ItemStack stack) {
        if (inventorySlot < 0) return;

        // Empty slots are always populated with ItemStack.EMPTY instead of null.
        if (stack == null) stack = ItemStack.EMPTY;

        /*
         * Offset hotbar and main inventory since their implementation differs from concept of template slots.
         * Hotbar in inventory is always placed before main inventory slots in player inventory, where
         * the hotbar in template is always after the main inventory slots.
         */
        if (inventorySlot >= 27) {
            player.getInventory().setItem(inventorySlot - 27, stack.copy());
        } else {
            player.getInventory().setItem(inventorySlot + 9, stack.copy());
        }

        if (player.containerMenu instanceof ServerContainer container) {
            UITask.builder().execute(container::refresh).build();
        }
    }

    public static void setToInventorySlot(@NotNull ServerPlayer player, int inventoryRow, int inventoryCol, @Nullable ItemStack stack) {
        setToInventorySlot(player, inventoryRow * 9 + inventoryCol, stack);
    }

    public static void addToInventorySlot(@NotNull ServerPlayer player, @NotNull ItemStack stack) {
        if (stack == ItemStack.EMPTY) return;

        player.getInventory().add(stack.copy());

        if (player.containerMenu instanceof ServerContainer container) {
            UITask.builder().execute(container::refresh).build();
        }
    }

    @NotNull
    public static ItemStack getStackAtSlot(@NotNull ServerPlayer player, int inventorySlot) {
        /*
         * Offset hotbar and main inventory since their implementation differs from concept of template slots.
         * Hotbar in inventory is always placed before main inventory slots in player inventory, where
         * the hotbar in template is always after the main inventory slots.
         */
        if (inventorySlot >= 27) {
            return player.getInventory().getItem(inventorySlot - 27);
        } else {
            return player.getInventory().getItem(inventorySlot + 9);
        }
    }
}
