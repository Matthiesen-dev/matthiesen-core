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

    /**
     * Constructs a new VirtualLecternMenu with the specified parameters.
     *
     * @param id               The unique identifier for this menu instance.
     * @param container        The SimpleContainer representing the inventory of the lectern.
     * @param data             The ContainerData used to manage the state of the lectern, such as the current page.
     * @param callbacksToClean A list of callback identifiers that should be invalidated when this menu is closed.
     */
    public VirtualLecternMenu(int id, SimpleContainer container, ContainerData data, List<String> callbacksToClean) {
        super(id, container, data);
        this.callbacksToClean = callbacksToClean;
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
}
