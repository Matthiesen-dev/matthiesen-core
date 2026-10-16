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
public class DispenserTemplate extends Template {

    protected DispenserTemplate(@NotNull TemplateSlotDelegate[] slots) {
        super(TemplateType.DISPENSER, slots);
    }

    public TemplateSlotDelegate getSlot(int row, int col) {
        return getSlot(row * 3 + col);
    }

    public DispenserTemplate set(int index, @Nullable Button button) {
        getSlot(index).setButton(button);
        return this;
    }

    public DispenserTemplate set(int row, int col, @Nullable Button button) {
        return set(row * 3 + col, button);
    }

    public DispenserTemplate fill(@Nullable Button button) {
        for (int i = 0; i < getSize(); i++) {
            if (getSlot(i).getButton().isEmpty()) {
                getSlot(i).setButton(button);
            }
        }
        return this;
    }

    public DispenserTemplate fillFromList(@NotNull List<Button> buttons) {
        Iterator<Button> iterator = buttons.iterator();
        for (int i = 0; i < getSize(); i++) {
            if (getSlot(i).getButton().isEmpty()) {
                getSlot(i).setButton((iterator.hasNext()) ? iterator.next() : null);
            }
        }
        return this;
    }

    public DispenserTemplate clear() {
        for (int slotIndex = 0; slotIndex < getSize(); slotIndex++) {
            getSlot(slotIndex).setButton(null);
        }
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public DispenserTemplate clone() {
        TemplateSlotDelegate[] clonedSlots = new TemplateSlotDelegate[getSize()];
        for (int i = 0; i < getSize(); i++) {
            Button button = getSlot(i).getButton().orElse(null);
            clonedSlots[i] = new TemplateSlotDelegate(button, i);
        }
        return new DispenserTemplate(clonedSlots);
    }

    public static class Builder {

        private DispenserTemplate templateInstance;

        public Builder() {
            this.templateInstance = new DispenserTemplate(TemplateHelper.slotsOf(10));
        }

        public Builder set(int index, @Nullable Button button) {
            templateInstance.set(index, button);
            return this;
        }

        public Builder set(int row, int col, @Nullable Button button) {
            templateInstance.set(row, col, button);
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

        public DispenserTemplate build() {
            DispenserTemplate templateToReturn = templateInstance;
            templateInstance = new DispenserTemplate(TemplateHelper.slotsOf(10));
            return templateToReturn;
        }

    }
}
