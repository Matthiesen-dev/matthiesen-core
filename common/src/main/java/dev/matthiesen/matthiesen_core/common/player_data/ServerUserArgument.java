package dev.matthiesen.matthiesen_core.common.player_data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.matthiesen.matthiesen_core.common.core.data.SavedPlayerData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Argument type for server users, which can be either online or offline players.
 */
@SuppressWarnings("unused")
public final class ServerUserArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498");

    /**
     * Creates a new instance of ServerUserArgument.
     */
    public ServerUserArgument() {}

    /**
     * Creates a new instance of ServerUserArgument.
     * @return A new instance of ServerUserArgument.
     */
    public static ServerUserArgument playerArg() {
        return new ServerUserArgument();
    }

    /**
     * Gets the ServerUser from the command context.
     * @param context The command context.
     * @param string The argument name.
     * @return The ServerUser, or null if not found.
     */
    public static ServerUser getUser(CommandContext<CommandSourceStack> context, String string) {
        var name = context.getArgument(string, String.class);
        if (name == null) return null;
        UUID uuid = null;
        // If string is a valid UUID
        try {
            uuid = UUID.fromString(name);
        } catch (IllegalArgumentException e) {
            // Do nothing
        }
        if (uuid != null) {
            // If UUID is valid, check if player exists
            if (SavedPlayerData.hasSavedPlayerData(uuid)) return new ServerUser(uuid);
            // Else continue
        }
        // Check if the player name exists in the saved player data
        if (SavedPlayerData.hasSavedPlayerData(name)) return new ServerUser(name);
        return null;
    }

    /**
     * Parses the input string to extract the server user argument. This method reads a string from the provided StringReader and returns it as the argument value. If the input is not a valid string, a CommandSyntaxException is thrown.
     * @param reader The StringReader instance used to read the input string.
     * @return The parsed string representing the server user argument.
     * @throws CommandSyntaxException If the input string is not valid or cannot be parsed correctly.
     */
    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    /**
     * Provides suggestions for the server user argument based on the current input. This method generates a list of suggestions for player names and UUIDs that match the input string. It uses the SharedSuggestionProvider to retrieve online player names and fills the suggestions builder with matching entries. If the source is not a SharedSuggestionProvider, an empty Suggestions object is returned.
     * @param context The command context containing the source and other relevant information.
     * @param builder The SuggestionsBuilder used to build the list of suggestions based on the current input.
     * @return A CompletableFuture containing the Suggestions object with the generated suggestions for the server user argument. If no suggestions are available, an empty Suggestions object is returned.
     * @param <S> The type of the command source, which extends CommandSourceStack.
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Object var4 = context.getSource();
        if (var4 instanceof SharedSuggestionProvider sharedSuggestionProvider) {
            StringReader stringReader = new StringReader(builder.getInput());
            stringReader.setCursor(builder.getStart());

            EntitySelectorParser entitySelectorParser = new EntitySelectorParser(stringReader, EntitySelectorParser.allowSelectors(sharedSuggestionProvider));

            try {
                entitySelectorParser.parse();
            } catch (CommandSyntaxException e) {
                // Handle exception
            }

            return entitySelectorParser.fillSuggestions(builder, (suggestionsBuilder -> {
                Collection<String> collection = sharedSuggestionProvider.getOnlinePlayerNames();
                SharedSuggestionProvider.suggest(collection, suggestionsBuilder);
            }));
        } else {
            return Suggestions.empty();
        }
    }

    /**
     * Gets a collection of example strings for the server user argument. These examples can be used for documentation or testing purposes to illustrate valid input formats for the argument. The examples include a player name, a numeric string, and a UUID string.
     * @return A collection of example strings representing valid input formats for the server user argument.
     */
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
