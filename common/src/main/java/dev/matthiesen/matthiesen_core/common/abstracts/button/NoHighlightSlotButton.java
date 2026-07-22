package dev.matthiesen.matthiesen_core.common.abstracts.button;

import net.minecraft.world.Container;

/**
 * A slot button that cannot be highlighted when hovered over. This is useful for buttons in GUIs that should not have a highlight effect when hovered over, such
 * as buttons that are purely decorative or that should not draw attention to themselves. This slot will still render the item in the slot and will still call the onTake
 * method when the player clicks on the slot, so it can be used to trigger actions when the button is clicked without drawing attention to itself.
 */
@SuppressWarnings("unused")
public class NoHighlightSlotButton extends SlotButton {

    /**
     * Default constructor for the NoHighlightSlotButton class. This constructor is used when creating a new instance of the NoHighlightSlotButton class.
     * It takes in the container, slot index, and x and y coordinates for the button, and passes them to the superclass constructor
     * @param container The current container for the button
     * @param slot The index of the slot that this button represents in the container
     * @param x The x coordinate of the button in the GUI. This is used to position the button correctly within the GUI layout.
     * @param y The y coordinate of the button in the GUI. This is used to position the button correctly within the GUI layout.
     */
    public NoHighlightSlotButton(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean isHighlightable() { return false; }
}
