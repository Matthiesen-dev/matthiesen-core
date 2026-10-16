/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.page;

import dev.matthiesen.matthiesen_core.common.api.server_menu.data.EventEmitter;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.InventoryTemplate;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a base implementation of a page in a server menu system.
 * This class provides common functionality for managing templates, inventory templates, and titles,
 * as well as event subscription and notification for page updates.
 */
public abstract class PageBase implements Page {

    private final EventEmitter<Page> eventEmitter = new EventEmitter<>();
    private Template template;
    private InventoryTemplate inventoryTemplate;
    private Component title;

    /**
     * Constructs a new PageBase instance with the specified template, inventory template, and title.
     *
     * @param template          The template for this page. Must not be null.
     * @param inventoryTemplate The inventory template for this page. Can be null if no inventory is needed.
     * @param title             The title of the page as a Component. If null, an empty Component will be used.
     */
    public PageBase(@NotNull Template template,
                    @Nullable InventoryTemplate inventoryTemplate,
                    @Nullable Component title) {
        this.template = template;
        this.inventoryTemplate = inventoryTemplate;
        this.title = (title != null) ? title : Component.empty();
    }

    /**
     * Returns the template associated with this page.
     *
     * @return The Template instance for this page.
     * @throws IllegalStateException if the template is not set.
     */
    public Template getTemplate() {
        if (template == null) throw new IllegalStateException("template could not be found on the page!");
        return template;
    }

    /**
     * Sets the template for this page. If the provided template is null, it will throw an IllegalArgumentException.
     *
     * @param template The new template for the page. Must not be null.
     * @throws IllegalArgumentException if the provided template is null.
     */
    public void setTemplate(@NotNull Template template) {
        this.template = template;
        update();
    }

    @Override
    public Optional<InventoryTemplate> getInventoryTemplate() {
        return Optional.ofNullable(inventoryTemplate);
    }

    /**
     * Sets the inventory template for this page. If the provided inventory template is null, it will be cleared.
     *
     * @param inventoryTemplate The new inventory template for the page, or null to clear it.
     */
    @SuppressWarnings("unused")
    public void setPlayerInventoryTemplate(@Nullable InventoryTemplate inventoryTemplate) {
        this.inventoryTemplate = inventoryTemplate;
    }

    /**
     * Returns the title of the page as a Component.
     *
     * @return The title of the page.
     */
    public Component getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the page. If the provided title is null, it will be set to an empty string.
     *
     * @param title The new title for the page, or null to set it to an empty string.
     */
    public void setTitle(@Nullable String title) {
        this.setTitle(title == null ? null : Component.literal(title));
    }

    /**
     * Sets the title of the page. If the provided title is null, it will be set to an empty Component.
     *
     * @param title The new title for the page, or null to set it to an empty Component.
     */
    public void setTitle(@Nullable Component title) {
        this.title = (title == null) ? Component.empty() : title;
        update();
    }

    /**
     * Subscribes the specified observer to receive updates about changes to this page.
     *
     * @param observer The observer to subscribe. Must not be null.
     * @param consumer A consumer that will be called with the updated page when changes occur. Must not be null.
     */
    public void subscribe(@NotNull Object observer, @NotNull Consumer<Page> consumer) {
        this.eventEmitter.subscribe(observer, consumer);
    }

    /**
     * Unsubscribes the specified observer from receiving updates about changes to this page.
     *
     * @param observer The observer to unsubscribe. Must not be null.
     */
    public void unsubscribe(@NotNull Object observer) {
        this.eventEmitter.unsubscribe(observer);
    }

    /**
     * Notifies all subscribers that the state of this page has changed.
     * This method should be called whenever the page's template, inventory template, or title is modified.
     */
    public void update() {
        this.eventEmitter.emit(this);
    }

}
