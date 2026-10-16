/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.data;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

/**
 * A generic event emitter that allows observers to subscribe and receive events of type T.
 * This class implements the Subject interface, providing methods for subscribing, unsubscribing, and emitting events.
 *
 * @param <T> The type of the event data that will be emitted to observers.
 */
public class EventEmitter<T> implements Subject<T> {

    private final Map<Object, Consumer<T>> observers = Maps.newHashMap();

    @Override
    public void subscribe(@NotNull Object subscriber, @NotNull Consumer<T> consumer) {
        this.observers.put(subscriber, consumer);
    }

    @Override
    public void unsubscribe(@NotNull Object subscriber) {
        this.observers.remove(subscriber);
    }

    /**
     * Emits an event to all subscribed observers, passing the specified value to each observer's consumer.
     *
     * @param value The value to emit to the observers.
     */
    public void emit(T value) {
        this.observers.values().forEach((observer) -> observer.accept(value));
    }

}
