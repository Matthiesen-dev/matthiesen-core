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
public class HopperTemplate extends Template {

    protected HopperTemplate(@NotNull TemplateSlotDelegate[] slots) {
        super(TemplateType.HOPPER, slots);
    }

    public static Builder builder() {
        return new Builder();
    }

    public HopperTemplate set(int index, @Nullable Button button) {
        getSlot(index).setButton(button);
        return this;
    }

    public HopperTemplate fill(@Nullable Button button) {
        for (int i = 0; i < getSize(); i++) {
            if (getSlot(i).getButton().isEmpty()) {
                getSlot(i).setButton(button);
            }
        }
        return this;
    }

    public HopperTemplate fillFromList(@NotNull List<Button> buttons) {
        Iterator<Button> iterator = buttons.iterator();
        for (int i = 0; i < getSize(); i++) {
            if (getSlot(i).getButton().isEmpty()) {
                getSlot(i).setButton((iterator.hasNext()) ? iterator.next() : null);
            }
        }
        return this;
    }

    public HopperTemplate clear() {
        for (int slotIndex = 0; slotIndex < getSize(); slotIndex++) {
            getSlot(slotIndex).setButton(null);
        }
        return this;
    }

    @Override
    public HopperTemplate clone() {
        TemplateSlotDelegate[] clonedSlots = new TemplateSlotDelegate[getSize()];
        for (int i = 0; i < getSize(); i++) {
            Button button = getSlot(i).getButton().orElse(null);
            clonedSlots[i] = new TemplateSlotDelegate(button, i);
        }
        return new HopperTemplate(clonedSlots);
    }

    public static class Builder {

        private HopperTemplate templateInstance;

        public Builder() {
            this.templateInstance = new HopperTemplate(TemplateHelper.slotsOf(5));
        }

        public Builder set(int index, @Nullable Button button) {
            templateInstance.set(index, button);
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

        public HopperTemplate build() {
            HopperTemplate templateToReturn = templateInstance;
            templateInstance = new HopperTemplate(TemplateHelper.slotsOf(5));
            return templateToReturn;
        }

    }
}
