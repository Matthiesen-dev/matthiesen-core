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
     */
    <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider);

    /**
     * Registers a block entity renderer.
     */
    <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider);

    /**
     * Registers an entity renderer from a supplier-based entity type.
     */
    default <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> rendererProvider) {
        registerEntityRenderer(entityTypeSupplier.get(), rendererProvider);
    }

    /**
     * Registers a block entity renderer from a supplier-based block entity type.
     */
    default <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<? extends T>> blockEntityTypeSupplier, BlockEntityRendererProvider<T> rendererProvider) {
        registerBlockEntityRenderer(blockEntityTypeSupplier.get(), rendererProvider);
    }
}


