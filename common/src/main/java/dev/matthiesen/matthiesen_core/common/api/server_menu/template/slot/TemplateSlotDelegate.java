/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.template.slot;

import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.data.EventEmitter;
import dev.matthiesen.matthiesen_core.common.api.server_menu.data.Subject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public final class TemplateSlotDelegate implements Subject<TemplateSlotDelegate> {

    private final EventEmitter<TemplateSlotDelegate> eventEmitter = new EventEmitter<>();
    private Button button;
    private final int index;

    public TemplateSlotDelegate(@Nullable Button button, int index) {
        this.button = button;
        this.index = index;
    }

    public Optional<Button> getButton() {
        return Optional.ofNullable(this.button);
    }

    public void setButton(@Nullable Button button) {
        if (this.button != null) {
            this.button.unsubscribe(this);
        }

        this.button = button;
        if (this.button != null) {
            this.button.subscribe(this, this::update);
        }

        this.update();
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public void subscribe(@NotNull Object observer, @NotNull Consumer<TemplateSlotDelegate> consumer) {
        this.eventEmitter.subscribe(observer, consumer);
    }

    @Override
    public void unsubscribe(@NotNull Object observer) {
        this.eventEmitter.unsubscribe(observer);
    }

    public void update() {
        this.eventEmitter.emit(this);
    }
}
