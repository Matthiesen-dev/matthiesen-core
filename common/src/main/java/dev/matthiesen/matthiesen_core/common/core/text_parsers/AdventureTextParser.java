package dev.matthiesen.matthiesen_core.common.core.text_parsers;

import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.registry.TextParserRegistryManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;

/**
 * AdventureTextParser is a text parser implementation that uses the MiniMessage library to parse and format text. It
 * converts text with MiniMessage markup into Minecraft components, allowing for rich text formatting and styling.
 */
public final class AdventureTextParser implements TextParser {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Registers the AdventureTextParser with the MatthiesenCoreCommon text parser manager. This method should be called
     * during the initialization phase of the mod to ensure that the AdventureTextParser is available for use.
     */
    public static void register() {
        MatthiesenCoreCommon.INSTANCE.getTextParserManager().registerTextParser(new AdventureTextParser());
    }

    /**
     * Constructs a new AdventureTextParser instance. This parser does not require any initialization or configuration.
     */
    public AdventureTextParser() {}

    @Override
    public String type() {
        return BuiltInTextParsers.ADVENTURE.getId();
    }

    @Override
    public Component parse(String text) {
        try {
            var adventureComponent = miniMessage.deserialize(text);
            var json = JSONComponentSerializer.json().serialize(adventureComponent);
            return Component.Serializer.fromJson(json, RegistryAccess.EMPTY);
        } catch (Exception e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("AdventureTextParser.parse() failed to parse text: " + text + ". Falling back to vanilla parser.", e);
            return TextParserRegistryManager.VANILLA_PARSER.parse(text);
        }
    }
}
