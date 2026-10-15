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

/**
 * Represents an action performed on a movable button in a server menu, encapsulating the player, click type, button, template, page, and slot involved in the action.
 * This class is used to pass information about the action to event handlers or callbacks.
 */
public class MovableButtonAction extends ButtonAction {

    private boolean cancelled;

    /**
     * Constructs a new MovableButtonAction instance with the specified parameters.
     *
     * @param player    The ServerPlayer associated with this action. Must not be null.
     * @param clickType The type of button click that triggered this action. Must not be null.
     * @param button    The Button involved in this action. Must not be null.
     * @param template  The Template associated with this action. Must not be null.
     * @param page      The Page associated with this action. Must not be null.
     * @param slot      The slot index where the action occurred.
     */
    public MovableButtonAction(@NotNull ServerPlayer player,
                               @NotNull ButtonClick clickType,
                               @NotNull Button button,
                               @NotNull Template template,
                               @NotNull Page page, int slot) {
        super(player, clickType, button, template, page, slot);
    }

    /**
     * Sets the cancelled state of the action.
     *
     * @param cancelled true to cancel the action, false to allow it.
     */
    @SuppressWarnings("unused")
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Checks if the action has been cancelled.
     *
     * @return true if the action is cancelled, false otherwise.
     */
    public boolean isCancelled() {
        return cancelled;
    }

}
