package dev.matthiesen.matthiesen_core.common.api.energy;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Interface for platform-specific implementations of energy-related item behaviors.
 * This interface defines methods for checking if an item can receive energy and for charging items.
 */
@SuppressWarnings("unused")
public interface IPowerPlatformItems {

    /**
     * Registers the energy capability for the specified items.
     *
     * @param items the items to register the energy capability for
     */
    void registerItemEnergyCapability(ItemLike... items);

    /**
     * Checks whether the given item stack can receive energy.
     *
     * @param stack the item stack to check
     * @return true if the item stack can receive energy, false otherwise
     */
    boolean canChargeItem(ItemStack stack);

    /**
     * Attempts to insert up to {@code maxAmount} energy into the given item stack.
     *
     * @param stack the item stack to charge
     * @param maxAmount the maximum amount of energy to insert
     * @return the amount actually inserted
     */
    long chargeItem(ItemStack stack, long maxAmount);
}
