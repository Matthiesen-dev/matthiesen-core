/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A PlaceholderButton is a wrapper around another Button that can be used as a placeholder in server menus.
 * It delegates all method calls to the wrapped button, allowing for flexible button management and display.
 */
public class PlaceholderButton implements Button {

    private final Button button;

    /**
     * Constructs a PlaceholderButton that wraps the given button.
     *
     * @param button The button to wrap.
     */
    public PlaceholderButton(@NotNull Button button) {
        this.button = button;
    }

    /**
     * Creates a new PlaceholderButton with an empty display.
     * This constructor is useful for creating placeholder buttons that do not have any specific functionality or display.
     */
    @SuppressWarnings("unused")
    public PlaceholderButton() {
        this(
                ServerButton.builder()
                        .display(ItemStack.EMPTY)
                        .build()
        );
    }

    /**
     * Creates a new PlaceholderButton that wraps the given button.
     *
     * @param button The button to wrap.
     * @return A new PlaceholderButton instance.
     */
    public static PlaceholderButton of(@NotNull Button button) {
        return new PlaceholderButton(button);
    }

    @Override
    public ItemStack getDisplay() {
        return button.getDisplay();
    }

    @Override
    public void onClick(@NotNull ButtonAction action) {
        button.onClick(action);
    }

    @Override
    public void subscribe(@NotNull Object observer, @NotNull Consumer<Button> consumer) {
        button.subscribe(observer, consumer);
    }

    @Override
    public void unsubscribe(@NotNull Object observer) {
        button.unsubscribe(observer);
    }

}
