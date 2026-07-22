package dev.matthiesen.matthiesen_core.common.core.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * This class is used to create section headers and placeholders for creative mode tabs. It allows for the creation of ItemStacks
 * that represent section headers and placeholders, and provides methods to extract the section ID and creative mode tab ID from these ItemStacks.
 */
public final class CreativeTabSectionHeaderItem extends Item {
    private static final String SECTION_ID_KEY = "SectionID";
    private static final String CREATIVE_MODE_TAB_ID = "CreativeModeTabID";
    private static final String ROLE_KEY = "SectionRole";
    private static final String ROLE_HEADER = "header";
    private static final String ROLE_PLACEHOLDER = "placeholder";

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     */
    public CreativeTabSectionHeaderItem() {
        super(new Properties().stacksTo(1));
    }

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @param item the Item to create a header stack for
     * @param creativeModeTabId the ResourceLocation of the CreativeModeTab this section belongs to
     * @param sectionId the ResourceLocation of the section this header represents
     * @return an ItemStack representing the section header
     */
    public static ItemStack createHeaderStack(Item item, ResourceLocation creativeModeTabId, ResourceLocation sectionId) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = new CompoundTag();
        tag.putString(CREATIVE_MODE_TAB_ID, creativeModeTabId.toString());
        tag.putString(SECTION_ID_KEY, sectionId.toString());
        tag.putString(ROLE_KEY, ROLE_HEADER);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @param item the Item to create a placeholder stack for
     * @return an ItemStack representing a placeholder for the section header
     */
    public static ItemStack createPlaceholderStack(Item item) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = new CompoundTag();
        tag.putString(ROLE_KEY, ROLE_PLACEHOLDER);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @param stack the ItemStack to check
     * @return true if the ItemStack is a placeholder, false otherwise
     */
    public static boolean isPlaceholder(ItemStack stack) {
        return ROLE_PLACEHOLDER.equals(getRole(stack));
    }

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @param stack the ItemStack to extract the section ID from
     * @return the ResourceLocation of the section ID, or null if not present or invalid
     */
    public static ResourceLocation getSectionId(ItemStack stack) {
        String sectionId = getOrDefault(stack, SECTION_ID_KEY);
        if (sectionId.isBlank()) {
            return null;
        }
        try {
            return ResourceLocation.parse(sectionId);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @param stack the ItemStack to extract the CreativeModeTab ID from
     * @return the ResourceLocation of the CreativeModeTab ID, or null if not present or invalid
     */
    public static ResourceLocation getCreativeModeTabId(ItemStack stack) {
        String tabId = getOrDefault(stack, CREATIVE_MODE_TAB_ID);
        if (tabId.isBlank()) {
            return null;
        }
        try {
            return ResourceLocation.parse(tabId);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @param stack the ItemStack to extract the role from
     * @return the role of the ItemStack, or null if not present
     */
    private static String getRole(ItemStack stack) {
        return getOrDefault(stack, ROLE_KEY);
    }

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @param stack the ItemStack to extract the value from
     * @param key the key to look for in the CustomData
     * @return the value associated with the key, or an empty string if not present
     */
    private static String getOrDefault(ItemStack stack, String key) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag()
                .getString(key);
    }
}
