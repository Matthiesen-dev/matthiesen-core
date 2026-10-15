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

/**
 * A utility class that provides methods for manipulating a player's inventory in a server menu system.
 * This class includes methods for setting, adding, and retrieving ItemStacks in specific inventory slots,
 * as well as handling the synchronization of the player's container menu after inventory changes.
 */
@SuppressWarnings("unused")
public class InventoryHelper {

    /**
     * Sets the specified ItemStack to the player's inventory at the given slot index.
     * The inventory is treated as a linear array of slots, and the slot index is used to determine where to place the item.
     *
     * @param player        The ServerPlayer whose inventory is to be modified.
     * @param inventorySlot The index of the inventory slot to set. Must be a non-negative integer.
     * @param stack         The ItemStack to set in the specified slot. If null, the slot will be cleared.
     */
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

    /**
     * Sets the specified ItemStack to the player's inventory at the given row and column.
     * The inventory is treated as a grid with 9 columns, and the row and column are used to calculate the slot index.
     *
     * @param player       The ServerPlayer whose inventory is to be modified.
     * @param inventoryRow The row in the inventory grid (0-based).
     * @param inventoryCol The column in the inventory grid (0-based).
     * @param stack        The ItemStack to set in the specified slot. If null, the slot will be cleared.
     */
    public static void setToInventorySlot(@NotNull ServerPlayer player, int inventoryRow, int inventoryCol, @Nullable ItemStack stack) {
        setToInventorySlot(player, inventoryRow * 9 + inventoryCol, stack);
    }

    /**
     * Adds the specified ItemStack to the player's inventory, if possible. If the inventory is full, the item will not be added.
     * After adding the item, the player's container menu will be refreshed to reflect the change.
     *
     * @param player The ServerPlayer whose inventory is to be modified.
     * @param stack  The ItemStack to add to the player's inventory. Must not be null or empty.
     */
    public static void addToInventorySlot(@NotNull ServerPlayer player, @NotNull ItemStack stack) {
        if (stack == ItemStack.EMPTY) return;

        player.getInventory().add(stack.copy());

        if (player.containerMenu instanceof ServerContainer container) {
            UITask.builder().execute(container::refresh).build();
        }
    }

    /**
     * Clears the specified inventory slot for the given player, setting it to an empty ItemStack.
     *
     * @param player        The ServerPlayer whose inventory slot is to be cleared.
     * @param inventorySlot The index of the inventory slot to clear. Must be a non-negative integer.
     * @return The ItemStack that was previously in the specified inventory slot.
     */
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
