package dev.matthiesen.matthiesen_core.common.utility.player_data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.matthiesen.matthiesen_core.common.core.data.SavedPlayerData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Argument type for server users, which can be either online or offline players.
 */
@SuppressWarnings("unused")
public final class ServerUserArguments {

    /**
     * Creates a new RequiredArgumentBuilder for server user arguments. This method sets up the argument type to accept a string input, which can be either a player name or a UUID. It also provides suggestions for the argument based on the current input, allowing for easier command usage.
     * @param name The name of the argument, which will be used in the command context to retrieve the value.
     * @return A RequiredArgumentBuilder configured for server user arguments, with suggestions enabled.
     */
    public static RequiredArgumentBuilder<CommandSourceStack, String> serverUserArgument(String name) {
        return Commands.argument(name, StringArgumentType.word())
                .suggests(ServerUserArguments::argumentSuggestions);
    }

    /**
     * Provides suggestions for the server user argument based on the current input. This method generates a list of suggestions for player names and UUIDs that match the input string. It uses the SharedSuggestionProvider to retrieve online player names and fills the suggestions builder with matching entries. If the source is not a SharedSuggestionProvider, an empty Suggestions object is returned.
     * @param context The command context containing the source and other relevant information.
     * @param builder The SuggestionsBuilder used to build the list of suggestions based on the current input.
     * @return A CompletableFuture containing the Suggestions object with the generated suggestions for the server user argument. If no suggestions are available, an empty Suggestions object is returned.
     * @param <S> The type of the command source, which extends CommandSourceStack.
     */
    public static <S> CompletableFuture<Suggestions> argumentSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
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
}
