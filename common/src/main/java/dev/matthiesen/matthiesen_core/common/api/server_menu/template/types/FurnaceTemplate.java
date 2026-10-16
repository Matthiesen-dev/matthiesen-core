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

@SuppressWarnings("unused")
public class FurnaceTemplate extends Template {

    public FurnaceTemplate(@NotNull TemplateSlotDelegate[] slots) {
        super(TemplateType.FURNACE, slots);
    }

    public TemplateSlotDelegate getInputMaterial() {
        return getSlot(0);
    }

    public TemplateSlotDelegate getFuel() {
        return getSlot(1);
    }

    public TemplateSlotDelegate getOutputMaterial() {
        return getSlot(2);
    }

    public FurnaceTemplate inputMaterial(@Nullable Button button) {
        getSlot(0).setButton(button);
        return this;
    }

    public FurnaceTemplate fuel(@Nullable Button button) {
        getSlot(1).setButton(button);
        return this;
    }

    public FurnaceTemplate outputMaterial(@Nullable Button button) {
        getSlot(2).setButton(button);
        return this;
    }

    public FurnaceTemplate clear() {
        for (int slotIndex = 0; slotIndex < getSize(); slotIndex++) {
            getSlot(slotIndex).setButton(null);
        }
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public FurnaceTemplate clone() {
        TemplateSlotDelegate[] clonedSlots = new TemplateSlotDelegate[getSize()];
        for (int i = 0; i < getSize(); i++) {
            Button button = getSlot(i).getButton().orElse(null);
            clonedSlots[i] = new TemplateSlotDelegate(button, i);
        }
        return new FurnaceTemplate(clonedSlots);
    }

    public static class Builder {

        private FurnaceTemplate templateInstance;

        public Builder() {
            this.templateInstance = new FurnaceTemplate(TemplateHelper.slotsOf(3));
        }

        public Builder inputMaterial(@Nullable Button button) {
            templateInstance.inputMaterial(button);
            return this;
        }

        public Builder fuel(@Nullable Button button) {
            templateInstance.fuel(button);
            return this;
        }

        public Builder outputMaterial(@Nullable Button button) {
            templateInstance.outputMaterial(button);
            return this;
        }

        public FurnaceTemplate build() {
            FurnaceTemplate templateToReturn = templateInstance;
            templateInstance = new FurnaceTemplate(TemplateHelper.slotsOf(3));
            return templateToReturn;
        }

    }
}
