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

/**
 * Represents an action performed on a server menu page, encapsulating the player and the page involved in the action.
 * This class is used to pass information about the action to event handlers or callbacks.
 */
public class PageAction {

    private final ServerPlayer player;
    private final Page page;

    /**
     * Constructs a new PageAction instance with the specified player and page.
     *
     * @param player The ServerPlayer associated with this action. Must not be null.
     * @param page   The Page associated with this action. Must not be null.
     */
    public PageAction(@NotNull ServerPlayer player, @NotNull Page page) {
        this.player = player;
        this.page = page;
    }

    /**
     * Returns the player associated with this action.
     *
     * @return The ServerPlayer instance for this action.
     */
    public ServerPlayer getPlayer() {
        return player;
    }

    /**
     * Returns the page associated with this action.
     *
     * @return The Page instance for this action.
     */
    public Page getPage() {
        return page;
    }

}