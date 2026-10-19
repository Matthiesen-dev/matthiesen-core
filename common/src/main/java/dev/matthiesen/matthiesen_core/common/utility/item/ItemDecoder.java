package dev.matthiesen.matthiesen_core.common.utility.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Utility class for decoding items from strings. If the string cannot be decoded, it will return the provided fallback
 * item and log an error.
 */
@SuppressWarnings("unused")
public final class ItemDecoder {
    public static final String FALLBACK_ITEM_STRING = "minecraft:air";
    public static final Item FALLBACK_ITEM = Items.AIR;

    private static HolderLookup.Provider getRegistries() {
        return MatthiesenCoreCommon.INSTANCE.getCommonUtils().getServer().registryAccess();
    }

    private static ItemStack parseItemSnbt(String input, HolderLookup.Provider registries) throws CommandSyntaxException {
        Tag tag = TagParser.parseTag(input);
        return ItemStack.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
                .getOrThrow(IllegalArgumentException::new);
    }

    private static ItemStack parseItemString(String input, HolderLookup.Provider registries) throws CommandSyntaxException {
        StringReader reader = new StringReader(input);
        ItemParser.ItemResult result = new ItemParser(registries).parse(reader);
        if (reader.canRead()) {
            throw new IllegalArgumentException("Unexpected trailing characters at position " + reader.getCursor());
        }
        return new ItemStack(result.item(), 1, result.components());
    }

    private static Block parseBlockSnbt(String input, HolderLookup.Provider registries) throws CommandSyntaxException {
        Tag tag = TagParser.parseTag(input);
        return Block.CODEC.decoder().parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
                .getOrThrow(IllegalArgumentException::new);
    }

    private static Block parseBlockString(String input, HolderLookup.Provider registries) throws CommandSyntaxException {
        StringReader reader = new StringReader(input);
        ItemParser.ItemResult result = new ItemParser(registries).parse(reader);
        if (reader.canRead()) {
            throw new IllegalArgumentException("Unexpected trailing characters at position " + reader.getCursor());
        }
        ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(result.item().value());
        Block block = BuiltInRegistries.BLOCK.get(blockId);
        if (block == Blocks.AIR) {
            throw new IllegalArgumentException("Decoded item is not a block: " + blockId);
        }
        return block;
    }

    /**
     * Default constructor for the ItemDecoder class. This constructor is used when creating a new instance of the ItemDecoder
     * class. Since all methods in this class are static, there is no need to create an instance of the class to use its methods.
     */
    private ItemDecoder() {}

    /**
     * Serializes an ItemStack to a string. If the ItemStack is empty, it will return the fallback item string.
     *
     * @param stack The ItemStack to serialize.
     * @param fallback The fallback item string to return if the ItemStack is empty.
     * @return The serialized string representation of the ItemStack, or the fallback item string if the ItemStack is empty.
     */
    public static String serialize(ItemStack stack, String fallback) {
        if (stack.isEmpty()) {
            return fallback;
        }
        return new ItemInput(stack.getItemHolder(), stack.getComponentsPatch()).serialize(getRegistries());
    }

    /**
     * Serializes an ItemStack to a string. If the ItemStack is empty, it will return the fallback item string.
     *
     * @param stack The ItemStack to serialize.
     * @return The serialized string representation of the ItemStack, or the fallback item string if the ItemStack is empty.
     */
    public static String serialize(ItemStack stack) {
        return serialize(stack, FALLBACK_ITEM_STRING);
    }

    /**
     * Serializes an Item to a string. If the Item is null, it will return the fallback item string.
     *
     * @param item The Item to serialize.
     * @return The serialized string representation of the Item, or the fallback item string if the Item is null.
     */
    public static String serialize(Item item) {
        return serialize(new ItemStack(item), FALLBACK_ITEM_STRING);
    }

    /**
     * Serializes an Item to a string. If the Item is null, it will return the provided fallback item string.
     *
     * @param item The Item to serialize.
     * @param fallback The fallback item string to return if the Item is null.
     * @return The serialized string representation of the Item, or the fallback item string if the Item is null.
     */
    public static String serialize(Item item, String fallback) {
        return serialize(new ItemStack(item), fallback);
    }

