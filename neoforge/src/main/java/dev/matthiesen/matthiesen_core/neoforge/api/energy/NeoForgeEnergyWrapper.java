package dev.matthiesen.matthiesen_core.neoforge.api.energy;

import dev.matthiesen.matthiesen_core.common.api.energy.AbstractCommonEnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * The NeoForgeEnergyWrapper class implements the IEnergyStorage interface and serves as a wrapper for the CommonEnergyStorage instance.
 * It provides methods to interact with the energy storage, allowing for energy insertion, extraction, and retrieval of energy information.
 */
@SuppressWarnings("unused")
public final class NeoForgeEnergyWrapper implements IEnergyStorage {
    private final AbstractCommonEnergyStorage storage;

    /**
     * Constructs a new NeoForgeEnergyWrapper that wraps the provided CommonEnergyStorage instance.
     * @param storage The CommonEnergyStorage instance to be wrapped by this NeoForgeEnergyWrapper.
     */
    public NeoForgeEnergyWrapper(AbstractCommonEnergyStorage storage) {
        this.storage = storage;
    }

    @Override
    public int receiveEnergy(int i, boolean bl) {
        return storage.insert(i, bl);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return storage.extract(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return (int) storage.getEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) storage.getCapacity();
    }

    @Override
    public boolean canExtract() {
        return storage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return storage.canReceive();
    }
}
