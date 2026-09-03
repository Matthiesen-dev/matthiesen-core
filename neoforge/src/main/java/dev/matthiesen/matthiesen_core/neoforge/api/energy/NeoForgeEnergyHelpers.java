package dev.matthiesen.matthiesen_core.neoforge.api.energy;

import dev.matthiesen.matthiesen_core.common.api.energy.AbstractCommonEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * A helper class for energy-related operations in NeoForge.
 */
@SuppressWarnings("unused")
public final class NeoForgeEnergyHelpers {

    /**
     * Constructs a new instance of NeoForgeEnergyHelpers.
     * This class is not intended to be instantiated, as it provides static utility methods for energy management.
     */
    public NeoForgeEnergyHelpers() {}

    /**
     * Distributes energy from the given {@code storage} to adjacent blocks in the {@code level} at the given {@code pos}.
     *
     * @param storage the energy storage to distribute energy from
     * @param level the level in which the block resides
     * @param pos the position of the block to distribute energy from
     */
    public void distributeEnergy(AbstractCommonEnergyStorage storage, Level level, BlockPos pos) {
        int availableEnergy = (int) storage.getEnergy();
        int maxTransfer = Math.toIntExact(Math.min(availableEnergy, storage.getMaxExtract()));

        for (Direction direction : Direction.values()) {
            if (maxTransfer <= 0) break;

            BlockPos neighborPos = pos.relative(direction);
            IEnergyStorage targetStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.getOpposite());

            if (targetStorage != null && targetStorage.canReceive()) {
                int accepted = targetStorage.receiveEnergy(maxTransfer, false);
                if (accepted > 0) {
                    storage.setEnergy(storage.getEnergy() - accepted);
                    maxTransfer -= accepted;
                }
            }
        }
    }

    /**
     * Checks if the block at {@code pos} in {@code level} supports energy transfer in the given {@code direction}.
     *
     * @param level the level to check
     * @param pos the position of the block to check
     * @param direction the direction to check for energy transfer
     * @return true if the block supports energy transfer in the given direction, false otherwise
     */
    public boolean supportsEnergyTransfer(Level level, BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        IEnergyStorage targetStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.getOpposite());
        return targetStorage != null && (targetStorage.canReceive() || targetStorage.canExtract());
    }

    /**
     * Attempts to push up to {@code maxAmount} energy into the block at {@code targetPos},
     * queried from {@code fromSide} (the face of the target block that faces the cable).
     *
     * @param maxAmount The maximum amount of energy to push.
     * @param level The level in which the target block resides.
     * @param targetPos The position of the target block to push energy into.
     * @param fromSide The direction from which the energy is being pushed (the face of the target block that faces the cable).
     * @return the amount actually inserted
     */
    public long pushEnergyTo(long maxAmount, Level level, BlockPos targetPos, Direction fromSide) {
        IEnergyStorage targetStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, targetPos, fromSide);
        if (targetStorage == null || !targetStorage.canReceive()) return 0;
        return targetStorage.receiveEnergy((int) Math.min(maxAmount, Integer.MAX_VALUE), false);
    }

    /**
     * Checks whether the given stack can receive energy.
     *
     * @param stack The item stack to check.
     * @return true if the stack can receive energy, false otherwise
     */
    public boolean canChargeItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null && energyStorage.canReceive();
    }

    /**
     * Attempts to insert up to {@code maxAmount} energy into the given item stack.
     *
     * @param stack The item stack to charge.
     * @param maxAmount The maximum amount of energy to insert.
     * @return the amount actually inserted
     */
    public long chargeItem(ItemStack stack, long maxAmount) {
        if (stack.isEmpty() || maxAmount <= 0) return 0;
        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage == null || !energyStorage.canReceive()) return 0;
        return energyStorage.receiveEnergy((int) Math.min(maxAmount, Integer.MAX_VALUE), false);
    }
}
