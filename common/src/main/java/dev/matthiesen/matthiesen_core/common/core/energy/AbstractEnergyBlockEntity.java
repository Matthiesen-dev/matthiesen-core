package dev.matthiesen.matthiesen_core.common.core.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The AbstractEnergyBlockEntity class serves as a base class for block entities that require energy storage capabilities.
 * It extends the BlockEntity class and provides an abstract method to retrieve the associated CommonEnergyStorage instance.
 * Subclasses must implement this method to provide their specific energy storage implementation.
 */
public abstract class AbstractEnergyBlockEntity extends BlockEntity {
    public AbstractEnergyBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    /**
     * Returns the CommonEnergyStorage instance associated with this block entity. This method must be implemented by subclasses to provide the specific energy storage for the block entity.
     * @return The CommonEnergyStorage instance for this block entity.
     */
    public abstract CommonEnergyStorage getEnergyStorage();

    /**
     * Distributes energy from this block entity's energy storage to adjacent blocks that can receive energy.
     * @param level The level in which the block entity exists.
     * @param pos The position of the block entity.
     */
    @SuppressWarnings("unused")
    public void distributeEnergy(Level level, BlockPos pos) {
        getEnergyStorage().distributeEnergy(level, pos);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        getEnergyStorage().load(compoundTag);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        getEnergyStorage().save(compoundTag);
    }
}
