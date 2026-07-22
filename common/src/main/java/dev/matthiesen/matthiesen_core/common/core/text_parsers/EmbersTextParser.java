package dev.matthiesen.matthiesen_core.common.core.text_parsers;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import dev.matthiesen.matthiesen_core.common.core.registry.TextParserRegistryManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.tysontheember.emberstextapi.immersivemessages.api.MarkupParser;
import net.tysontheember.emberstextapi.immersivemessages.api.TextSpan;
import net.tysontheember.emberstextapi.util.StyleUtil;

import java.util.List;

/**
 * EmbersTextParser is a text parser implementation that uses the EmbersTextAPI to parse and format text.
 * It converts text with markup into Minecraft components, applying styles such as bold, italic, and color.
 */
public final class EmbersTextParser implements TextParser {
    @Override
    public String type() {
        return BuiltInTextParsers.EMBERS.getId();
    }

    @Override
    public Component parse(String text) {
        if (MatthiesenCoreCommon.INSTANCE.getCommonUtils().isModLoaded(BuiltInTextParsers.EMBERS.getId())) {
            List<TextSpan> spans = MarkupParser.parse(text);
            MutableComponent result = Component.empty();
            for (TextSpan span : spans) {
                // applyTextSpanFormatting handles bold/italic/effects but intentionally skips color
                Style style = StyleUtil.applyTextSpanFormatting(Style.EMPTY, span);
                if (span.getColor() != null) {
                    style = style.withColor(span.getColor());
                }
                result.append(Component.literal(span.getContent()).withStyle(style));
            }
            return result;
        }
        MatthiesenCoreCommon.INSTANCE.createErrorLog("EmbersTextParserFabric.parse() called but EmbersTextAPI is not loaded. Falling back to vanilla parser.");
        return TextParserRegistryManager.VANILLA_PARSER.parse(text);
    }
}
