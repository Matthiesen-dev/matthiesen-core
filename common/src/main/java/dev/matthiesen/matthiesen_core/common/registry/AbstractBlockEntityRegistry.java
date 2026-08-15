package dev.matthiesen.matthiesen_core.common.registry;

import dev.matthiesen.matthiesen_core.common.api.platform.registry.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Convenience base class for registries that register {@link BlockEntityType} instances.
 *
 * <p>This type locks registration to the block entity type registry category by wiring
 * {@link SupportedRegistries#BLOCK_ENTITY} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractBlockEntityRegistry extends AbstractRegistry<BlockEntityType<?>> {
    /**
     * Creates a block entity type registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractBlockEntityRegistry(String modId) {
        super(modId, SupportedRegistries.BLOCK_ENTITY);
    }

    /**
     * Creates a block entity type registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform block entity type registrations
     */
    protected AbstractBlockEntityRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.BLOCK_ENTITY);
    }

    /**
     * Registers a block entity type with the given name, factory, and block supplier.
     * @param name the name of the block entity type
     * @param entityFactory the factory used to create instances of the block entity
     * @param blockSupplier the supplier of the block associated with the block entity
     * @return a supplier of the registered block entity type
     * @param <T> the type of the block entity
     */
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> register(
            String name,
            BiFunction<BlockPos, BlockState, T> entityFactory,
            Supplier<? extends Block> blockSupplier
    ) {
        return register(name, () -> BlockEntityType.Builder.of(
                entityFactory::apply,
                blockSupplier.get()
        ).build(null));
    }

    /**
     * Registers a block entity type with the given name, factory, and a map of block suppliers.
     * @param name the name of the block entity type
     * @param entityFactory the factory used to create instances of the block entity
     * @param blockSupplierMap a map of block suppliers associated with the block entity
     * @return a supplier of the registered block entity type
     * @param <T> the type of the block entity
     */
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> register(
            String name,
            BiFunction<BlockPos, BlockState, T> entityFactory,
            Map<String, Supplier<? extends Block>> blockSupplierMap
    ) {
        return register(name, () -> BlockEntityType.Builder.of(
                entityFactory::apply,
                blockSupplierMap.values().stream().map(Supplier::get).toArray(Block[]::new)
        ).build(null));
    }

    /**
     * Registers a block entity type with the given name, factory, and a list of block suppliers.
     * @param name the name of the block entity type
     * @param entityFactory the factory used to create instances of the block entity
     * @param blockSuppliers a list of block suppliers associated with the block entity
     * @return a supplier of the registered block entity type
     * @param <T> the type of the block entity
     */
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> register(
            String name,
            BiFunction<BlockPos, BlockState, T> entityFactory,
            List<Supplier<? extends Block>> blockSuppliers
    ) {
        return register(name, () -> BlockEntityType.Builder.of(
                entityFactory::apply,
                blockSuppliers.stream().map(Supplier::get).toArray(Block[]::new)
        ).build(null));
    }

    /**
     * Registers an energy capability for the mod. This method allows you to register a new energy capability that can
     * be used by blocks and entities in your mod.
     * Energy capabilities are used to define how energy is stored, transferred, and consumed within the game, allowing for
     * the creation of custom energy systems and mechanics. By registering an energy capability, you can create blocks and
     * entities that can interact with energy in unique ways, enhancing the gameplay experience and providing new challenges
     * and opportunities for players. The block entity type supplier is used to specify which block entity types should have
     * the registered energy capability, allowing you to control which blocks and entities can store or transfer energy.
     * @param blockEntityTypeSupplier A supplier that provides the block entity type that should have the registered energy
     *                                capability. This allows you to specify which block entity types should be able to store or transfer
     *                                energy, enabling you to create custom energy systems that are specific to your mod's content.
     */
    public void registerEnergyCapability(Supplier<BlockEntityType<? extends BlockEntity>> blockEntityTypeSupplier) {
        MatthiesenCoreCommon.INSTANCE.getCommonRegistry().registerEnergyCapability(blockEntityTypeSupplier);
    }
}

