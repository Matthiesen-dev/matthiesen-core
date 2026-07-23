package dev.matthiesen.matthiesen_core.common.registry;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.CreativeModeAugmentsManager;
import dev.matthiesen.matthiesen_core.common.core.registry.CreativeModeTabSectionsManager;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Convenience base class for registries that register {@link CreativeModeTab} instances.
 *
 * <p>This type locks registration to the creative mode tab registry category by wiring
 * {@link SupportedRegistries#CREATIVE_MODE_TAB} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractCreativeModeTabRegistry extends AbstractRegistry<CreativeModeTab> {
    private String modId;
    /**
     * Creates a creative mode tab registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractCreativeModeTabRegistry(String modId) {
        super(modId, SupportedRegistries.CREATIVE_MODE_TAB);
        this.modId = modId;
    }

    /**
     * Creates a creative mode tab registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform creative mode tab registrations
     */
    protected AbstractCreativeModeTabRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.CREATIVE_MODE_TAB);
    }

    /**
     * Creates a new {@link CreativeModeTab.Builder} instance for use in registrations. This is a convenience method that simply delegates to the underlying registry builder's {@code newCreativeTabBuilder()} method, but it can be overridden by subclasses if they need to customize the builder creation process.
     * @return A new instance of {@link CreativeModeTab.Builder} for use in creative mode tab registrations.
     */
    protected final CreativeModeTab.Builder newCreativeModeTabBuilder() {
        return this.getRegistryBuilder().newCreativeTabBuilder();
    }


    /**
     * Registers multiple item augmentations to a creative mode tab using a registrar callback.
     *
     * @param registrarConsumer callback that receives a registrar helper
     */
    protected final void registerTabAugmentations(Consumer<CreativeModeAugmentsManager.TabAugmentationRegistrar> registrarConsumer) {
        MatthiesenCoreCommon.INSTANCE.getCreativeModeAugmentsManager().registerTabAugmentations(registrarConsumer);
    }

    /**
     * Registers an item to be added to a creative mode tab.
     *
     * @param tab the target creative mode tab
     * @param item the item to add to the tab
     */
    protected final void registerTabAugmentation(ResourceKey<CreativeModeTab> tab, ItemStack item) {
        MatthiesenCoreCommon.INSTANCE.getCreativeModeAugmentsManager().registerTabAugmentation(tab, item);
    }

    /**
     * Registers multiple items to be added to a creative mode tab.
     *
     * @param tab the target creative mode tab
     * @param items the items to add to the tab
     */
    protected final void registerTabAugmentations(ResourceKey<CreativeModeTab> tab, List<ItemStack> items) {
        MatthiesenCoreCommon.INSTANCE.getCreativeModeAugmentsManager().registerTabAugmentations(tab, items);
    }

    /**
     * Registers a simple creative mode tab with the specified name, title, display icon, and display items. This method creates a new
     * {@link CreativeModeTab} instance using the provided parameters and registers it with the registry.
     * @param name The name of the creative mode tab to register. This will be used to construct the resource location for the tab.
     * @param title The title of the creative mode tab, represented as a {@link Component}.
     * @param displayIcon A {@link Supplier} that provides the display icon for the creative mode tab as an {@link ItemStack}.
     * @param displayItems A {@link Supplier} that provides a list of {@link ItemStack} instances to be displayed in the creative mode tab.
     * @return A {@link Supplier} that provides the registered {@link CreativeModeTab} instance.
     */
    protected final Supplier<CreativeModeTab> registerSimpleCreativeTab(String name, Component title, Supplier<ItemStack> displayIcon, Supplier<List<ItemStack>> displayItems) {
        return register(name, () -> newCreativeModeTabBuilder()
                .title(title)
                .icon(displayIcon)
                .displayItems((parameters, output) -> displayItems.get().forEach(output::accept))
                .build()
        );
    }

    /**
     * Registers a simple creative mode tab with the specified name, title, display icon, and display items. This method creates a new
     * {@link CreativeModeTab} instance using the provided parameters and registers it with the registry.
     * @param location The {@link ResourceLocation} representing the name of the creative mode tab to register. The path of the resource location will be used to construct the resource location for the tab.
     * @param title The title of the creative mode tab, represented as a {@link Component}.
     * @param displayIcon A {@link Supplier} that provides the display icon for the creative mode tab as an {@link ItemStack}.
     * @param displayItems A {@link Supplier} that provides a list of {@link ItemStack} instances to be displayed in the creative mode tab.
     * @return A {@link Supplier} that provides the registered {@link CreativeModeTab} instance.
     */
    protected final Supplier<CreativeModeTab> registerSimpleCreativeTab(ResourceLocation location, Component title, Supplier<ItemStack> displayIcon, Supplier<List<ItemStack>> displayItems) {
        return register(location.getPath(), () -> newCreativeModeTabBuilder()
                .title(title)
                .icon(displayIcon)
                .displayItems((parameters, output) -> displayItems.get().forEach(output::accept))
                .build()
        );
    }

    /**
     * Registers a sectioned creative mode tab with the specified name, title, display icon, and a builder consumer for configuring the sections.
     * This method registers the creative mode tab and delegates the section registration to {@link CreativeModeTabSectionsManager}.
     * @param name The name of the creative mode tab to register. This will be used to construct the resource location for the tab.
     * @param title The title of the creative mode tab, represented as a {@link Component}.
     * @param displayIcon A {@link Supplier} that provides the display icon for the creative mode tab as an {@link ItemStack}.
     * @param builderConsumer A {@link Consumer} that configures the section builder for creative mode tab sections.
     * @return A {@link Supplier} that provides the registered {@link CreativeModeTab} instance.
     */
    protected final Supplier<CreativeModeTab> registerSectionedCreativeTab(String name, Component title, Supplier<ItemStack> displayIcon, Consumer<CreativeModeTabSectionsManager.SectionBuilder> builderConsumer) {
        CreativeModeTabSectionsManager.INSTANCE.addAutoRegistration(modId, () -> registerCreativeModeTabSections(name, builderConsumer));
        return register(name, () -> newCreativeModeTabBuilder()
                .title(title)
                .icon(displayIcon)
                .displayItems((parameters, output) -> getCreativeModeTabSectionItems(name).forEach(output::accept))
                .build()
        );
    }

    /**
     * Registers a sectioned creative mode tab with the specified name, title, display icon, and a builder consumer for configuring the sections.
     * This method registers the creative mode tab and delegates the section registration to {@link CreativeModeTabSectionsManager}.
     * @param location The {@link ResourceLocation} representing the name of the creative mode tab to register. The path of the resource location will be used to construct the resource location for the tab.
     * @param title The title of the creative mode tab, represented as a {@link Component}.
     * @param displayIcon A {@link Supplier} that provides the display icon for the creative mode tab as an {@link ItemStack}.
     * @param builderConsumer A {@link Consumer} that configures the section builder for creative mode tab sections.
     * @return A {@link Supplier} that provides the registered {@link CreativeModeTab} instance.
     */
    protected final Supplier<CreativeModeTab> registerSectionedCreativeTab(ResourceLocation location, Component title, Supplier<ItemStack> displayIcon, Consumer<CreativeModeTabSectionsManager.SectionBuilder> builderConsumer) {
        var name = location.getPath();
        CreativeModeTabSectionsManager.INSTANCE.addAutoRegistration(modId, () -> registerCreativeModeTabSections(name, builderConsumer));
        return register(name, () -> newCreativeModeTabBuilder()
                .title(title)
                .icon(displayIcon)
                .displayItems((parameters, output) -> getCreativeModeTabSectionItems(name).forEach(output::accept))
                .build()
        );
    }

    /**
     * Registers creative mode tab sections using the provided {@link Consumer} to configure the section builder. This
     * method delegates to {@link CreativeModeTabSectionsManager#registerCreativeModeTabSections(ResourceLocation, Consumer)} with the mod ID and the provided consumer.
     * @param builderConsumer A {@link Consumer} that configures the section builder for creative mode tab sections.
     */
    protected final void registerCreativeModeTabSections(String creativeModeTabId, Consumer<CreativeModeTabSectionsManager.SectionBuilder> builderConsumer) {
        CreativeModeTabSectionsManager.INSTANCE.registerCreativeModeTabSections(ResourceLocation.fromNamespaceAndPath(modId, creativeModeTabId), builderConsumer);
    }

    /**
     * Retrieves the list of {@link ItemStack} instances associated with the specified creative mode tab ID. This method delegates to {@link CreativeModeTabSectionsManager#getTabSections(ResourceLocation)} to obtain the sections and then flattens the resulting lists of items into a single list.
     * @param creativeModeTabId The ID of the creative mode tab for which to retrieve the associated items.
     * @return A list of {@link ItemStack} instances associated with the specified creative mode tab ID.
     */
    protected final List<ItemStack> getCreativeModeTabSectionItems(String creativeModeTabId) {
        return CreativeModeTabSectionsManager.INSTANCE.getTabSections(ResourceLocation.fromNamespaceAndPath(modId, creativeModeTabId))
                .sections()
                .values().
                stream()
                .flatMap(List::stream)
                .toList();
    }
}

