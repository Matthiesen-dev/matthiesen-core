package dev.matthiesen.matthiesen_core.common.core.server_menu;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The MenuCallbackEngine is responsible for managing callbacks associated with server menu actions.
 * It allows for the registration of Java action lambdas that can be executed when a player interacts with a menu button.
 * Each callback is mapped to a unique, executable hidden command string, which can be invoked by players.
 */
public final class MenuCallbackEngine {
    private static final Map<UUID, Consumer<ServerPlayer>> CALLBACKS = new HashMap<>();

    /**
     * Initializes the MenuCallbackEngine with the provided CommandDispatcher.
     * This method must be called before registering any callbacks.
     *
     * @param dispatcher The CommandDispatcher to use for registering callback commands.
     */
    public static void init(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("matthiesen_core_menu_cb")
                .requires(source -> source.hasPermission(0)) // Available to all players safely
                .then(Commands.argument("callback_id", UuidArgument.uuid())
                        .executes(MenuCallbackEngine::runCallback));

        dispatcher.register(command);
    }

    /**
     * Executes the callback associated with the provided command context.
     * If a valid callback is found, it will be executed with the player who triggered the command.
     *
     * @param context The command context containing the source and arguments for the callback.
     * @return An integer indicating the success of the callback execution (1 for success).
     */
    public static int runCallback(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (source.getEntity() instanceof ServerPlayer player) {
            UUID callbackId = UuidArgument.getUuid(context, "callback_id");
            Consumer<ServerPlayer> callback = CALLBACKS.get(callbackId);
            if (callback != null) {
                callback.accept(player);
            }
        }
        return 1;
    }

    /**
     * Registers a Java action lambda and maps it to a unique, executable hidden command string.
     *
     * @param action The code to execute when a player clicks the button.
     * @return The complete command string to execute (e.g., "/matthiesen_core_menu_cb 550e8400-e29b-41d4-a716-446655440000")
     */
    public static String registerCallback(Consumer<ServerPlayer> action) {
        UUID id = UUID.randomUUID();
        CALLBACKS.put(id, action);
        return "/matthiesen_core_menu_cb " + id;
    }

    /**
     * Invalidates a previously registered callback based on the command string.
     * This removes the callback from the internal map, preventing it from being executed again.
     *
     * @param commandString The command string associated with the callback to invalidate.
     */
    public static void invalidate(String commandString) {
        if (commandString.startsWith("/matthiesen_core_menu_cb ")) {
            try {
                UUID id = UUID.fromString(commandString.substring(26));
                CALLBACKS.remove(id);
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
