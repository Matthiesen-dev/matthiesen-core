package dev.matthiesen.matthiesen_core.common.api.server_menu.virtual;

import dev.matthiesen.matthiesen_core.common.core.server_menu.MenuCallbackEngine;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.LecternMenu;

import java.util.List;

/**
 * Represents a virtual lectern menu that can be opened by players in a server menu system.
 * This class extends the LecternMenu and provides additional functionality for managing callbacks
 * and ensuring that the menu remains valid for players.
 */
public class VirtualLecternMenu extends LecternMenu {
    private final List<String> callbacksToClean;
    private final boolean isBookRemovable;

    /**
     * Constructs a new VirtualLecternMenu with the specified parameters.
     *
     * @param id               The unique identifier for this menu instance.
     * @param container        The SimpleContainer representing the inventory of the lectern.
     * @param data             The ContainerData used to manage the state of the lectern, such as the current page.
     * @param callbacksToClean A list of callback identifiers that should be invalidated when this menu is closed.
     */
    public VirtualLecternMenu(int id, SimpleContainer container, ContainerData data, List<String> callbacksToClean) {
        this(id, container, data, callbacksToClean, false);
    }

    /**
     * Constructs a new VirtualLecternMenu with the specified parameters, including the option to specify whether the book is removable.
     *
     * @param id               The unique identifier for this menu instance.
     * @param container        The SimpleContainer representing the inventory of the lectern.
     * @param data             The ContainerData used to manage the state of the lectern, such as the current page.
     * @param callbacksToClean A list of callback identifiers that should be invalidated when this menu is closed.
     * @param isBookRemovable  A boolean indicating whether the book can be removed from the lectern by players.
     */
    public VirtualLecternMenu(int id, SimpleContainer container, ContainerData data, List<String> callbacksToClean, boolean isBookRemovable) {
        super(id, container, data);
        this.callbacksToClean = callbacksToClean;
        this.isBookRemovable = isBookRemovable;
    }

    /**
     * Determines whether the menu is still valid for the given player.
     * In this implementation, it always returns true, indicating that the menu is always valid.
     *
     * @param player The player to check validity for.
     * @return true, indicating the menu is always valid.
     */
    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    /**
     * Called when the menu is removed (closed) by the player.
     * This method invalidates any callbacks associated with this menu to prevent memory leaks or unintended behavior.
     *
     * @param player The player who closed the menu.
     */
    @Override
    public void removed(Player player) {
        super.removed(player);
        if (this.callbacksToClean != null) {
            for (String cmd : this.callbacksToClean) {
                MenuCallbackEngine.invalidate(cmd);
            }
        }
    }

    /**
     * Determines whether the book in the lectern can be removed by players.
     * If the book is not removable, we return early otherwise, we call the superclass method to handle the removal.
     *
     * @param player The player attempting to remove the book.
     * @param i The button index being clicked (not used in this implementation).
     * @return true if the book can be removed, false otherwise.
     */
    @Override
    public boolean clickMenuButton(Player player, int i) {
        if (i == 3 && !this.isBookRemovable) return false;
        return super.clickMenuButton(player, i);
    }
}
