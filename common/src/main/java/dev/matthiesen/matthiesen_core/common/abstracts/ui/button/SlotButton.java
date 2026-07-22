package dev.matthiesen.matthiesen_core.common.abstracts.ui.button;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A simple slot that cannot be interacted with. This is useful for buttons in GUIs, as it prevents the player from accidentally moving items into or out of the slot.
 * The slot will still render the item in the slot, so it can be used to display an item as part of the button's appearance. The slot will also still call the onTake
 * method when the player clicks on the slot, so it can be used to trigger actions when the button is
 */
@SuppressWarnings("unused")
public class SlotButton extends Slot {
    /**
     * Default constructor for the SlotButton class. This constructor is used when creating a new instance of the SlotButton class. It takes in the container,
     * slot index, and x and y coordinates for the button, and passes them to the superclass constructor
     * @param container The current container for the button
     * @param slot The index of the slot that this button represents in the container
     * @param x The x coordinate of the button in the GUI. This is used to position the button correctly within the GUI layout.
     * @param y The y coordinate of the button in the GUI. This is used to position the button correctly within the GUI layout.
     */
    public SlotButton(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPickup(Player p) { return false; }

    @Override
    public boolean mayPlace(ItemStack s) { return false; }
}
