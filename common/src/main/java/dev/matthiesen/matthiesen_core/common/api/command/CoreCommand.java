package dev.matthiesen.matthiesen_core.common.api.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * Interface for a core command that can be registered with the command dispatcher.
 * Implementations of this interface should provide the logic for registering the command with the dispatcher.
 */
public interface CoreCommand {
    /**
     * Registers the command with the given dispatcher, registry, and context.
     * @param dispatcher The command dispatcher to register the command with.
     * @param registry The command build context.
     * @param context The command selection context.
     */
    void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context);
}
