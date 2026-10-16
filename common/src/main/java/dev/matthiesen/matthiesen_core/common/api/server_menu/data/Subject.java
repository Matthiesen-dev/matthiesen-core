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

/**
 * Represents a subject in the observer pattern, allowing observers to subscribe and receive updates when the subject's state changes.
 *
 * @param <T> The type of data that the subject will notify its observers about.
 */
public interface Subject<T> {
    /**
     * Subscribes the specified observer to receive updates from this subject. The provided Consumer will be executed whenever the subject's state changes.
     *
     * @param observer The observer subscribing to this subject.
     * @param consumer The Consumer to execute when the subject's state changes.
     */
    void subscribe(@NotNull Object observer, @NotNull Consumer<T> consumer);

    /**
     * Subscribes the specified observer to receive updates from this subject. The provided Runnable will be executed whenever the subject's state changes.
     *
     * @param observer The observer subscribing to this subject.
     * @param runnable The Runnable to execute when the subject's state changes.
     */
    default void subscribe(@NotNull Object observer, @NotNull Runnable runnable) {
        this.subscribe(observer, t -> runnable.run());
    }

    /**
     * Unsubscribes the specified observer from receiving updates from this subject.
     *
     * @param observer The observer to unsubscribe.
     */
    void unsubscribe(@NotNull Object observer);
}
