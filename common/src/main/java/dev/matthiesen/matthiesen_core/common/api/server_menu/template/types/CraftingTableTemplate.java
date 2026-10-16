/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.template.types;

import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.helpers.TemplateHelper;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.TemplateType;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.slot.TemplateSlotDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public class CraftingTableTemplate extends Template {

    protected CraftingTableTemplate(@NotNull TemplateSlotDelegate[] slots) {
        super(TemplateType.CRAFTING_TABLE, slots);
    }

    public static Builder builder() {
        return new Builder();
    }

    public CraftingTableTemplate set(int index, @Nullable Button button) {
        getSlot(index).setButton(button);
        return this;
    }

    public CraftingTableTemplate setGrid(int row, int col, @Nullable Button button) {
        getSlot(row * 3 + col + 1).setButton(button);
        return this;
    }

    public CraftingTableTemplate setResultItem(@Nullable Button button) {
        getSlot(0).setButton(button);
        return this;
    }

    public CraftingTableTemplate fill(@Nullable Button button) {
        for (int i = 0; i < getSize(); i++) {
            if (getSlot(i).getButton().isEmpty()) {
                getSlot(i).setButton(button);
            }
        }
        return this;
    }

    public CraftingTableTemplate fillFromList(@NotNull List<Button> buttons) {
        Iterator<Button> iterator = buttons.iterator();
        for (int i = 0; i < getSize(); i++) {
            if (getSlot(i).getButton().isEmpty()) {
                getSlot(i).setButton((iterator.hasNext()) ? iterator.next() : null);
            }
        }
        return this;
    }

    public CraftingTableTemplate fillGrid(@Nullable Button button) {
        for (int i = 1; i < 10; i++) {
            if (getSlot(i).getButton().isEmpty()) {
                getSlot(i).setButton(button);
            }
        }
        return this;
    }

    public CraftingTableTemplate fillGridFromList(@NotNull List<Button> buttons) {
        Iterator<Button> iterator = buttons.iterator();
        for (int i = 1; i < 10; i++) {
            if (getSlot(i).getButton().isEmpty()) {
                getSlot(i).setButton((iterator.hasNext()) ? iterator.next() : null);
            }
        }
        return this;
    }

    public CraftingTableTemplate clear() {
        for (int slotIndex = 0; slotIndex < getSize(); slotIndex++) {
            getSlot(slotIndex).setButton(null);
        }
        return this;
    }

    @Override
    public CraftingTableTemplate clone() {
        TemplateSlotDelegate[] clonedSlots = new TemplateSlotDelegate[getSize()];
        for (int i = 0; i < getSize(); i++) {
            Button button = getSlot(i).getButton().orElse(null);
            clonedSlots[i] = new TemplateSlotDelegate(button, i);
        }
        return new CraftingTableTemplate(clonedSlots);
    }

    public static class Builder {

        private CraftingTableTemplate templateInstance;

        public Builder() {
            this.templateInstance = new CraftingTableTemplate(TemplateHelper.slotsOf(10));
        }

        public Builder set(int index, @Nullable Button button) {
            templateInstance.set(index, button);
            return this;
        }

        public Builder setGrid(int row, int col, @Nullable Button button) {
            templateInstance.setGrid(row, col, button);
            return this;
        }

        public Builder setResultItem(@Nullable Button button) {
            templateInstance.setResultItem(button);
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

        public Builder fillGrid(@Nullable Button button) {
            templateInstance.fillGrid(button);
            return this;
        }

        public Builder fillGridFromList(@NotNull List<Button> buttons) {
            templateInstance.fillGridFromList(buttons);
            return this;
        }

        public CraftingTableTemplate build() {
            CraftingTableTemplate templateToReturn = templateInstance;
            templateInstance = new CraftingTableTemplate(TemplateHelper.slotsOf(10));
            return templateToReturn;
        }

    }
}
