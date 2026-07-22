package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.item.CreativeTabSectionHeaderItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class manages the registration and retrieval of creative mode tab sections for a mod.
 * It allows mods to define sections within their creative mode tabs, including section titles, priorities, and associated metadata such as colors.
 * The sections can be populated with items, and the manager provides methods to retrieve the registered sections and their metadata for specific creative mode tabs.
 * <p>
 * Usage:
 * <pre>
 * // Register creative mode tab sections for a specific creative mode tab
 * MatthiesenLibCreativeModeTabSectionsManager.registerCreativeModeTabSections(new ResourceLocation("modid", "creative_tab"), builder -> {
 *     builder.registerSection(new ResourceLocation("modid", "section1"), Component.literal("Section 1"), 100);
 *     builder.registerSection(new ResourceLocation("modid", "section2"), Component.literal("Section 2"), 50, meta -> {
 *         meta.setSectionTitleColor(0xFF0000);
 *         meta.setSectionBackgroundColor(0x000000);
 *         meta.setSectionBackgroundImage(ResourceLocation.fromNamespaceAndPath("modid", "textures/gui/creative_tabs/section2_header.png"));
 *         meta.setSectionTitleShadow(false);
 *     });
 *     builder.addItemToSection(new ResourceLocation("modid", "section1"), new ItemStack(Items.DIAMOND));
 *     builder.addItemToSection(new ResourceLocation("modid", "section2"), new ItemStack(Items.GOLD_INGOT));
 * });
 * </pre>
 */
public final class CreativeModeTabSectionsManager {
    private static final Map<String, Runnable> autoRegistrations = new HashMap<>();
    private static final Map<ResourceLocation, CreativeModeTabSectionRegistration> MOD_TAB_SECTIONS = new HashMap<>();

    private static boolean autoRegistrationStarted = false;

    /**
     * Private constructor to prevent instantiation. This class is intended to be used as a static utility class for managing creative mode tab sections.
     */
    private CreativeModeTabSectionsManager() {}

    /**
     * Initializes the creative mode tab sections manager by invoking the initialization of the internal registry.
     */
    public static void initialize() {}

    /**
     * Registers an auto-registration task for a specific mod ID. The provided task will be executed during the creative mode tab sections registration process.
     * @param modId The mod ID for which the auto-registration task is being registered.
     * @param task The Runnable task to be executed during the creative mode tab sections registration process.
     */
    public static void addAutoRegistration(String modId, Runnable task) {
        if (!autoRegistrationStarted) {
            autoRegistrations.put(modId, task);
            return;
        }
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Running Creative-mode tab sections registration for " + modId);
        task.run();
    }

    /**
     * Retrieves the map of auto-registration tasks for creative mode tab sections.
     * @return A map where the keys are mod IDs and the values are Runnable tasks to be executed during the creative mode tab sections registration process.
     */
    public static Map<String, Runnable> getAutoRegistrations() {
        return autoRegistrations;
    }

    /**
     * Executes all registered auto-registration tasks for creative mode tab sections. This method iterates through the registered tasks and runs each one, allowing mods to register their creative mode tab sections automatically.
     */
    public static void runAutoRegistrations() {
        var tasks = CreativeModeTabSectionsManager.getAutoRegistrations();
        if (!tasks.isEmpty()) {
            for (var entry : tasks.entrySet()) {
                String modId = entry.getKey();
                Runnable task = entry.getValue();
                MatthiesenCoreCommon.INSTANCE.createInfoLog("Running Creative-mode tab sections registration for " + modId);
                task.run();
            }
        }
        autoRegistrationStarted = true;
    }

    /**
     * The Creative Tab Section Header Item
     */
    public static final Supplier<CreativeTabSectionHeaderItem> CREATIVE_TAB_SECTION_HEADER_ITEM;

    static {
        CREATIVE_TAB_SECTION_HEADER_ITEM = registerItem("section_header", CreativeTabSectionHeaderItem::new);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return MatthiesenCoreCommon.INSTANCE.getInternalRegistryBuilder().registerItem(name, item);
    }

    /**
     * Registers creative mode tab sections for a specific creative mode tab.
     * @param creativeTabResource The ResourceLocation identifier of the creative mode tab for which sections are being registered.
     * @param builderConsumer A Consumer that accepts a SectionBuilder to define the sections and their metadata for the specified creative mode tab.
     */
    public static void registerCreativeModeTabSections(ResourceLocation creativeTabResource, Consumer<SectionBuilder> builderConsumer) {
        SectionBuilder builder = new SectionBuilder();
        builderConsumer.accept(builder);
        MOD_TAB_SECTIONS.put(creativeTabResource, new CreativeModeTabSectionRegistration(builder.getSections(), builder.getMetadata()));
    }

    /**
     * Retrieves the registered sections for a specific creative mode tab.
     * @param creativeModeTabID The ResourceLocation identifier of the creative mode tab.
     * @return A CreativeModeTabSectionRegistration containing the sections and metadata for the specified creative mode tab, or null if no sections are registered.
     */
    public static CreativeModeTabSectionRegistration getTabSections(ResourceLocation creativeModeTabID) {
        return MOD_TAB_SECTIONS.get(creativeModeTabID);
    }

