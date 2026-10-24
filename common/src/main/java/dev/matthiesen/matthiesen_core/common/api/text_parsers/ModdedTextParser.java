package dev.matthiesen.matthiesen_core.common.api.text_parsers;

import dev.matthiesen.matthiesen_core.common.core.text_parsers.VanillaTextParser;
import net.minecraft.network.chat.Component;

/**
 * Interface for parsing text into Minecraft Components.
 */
public interface ModdedTextParser extends TextParser {
    /**
     * Returns the fallback TextParser to be used if the modded parser is not available or fails to parse the text.
     * @return The fallback TextParser instance.
     */
    default Component getFallbackParser(String text) {
        return VanillaTextParser.INSTANCE.parse(text);
    }
}
