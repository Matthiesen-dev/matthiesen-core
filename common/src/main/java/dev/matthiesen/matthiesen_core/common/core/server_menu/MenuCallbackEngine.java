package dev.matthiesen.matthiesen_core.common.core.server_menu;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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
    private static CommandDispatcher<CommandSourceStack> serverDispatcher;

    /**
     * Initializes the MenuCallbackEngine with the provided CommandDispatcher.
     * This method must be called before registering any callbacks.
     *
     * @param dispatcher The CommandDispatcher to use for registering callback commands.
     */
    public static void init(CommandDispatcher<CommandSourceStack> dispatcher) {
        serverDispatcher = dispatcher;
    }

    /**
     * Registers a Java action lambda and maps it to a unique, executable hidden command string.
     *
     * @param action The code to execute when a player clicks the button.
     * @return The complete command string to execute (e.g., "/matthiesen_core_menu_cb 550e8400-e29b-41d4-a716-446655440000")
     */
    public static String registerCallback(Consumer<ServerPlayer> action) {
        if (serverDispatcher == null) {
            throw new IllegalStateException("MenuCallbackEngine was not initialized with a CommandDispatcher!");
        }

        UUID id = UUID.randomUUID();
        CALLBACKS.put(id, action);

        String subCommandName = id.toString();
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("matthiesen_core_menu_cb")
                .then(Commands.literal(subCommandName)
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (source.getEntity() instanceof ServerPlayer player) {
                                Consumer<ServerPlayer> callback = CALLBACKS.get(id);
                                if (callback != null) {
                                    callback.accept(player);
                                }
                            }
                            return 1;
                        }));

        serverDispatcher.register(command);
        return "/matthiesen_core_menu_cb " + subCommandName;
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
                // Note: De-registering individual commands from Brigadier at runtime can be unstable;
                // keeping the command maps minimal via the HashMap callback removal is preferred.
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