    /**
     * Checks if a creative mode tab has registered sections.
     * @param creativeModeTabId The ResourceLocation identifier of the creative mode tab.
     * @return true if the creative mode tab has registered sections, false otherwise.
     */
    public static boolean hasTabSections(ResourceLocation creativeModeTabId) {
        return MOD_TAB_SECTIONS.containsKey(creativeModeTabId);
    }

    /**
     * Retrieves the metadata for a specific section within a creative mode tab.
     * @param creativeModeTabId The ResourceLocation identifier of the creative mode tab.
     * @param sectionId The ResourceLocation identifier of the section within the creative mode tab.
     * @return The SectionData containing the metadata for the specified section, or null if the section does not exist.
     */
    public static SectionData getTabMetaData(ResourceLocation creativeModeTabId, ResourceLocation sectionId) {
        CreativeModeTabSectionRegistration registration = MOD_TAB_SECTIONS.get(creativeModeTabId);
        if (registration != null) {
            return registration.metadata().get(sectionId);
        }
        return null;
    }

    /**
     * Represents the registration of creative mode tab sections, including the sections and their associated metadata.
     * This record holds a map of section IDs to their corresponding lists of ItemStacks, as well as a map of section IDs to their corresponding SectionData metadata.
     * It is used to store and retrieve the sections and metadata for a specific creative mode tab.
     * @param sections the map of section IDs to their corresponding lists of ItemStacks
     * @param metadata the map of section IDs to their corresponding SectionData metadata
     */
    public record CreativeModeTabSectionRegistration(Map<ResourceLocation, List<ItemStack>> sections, Map<ResourceLocation, SectionData> metadata) {}

    /**
     * Represents metadata for a creative mode tab section, including colors for the section title, accent, and background.
     * This class provides methods to set and retrieve these color values.
     * @param title the title of the section
     * @param priority the priority of the section (higher values indicate higher priority, i.e. closer to the top)
     * @param meta the metadata for the section, including colors for the title, accent, and background
     */
    public record SectionData(Component title, int priority, SectionDataMeta meta) {}

    /**
     * Builder class for constructing creative mode tab sections with associated metadata and items.
     * This builder allows for the registration of sections with titles, priorities, and customization of section metadata (e.g., colors).
     * It provides methods to register sections and add items to specific sections, as well as methods to retrieve the constructed sections and metadata.
     * The builder is intended to be used in conjunction with the registerCreativeModeTabSections method to define the structure and content of creative mode
     * tabs in a modular and organized manner.
     */
    public static class SectionBuilder {
        private final Map<ResourceLocation, List<ItemStack>> sections = new HashMap<>();
        private final Map<ResourceLocation, SectionData> metadata = new HashMap<>();

        /**
         * Constructs a new SectionBuilder instance for building creative mode tab sections.
         * This builder allows for the registration of sections with associated metadata and items.
         */
        public SectionBuilder() {}

        /**
         * Registers a section with the specified ID, title, and priority.
         * @param id the section identifier
         * @param title the section title as a Component
         * @param priority the priority of the section (higher values indicate higher priority, i.e. closer to the top)
         */
        public void registerSection(ResourceLocation id, Component title, int priority) {
            metadata.put(id, new SectionData(title, priority, SectionDataMeta.defaults()));
        }

        /**
         * Registers a section with the specified ID, title, priority, and metadata consumer.
         * @param id the section identifier
         * @param title the section title as a Component
         * @param priority the priority of the section (higher values indicate higher priority, i.e. closer to the top)
         * @param metaConsumer a Consumer that allows customization of the SectionDataMeta for this section (e.g., setting colors, title shadow, or background image)
         */
        public void registerSection(ResourceLocation id, Component title, int priority, Consumer<SectionDataMeta> metaConsumer) {
            SectionDataMeta meta = SectionDataMeta.defaults();
            metaConsumer.accept(meta);
            metadata.put(id, new SectionData(title, priority, meta));
        }

        /**
         * Adds an item to a specific section in the creative mode tab.
         * @param sectionId The ResourceLocation identifier of the section to which the item should be added.
         * @param item The ItemStack representing the item to be added to the section.
         */
        public void addItemToSection(ResourceLocation sectionId, ItemStack item) {
            sections.computeIfAbsent(sectionId, k -> new java.util.ArrayList<>()).add(item);
        }

        /**
         * Returns the sections registered in this builder.
         * @return A map of section IDs to their corresponding lists of ItemStacks.
         */
        public Map<ResourceLocation, List<ItemStack>> getSections() {
            return sections;
        }

        /**
         * Returns the metadata for the sections registered in this builder.
         * @return A map of section IDs to their corresponding SectionData metadata.
         */
        public Map<ResourceLocation, SectionData> getMetadata() {
            return metadata;
        }
    }

