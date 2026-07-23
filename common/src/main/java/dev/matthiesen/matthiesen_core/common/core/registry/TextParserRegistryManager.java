package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import dev.matthiesen.matthiesen_core.common.core.text_parsers.EmbersTextParser;
import dev.matthiesen.matthiesen_core.common.core.text_parsers.VanillaTextParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The TextParserRegistryManager is responsible for managing the registration and retrieval of text parsers within the mod.
 * It maintains a registry of text parsers, allowing for the addition of new parsers and the retrieval of existing ones based
 * on their type. The manager ensures that each parser is uniquely identified by its type and provides a default Vanilla text parser as a fallback option.
 */
@SuppressWarnings("unused")
public final class TextParserRegistryManager {
    /**
     * The singleton instance of the TextParserRegistryManager. This instance is used to manage the registration and retrieval of text parsers.
     */
    public static final TextParserRegistryManager INSTANCE = new TextParserRegistryManager();

    /**
     * The default Vanilla text parser that is always registered and used as a fallback if no other parser is found.
     */
    public static final TextParser VANILLA_PARSER = new VanillaTextParser();

    /**
     * The Embers text parser, which is conditionally registered based on the presence of the Embers mod. This parser is
     * used for advanced text formatting and features provided by the Embers mod. If the Embers mod is not loaded, this parser
     * will not be registered and will remain null. The presence of this parser can be checked using the isTextParserRegistered
     * method with the BuiltInTextParsers.EMBERS type.
     */
    public static volatile TextParser EMBERS_PARSER;

    private static final Map<String, TextParser> REGISTERED_PARSERS = new ConcurrentHashMap<>();

    private boolean initialized;
    private MatthiesenCoreCommon modInstance;

    /**
     * Initializes the Text Parser Registry Manager. This method should be called once during the mod's initialization phase.
     */
    public void initialize(MatthiesenCoreCommon instance) {
        if (initialized) return;
        initialized = true;
        modInstance = instance;
        modInstance.createInfoLog("Initializing Text Parser Registry Manager");
        registerTextParser(VANILLA_PARSER);

        if (modInstance.getCommonUtils().isModLoaded(BuiltInTextParsers.EMBERS.getId())) {
            EMBERS_PARSER = new EmbersTextParser();
            registerTextParser(EMBERS_PARSER);
        }
    }

    /**
     * Registers a new text parser with the registry.
     * @param parser The text parser to register.
     */
    public void registerTextParser(TextParser parser) {
        String name = parser.type();
        if (isTextParserRegistered(name)) return;
        parser.initialize();
        REGISTERED_PARSERS.put(name, parser);
        modInstance.createInfoLog("Registered text parser: " + name);
    }

    /**
     * Retrieves a registered text parser by its type.
     * @param type The type of the text parser to retrieve.
     * @return The registered text parser, or the Vanilla text parser if none is found.
     */
    public TextParser getTextParser(String type) {
        TextParser parser = REGISTERED_PARSERS.get(type);
        if (parser == null) {
            modInstance.createErrorLog("Text parser with name '" + type + "' is not registered. Falling back to Vanilla text parser.");
            parser = VANILLA_PARSER;
        }
        return parser;
    }

    /**
     * Retrieves a registered text parser by its built-in type.
     * @param type The built-in type of the text parser to retrieve.
     * @return The registered text parser, or the Vanilla text parser if none is found.
     */
    public TextParser getTextParser(BuiltInTextParsers type) {
        return getTextParser(type.getId());
    }

    /**
     * Checks if a text parser is registered with the given type.
     * @param type The type of the text parser to check.
     * @return true if a text parser with the given type is registered, false otherwise.
     */
    public boolean isTextParserRegistered(String type) {
        return REGISTERED_PARSERS.containsKey(type);
    }

    /**
     * Checks if a text parser is registered with the given built-in type.
     * @param type The built-in type of the text parser to check.
     * @return true if a text parser with the given built-in type is registered, false otherwise.
     */
    public boolean isTextParserRegistered(BuiltInTextParsers type) {
        return isTextParserRegistered(type.getId());
    }
}
