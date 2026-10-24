package dev.matthiesen.matthiesen_core.common.core.text_parsers;

import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import net.minecraft.network.chat.Component;

/**
 * A simple text parser for Vanilla Minecraft text formatting. This parser replaces '&amp;' with '§' to allow for color and formatting codes in text.
 */
public final class VanillaTextParser implements TextParser {
    /**
     * The singleton instance of the VanillaTextParser. This instance is used to parse text using Vanilla Minecraft formatting.
     */
    public static final VanillaTextParser INSTANCE = new VanillaTextParser();

    /**
     * Registers the VanillaTextParser with the MatthiesenCoreCommon text parser manager. This method should be called
     * during the initialization phase of the mod to ensure that the VanillaTextParser is available for use.
     */
    public static void register() {
        MatthiesenCoreCommon.INSTANCE.getTextParserManager().registerTextParser(INSTANCE);
    }

    /**
     * Constructs a new VanillaTextParser instance. This parser does not require any initialization or configuration.
     */
    public VanillaTextParser() {}

    @Override
    public String type() {
        return BuiltInTextParsers.VANILLA.getId();
    }

    @Override
    public Component parse(String text) {
        text = text.replace("&", "§");
        return Component.literal(text);
    }
}
