/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button.moveable;

import dev.matthiesen.matthiesen_core.common.api.server_menu.button.*;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class MovableButtonAction extends ButtonAction {

    private boolean cancelled;

    public MovableButtonAction(@NotNull ServerPlayer player,
                               @NotNull ButtonClick clickType,
                               @NotNull Button button,
                               @NotNull Template template,
                               @NotNull Page page, int slot) {
        super(player, clickType, button, template, page, slot);
    }

    @SuppressWarnings("unused")
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
