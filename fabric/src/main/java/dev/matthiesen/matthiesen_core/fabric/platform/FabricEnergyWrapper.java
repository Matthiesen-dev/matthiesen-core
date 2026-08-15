package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.core.energy.CommonEnergyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

/**
 * The FabricEnergyWrapper class implements the EnergyStorage interface and serves as a wrapper for the CommonEnergyStorage instance.
 * It provides methods to interact with the energy storage, allowing for energy insertion, extraction, and retrieval of energy information.
 */
public final class FabricEnergyWrapper implements EnergyStorage {
    private final CommonEnergyStorage storage;

    /**
     * Constructs a new FabricEnergyWrapper that wraps the provided CommonEnergyStorage instance.
     * @param storage The CommonEnergyStorage instance to be wrapped by this FabricEnergyWrapper.
     */
    public FabricEnergyWrapper(CommonEnergyStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean supportsExtraction() {
        return storage.canExtract();
    }

    @Override
    public boolean supportsInsertion() {
        return storage.canReceive();
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        long received = Math.min(storage.getCapacity() - storage.getEnergy(), Math.min(maxAmount, storage.getMaxExtract()));
        transaction.addCloseCallback((t, result) -> {
            if (result.wasCommitted()) {
                storage.setEnergy(storage.getEnergy() + received);
            }
        });
        return received;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        long extracted = Math.min(storage.getEnergy(), Math.min(maxAmount, storage.getMaxExtract()));
        transaction.addCloseCallback((t, result) -> {
            if (result.wasCommitted()) {
                storage.setEnergy(storage.getEnergy() - extracted);
            }
        });
        return extracted;
    }

    @Override
    public long getAmount() {
        return storage.getEnergy();
    }

    @Override
    public long getCapacity() {
        return storage.getCapacity();
    }
}
