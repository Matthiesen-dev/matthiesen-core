package dev.matthiesen.matthiesen_core.common.api.text_parsers;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum representing the built-in text parsers available in Matthiesen Core
 */
public enum BuiltInTextParsers {
    /**
     * The vanilla text parser, which uses Minecraft's built-in formatting codes.
     */
    VANILLA("vanilla"),

    /**
     * Ember's Text API parser, which allows for more advanced text formatting and features beyond what vanilla Minecraft provides.
     * See The <a href="https://tysontheember.dev/embers-text-api/intro/">Ember's Text API Documentation</a> for more information about this text parser
     * and its capabilities.
     */
    EMBERS("emberstextapi"),

    /**
     * The Adventure text parser, which uses the Adventure library for text formatting and features.
     */
    ADVENTURE("adventure", List.of("adventure-platform-fabric", "adventure_platform_neoforge"));

    private final String id;
    private final List<String> aliases;

    /**
     * Constructs a new enum constant for a built-in text parser with the given id.
     * @param id The unique identifier for the text parser.
     */
    BuiltInTextParsers(String id) {
        this.id = id;
        this.aliases = List.of(id);
    }

    /**
     * Constructs a new enum constant for a built-in text parser with the given id and aliases.
     * @param id The unique identifier for the text parser.
     * @param aliases A list of alternative names or identifiers for the text parser.
     */
    BuiltInTextParsers(String id, List<String> aliases) {
        this.id = id;
        this.aliases = aliases;
    }

    /**
     * Gets the selected Text parsers ID
     * @return the Text parsers ID
     */
    public String getId() {
        return id;
    }

    /**
     * Checks if the mod associated with this text parser is loaded.
     * @return true if the mod is loaded, false otherwise.
     */
    public boolean isModLoaded() {
        return aliases.stream().anyMatch(alias -> MatthiesenCoreCommon.INSTANCE.getCommonUtils().isModLoaded(alias));
    }
}
