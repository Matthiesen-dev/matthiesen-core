/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.template.types;

import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.InventoryListenerButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.helpers.TemplateHelper;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.LineType;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.slot.TemplateSlotDelegate;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class InventoryTemplate extends ChestTemplate {

    protected InventoryTemplate(@NotNull TemplateSlotDelegate[] slots) {
        super(slots);
    }

    public ItemStack getDisplayForSlot(@NotNull ServerPlayer player, int index) {
        Optional<Button> slot = this.getSlot(index).getButton();
        if (slot.isPresent() && slot.get() instanceof InventoryListenerButton) {
            return player.containerMenu.getItems().get(index + 9);
        }
        else if (slot.isPresent()) {
            return slot.get().getDisplay();
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public InventoryTemplate clone() {
        TemplateSlotDelegate[] references = new TemplateSlotDelegate[this.getSize()];
        for (int i = 0; i < getSize(); i++) {
            int row = i / 9;
            int col = i % 9;

            Optional<Button> button = this.getSlot(i).getButton();
            references[i] = new TemplateSlotDelegate(button.orElse(null), col + row * 9);
        }
        return new InventoryTemplate(references);
    }

    @Deprecated
    public NonNullList<ItemStack> getFullDisplay(@NotNull ServerPlayer player) {
        NonNullList<ItemStack> displays = NonNullList.create();

        int PLAYER_INVENTORY_OFFSET = 8;
        for (int i = 0; i <= PLAYER_INVENTORY_OFFSET; i++) displays.add(ItemStack.EMPTY);
        for (int i = 0; i < getSize(); i++) {
            displays.add(getDisplayForSlot(player, i));
        }
        return displays;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private InventoryTemplate templateInstance;

        public Builder() {
            this.templateInstance = new InventoryTemplate(TemplateHelper.slotsOf(4 * COLUMNS));
        }

        public Builder set(int index, @Nullable Button button) {
            templateInstance.set(index, button);
            return this;
        }

        public Builder set(int row, int col, @Nullable Button button) {
            templateInstance.set(row, col, button);
            return this;
        }

        public Builder row(int row, @Nullable Button button) {
            templateInstance.row(row, button);
            return this;
        }

        public Builder rowFromList(int row, @NotNull List<Button> buttons) {
            templateInstance.rowFromList(row, buttons);
            return this;
        }

        public Builder column(int col, @Nullable Button button) {
            templateInstance.column(col, button);
            return this;
        }

        public Builder columnFromList(int col, @NotNull List<Button> buttons) {
            templateInstance.columnFromList(col, buttons);
            return this;
        }

        public Builder line(@NotNull LineType lineType, int startRow, int startCol, int length, @Nullable Button button) {
            templateInstance.line(lineType, startRow, startCol, length, button);
            return this;
        }

        public Builder lineFromList(@NotNull LineType lineType, int startRow, int startCol, int length, @NotNull List<Button> buttons) {
            templateInstance.lineFromList(lineType, startRow, startCol, length, buttons);
            return this;
        }

        public Builder square(int startRow, int startCol, int size, @Nullable Button button) {
            templateInstance.square(startRow, startCol, size, button);
            return this;
        }

        public Builder squareFromList(int startRow, int startCol, int size, @NotNull List<Button> buttons) {
            templateInstance.squareFromList(startRow, startCol, size, buttons);
            return this;
        }

        public Builder rectangle(int startRow, int startCol, int length, int width, @Nullable Button button) {
            templateInstance.rectangle(startRow, startCol, length, width, button);
            return this;
        }

        public Builder rectangleFromList(int startRow, int startCol, int length, int width, @NotNull List<Button> buttons) {
            templateInstance.rectangleFromList(startRow, startCol, length, width, buttons);
            return this;
        }

        public Builder border(int startRow, int startCol, int length, int width, @Nullable Button button) {
            templateInstance.border(startRow, startCol, length, width, button);
            return this;
        }

        public Builder borderFromList(int startRow, int startCol, int length, int width, @NotNull List<Button> buttons) {
            templateInstance.borderFromList(startRow, startCol, length, width, buttons);
            return this;
        }

        public Builder checker(int startRow, int startCol, int length, int width, @Nullable Button button, @Nullable Button button2) {
            templateInstance.checker(startRow, startCol, length, width, button, button2);
            return this;
        }

        public Builder checkerFromList(int startRow, int startCol, int length, int width, @NotNull List<Button> buttons, @NotNull List<Button> buttons2) {
            templateInstance.checkerFromList(startRow, startCol, length, width, buttons, buttons2);
            return this;
        }

        public Builder fill(@Nullable Button button) {
            templateInstance.fill(button);
            return this;
        }

        public Builder fillFromList(@NotNull List<Button> buttons) {
            templateInstance.fillFromList(buttons);
            return this;
        }

        public InventoryTemplate build() {
            InventoryTemplate templateToReturn = templateInstance;
            templateInstance = new InventoryTemplate(TemplateHelper.slotsOf(4 * COLUMNS));
            return templateToReturn;
        }

    }

}