    /**
     * Represents metadata for a creative mode tab section, including colors for the section title, accent, and background.
     * This class provides methods to set and retrieve these color values.
     * It also provides a static method to create a SectionDataMeta instance with default color values.
     */
    public static class SectionDataMeta {
        private int sectionTitleColor;
        private int sectionTitleAccentColor;
        private int sectionBackgroundColor;
        private ResourceLocation sectionBackgroundImage;
        private boolean sectionTitleShadow;

        /**
         * Constructs a SectionDataMeta instance with the specified colors for the section title, accent, and background.
         * @param sectionTitleColor the color for the section title
         * @param sectionTitleAccentColor the accent color for the section title
         * @param sectionBackgroundColor the background color for the section
         */
        public SectionDataMeta(int sectionTitleColor, int sectionTitleAccentColor, int sectionBackgroundColor) {
            this(sectionTitleColor, sectionTitleAccentColor, sectionBackgroundColor, null, true);
        }

        /**
         * Constructs a SectionDataMeta instance with all available rendering metadata.
         * @param sectionTitleColor the color for the section title
         * @param sectionTitleAccentColor the accent color for the section title
         * @param sectionBackgroundColor the background color for the section
         * @param sectionBackgroundImage optional texture location used as the section background image
         * @param sectionTitleShadow whether title text should be rendered with a shadow
         */
        public SectionDataMeta(int sectionTitleColor, int sectionTitleAccentColor, int sectionBackgroundColor, ResourceLocation sectionBackgroundImage, boolean sectionTitleShadow) {
            this.sectionTitleColor = sectionTitleColor;
            this.sectionTitleAccentColor = sectionTitleAccentColor;
            this.sectionBackgroundColor = sectionBackgroundColor;
            this.sectionBackgroundImage = sectionBackgroundImage;
            this.sectionTitleShadow = sectionTitleShadow;
        }

        /**
         * Sets the color for the section title.
         * @param value The color value to set for the section title.
         * @return The current SectionDataMeta instance for method chaining.
         */
        public SectionDataMeta setSectionTitleColor(int value) {
            this.sectionTitleColor = value;
            return this;
        }

        /**
         * Sets the accent color for the section title.
         * @param value The accent color value to set for the section title.
         * @return The current SectionDataMeta instance for method chaining.
         */
        public SectionDataMeta setSectionTitleAccentColor(int value) {
            this.sectionTitleAccentColor = value;
            return this;
        }

        /**
         * Sets the background color for the section.
         * @param value The background color value to set for the section.
         * @return The current SectionDataMeta instance for method chaining.
         */
        public SectionDataMeta setSectionBackgroundColor(int value) {
            this.sectionBackgroundColor = value;
            return this;
        }

        /**
         * Sets the background image for the section header.
         *
         * <p>This must be a texture ResourceLocation (for example: {@code modid:textures/gui/creative_tabs/my_section_header.png}).
         * The recommended image size is {@code 160x16} to match the section header area. Passing {@code null} clears the
         * custom image and falls back to the color-based section header rendering.</p>
         *
         * @param value The texture ResourceLocation to use as the section header background image, or {@code null} to clear it.
         * @return The current SectionDataMeta instance for method chaining.
         */
        public SectionDataMeta setSectionBackgroundImage(ResourceLocation value) {
            this.sectionBackgroundImage = value;
            return this;
        }

        /**
         * Configures whether the section title text should be rendered with a shadow.
         *
         * <p>This setting applies to both color-based and image-based section headers. Defaults to {@code true}.</p>
         *
         * @param value {@code true} to render a text shadow, {@code false} to disable it.
         * @return The current SectionDataMeta instance for method chaining.
         */
        public SectionDataMeta setSectionTitleShadow(boolean value) {
            this.sectionTitleShadow = value;
            return this;
        }

        /**
         * Returns the color for the section title.
         * @return The color for the section title.
         */
        public int getSectionTitleColor() {
            return sectionTitleColor;
        }

        /**
         * Returns the accent color for the section title.
         * @return The accent color for the section title.
         */
        public int getSectionTitleAccentColor() {
            return sectionTitleAccentColor;
        }

        /**
         * Returns the background color for the section.
         * @return The background color for the section.
         */
        public int getSectionBackgroundColor() {
            return sectionBackgroundColor;
        }

        /**
         * Returns the optional section header background image location.
         *
         * <p>If this is {@code null}, the section header uses the configured color-based fallback rendering.</p>
         *
         * @return the section background texture ResourceLocation, or {@code null} when not configured.
         */
        public ResourceLocation getSectionBackgroundImage() {
            return sectionBackgroundImage;
        }

        /**
         * Returns whether section title text should be rendered with a shadow.
         * @return {@code true} if title shadow is enabled, otherwise {@code false}.
         */
        public boolean getSectionTitleShadow() {
            return sectionTitleShadow;
        }

        /**
         * Returns a SectionDataMeta instance with default values for title color, accent color, and background color.
         * @return A SectionDataMeta instance with default values.
         */
        public static SectionDataMeta defaults() {
            return new SectionDataMeta(0xFFFFFF, 0xFF5050B8, 0xCC1A1A1A, null, true);
        }
    }

}
