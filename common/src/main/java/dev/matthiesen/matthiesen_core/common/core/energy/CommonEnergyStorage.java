package dev.matthiesen.matthiesen_core.common.core.energy;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

/**
 * The CommonEnergyStorage class represents a basic energy storage system that can store, extract, and receive energy.
 * It provides methods for managing energy levels, saving/loading energy data, and distributing energy to adjacent blocks.
 */
public class CommonEnergyStorage {
    private long energy = 0;
    private final long capacity;
    private final long maxExtract;

    /**
     * Constructs a new CommonEnergyStorage with the specified capacity and maximum extraction rate.
     * @param capacity The maximum energy capacity of the storage.
     * @param maxExtract The maximum amount of energy that can be extracted from the storage at once.
     */
    public CommonEnergyStorage(long capacity, long maxExtract) {
        this.capacity = capacity;
        this.maxExtract = maxExtract;
    }

    /**
     * Distributes energy from this energy storage to adjacent blocks that can receive energy.
     * @param level The level in which the energy storage exists.
     * @param blockPos The position of the energy storage.
     */
    public void distributeEnergy(Level level, BlockPos blockPos) {
        MatthiesenCoreCommon.INSTANCE.getCommonRegistry().distributeEnergy(this, level, blockPos);
    }

    /**
     * Returns the current energy value of the storage.
     * @return The current energy value of the storage.
     */
    public long getEnergy() { return this.energy; }

    /**
     * Returns the maximum energy capacity of the storage.
     * @return The maximum energy capacity of the storage.
     */
    public long getCapacity() { return this.capacity; }

    /**
     * Sets the current energy value of the storage, ensuring it remains within the valid range of 0 to the storage's capacity.
     * @param energy The new energy value to set for the storage. It will be clamped between 0 and the storage's capacity.
     */
    public void setEnergy(long energy) { this.energy = Math.clamp(0, capacity, energy); }

    /**
     * Returns the maximum amount of energy that can be extracted from the storage at once.
     * @return The maximum extraction rate of the energy storage.
     */
    public long getMaxExtract() { return this.maxExtract; }

    /**
     * Extracts energy from the storage, up to the specified maximum amount and the storage's current energy level.
     * @param maxExtract The maximum amount of energy to extract.
     * @param simulate If true, the extraction is only simulated and does not actually modify the energy storage.
     * @return The actual amount of energy extracted from the storage.
     */
    public int extract(int maxExtract, boolean simulate) {
        long extracted = Math.min(this.energy, Math.min(maxExtract, this.maxExtract));
        if (!simulate) {
            this.energy -= extracted;
        }
        return (int) extracted;
    }

    /**
     * Inserts energy into the storage, up to the specified maximum amount and the storage's capacity.
     * @param maxReceive The maximum amount of energy to insert.
     * @param simulate If true, the insertion is only simulated and does not actually modify the energy storage.
     * @return The actual amount of energy inserted into the storage.
     */
    public int insert(int maxReceive, boolean simulate) {
        long received = Math.min(this.capacity - this.energy, Math.min(maxReceive, this.maxExtract));
        if (!simulate) {
            this.energy += received;
        }
        return (int) received;
    }

    /**
     * Saves the current energy value to the provided CompoundTag for persistence.
     * @param tag The CompoundTag to which the energy value will be saved.
     */
    public void save(CompoundTag tag) { tag.putLong("Energy", this.energy); }

    /**
     * Loads the energy value from the provided CompoundTag and sets it to the energy storage.
     * @param tag The CompoundTag containing the energy value to load into the energy storage.
     */
    public void load(CompoundTag tag) { this.energy = tag.getLong("Energy"); }

    /**
     * Indicates whether the energy storage can extract energy. In this case, it returns true if the maximum extraction rate is greater than zero.
     * @return true if the energy storage can extract energy, false otherwise.
     */
    public boolean canExtract() { return this.maxExtract > 0; }

    /**
     * Indicates whether the energy storage can receive energy. In this case, it always returns true since the storage can accept energy input.
     * @return true, indicating that the energy storage can receive energy.
     */
    public boolean canReceive() { return true; }
}
