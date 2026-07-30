package dev.matthiesen.matthiesen_core.common.core.text_parsers;

import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import net.minecraft.network.chat.Component;

/**
 * A simple text parser for Vanilla Minecraft text formatting. This parser replaces '&amp;' with '§' to allow for color and formatting codes in text.
 */
public final class VanillaTextParser implements TextParser {

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
