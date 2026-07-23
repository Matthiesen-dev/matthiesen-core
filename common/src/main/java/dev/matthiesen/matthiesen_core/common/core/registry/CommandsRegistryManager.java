package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.command.CommandRegistry;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.common.api.command.CoreCommand;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The CommandsRegistryManager is a singleton class responsible for managing the registration of commands across different platforms.
 * It maintains a list of registered commands and ensures that they are registered with the appropriate command registry when it becomes available.
 * This class is designed to be thread-safe, allowing for concurrent registration of commands from different parts of the mod.
 */
@SuppressWarnings("unused")
public final class CommandsRegistryManager {
    private static final List<CoreCommand> REGISTERED_COMMANDS = new CopyOnWriteArrayList<>();

    /**
     * Singleton instance of the CommandsRegistryManager. This instance is used to manage the registration of commands across different platforms.
     */
    public static final CommandsRegistryManager INSTANCE = new CommandsRegistryManager();

    private boolean initialized;
    private CommandRegistry commandRegistry;

    private CommandsRegistryManager() {}

    /**
     * Initializes the CommandsRegistryManager with the provided CommonLoaderRegistry. This method should be called once during the mod's
     * initialization phase. If the CommandsRegistryManager has already been initialized, this method will return immediately without performing any actions.
     * @param registry The CommonLoaderRegistry to use for command registration.
     */
    public synchronized void initialize(CommonLoaderRegistry registry) {
        if (initialized) return;

        initialized = true;
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Initialized Commands Registry Manager");
        registry.registerCommands(this::bindRegistrar);
    }

    /**
     * Registers a command with the CommandsRegistryManager. If the command registry has already been bound, the command will be registered
     * immediately. Otherwise, it will be stored and registered when the registry is bound.
     * @param command The command to register.
     */
    public synchronized void registerCommand(CoreCommand command) {
        REGISTERED_COMMANDS.add(command);
        if (commandRegistry != null) {
            commandRegistry.register(command);
        }
    }

    /**
     * Binds the command registrar to the provided registry and registers all previously registered commands.
     * @param registry The command registry to bind to.
     */
    private synchronized void bindRegistrar(CommandRegistry registry) {
        commandRegistry = registry;
        for (CoreCommand command : REGISTERED_COMMANDS) {
            registry.register(command);
        }
    }
}
