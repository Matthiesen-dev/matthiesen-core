package dev.matthiesen.matthiesen_core.common.api.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * Utility interface for registering entity and block entity renderers in a platform-neutral way.
 */
public interface EntityRendererRegistrar {
    /**
     * Registers an entity renderer.
     * @param entityType The type of the entity to register the renderer for.
     * @param rendererProvider The provider for the entity renderer.
     * @param <T> The type of the entity.
     */
    <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider);

    /**
     * Registers a block entity renderer.
     * @param blockEntityType The type of the block entity to register the renderer for.
     * @param rendererProvider The provider for the block entity renderer.
     * @param <T> The type of the block entity.
     */
    <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider);

    /**
     * Registers an entity renderer from a supplier-based entity type.
     * This method allows for deferred registration of entity renderers, where the entity type is provided by a supplier.
     * @param entityTypeSupplier The supplier providing the entity type to register the renderer for.
     * @param rendererProvider The provider for the entity renderer.
     * @param <T> The type of the entity.
     */
    default <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> rendererProvider) {
        registerEntityRenderer(entityTypeSupplier.get(), rendererProvider);
    }

    /**
     * Registers a block entity renderer from a supplier-based block entity type.
     * This method allows for deferred registration of block entity renderers, where the block entity type is provided by a supplier.
     * @param blockEntityTypeSupplier The supplier providing the block entity type to register the renderer for.
     * @param rendererProvider The provider for the block entity renderer.
     * @param <T> The type of the block entity.
     */
    default <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<? extends T>> blockEntityTypeSupplier, BlockEntityRendererProvider<T> rendererProvider) {
        registerBlockEntityRenderer(blockEntityTypeSupplier.get(), rendererProvider);
    }
}


