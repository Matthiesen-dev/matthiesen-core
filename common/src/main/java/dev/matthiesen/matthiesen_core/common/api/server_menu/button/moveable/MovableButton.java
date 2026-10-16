/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button.moveable;

import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ButtonAction;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a button in a server menu that can be moved by players.
 * This class extends ServerButton and adds functionality for handling pickup and drop actions.
 */
@SuppressWarnings("unused")
public class MovableButton extends ServerButton implements Movable {
    private final Consumer<MovableButtonAction> onPickup;
    private final Consumer<MovableButtonAction> onDrop;

    /**
     * Constructs a new MovableButton with the specified display item stack, onClick behavior, onPickup behavior, and onDrop behavior.
     *
     * @param display  The ItemStack to be used as the button's display.
     * @param onClick  A ButtonAction that defines the behavior when the button is clicked. Can be null for no action.
     * @param onPickup A MovableButtonAction that defines the behavior when the button is picked up. Can be null for no action.
     * @param onDrop   A MovableButtonAction that defines the behavior when the button is dropped. Can be null for no action.
     */
    protected MovableButton(@NotNull ItemStack display,
                            @Nullable Consumer<ButtonAction> onClick,
                            @Nullable Consumer<MovableButtonAction> onPickup,
                            @Nullable Consumer<MovableButtonAction> onDrop) {
        super(display, onClick);
        this.onPickup = onPickup;
        this.onDrop = onDrop;
    }

    /**
     * Invokes the onPickup behavior for this MovableButton with the provided MovableButtonAction.
     * If an onPickup behavior is defined, it will be executed; otherwise, no action will be taken.
     *
     * @param action The MovableButtonAction containing context about the pickup event.
     */
    public void onPickup(MovableButtonAction action) {
        if (onPickup != null) {
            this.onPickup.accept(action);
        }
    }

    /**
     * Invokes the onDrop behavior for this MovableButton with the provided MovableButtonAction.
     * If an onDrop behavior is defined, it will be executed; otherwise, no action will be taken.
     *
     * @param action The MovableButtonAction containing context about the drop event.
     */
    public void onDrop(MovableButtonAction action) {
        if (onDrop != null) {
            this.onDrop.accept(action);
        }
    }

    /**
     * Creates a new Builder instance for constructing a MovableButton.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing instances of MovableButton.
     * This class provides a fluent API for setting various properties of the MovableButton.
     */
    public static class Builder extends ServerButton.Builder {

        protected Consumer<MovableButtonAction> onPickup;
        protected Consumer<MovableButtonAction> onDrop;

        /**
         * Sets the display item stack for the button.
         *
         * @param display The ItemStack to be used as the button's display.
         * @return The current Builder instance for chaining.
         */
        public Builder display(@NotNull ItemStack display) {
            super.display(display);
            return this;
        }

        /**
         * Sets the onClick behavior for the button using a ButtonAction.
         * This method allows you to specify a custom action to be executed when the button is clicked.
         *
         * @param behaviour The ButtonAction to execute on click. If null, no action will be performed.
         * @return The current Builder instance for chaining.
         */
        public Builder onClick(@Nullable Consumer<ButtonAction> behaviour) {
            super.onClick(behaviour);
            return this;
        }

        /**
         * Sets the onClick behavior for the button using a Runnable.
         * This method allows you to specify a custom action to be executed when the button is clicked.
         *
         * @param behaviour The Runnable to execute on click. If null, no action will be performed.
         * @return The current Builder instance for chaining.
         */
        public Builder onClick(@Nullable Runnable behaviour) {
            super.onClick(behaviour);
            return this;
        }

        /**
         * Sets the onPickup behavior for the button using a MovableButtonAction.
         * This method allows you to specify a custom action to be executed when the button is picked up.
         *
         * @param behaviour The MovableButtonAction to execute on pickup. If null, no action will be performed.
         * @return The current Builder instance for chaining.
         */
        public Builder onPickup(@Nullable Consumer<MovableButtonAction> behaviour) {
            this.onPickup = behaviour;
            return this;
        }

        /**
         * Sets the onPickup behavior for the button using a Runnable.
         * This method allows you to specify a custom action to be executed when the button is picked up.
         *
         * @param behaviour The Runnable to execute on pickup. If null, no action will be performed.
         * @return The current Builder instance for chaining.
         */
        public Builder onPickup(@Nullable Runnable behaviour) {
            if (behaviour != null) {
                this.onPickup = (action) -> behaviour.run();
            }
            return this;
        }

        /**
         * Sets the onDrop behavior for the button using a MovableButtonAction.
         * This method allows you to specify a custom action to be executed when the button is dropped.
         *
         * @param behaviour The MovableButtonAction to execute on drop. If null, no action will be performed.
         * @return The current Builder instance for chaining.
         */
        public Builder onDrop(@Nullable Consumer<MovableButtonAction> behaviour) {
            this.onDrop = behaviour;
            return this;
        }

        /**
         * Sets the onDrop behavior for the button using a Runnable.
         * This method allows you to specify a custom action to be executed when the button is dropped.
         *
         * @param behaviour The Runnable to execute on drop. If null, no action will be performed.
         * @return The current Builder instance for chaining.
         */
        public Builder onDrop(@Nullable Runnable behaviour) {
            if (behaviour != null) {
                this.onDrop = (action) -> behaviour.run();
            }
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
        @Override
        public <T> ServerButton.Builder with(DataComponentType<T> type, T value) {
            super.with(type, value);
            return this;
        }

        /**
         * Builds and returns a new MovableButton instance based on the current state of the Builder.
         * @return A new MovableButton instance.
         * @throws IllegalStateException if the display item stack is not set.
         */
        public MovableButton build() {
            validate();
            return new MovableButton(this.display, onClick, onPickup, onDrop);
        }

    }
}
