package dev.matthiesen.matthiesen_core.common.abstracts;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.command.CoreCommand;

/**
 * Abstract base class for command registries. Provides a default implementation of the init method and a static register method to register commands with the MatthiesenLib command system.
 * Subclasses can extend this class to create specific command registries for different modules or features.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommandRegistry {
    /**
     * Default constructor for the AbstractCommandRegistry class. No initialization is required as setup is handled in the init method.
     */
    protected AbstractCommandRegistry() {}

    /**
     * Registers a command with the MatthiesenLib command system. This method can be called at any time, and the command will be registered appropriately based on the state of the command registrar.
     * @param command The CoreCommand to register. This command will be added to the MatthiesenLib command system and will be available for use once the registrar is ready.
     */
    protected final void register(CoreCommand command) {
        MatthiesenCoreCommon.INSTANCE.getCommandsRegistryManager().registerCommand(command);
    }
}

