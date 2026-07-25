package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Manages augmentations (item additions) to creative mode tabs in common code.
 *
 * <p>The manager queues item augmentations and applies them when items are being added to tabs.
 * Multiple items can be registered for a single tab, and registrations can happen before or after
 * tab population, with late registrations being applied on demand.</p>
 */
@SuppressWarnings("unused")
public final class CreativeModeAugmentsManager {
	private static final List<TabAugmentation> REGISTERED_AUGMENTATIONS = new CopyOnWriteArrayList<>();

	/**
	 * Shared singleton instance used to manage creative tab augmentations.
	 */
	public static final CreativeModeAugmentsManager INSTANCE = new CreativeModeAugmentsManager();

	private boolean initialized;

	private CreativeModeAugmentsManager() {}

	/**
	 * Initializes the creative mode augments manager. This method is idempotent.
	 */
	public synchronized void initialize(MatthiesenCoreCommon modInstance) {
		if (initialized) {
			return;
		}
		initialized = true;
		modInstance.createInfoLog("Initialized CreativeModeAugmentsManager");
		modInstance.getCommonRegistry().initializeCreativeModeTabAugmentations();
	}

	/**
	 * Registers multiple item augmentations to a creative mode tab using a registrar callback.
	 *
	 * @param registrarConsumer callback that receives a registrar helper
	 */
	public void registerTabAugmentations(Consumer<TabAugmentationRegistrar> registrarConsumer) {
		registrarConsumer.accept(this::registerTabAugmentation);
	}

	/**
	 * Registers an item to be added to a creative mode tab.
	 *
	 * @param tab the target creative mode tab
	 * @param item the item to add to the tab
	 */
	public void registerTabAugmentation(ResourceKey<CreativeModeTab> tab, ItemStack item) {
		registerTabAugmentation(tab, () -> item);
	}

	/**
	 * Registers an item supplier to be added to a creative mode tab.
	 *
	 * <p>The supplier is evaluated when the tab contents are built, which avoids
	 * early access to unbound deferred registry entries on NeoForge.</p>
	 *
	 * @param tab the target creative mode tab
	 * @param itemSupplier supplier that creates the item stack to add
	 */
	public void registerTabAugmentation(ResourceKey<CreativeModeTab> tab, Supplier<ItemStack> itemSupplier) {
		registerTabAugmentationInternal(new TabAugmentation(tab, itemSupplier));
	}

	/**
	 * Registers an item supplier to be added to a creative mode tab.
	 *
	 * <p>This is a convenience overload for deferred item registrations where
	 * calling {@code get()} too early would fail (e.g., NeoForge DeferredHolder).</p>
	 *
	 * @param tab the target creative mode tab
	 * @param itemSupplier supplier that creates the item to add
	 */
	public void registerTabItemAugmentation(ResourceKey<CreativeModeTab> tab, Supplier<? extends Item> itemSupplier) {
		registerTabAugmentation(tab, () -> new ItemStack(itemSupplier.get()));
	}

	/**
	 * Registers multiple items to be added to a creative mode tab.
	 *
	 * @param tab the target creative mode tab
	 * @param items the items to add to the tab
	 */
	public void registerTabAugmentations(ResourceKey<CreativeModeTab> tab, List<ItemStack> items) {
		for (ItemStack item : items) {
			registerTabAugmentation(tab, item);
		}
	}

	/**
	 * Registers multiple item suppliers to be added to a creative mode tab.
	 *
	 * @param tab the target creative mode tab
	 * @param itemSuppliers suppliers that create items to add to the tab
	 */
	public void registerTabItemAugmentations(ResourceKey<CreativeModeTab> tab, Iterable<? extends Supplier<? extends Item>> itemSuppliers) {
		for (Supplier<? extends Item> itemSupplier : itemSuppliers) {
			registerTabItemAugmentation(tab, itemSupplier);
		}
	}

	/**
	 * Retrieves all augmentations registered for a specific creative mode tab.
	 *
	 * @param tab the creative mode tab key
	 * @return a list of item stacks to be added to the tab
	 */
	public List<ItemStack> getAugmentationsForTab(ResourceKey<CreativeModeTab> tab) {
		List<ItemStack> result = new ArrayList<>();
		for (TabAugmentation augmentation : REGISTERED_AUGMENTATIONS) {
			if (augmentation.tab().equals(tab)) {
				result.add(augmentation.itemSupplier().get().copy());
			}
		}
		return result;
	}

	/**
	 * Checks if any augmentations are registered for a specific creative mode tab.
	 *
	 * @param tab the creative mode tab key
	 * @return true if augmentations exist for this tab, false otherwise
	 */
	public boolean hasAugmentationsForTab(ResourceKey<CreativeModeTab> tab) {
		for (TabAugmentation augmentation : REGISTERED_AUGMENTATIONS) {
			if (augmentation.tab().equals(tab)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Utility interface for registering creative tab item augmentations.
	 */
	@FunctionalInterface
	public interface TabAugmentationRegistrar {
		/**
		 * Registers an item supplier to be added to a creative mode tab.
		 *
		 * @param tab the target creative mode tab
		 * @param itemSupplier supplier that creates the item to add
		 */
		void register(ResourceKey<CreativeModeTab> tab, Supplier<ItemStack> itemSupplier);

		/**
		 * Registers an item supplier to be added to a creative mode tab.
		 *
		 * @param tab the target creative mode tab
		 * @param itemSupplier supplier that creates the item to add
		 */
		default void registerItem(ResourceKey<CreativeModeTab> tab, Supplier<? extends Item> itemSupplier) {
			register(tab, () -> new ItemStack(itemSupplier.get()));
		}

		/**
		 * Registers multiple item suppliers to be added to a creative mode tab.
		 *
		 * @param tab the target creative mode tab
		 * @param itemSuppliers suppliers that create items to add
		 */
		default void registerItems(ResourceKey<CreativeModeTab> tab, Iterable<? extends Supplier<? extends Item>> itemSuppliers) {
			for (Supplier<? extends Item> itemSupplier : itemSuppliers) {
				registerItem(tab, itemSupplier);
			}
		}

		/**
		 * Registers an item to be added to a creative mode tab.
		 *
		 * @param tab the target creative mode tab
		 * @param item the item to add to the tab
		 */
		default void register(ResourceKey<CreativeModeTab> tab, ItemStack item) {
			register(tab, () -> item);
		}
	}

	private synchronized void registerTabAugmentationInternal(TabAugmentation augmentation) {
		REGISTERED_AUGMENTATIONS.add(augmentation);
	}

	/**
	 * Represents a single item augmentation to a creative mode tab.
	 */
	private record TabAugmentation(ResourceKey<CreativeModeTab> tab, Supplier<ItemStack> itemSupplier) {}
}
