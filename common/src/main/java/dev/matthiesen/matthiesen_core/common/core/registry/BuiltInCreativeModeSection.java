package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * This class manages the built-in creative mode sections for the Matthiesen Core mod.
 * It registers a built-in creative tab and appends sections directly to
 * {@link CreativeModeTabSectionsManager}.
 */
public final class BuiltInCreativeModeSection {
    /**
     * The unique identifier for the Matthiesen Core creative mode tab.
     * This ID is used to register the creative mode tab within the game.
     */
    public static final ResourceLocation MATTHIESEN_CORE_TAB_ID = MatthiesenCoreCommon.modResource("matthiesen_core_misc");

    public static final int MATTHIESEN_CORE_LOGO_MODEL_ID = 2047002;

    private static final Map<RegistrationKey, List<Supplier<Item>>> ITEM_SUPPLIERS = new HashMap<>();
    private static final AtomicBoolean TAB_REGISTERED = new AtomicBoolean(false);

    /**
     * The singleton instance of the BuiltInCreativeModeSection class.
     * This instance is used to manage the creative mode sections and item registrations.
     */
    public static final BuiltInCreativeModeSection INSTANCE = new BuiltInCreativeModeSection();

    private BuiltInCreativeModeSection() {}

    public static volatile Supplier<CreativeModeTab> MATTHIESEN_CORE_TAB;

    /**
     * Initializes the BuiltInCreativeModeSection instance.
     */
    public void initialize() {}

    public record RegistrationKey(ResourceLocation sectionId, Component title, int priority) {}

    /**
     * Registers an item to a specific creative mode section.
     * @param registrationKey the unique identifier of the creative mode section
     * @param itemSupplier a supplier that provides the item to be registered
     */
    public void registerItemToSection(RegistrationKey registrationKey, Supplier<Item> itemSupplier) {
        ITEM_SUPPLIERS.computeIfAbsent(registrationKey, k -> new ArrayList<>()).add(itemSupplier);
        registerTabIfNeeded();
        syncTabSections();
    }

    /**
     * Registers a list of items to a specific creative mode section.
     * @param registrationKey the unique identifier of the creative mode section
     * @param itemSuppliers a list of suppliers that provide the items to be registered
     */
    public void registerSectionWithItems(RegistrationKey registrationKey, List<Supplier<Item>> itemSuppliers) {
        ITEM_SUPPLIERS.put(registrationKey, new ArrayList<>(itemSuppliers));
        registerTabIfNeeded();
        syncTabSections();
    }

    private void registerTabIfNeeded() {
        if (!TAB_REGISTERED.compareAndSet(false, true)) {
            return;
        }

        MatthiesenCoreCommon.INSTANCE.createInfoLog("Registering built-in creative mode tab: " + MATTHIESEN_CORE_TAB_ID);
        MATTHIESEN_CORE_TAB = MatthiesenCoreCommon.INSTANCE.getCommonRegistry().registerCreativeModeTab(
                MATTHIESEN_CORE_TAB_ID,
                () -> MatthiesenCoreCommon.INSTANCE.getCommonRegistry().newCreativeTabBuilder()
                        .title(Component.literal("Matthiesen Core Misc"))
                        .icon(getCreativeTabIcon())
                        .displayItems((parameters, output) -> getCreativeModeTabSectionItems().forEach(output::accept))
                        .build()
        );
    }

    // Keep the section snapshot in sync if registrations happen after tab contents are first queried.
    private void syncTabSections() {
        CreativeModeTabSectionsManager.INSTANCE.registerCreativeModeTabSections(MATTHIESEN_CORE_TAB_ID, this::populateSections);
    }

    private List<ItemStack> getCreativeModeTabSectionItems() {
        CreativeModeTabSectionsManager.CreativeModeTabSectionRegistration registration =
                CreativeModeTabSectionsManager.INSTANCE.getTabSections(MATTHIESEN_CORE_TAB_ID);
        if (registration == null) {
            return List.of();
        }

        return registration.sections().values().stream().flatMap(List::stream).toList();
    }

    private void populateSections(CreativeModeTabSectionsManager.SectionBuilder sectionBuilder) {
        for (Map.Entry<RegistrationKey, List<Supplier<Item>>> entry : ITEM_SUPPLIERS.entrySet()) {
            RegistrationKey registrationKey = entry.getKey();
            List<Supplier<Item>> suppliers = entry.getValue();

            sectionBuilder.registerSection(
                    registrationKey.sectionId(),
                    registrationKey.title(),
                    registrationKey.priority()
            );

            for (Supplier<Item> supplier : suppliers) {
                Item suppliedItem = Objects.requireNonNull(supplier.get(), "Item supplier for section '" + registrationKey.sectionId() + "' returned null");
                sectionBuilder.addItemToSection(registrationKey.sectionId(), new ItemStack(suppliedItem));
            }
        }
    }

    private static Supplier<ItemStack> getCreativeTabIcon() {
        return () -> new ItemBuilder(Items.BARRIER)
                .hideAdditional()
                .setCustomName(Component.literal("Matthiesen Core Misc"))
                .setModelData(MATTHIESEN_CORE_LOGO_MODEL_ID)
                .build();
    }
}
