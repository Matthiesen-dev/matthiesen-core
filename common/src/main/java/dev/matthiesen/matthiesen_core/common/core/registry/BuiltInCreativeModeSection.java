package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.registry.AbstractCreativeModeTabRegistry;
import dev.matthiesen.matthiesen_core.common.utility.item.ItemBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class manages the built-in creative mode sections for the Matthiesen Core mod. It allows for the registration of
 * items to specific creative mode sections and provides a way to create a custom creative mode tab with these sections.
 * <p>
 *     This Registry is lazy to prevent issues with server-only logic. It is initialized only when needed, and it ensures that the creative mode tab and its sections are set up correctly.
 * </p>
 */
public final class BuiltInCreativeModeSection {
    /**
     * The unique identifier for the Matthiesen Core creative mode tab.
     * This ID is used to register the creative mode tab within the game.
     */
    public static final ResourceLocation MATTHIESEN_CORE_TAB_ID = MatthiesenCoreCommon.modResource("matthiesen_core_tab");

    public static final int MATTHIESEN_CORE_LOGO_MODEL_ID = 2047002;

    private static final Map<ResourceLocation, List<Supplier<Item>>> ITEM_SUPPLIERS = new HashMap<>();

    /**
     * The singleton instance of the BuiltInCreativeModeSection class.
     * This instance is used to manage the creative mode sections and item registrations.
     */
    public static final BuiltInCreativeModeSection INSTANCE = new BuiltInCreativeModeSection();

    private BuiltInCreativeModeSection() {}

    private volatile MatthiesenCoreCreativeTabsRegistry creativeTabsRegistry;

    /**
     * Initializes the BuiltInCreativeModeSection instance.
     */
    public void initialize() {}

    private void setupRegistry() {
        if (creativeTabsRegistry == null) {
            synchronized (this) {
                if (creativeTabsRegistry == null) {
                    creativeTabsRegistry = new MatthiesenCoreCreativeTabsRegistry();
                }
            }
        }
    }

    /**
     * Registers an item to a specific creative mode section.
     * @param sectionId the unique identifier of the creative mode section
     * @param itemSupplier a supplier that provides the item to be registered
     */
    public void registerItemToSection(ResourceLocation sectionId, Supplier<Item> itemSupplier) {
        setupRegistry();
        ITEM_SUPPLIERS.computeIfAbsent(sectionId, k -> new ArrayList<>()).add(itemSupplier);
    }

    /**
     * Registers a list of items to a specific creative mode section.
     * @param sectionId the unique identifier of the creative mode section
     * @param itemSuppliers a list of suppliers that provide the items to be registered
     */
    public void registerSectionWithItems(ResourceLocation sectionId, List<Supplier<Item>> itemSuppliers) {
        setupRegistry();
        ITEM_SUPPLIERS.put(sectionId, itemSuppliers);
    }

    /**
     * Internal class that extends AbstractCreativeModeTabRegistry to manage the creative mode tab and its sections.
     */
    public static class MatthiesenCoreCreativeTabsRegistry extends AbstractCreativeModeTabRegistry {

        /**
         * The supplier for the Matthiesen Core creative mode tab. This supplier is used to create the tab and its sections.
         */
        public Supplier<CreativeModeTab> MATTHIESEN_CORE_TAB;

        protected MatthiesenCoreCreativeTabsRegistry() {
            super(MatthiesenCoreCommon.INSTANCE.getRegistryBuilder());

            MATTHIESEN_CORE_TAB = registerSectionedCreativeTab(
                    MATTHIESEN_CORE_TAB_ID,
                    Component.literal("Matthiesen Core Misc"),
                    getCreativeTabIcon(),
                    sectionBuilder -> {
                        for (Map.Entry<ResourceLocation, List<Supplier<Item>>> entry : ITEM_SUPPLIERS.entrySet()) {
                            ResourceLocation sectionId = entry.getKey();
                            List<Supplier<Item>> suppliers = entry.getValue();

                            sectionBuilder.registerSection(
                                    sectionId,
                                    Component.literal(sectionId.getPath()),
                                    100
                            );

                            for (Supplier<Item> supplier : suppliers) {
                                sectionBuilder.addItemToSection(sectionId, new ItemStack(supplier.get()));
                            }
                        }
                    }
            );
        }

        private static Supplier<ItemStack> getCreativeTabIcon() {
            return () -> new ItemBuilder(Items.BARRIER)
                    .hideAdditional()
                    .setCustomName(Component.literal("Matthiesen Core Misc"))
                    .setModelData(MATTHIESEN_CORE_LOGO_MODEL_ID)
                    .build();
        }
    }
}
