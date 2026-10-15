/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.data;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface Subject<T> {
    void subscribe(@NotNull Object observer, @NotNull Consumer<T> consumer);

    default void subscribe(@NotNull Object observer, @NotNull Runnable runnable) {
        this.subscribe(observer, t -> runnable.run());
    }

    void unsubscribe(@NotNull Object observer);
}
