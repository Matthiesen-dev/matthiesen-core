package dev.matthiesen.matthiesen_core.common.api.command;

/**
 * A functional interface for registering commands with the platform's command system.
 */
@FunctionalInterface
public interface CommandRegistry {
    /**
     * Registers a command with the platform's command system.
     * @param command The command to register.
     */
    void register(CoreCommand command);
}
