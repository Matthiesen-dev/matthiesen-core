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

/**
 * Represents a delegate for a slot in a server menu template, managing the button associated with the slot and notifying observers of changes.
 * This class implements the Subject interface to allow observers to subscribe and receive updates when the button state changes.
 */
public final class TemplateSlotDelegate implements Subject<TemplateSlotDelegate> {

    private final EventEmitter<TemplateSlotDelegate> eventEmitter = new EventEmitter<>();
    private Button button;
    private final int index;

    /**
     * Constructs a new TemplateSlotDelegate with the specified button and index.
     *
     * @param button The button associated with this slot delegate, or null if no button is set.
     * @param index  The index of this slot delegate within its parent template.
     */
    public TemplateSlotDelegate(@Nullable Button button, int index) {
        this.button = button;
        this.index = index;
    }

    /**
     * Returns the button associated with this TemplateSlotDelegate.
     *
     * @return An Optional containing the button if present, or an empty Optional if no button is set.
     */
    public Optional<Button> getButton() {
        return Optional.ofNullable(this.button);
    }

    /**
     * Sets the button for this TemplateSlotDelegate. If a button is already set, it will be unsubscribed from this delegate.
     * The new button will be subscribed to this delegate, and the state will be updated.
     *
     * @param button The new button to set, or null to clear the button.
     */
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

    /**
     * Returns the index of this TemplateSlotDelegate within its parent template.
     *
     * @return The index of this slot delegate.
     */
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

    /**
     * Notifies all subscribers that the state of this TemplateSlotDelegate has changed.
     * This method is called whenever the button associated with this slot is updated.
     */
    public void update() {
        this.eventEmitter.emit(this);
    }
}
