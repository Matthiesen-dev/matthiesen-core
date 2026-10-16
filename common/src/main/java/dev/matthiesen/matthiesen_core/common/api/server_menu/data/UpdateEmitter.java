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
 * An abstract class that provides a mechanism for emitting updates to subscribed observers.
 * This class implements the Subject interface, allowing observers to subscribe and unsubscribe from updates.
 * The generic type T represents the type of the object that will be emitted to observers.
 *
 * @param <T> The type of the object that will be emitted to observers.
 */
@SuppressWarnings("unused")
public abstract class UpdateEmitter<T> implements Subject<T> {

    private final EventEmitter<T> eventEmitter = new EventEmitter<>();

    /**
     * Constructs a new UpdateEmitter instance. This constructor checks that the generic type T is correctly specified by the subclass.
     * If the generic type is not correctly specified, an IllegalStateException is thrown.
     */
    @SuppressWarnings("unchecked")
    public UpdateEmitter() {
        try {
            T check = (T) this;
        } catch (ClassCastException e) {
            throw new IllegalStateException("bad generic given for superclass");
        }
    }

    @Override
    public void subscribe(@NotNull Object observer, @NotNull Consumer<T> consumer) {
        this.eventEmitter.subscribe(observer, consumer);
    }

    @Override
    public void unsubscribe(@NotNull Object observer) {
        this.eventEmitter.unsubscribe(observer);
    }

    /**
     * Emits an update to all subscribed observers, notifying them of a change in the state of this UpdateEmitter.
     * This method should be called whenever the state of the object changes and observers need to be informed.
     */
    @SuppressWarnings("unchecked")
    public void update() {
        this.eventEmitter.emit((T) this);
    }

}