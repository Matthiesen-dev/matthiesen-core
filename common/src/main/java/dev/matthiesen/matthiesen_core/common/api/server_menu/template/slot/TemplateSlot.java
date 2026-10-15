/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.template.slot;

import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TemplateSlot extends Slot {

    private final TemplateSlotDelegate delegate;

    public TemplateSlot(Container container, @NotNull TemplateSlotDelegate delegate, int xPosition, int yPosition) {
        super(container, delegate.getIndex(), xPosition, yPosition);
        this.delegate = delegate;
    }

    public TemplateSlotDelegate getDelegate() {
        return this.delegate;
    }

    public void setButton(@Nullable Button button) {
        this.delegate.setButton(button);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return this.delegate.getButton().map(Button::getDisplay).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

}
