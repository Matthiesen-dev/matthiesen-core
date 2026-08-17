package dev.matthiesen.matthiesen_core.common.api.energy;

/**
 * The AbstractCommonEnergyGenerator class extends the AbstractCommonEnergyStorage class and represents an energy generator that can generate energy up to a specified capacity.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommonEnergyGenerator extends AbstractCommonEnergyStorage {

    /**
     * Constructs a new AbstractCommonEnergyGenerator with the specified capacity and maximum extraction rate.
     * @param capacity The maximum energy capacity of the generator.
     * @param maxExtract The maximum amount of energy that can be extracted from the generator at once.
     */
    public AbstractCommonEnergyGenerator(long capacity, long maxExtract) {
        super(capacity, maxExtract);
    }

    /**
     * Generates energy and adds it to the generator's energy storage, up to the specified amount and the generator's capacity.
     * @param amount The amount of energy to generate.
     * @return The actual amount of energy generated and added to the storage.
     */
    public long generate(long amount) {
        long generated = Math.min(this.getCapacity() - this.getEnergy(), amount);
        this.setEnergy(this.getEnergy() + generated);
        return generated;
    }

    /**
     * Inserts energy into the generator's energy storage. Since generators do not accept energy input, this method always returns 0.
     * @param maxReceive The maximum amount of energy to insert.
     * @param simulate If true, the insertion is only simulated and does not actually modify the energy storage.
     * @return Always returns 0, indicating that no energy was inserted into the generator.
     */
    @Override
    public int insert(int maxReceive, boolean simulate) {
        return 0;
    }

    /**
     * Indicates whether the generator can receive energy. In this case, it always returns false since generators do not accept energy input.
     * @return false, indicating that the generator cannot receive energy.
     */
    public boolean canReceive() { return false; }
}
