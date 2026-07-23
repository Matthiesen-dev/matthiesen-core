package dev.matthiesen.matthiesen_core.common.core.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import dev.matthiesen.matthiesen_core.common.utility.item.ItemBuilder;

/**
 * Utility for creative tab section marker stacks.
 *
 * <p>Markers use a vanilla barrier stack with custom data and custom model data so no custom
 * item registration is required. This keeps the system safe for server-only installations while
 * still allowing client-side model overrides.</p>
 */
public final class CreativeTabSectionHeaderItem {
    public static final int MARKER_MODEL_DATA = 2047001;

    private static final String SECTION_ID_KEY = "SectionID";
    private static final String CREATIVE_MODE_TAB_ID = "CreativeModeTabID";
    private static final String ROLE_KEY = "SectionRole";
    private static final String ROLE_HEADER = "header";
    private static final String ROLE_PLACEHOLDER = "placeholder";

    private CreativeTabSectionHeaderItem() {}

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @param creativeModeTabId the ResourceLocation of the CreativeModeTab this section belongs to
     * @param sectionId the ResourceLocation of the section this header represents
     * @return an ItemStack representing the section header
     */
    public static ItemStack createHeaderStack(ResourceLocation creativeModeTabId, ResourceLocation sectionId) {
        CompoundTag tag = new CompoundTag();
        tag.putString(CREATIVE_MODE_TAB_ID, creativeModeTabId.toString());
        tag.putString(SECTION_ID_KEY, sectionId.toString());
        tag.putString(ROLE_KEY, ROLE_HEADER);

        return new ItemBuilder(Items.BARRIER)
                .setModelData(MARKER_MODEL_DATA)
                .setCustomData(CustomData.of(tag))
                .hideAdditional()
                .setCustomName(Component.empty())
                .build();
    }

    /**
     * Used by the mixin to identify section rows and extract custom section data.
     * @return an ItemStack representing a placeholder for the section header
     */
    public static ItemStack createPlaceholderStack() {
        CompoundTag tag = new CompoundTag();
        tag.putString(ROLE_KEY, ROLE_PLACEHOLDER);

        return new ItemBuilder(Items.BARRIER)
                .setModelData(MARKER_MODEL_DATA)
                .setCustomData(CustomData.of(tag))
                .hideAdditional()
                .setCustomName(Component.empty())
                .build();
    }

    /**
     * Checks whether an ItemStack is a section header marker managed by this utility.
     *
     * @param stack the stack to inspect
     * @return true when the stack is a tagged creative tab section marker
     */
    public static boolean isSectionMarkerStack(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != Items.BARRIER) {
            return false;
        }

        String role = getRole(stack);
        return ROLE_HEADER.equals(role) || ROLE_PLACEHOLDER.equals(role);
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
