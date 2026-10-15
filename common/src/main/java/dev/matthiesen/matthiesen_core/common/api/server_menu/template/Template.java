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

/**
 * Represents a template for a server menu, defining the layout and behavior of the menu.
 * Each template has a specific type and contains an array of slots that can hold buttons.
 */
public abstract class Template extends UpdateEmitter<Template> {

    /**
     * The type of the template, defining its layout and behavior.
     */
    protected final TemplateType templateType;
    private final TemplateSlotDelegate[] slots;

    /**
     * Constructs a new Template with the specified TemplateType and an array of TemplateSlotDelegate objects.
     *
     * @param templateType The type of the template.
     * @param slots        An array of TemplateSlotDelegate objects representing the slots in the template.
     */
    protected Template(@NotNull TemplateType templateType, @NotNull TemplateSlotDelegate[] slots) {
        this.templateType = templateType;
        this.slots = slots;
    }

    /**
     * Returns the TemplateType of this Template.
     *
     * @return The TemplateType associated with this Template.
     */
    public final TemplateType getTemplateType() {
        return this.templateType;
    }

    /**
     * Returns the number of slots in this template.
     *
     * @return The size of the template, representing the number of slots.
     */
    public final int getSize() {
        return this.slots.length;
    }

    /**
     * Returns the TemplateSlotDelegate at the specified index.
     *
     * @param index The index of the slot to retrieve.
     * @return The TemplateSlotDelegate at the specified index.
     */
    public final TemplateSlotDelegate getSlot(int index) {
        return this.slots[index];
    }

    /**
     * Returns an immutable list of all slots in this template.
     *
     * @return An immutable list of TemplateSlotDelegate objects representing the slots in this template.
     */
    public final List<TemplateSlotDelegate> getSlots() {
        return ImmutableList.copyOf(this.slots);
    }

    /**
     * Sets the button for the slot at the specified index.
     *
     * @param index  The index of the slot to set the button for.
     * @param slot The slot to set for the slot, or null to clear it.
     */
    @SuppressWarnings("unused")
    public final void setSlot(int index, TemplateSlotDelegate slot) {
        this.slots[index] = slot;
    }

    /**
     * Creates a deep copy of this Template, including its slots and buttons.
     *
     * @return A new Template instance that is a deep copy of this one.
     */
    public abstract Template clone();

}
