package dev.matthiesen.matthiesen_core.common.utility.item;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Utility class for decoding items from strings. If the string cannot be decoded, it will return the provided fallback
 * item and log an error.
 */
@SuppressWarnings("unused")
public final class ItemDecoder {

    /**
     * Default constructor for the ItemDecoder class. This constructor is used when creating a new instance of the ItemDecoder
     * class. Since all methods in this class are static, there is no need to create an instance of the class to use its methods.
     */
    private ItemDecoder() {}

    /**
     * Decodes an item from a string. The string should be in the format "namespace:path". If the string cannot be decoded,
     * it will return the provided fallback item and log an error.
     *
     * @param string The string to decode, in the format "namespace:path".
     * @param fallback The item to return if the string cannot be decoded.
     * @return The decoded item, or the fallback item if the string cannot be decoded.
     */
    public static Item stringToItem(String string, Item fallback) {
        ResourceLocation rsLoc = ResourceLocation.parse(string);
        Item item = BuiltInRegistries.ITEM.get(rsLoc);
        if (item == Items.AIR) {
            var fallbackKey = BuiltInRegistries.ITEM.getKey(fallback);
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to decode item from string: " + string + ". Defaulting to fallback item: " + fallbackKey);
            return fallback;
        }
        return item;
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
        ResourceLocation rsLoc = ResourceLocation.parse(string);
        Block block = BuiltInRegistries.BLOCK.get(rsLoc);
        if (block == Blocks.AIR) {
            var fallbackKey = BuiltInRegistries.BLOCK.getKey(fallback);
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to decode block from string: " + string + ". Defaulting to fallback block: " + fallbackKey);
            return fallback;
        }
        return block;
    }
}

