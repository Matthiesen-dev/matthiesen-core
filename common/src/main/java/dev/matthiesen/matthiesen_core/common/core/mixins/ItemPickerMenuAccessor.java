package dev.matthiesen.matthiesen_core.common.core.mixins;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * This mixin interface provides access to the private fields and methods of the ItemPickerMenu class in Minecraft.
 * It allows other classes to retrieve the list of items in the item picker menu and invoke the scrollTo method to scroll the menu to a specific position.
 * <p>
 * The getItemsList() method is an accessor that retrieves the list of items in the item picker menu, while the invokeScrollTo(float scroll)
 * method is an invoker that allows other classes to call the private scrollTo method of the ItemPickerMenu class.
 * </p>
 * <p>
 * This mixin is useful for mods that need to interact with the item picker menu, such as scrolling to a specific position or retrieving the list of items displayed in the menu.
 * </p>
 * <p>
 * Note: This mixin is intended for use with the Minecraft client and may not be applicable to server-side code. It is also important to ensure
 * that the mixin is applied correctly and that the target class is compatible with the mixin to avoid runtime errors.
 * </p>
 */
@Mixin(targets = "net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$ItemPickerMenu")
public interface ItemPickerMenuAccessor {
    /**
     * Accessor method to retrieve the list of items in the ItemPickerMenu. This method allows access to the private 'items' field
     * of the ItemPickerMenu class, which contains the list of ItemStacks displayed in the item picker menu.
     * @return A NonNullList of ItemStacks representing the items in the item picker menu.
     */
    @Accessor("items")
    NonNullList<ItemStack> getItemsList();

    /**
     * Invokes the private scrollTo method of the ItemPickerMenu class to scroll the item picker menu to a specific position.
     * @param scroll The scroll position to which the item picker menu should be scrolled. This value is typically between 0.0 (top) and 1.0 (bottom).
     */
    @Invoker("scrollTo")
    void invokeScrollTo(float scroll);
}
