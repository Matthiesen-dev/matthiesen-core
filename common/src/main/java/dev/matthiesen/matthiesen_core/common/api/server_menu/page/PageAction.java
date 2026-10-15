/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.page;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class PageAction {

    private final ServerPlayer player;
    private final Page page;

    public PageAction(@NotNull ServerPlayer player, @NotNull Page page) {
        this.player = player;
        this.page = page;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public Page getPage() {
        return page;
    }

}