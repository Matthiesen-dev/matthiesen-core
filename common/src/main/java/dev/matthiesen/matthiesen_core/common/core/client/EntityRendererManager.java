package dev.matthiesen.matthiesen_core.common.core.client;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.client.EntityRendererRegistrar;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Manages entity and block entity renderer registrations in common code.
 *
 * <p>The manager queues registrations and applies them when platform-specific renderer registration callbacks run.
 * Additional registrations after activation are applied immediately.</p>
 */
@SuppressWarnings("unused")
public final class EntityRendererManager {
	private static final List<RendererRegistration> REGISTERED_ENTITY_RENDERERS = new CopyOnWriteArrayList<>();
	private static final List<RendererRegistration> REGISTERED_BLOCK_ENTITY_RENDERERS = new CopyOnWriteArrayList<>();

	/**
	 * Shared singleton instance used by common client bootstrap.
	 */
	public static final EntityRendererManager INSTANCE = new EntityRendererManager();

	private volatile EntityRendererRegistrar activeRegistrar;
	private int appliedEntityRegistrations;
	private int appliedBlockEntityRegistrations;
	private boolean initialized;

	private EntityRendererManager() {}

	/**
	 * Installs the platform renderer registration callback once.
	 *
	 * @param clientEventsListeners platform event listener bridge
	 */
	public synchronized void initialize(CommonLoaderClientEventsListeners clientEventsListeners) {
		if (initialized) {
			return;
		}
		initialized = true;
		clientEventsListeners.applyEntityRendererRegistrations((entityRegistrar, blockEntityRegistrar) ->
				applyEntityRendererRegistrations(new EntityRendererRegistrar() {
					@Override
					public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider) {
						entityRegistrar.accept(entityType, rendererProvider);
					}

					@Override
					public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
						blockEntityRegistrar.accept(blockEntityType, rendererProvider);
					}
				})
		);
		MatthiesenCoreCommon.INSTANCE.createInfoLog("Initialized EntityRendererManager");
	}

	/**
	 * Registers multiple entity and block entity renderers via a registrar callback.
	 *
	 * @param registrarConsumer callback that receives an entity renderer registrar helper
	 */
	public void registerEntityRenderers(Consumer<EntityRendererRegistrar> registrarConsumer) {
		registrarConsumer.accept(new EntityRendererRegistrar() {
			@Override
			public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider) {
				registerEntityRendererInternal(new EntityRendererEntry<>(entityType, rendererProvider));
			}

			@Override
			public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
				registerBlockEntityRendererInternal(new BlockEntityRendererEntry<>(blockEntityType, rendererProvider));
			}
		});
	}

	/**
	 * Queues an entity renderer registration using a supplier-based entity type.
	 *
	 * @param entityTypeSupplier supplier for the entity type
	 * @param rendererProvider renderer provider implementation
	 * @param <T> entity type
	 */
	public <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<T> rendererProvider) {
		registerEntityRendererInternal(registrar -> registrar.registerEntityRenderer(entityTypeSupplier, rendererProvider));
	}

	/**
	 * Queues an entity renderer registration using a concrete entity type.
	 *
	 * @param entityType entity type to register
	 * @param rendererProvider renderer provider implementation
	 * @param <T> entity type
	 */
	public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> rendererProvider) {
		registerEntityRendererInternal(new EntityRendererEntry<>(entityType, rendererProvider));
	}

	/**
	 * Queues a block entity renderer registration using a supplier-based block entity type.
	 *
	 * @param blockEntityTypeSupplier supplier for the block entity type
	 * @param rendererProvider renderer provider implementation
	 * @param <T> block entity type
	 */
	public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<? extends T>> blockEntityTypeSupplier, BlockEntityRendererProvider<T> rendererProvider) {
		registerBlockEntityRendererInternal(registrar -> registrar.registerBlockEntityRenderer(blockEntityTypeSupplier, rendererProvider));
	}

	/**
	 * Queues a block entity renderer registration using a concrete block entity type.
	 *
	 * @param blockEntityType block entity type to register
	 * @param rendererProvider renderer provider implementation
	 * @param <T> block entity type
	 */
	public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererProvider) {
		registerBlockEntityRendererInternal(new BlockEntityRendererEntry<>(blockEntityType, rendererProvider));
	}

	private synchronized void applyEntityRendererRegistrations(EntityRendererRegistrar registrar) {
		activeRegistrar = registrar;

		for (int i = appliedEntityRegistrations; i < REGISTERED_ENTITY_RENDERERS.size(); i++) {
			REGISTERED_ENTITY_RENDERERS.get(i).apply(registrar);
		}
		appliedEntityRegistrations = REGISTERED_ENTITY_RENDERERS.size();

		for (int i = appliedBlockEntityRegistrations; i < REGISTERED_BLOCK_ENTITY_RENDERERS.size(); i++) {
			REGISTERED_BLOCK_ENTITY_RENDERERS.get(i).apply(registrar);
		}
		appliedBlockEntityRegistrations = REGISTERED_BLOCK_ENTITY_RENDERERS.size();
	}

	private synchronized void registerEntityRendererInternal(RendererRegistration registration) {
		REGISTERED_ENTITY_RENDERERS.add(registration);
		if (activeRegistrar != null) {
			registration.apply(activeRegistrar);
			appliedEntityRegistrations = REGISTERED_ENTITY_RENDERERS.size();
		}
	}

	private synchronized void registerBlockEntityRendererInternal(RendererRegistration registration) {
		REGISTERED_BLOCK_ENTITY_RENDERERS.add(registration);
		if (activeRegistrar != null) {
			registration.apply(activeRegistrar);
			appliedBlockEntityRegistrations = REGISTERED_BLOCK_ENTITY_RENDERERS.size();
		}
	}

	@FunctionalInterface
	private interface RendererRegistration {
		void apply(EntityRendererRegistrar registrar);
	}

	private record EntityRendererEntry<T extends Entity>(
			EntityType<? extends T> entityType,
			EntityRendererProvider<T> rendererProvider
	) implements RendererRegistration {
		@Override
		public void apply(EntityRendererRegistrar registrar) {
			registrar.registerEntityRenderer(entityType, rendererProvider);
		}
	}

	private record BlockEntityRendererEntry<T extends BlockEntity>(
			BlockEntityType<? extends T> blockEntityType,
			BlockEntityRendererProvider<T> rendererProvider
	) implements RendererRegistration {
		@Override
		public void apply(EntityRendererRegistrar registrar) {
			registrar.registerBlockEntityRenderer(blockEntityType, rendererProvider);
		}
	}
}
