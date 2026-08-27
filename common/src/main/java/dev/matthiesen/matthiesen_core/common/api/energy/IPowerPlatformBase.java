package dev.matthiesen.matthiesen_core.common.api.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * Interface representing a power platform that provides energy-related functionalities.
 */
@SuppressWarnings("unused")
public interface IPowerPlatformBase {

    /**
     * Registers the energy capability for the given block entity type.
     *
     * @param blockEntityTypeSupplier a supplier that provides the block entity type to register the energy capability for
     */
    void registerEnergyCapability(Supplier<BlockEntityType<?>> blockEntityTypeSupplier);

    /**
     * Distributes energy from the given {@code storage} to adjacent blocks in the {@code level} at the given {@code pos}.
     *
     * @param storage the energy storage to distribute energy from
     * @param level the level to distribute energy in
     * @param pos the position of the block to distribute energy from
     */
    void distributeEnergy(AbstractCommonEnergyStorage storage, Level level, BlockPos pos);

    /**
     * Checks if the block at {@code pos} in {@code level} supports energy transfer in the given {@code direction}.
     *
     * @param level the level to check
     * @param pos the position of the block to check
     * @param direction the direction to check for energy transfer
     * @return true if the block supports energy transfer in the given direction, false otherwise
     */
    boolean supportsEnergyTransfer(Level level, BlockPos pos, Direction direction);

    /**
     * Attempts to push up to {@code maxAmount} energy into the block at {@code targetPos},
     * queried from {@code fromSide} (the face of the target block that faces the cable).
     *
     * @param maxAmount the maximum amount of energy to push
     * @param level the level in which the target block is located
     * @param targetPos the position of the target block
     * @param fromSide the direction from which energy is being pushed
     * @return the amount of energy actually pushed into the target block
     */
    long pushEnergyTo(long maxAmount, Level level, BlockPos targetPos, Direction fromSide);
}
