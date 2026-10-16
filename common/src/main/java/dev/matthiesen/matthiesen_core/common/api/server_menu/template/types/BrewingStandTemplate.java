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
public class BrewingStandTemplate extends Template {

    protected BrewingStandTemplate(@NotNull TemplateSlotDelegate[] slots) {
        super(TemplateType.BREWING_STAND, slots);
    }

    public TemplateSlotDelegate getFuel() {
        return getSlot(0);
    }

    public TemplateSlotDelegate getBottle(int index) {
        if (index < 0 || index > 3) return null;
        return getSlot(index + 1);
    }

    public TemplateSlotDelegate getIngredient() {
        return getSlot(4);
    }

    public BrewingStandTemplate fuel(@Nullable Button button) {
        getSlot(4).setButton(button);
        return this;
    }

    public BrewingStandTemplate ingredient(@Nullable Button button) {
        getSlot(3).setButton(button);
        return this;
    }

    public BrewingStandTemplate bottle(int index, @Nullable Button button) {
        if (index >= 0 && index < 3) getSlot(index).setButton(button);
        return this;
    }

    public BrewingStandTemplate bottles(@Nullable Button button) {
        for (int i = 0; i < 3; i++) bottle(i, button);
        return this;
    }

    public BrewingStandTemplate bottlesFromList(@NotNull List<Button> buttons) {
        Iterator<Button> iterator = buttons.iterator();
        for (int i = 0; i < 3; i++) bottle(i, (iterator.hasNext()) ? iterator.next() : null);
        return this;
    }

    public BrewingStandTemplate clear() {
        for (int slotIndex = 0; slotIndex < getSize(); slotIndex++) {
            getSlot(slotIndex).setButton(null);
        }
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public BrewingStandTemplate clone() {
        TemplateSlotDelegate[] clonedSlots = new TemplateSlotDelegate[getSize()];
        for (int i = 0; i < getSize(); i++) {
            Button button = getSlot(i).getButton().orElse(null);
            clonedSlots[i] = new TemplateSlotDelegate(button, i);
        }
        return new BrewingStandTemplate(clonedSlots);
    }

    public static class Builder {

        private BrewingStandTemplate templateInstance;

        public Builder() {
            this.templateInstance = new BrewingStandTemplate(TemplateHelper.slotsOf(5));
        }

        public Builder fuel(@Nullable Button button) {
            templateInstance.fuel(button);
            return this;
        }

        public Builder ingredient(@Nullable Button button) {
            templateInstance.ingredient(button);
            return this;
        }

        public Builder bottle(int index, @Nullable Button button) {
            templateInstance.bottle(index, button);
            return this;
        }

        public Builder bottles(@Nullable Button button) {
            templateInstance.bottles(button);
            return this;
        }

        public Builder bottlesFromList(@NotNull List<Button> buttons) {
            templateInstance.bottlesFromList(buttons);
            return this;
        }

        public BrewingStandTemplate build() {
            BrewingStandTemplate templateToReturn = templateInstance;
            templateInstance = new BrewingStandTemplate(TemplateHelper.slotsOf(5));
            return templateToReturn;
        }

    }
}
