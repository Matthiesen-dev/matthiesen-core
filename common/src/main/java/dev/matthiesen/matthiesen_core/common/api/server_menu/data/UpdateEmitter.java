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

@SuppressWarnings("unused")
public abstract class UpdateEmitter<T> implements Subject<T> {

    private final EventEmitter<T> eventEmitter = new EventEmitter<>();

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

    @SuppressWarnings("unchecked")
    public void update() {
        this.eventEmitter.emit((T) this);
    }

}