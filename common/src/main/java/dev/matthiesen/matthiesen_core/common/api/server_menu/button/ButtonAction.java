/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents an action performed on a button within a server menu.
 * This class encapsulates the details of the action, including the player who performed it,
 * the type of click, the button involved, the template and page context, and the slot index.
 */
@SuppressWarnings("unused")
public class ButtonAction {

    private final ServerPlayer player;
    private final ButtonClick clickType;
    private final Button button;
    private final Template template;
    private final Page page;
    private final int slot;

    /**
     * Constructs a new ButtonAction with the specified parameters.
     *
     * @param player    The server player who triggered the action.
     * @param clickType The type of click that triggered the action.
     * @param button    The button associated with the action.
     * @param template  The template associated with the button action.
     * @param page      The page associated with the button action.
     * @param slot      The index of the slot that was clicked.
     */
    public ButtonAction(@NotNull ServerPlayer player,
                        @NotNull ButtonClick clickType,
                        @NotNull Button button,
                        @NotNull Template template,
                        @NotNull Page page,
                        int slot) {
        this.player = Objects.requireNonNull(player);
        this.clickType = Objects.requireNonNull(clickType);
        this.button = Objects.requireNonNull(button);
        this.template = Objects.requireNonNull(template);
        this.page = Objects.requireNonNull(page);
        this.slot = slot;
    }

    /**
     * Returns the player who triggered this button action.
     *
     * @return The server player.
     */
    public ServerPlayer getPlayer() {
        return player;
    }

    /**
     * Returns the type of click that triggered this button action.
     *
     * @return The click type.
     */
    public ButtonClick getClickType() {
        return clickType;
    }

    /**
     * Returns the button associated with this action.
     *
     * @return The button.
     */
    public Button getButton() {
        return button;
    }

    /**
     * Returns the template associated with this button action.
     *
     * @return The template.
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Returns the page associated with this button action.
     *
     * @return The page.
     */
    public Page getPage() {
        return page;
    }

    /**
     * Returns the index of the slot that was clicked.
     *
     * @return The index of the clicked slot.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Determines if the slot is part of the player's inventory.
     * This is based on the slot index and the size of the template.
     *
     * @return true if the slot is in the inventory, false otherwise.
     */
    public boolean isSlotInInventory() {
        return page.getInventoryTemplate().isPresent() && slot >= template.getSize();
    }

    /**
     * Returns the index of the slot within the inventory, if applicable.
     * If the slot is not part of the inventory, an empty Optional is returned.
     *
     * @return An Optional containing the inventory slot index, or empty if not applicable.
     */
    public Optional<Integer> getInventorySlot() {
        if (isSlotInInventory()) {
            return Optional.of(slot - template.getSize());
        } else {
            return Optional.empty();
        }
    }

}
