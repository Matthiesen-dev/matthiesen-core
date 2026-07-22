package dev.matthiesen.matthiesen_core.common.abstracts;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A simple menu that has no inventory. This is useful for GUIs that don't need to interact with the player's inventory,
 * such as a crafting table or furnace GUI. This menu will not allow the player to move items in or out of the menu, and it will not have any slots.
 * It can be used as a base class for GUIs that don't need an inventory,
 */
@SuppressWarnings("unused")
public abstract class AbstractNoInventoryMenu extends AbstractContainerMenu {
    /**
     * Default constructor for the AbstractNoInventoryMenu class. This constructor is used when creating a new menu instance without a specific menu type.
     * @param menuType The menu type for this menu. This can be null if the menu does not have a specific type.
     * @param i The container ID for this menu. This is used to identify the menu instance and should be unique for each menu instance.
     */
    protected AbstractNoInventoryMenu(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
