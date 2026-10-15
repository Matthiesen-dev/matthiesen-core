/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import dev.matthiesen.matthiesen_core.common.api.server_menu.data.UpdateEmitter;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public abstract class ButtonBase extends UpdateEmitter<Button> implements Button {

    private ItemStack display;

    protected ButtonBase(@NotNull ItemStack display) {
        this.display = requireNonNull(display);
    }

    public final ItemStack getDisplay() {
        return display;
    }

    public void setDisplay(@NotNull ItemStack display) {
        this.display = display;
        update();
    }

}
