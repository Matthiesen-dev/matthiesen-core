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

public class PlaceholderButton implements Button {

    private final Button button;

    public PlaceholderButton(@NotNull Button button) {
        this.button = button;
    }

    @SuppressWarnings("unused")
    public PlaceholderButton() {
        this(
                ServerButton.builder()
                        .display(ItemStack.EMPTY)
                        .build()
        );
    }

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
