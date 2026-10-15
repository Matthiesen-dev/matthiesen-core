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

@SuppressWarnings("unused")
public class MovableButton extends ServerButton implements Movable {
    private final Consumer<MovableButtonAction> onPickup;
    private final Consumer<MovableButtonAction> onDrop;

    protected MovableButton(@NotNull ItemStack display,
                            @Nullable Consumer<ButtonAction> onClick,
                            @Nullable Consumer<MovableButtonAction> onPickup,
                            @Nullable Consumer<MovableButtonAction> onDrop) {
        super(display, onClick);
        this.onPickup = onPickup;
        this.onDrop = onDrop;
    }

    public void onPickup(MovableButtonAction action) {
        if (onPickup != null) {
            this.onPickup.accept(action);
        }
    }

    public void onDrop(MovableButtonAction action) {
        if (onDrop != null) {
            this.onDrop.accept(action);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ServerButton.Builder {

        protected Consumer<MovableButtonAction> onPickup;
        protected Consumer<MovableButtonAction> onDrop;

        public Builder display(@NotNull ItemStack display) {
            super.display(display);
            return this;
        }

        public Builder onClick(@Nullable Consumer<ButtonAction> behaviour) {
            super.onClick(behaviour);
            return this;
        }

        public Builder onClick(@Nullable Runnable behaviour) {
            super.onClick(behaviour);
            return this;
        }

        public Builder onPickup(@Nullable Consumer<MovableButtonAction> behaviour) {
            this.onPickup = behaviour;
            return this;
        }

        public Builder onPickup(@Nullable Runnable behaviour) {
            if (behaviour != null) {
                this.onPickup = (action) -> behaviour.run();
            }
            return this;
        }

        public Builder onDrop(@Nullable Consumer<MovableButtonAction> behaviour) {
            this.onDrop = behaviour;
            return this;
        }

        public Builder onDrop(@Nullable Runnable behaviour) {
            if (behaviour != null) {
                this.onDrop = (action) -> behaviour.run();
            }
            return this;
        }

        @Override
        public <T> ServerButton.Builder with(DataComponentType<T> type, T value) {
            super.with(type, value);
            return this;
        }

        public MovableButton build() {
            validate();
            return new MovableButton(this.display, onClick, onPickup, onDrop);
        }

    }
}
