package dev.matthiesen.matthiesen_core.common.api.text_parsers;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import net.minecraft.network.chat.Component;

/**
 * Interface for parsing text into Minecraft Components.
 */
@SuppressWarnings("unused")
public interface TextParser {
    /**
     * Returns the type of the TextParser. This can be used to identify different implementations of the TextParser interface.
     * @return A string representing the type of the TextParser.
     */
    String type();

    /**
     * Parses the given text and returns a Component representation of it.
     * @param text The text to be parsed.
     * @return A Component representation of the parsed text.
     */
    Component parse(String text);

    /**
     * Initializes the TextParser. This method is called during the initialization phase of the mod.
     */
    default void initialize() {
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Initializing TextParser: " + type());
    }
}
