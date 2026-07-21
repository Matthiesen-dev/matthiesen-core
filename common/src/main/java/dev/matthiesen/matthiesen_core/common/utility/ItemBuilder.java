package dev.matthiesen.matthiesen_core.common.utility;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemLore;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for building ItemStacks with a fluent API. This is used to avoid having to duplicate code for creating ItemStacks across platforms.
 * It provides methods for adding lore, setting custom data, hiding additional tooltip information, setting a custom name,
 * and setting custom model data. After setting the desired properties on the ItemBuilder, the build() method should be called to get
 * the final ItemStack. After calling build(), the ItemBuilder should not be used anymore, as it may cause unexpected behavior.
 */
@SuppressWarnings("unused")
public class ItemBuilder {
    private final ItemStack stack;

    /**
     * Creates a new ItemBuilder with the specified item. This will create a new ItemStack with the given item, and it will be
     * used as the base for building the final ItemStack.
     * @param item The item to create the ItemStack with. This is used to specify the type of item that the final ItemStack
     *             will be, and it will be the base for all other properties that are set on the ItemBuilder.
     */
    public ItemBuilder(Item item) {
        this.stack = new ItemStack(item);
    }

    /**
     * Creates a new ItemBuilder with the specified ItemStack. This will use the given ItemStack as the base for building the
     * final ItemStack, and it will allow for modifying an existing ItemStack with the provided methods.
     * @param item The ItemStack to use as the base for building the final ItemStack. This is used to specify an existing ItemStack
     *             that will be modified with the provided methods, and it will be the base for all other properties that are set on the ItemBuilder.
     */
    public ItemBuilder(ItemStack item) {
        this.stack = item;
    }

    /**
     * Adds lore to the ItemStack. This will append the new lore to the existing lore, if there is any. It will also set the italic style of the lore to false,
     * as Minecraft defaults to italic for lore and this is usually not desired.
     *
     * @param newLore The new lore to add to the ItemStack
     * @return The ItemBuilder instance, for chaining
     */
    public ItemBuilder addLore(Component[] newLore) {
        ItemLore itemLore = stack.get(DataComponents.LORE);
        if (itemLore == null) {
            itemLore = new ItemLore(List.of());
        }

        List<Component> list = Stream.concat(itemLore.lines().stream(), Arrays.stream(newLore))
                .collect(Collectors.toList());

        // Go through every Component, and get the current style and set Italic to false
        list = list.stream()
                .map(component ->
                        component.copy().withStyle(component.getStyle().withItalic(false))
                )
                .collect(Collectors.toList());

        itemLore = new ItemLore(list);
        stack.set(DataComponents.LORE, itemLore);
        return this;
    }

    /**
     * Sets the custom data of the ItemStack. This will append the new custom data to the existing custom data, if
     * there is any. It will also set the max stack size to 1, as items with custom data usually should not be stackable.
     *
     * @param data The custom data to set on the ItemStack
     * @return The ItemBuilder instance, for chaining
     */
    public ItemBuilder setCustomData(CustomData data) {
        // If the stack has custom data, get it and append the new data to it,
        // otherwise create a new custom data with the new data
        CustomData customData = stack.has(DataComponents.CUSTOM_DATA)
                ? stack.get(DataComponents.CUSTOM_DATA)
                : CustomData.of(new CompoundTag());

        assert customData != null;
        CompoundTag newTag = customData.copyTag();
        CompoundTag tag = data.copyTag();

        tag.getAllKeys().forEach(key -> newTag.put(key, tag.get(key)));

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag));
        stack.set(DataComponents.MAX_STACK_SIZE, 1);
        return this;
    }

    /**
     * Hides the additional tooltip information of the ItemStack, such as enchantments, attributes, etc. This is useful
     * for items that have custom data or lore, and you don't want the default Minecraft tooltip to be shown.
     *
     * @return The ItemBuilder instance, for chaining
     */
    public ItemBuilder hideAdditional() {
        stack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        return this;
    }

    /**
     * Sets the custom name of the ItemStack. This will override the default name of the item, and can be
     * used to give the item a unique name. It will also set the italic style of the name to false,
     *
     * @param customName The custom name to set on the ItemStack
     * @return The ItemBuilder instance, for chaining
     */
    public ItemBuilder setCustomName(Component customName) {
        stack.set(DataComponents.CUSTOM_NAME, customName.copy().withStyle(customName.getStyle().withItalic(false)));
        return this;
    }

    /**
     * Sets the custom model data of the ItemStack. This is used to specify a custom model for the item, which can
     * be defined in a resource pack.
     *
     * @param value The custom model data value to set on the ItemStack. This is an integer that can be used in a resource pack to specify a custom model for the item.
     * @return The ItemBuilder instance, for chaining
     */
    public ItemBuilder setModelData(int value) {
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(value));
        return this;
    }

    /**
     * Modifies the ItemStack with a custom modifier function. This allows for any additional modifications to the ItemStack that are not covered by the provided methods.
     * @param modifier A UnaryOperator that takes the current ItemStack and returns a modified ItemStack. This function will be applied to the current ItemStack, allowing for any custom modifications to be made.
     * @return The ItemBuilder instance, for chaining. After calling this method, the provided modifier function will have been applied to the current ItemStack, allowing for any custom modifications to be made before building the final ItemStack with the build() method.
     */
    public ItemBuilder modifyStack(UnaryOperator<ItemStack> modifier) {
        modifier.apply(this.stack);
        return this;
    }

    /**
     * Builds the ItemStack with the specified properties. This should be called after all desired properties have been set on the ItemBuilder.
     *
     * @return The built ItemStack with the specified properties. After calling this method, the ItemBuilder should not
     * be used anymore, as it may cause unexpected behavior.
     */
    public ItemStack build() {
        return this.stack;
    }
}

