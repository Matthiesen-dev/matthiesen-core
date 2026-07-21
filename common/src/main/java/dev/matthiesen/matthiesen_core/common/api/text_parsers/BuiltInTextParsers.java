package dev.matthiesen.matthiesen_core.common.api.text_parsers;

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
    EMBERS("emberstextapi");

    private final String id;

    /**
     * Constructs a new enum constant for a built-in text parser with the given id.,
     */
    BuiltInTextParsers(String id) {
        this.id = id;
    }

    /**
     * Gets the selected Text parsers ID
     * @return the Text parsers ID
     */
    public String getId() {
        return id;
    }
}
