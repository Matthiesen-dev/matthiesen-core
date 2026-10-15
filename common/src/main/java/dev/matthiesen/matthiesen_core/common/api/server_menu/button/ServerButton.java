/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a button in a server menu that can be clicked by players.
 * This class provides a way to define the display of the button and the action to be performed when the button is clicked.
 */
public class ServerButton extends ButtonBase {

    private final Consumer<ButtonAction> onClick;

    /**
     * Constructs a new ServerButton with the specified display item stack and onClick behavior.
     *
     * @param display The ItemStack to be used as the button's display.
     * @param onClick A Consumer<ButtonAction> that defines the behavior when the button is clicked. Can be null for no action.
     */
    protected ServerButton(@NotNull ItemStack display, @Nullable Consumer<ButtonAction> onClick) {
        super(display);
        this.onClick = onClick;
    }

    @Override
    public void onClick(@NotNull ButtonAction action) {
        if (onClick != null) onClick.accept(action);
    }

    /**
     * Creates a new Builder instance for constructing a ServerButton.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new ServerButton with the specified display item stack.
     *
     * @param stack The ItemStack to be used as the button's display.
     * @return A new ServerButton instance.
     */
    public static ServerButton of(ItemStack stack) {
        return builder()
                .display(stack)
                .build();
    }

    /**
     * Builder class for constructing instances of ServerButton.
     * This class provides a fluent API for setting various properties of the ServerButton.
     */
    public static class Builder {

        protected ItemStack display;
        protected Consumer<ButtonAction> onClick;

        /**
         * Sets the display item stack for the button.
         *
         * @param display The ItemStack to be used as the button's display.
         * @return The current Builder instance for chaining.
         */
        public Builder display(@NotNull ItemStack display) {
            this.display = display.copy();
            return this;
        }

        /**
         * Sets a data component for the button's display item stack.
         *
         * @param type  The DataComponentType to set.
         * @param value The value of the data component.
         * @param <T>   The type of the data component.
         * @return The current Builder instance for chaining.
         */
        public <T> Builder with(DataComponentType<T> type, T value) {
            this.display.set(type, value);
            return this;
        }

        /**
         * Sets the onClick behavior for the button using a Consumer<ButtonAction>.
         * This method allows you to specify a custom action to be executed when the button is clicked.
         *
         * @param behaviour The Consumer<ButtonAction> to execute on click. If null, no action will be performed.
         * @return The current Builder instance for chaining.
         */
        public Builder onClick(@Nullable Consumer<ButtonAction> behaviour) {
            this.onClick = behaviour;
            return this;
        }

        /**
         * Sets the onClick behavior for the button using a Runnable.
         * This method allows you to specify a simple action to be executed when the button is clicked.
         *
         * @param behaviour The Runnable to execute on click. If null, no action will be performed.
         * @return The current Builder instance for chaining.
         */
        public Builder onClick(@Nullable Runnable behaviour) {
            this.onClick = (behaviour != null) ? (action) -> behaviour.run() : null;
            return this;
        }

        /**
         * Builds and returns a new ServerButton instance based on the current state of the builder.
         *
         * @return A new ServerButton instance.
         * @throws IllegalStateException if the display item stack is not defined.
         */
        public ServerButton build() {
            validate();
            return new ServerButton(this.display, onClick);
        }

        /**
         * Validates the builder's state before constructing a ServerButton.
         * Ensures that the display item stack is defined.
         * Throws an IllegalStateException if the display is null.
         */
        protected void validate() {
            if (display == null) throw new IllegalStateException("button display must be defined");
        }

    }

}