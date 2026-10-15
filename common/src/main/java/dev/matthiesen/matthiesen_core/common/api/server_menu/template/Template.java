/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.template;

import com.google.common.collect.ImmutableList;
import dev.matthiesen.matthiesen_core.common.api.server_menu.data.UpdateEmitter;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.slot.TemplateSlotDelegate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Template extends UpdateEmitter<Template> {

    protected final TemplateType templateType;
    private final TemplateSlotDelegate[] slots;

    protected Template(@NotNull TemplateType templateType, @NotNull TemplateSlotDelegate[] slots) {
        this.templateType = templateType;
        this.slots = slots;
    }

    public final TemplateType getTemplateType() {
        return this.templateType;
    }

    public final int getSize() {
        return this.slots.length;
    }

    public final TemplateSlotDelegate getSlot(int index) {
        return this.slots[index];
    }

    public final List<TemplateSlotDelegate> getSlots() {
        return ImmutableList.copyOf(this.slots);
    }

    @SuppressWarnings("unused")
    public final void setSlot(int index, TemplateSlotDelegate slot) {
        this.slots[index] = slot;
    }

    public abstract Template clone();

}
