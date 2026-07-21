package dev.matthiesen.matthiesen_core.common.utility;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Fluent helper for building Brigadier command trees in a more readable way.
 * <p>
 * This builder is intended to reduce the amount of nested {@code Commands.literal(...)} and
 * {@code Commands.argument(...)} calls needed when registering complex command hierarchies. It wraps a root
 * {@link LiteralArgumentBuilder} and exposes a compact API for attaching requirements, execution handlers,
 * nested literals, and arguments.
 * <p>
 * Typical usage looks like this:
 * <pre>{@code
 * dispatcher.register(
 *     new CommandBuilder("fallingstar")
 *         .requires(src -> src.hasPermission(4))
 *         .then("help", help -> help.executes(this::help))
 *         .then("reload", reload -> reload.executes(this::reload))
 *         .then("status", status -> status
 *             .executes(this::status)
 *             .then("brief", brief -> brief.executes(this::status))
 *             .then("full", full -> full.executes(this::statusFull)))
 *         .then("cleanup", cleanup -> cleanup.executes(this::cleanup))
 *         .then("force", force -> force
 *             .executes(this::forceOnce)
 *             .argument("count", IntegerArgumentType.integer(1, 16),
 *                 count -> count.executes(this::forceCount)))
 *         .build()
 * );
 * }</pre>
 * <p>
 * The builder is mutable and designed for one-time configuration during command registration.
 */
@SuppressWarnings("unused")
public class CommandBuilder {
    private LiteralArgumentBuilder<CommandSourceStack> base;

    /**
     * Creates a new command builder for a literal root command.
     *
     * @param baseCmdName the literal command name to register as the root node
     */
    public CommandBuilder(String baseCmdName) {
        this.base = Commands.literal(baseCmdName);
    }

    /**
     * Creates a new command builder for a literal root command and immediately applies a requirement.
     *
     * @param baseCmdName the literal command name to register as the root node
     * @param requirement the predicate that determines whether a command source may use this node; may be {@code null}
     */
    public CommandBuilder(String baseCmdName, Predicate<CommandSourceStack> requirement) {
        this.base = Commands.literal(baseCmdName);
        if (requirement != null) {
            this.base = this.base.requires(requirement);
        }
    }

    /**
     * Creates a new command builder for a literal root command and immediately applies both a requirement and
     * execution callback.
     *
     * @param baseCmdName the literal command name to register as the root node
     * @param requirement the predicate that determines whether a command source may use this node; may be {@code null}
     * @param executes the command callback to execute when the root node runs; may be {@code null}
     */
    public CommandBuilder(String baseCmdName, Predicate<CommandSourceStack> requirement, Command<CommandSourceStack> executes) {
        this.base = Commands.literal(baseCmdName);
        if (requirement != null) {
            this.base = this.base.requires(requirement);
        }
        if (executes != null) {
            this.base = this.base.executes(executes);
        }
    }

    /**
     * Sets or replaces the requirement predicate for the root command node.
     * <p>
     * This is a convenience wrapper around Brigadier's {@code requires(...)} method and returns the same builder
     * so you can continue chaining configuration calls.
     *
     * @param requirement the predicate that determines whether a command source may use this node; may be {@code null}
     * @return this builder instance for chaining
     */
    public CommandBuilder requires(Predicate<CommandSourceStack> requirement) {
        if (requirement != null) {
            this.base = this.base.requires(requirement);
        }
        return this;
    }

    /**
     * Sets or replaces the execution callback for the root command node.
     * <p>
     * This is a convenience wrapper around Brigadier's {@code executes(...)} method and returns the same builder
     * so you can continue chaining configuration calls.
     *
     * @param executes the command callback to execute when the root node runs; may be {@code null}
     * @return this builder instance for chaining
     */
    public CommandBuilder executes(Command<CommandSourceStack> executes) {
        if (executes != null) {
            this.base = this.base.executes(executes);
        }
        return this;
    }

    /**
     * Adds an already-built literal child node to the current command tree.
     *
     * @param child the literal child node to append; may be {@code null}
     * @return this builder instance for chaining
     */
    public CommandBuilder then(LiteralArgumentBuilder<CommandSourceStack> child) {
        if (child != null) {
            this.base.then(child);
        }
        return this;
    }

    /**
     * Adds an already-built argument child node to the current command tree.
     *
     * @param child the argument child node to append; may be {@code null}
     * @return this builder instance for chaining
     */
    public CommandBuilder then(RequiredArgumentBuilder<CommandSourceStack, ?> child) {
        if (child != null) {
            this.base.then(child);
        }
        return this;
    }

    /**
     * Adds another {@code CommandBuilder} as a literal child node.
     * <p>
     * This is useful when you want to break a larger command tree into smaller, reusable pieces.
     *
     * @param child another command builder to append as a child node; may be {@code null}
     * @return this builder instance for chaining
     */
    public CommandBuilder then(CommandBuilder child) {
        if (child != null) {
            this.base.then(child.build());
        }
        return this;
    }

    /**
     * Creates and configures a literal child node in one step.
     * <p>
     * The provided consumer receives a fresh {@code CommandBuilder} rooted at the supplied literal name. Use it to
     * attach subcommands, requirements, or execution handlers before the child node is added to the current tree.
     *
     * @param literalName the literal name of the child command
     * @param builder a callback used to configure the child builder; may be {@code null}
     * @return this builder instance for chaining
     */
    public CommandBuilder then(String literalName, Consumer<CommandBuilder> builder) {
        var child = new CommandBuilder(literalName);
        if (builder != null) {
            builder.accept(child);
        }
        return then(child);
    }

    /**
     * Creates and configures an argument child node in one step.
     * <p>
     * This is the main convenience method for commands that take typed arguments such as integers, strings, or
     * entity selectors. The provided consumer receives the raw Brigadier argument builder so you can attach
     * execution handlers or further nested children.
     *
     * @param name the argument name as it will appear in the command syntax
     * @param type the Brigadier argument type definition
     * @param builder a callback used to configure the argument builder; may be {@code null}
     * @param <T> the parsed value type for the argument
     * @return this builder instance for chaining
     */
    public <T> CommandBuilder argument(String name, ArgumentType<T> type, Consumer<RequiredArgumentBuilder<CommandSourceStack, T>> builder) {
        var child = Commands.argument(name, type);
        if (builder != null) {
            builder.accept(child);
        }
        return then(child);
    }

    /**
     * Returns the configured root Brigadier builder.
     * <p>
     * Call this once you have finished composing the command tree and are ready to register it with a dispatcher.
     *
     * @return the configured literal argument builder for the command root
     */
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return base;
    }
}
