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

public abstract class PageBase implements Page {

    private final EventEmitter<Page> eventEmitter = new EventEmitter<>();
    private Template template;
    private InventoryTemplate inventoryTemplate;
    private Component title;

    public PageBase(@NotNull Template template,
                    @Nullable InventoryTemplate inventoryTemplate,
                    @Nullable Component title) {
        this.template = template;
        this.inventoryTemplate = inventoryTemplate;
        this.title = (title != null) ? title : Component.empty();
    }

    public Template getTemplate() {
        if (template == null) throw new IllegalStateException("template could not be found on the page!");
        return template;
    }

    public void setTemplate(@NotNull Template template) {
        this.template = template;
        update();
    }

    @Override
    public Optional<InventoryTemplate> getInventoryTemplate() {
        return Optional.ofNullable(inventoryTemplate);
    }

    @SuppressWarnings("unused")
    public void setPlayerInventoryTemplate(@Nullable InventoryTemplate inventoryTemplate) {
        this.inventoryTemplate = inventoryTemplate;
    }

    public Component getTitle() {
        return this.title;
    }

    public void setTitle(@Nullable String title) {
        this.setTitle(title == null ? null : Component.literal(title));
    }

    public void setTitle(@Nullable Component title) {
        this.title = (title == null) ? Component.empty() : title;
        update();
    }

    public void subscribe(@NotNull Object observer, @NotNull Consumer<Page> consumer) {
        this.eventEmitter.subscribe(observer, consumer);
    }

    public void unsubscribe(@NotNull Object observer) {
        this.eventEmitter.unsubscribe(observer);
    }

    public void update() {
        this.eventEmitter.emit(this);
    }

}