    /**
     * Serializes a Block to a string. If the Block is null, it will return the fallback item string.
     *
     * @param block The Block to serialize.
     * @return The serialized string representation of the Block, or the fallback item string if the Block is null.
     */
    public static String serialize(Block block) {
        return serialize(new ItemStack(block.asItem()), FALLBACK_ITEM_STRING);
    }

    /**
     * Serializes a Block to a string. If the Block is null, it will return the provided fallback item string.
     *
     * @param block The Block to serialize.
     * @param fallback The fallback item string to return if the Block is null.
     * @return The serialized string representation of the Block, or the fallback item string if the Block is null.
     */
    public static String serialize(Block block, String fallback) {
        return serialize(new ItemStack(block.asItem()), fallback);
    }

    /**
     * Deserializes a string to an ItemStack. If the string cannot be deserialized, it will return an empty ItemStack and log an error.
     *
     * @param rawStack The string to deserialize.
     * @param fallback The fallback ItemStack to return if the string cannot be deserialized.
     * @return The deserialized ItemStack, or an empty ItemStack if the string cannot be deserialized.
     */
    public static ItemStack stringToItemStack(String rawStack, ItemStack fallback) {
        if (rawStack == null || rawStack.isBlank()) {
            return fallback;
        }
        String input = rawStack.trim();
        HolderLookup.Provider registries = getRegistries();
        try {
            return input.startsWith("{") ? parseItemSnbt(input, registries) : parseItemString(input, registries);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createWarnLog("Failed to parse item '" + rawStack + "': " + e.getMessage());
            return fallback;
        }
    }

    /**
     * Deserializes a string to an ItemStack. If the string cannot be deserialized, it will return an empty ItemStack and log an error.
     *
     * @param rawStack The string to deserialize.
     * @return The deserialized ItemStack, or an empty ItemStack if the string cannot be deserialized.
     */
    public static ItemStack stringToItemStack(String rawStack) {
        return stringToItemStack(rawStack, ItemStack.EMPTY);
    }

    /**
     * Decodes an item from a string. The string should be in the format "namespace:path". If the string cannot be decoded,
     * it will return the provided fallback item and log an error.
     *
     * @param rawItem The string to decode, in the format "namespace:path".
     * @param fallback The item to return if the string cannot be decoded.
     * @return The decoded item, or the fallback item if the string cannot be decoded.
     */
    public static Item stringToItem(String rawItem, Item fallback) {
        return stringToItemStack(rawItem, new ItemStack(fallback)).getItem();
    }

    /**
     * Decodes an item from a string. The string should be in the format "namespace:path". If the string cannot be decoded,
     * it will return the fallback item (minecraft:air) and log an error.
     *
     * @param rawItem The string to decode, in the format "namespace:path".
     * @return The decoded item, or the fallback item (minecraft:air) if the string cannot be decoded.
     */
    public static Item stringToItem(String rawItem) {
        return stringToItemStack(rawItem, new ItemStack(Items.AIR)).getItem();
    }

    /**
     * Decodes a block from a string. The string should be in the format "namespace:path". If the string cannot be decoded,
     * it will return the provided fallback block and log an error.
     *
     * @param string The string to decode, in the format "namespace:path".
     * @param fallback The block to return if the string cannot be decoded.
     * @return The decoded block, or the fallback block if the string cannot be decoded.
     */
    public static Block stringToBlock(String string, Block fallback) {
        if (string == null || string.isBlank()) {
            return fallback;
        }
        String input = string.trim();
        HolderLookup.Provider registries = getRegistries();
        try {
            return input.startsWith("{") ? parseBlockSnbt(input, registries) : parseBlockString(input, registries);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createWarnLog("Failed to parse block '" + string + "': " + e.getMessage());
            return fallback;
        }
    }

    /**
     * Decodes a block from a string. The string should be in the format "namespace:path". If the string cannot be decoded,
     * it will return the fallback block (minecraft:air) and log an error.
     *
     * @param string The string to decode, in the format "namespace:path".
     * @return The decoded block, or the fallback block (minecraft:air) if the string cannot be decoded.
     */
    public static Block stringToBlock(String string) {
        return stringToBlock(string, Blocks.AIR);
    }
}

