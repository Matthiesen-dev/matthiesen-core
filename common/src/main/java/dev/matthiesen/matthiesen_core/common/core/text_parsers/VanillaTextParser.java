package dev.matthiesen.matthiesen_core.common.core.text_parsers;

import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import net.minecraft.network.chat.Component;

/**
 * A simple text parser that replaces '&' with '§' and returns a literal component.
 */
public final class VanillaTextParser implements TextParser {
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
