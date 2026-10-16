/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.page;

import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.InventoryTemplate;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a server page that can be opened and closed, with customizable behavior for these events.
 * This class extends PageBase and provides additional functionality for handling onOpen and onClose events.
 */
public class ServerPage extends PageBase {

    private final Consumer<PageAction> onOpen, onClose;

    /**
     * Constructs a new ServerPage instance with the specified template, inventory template, title, and event handlers.
     *
     * @param template          The template for this page. Must not be null.
     * @param inventoryTemplate The inventory template for this page. Can be null if no inventory is needed.
     * @param title             The title of the page as a Component. If null, an empty Component will be used.
     * @param onOpen            A PageAction to execute when the page is opened. Can be null if no action is needed.
     * @param onClose           A PageAction to execute when the page is closed. Can be null if no action is needed.
     */
    public ServerPage(@NotNull Template template,
                     @Nullable InventoryTemplate inventoryTemplate,
                     @Nullable Component title,
                     @Nullable Consumer<PageAction> onOpen,
                     @Nullable Consumer<PageAction> onClose) {
        super(template, inventoryTemplate, title);
        this.onOpen = onOpen;
        this.onClose = onClose;
    }

    @Override
    public void onOpen(@NotNull PageAction action) {
        if (onOpen != null) onOpen.accept(action);
    }

    @Override
    public void onClose(@NotNull PageAction action) {
        if (onClose != null) onClose.accept(action);
    }

    /**
     * Creates a new Builder instance for constructing a ServerPage.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing instances of ServerPage.
     * This class provides a fluent API for setting various properties of the ServerPage.
     */
    public static class Builder {

        protected Component title;
        protected Template template;
        protected InventoryTemplate inventoryTemplate;
        protected Consumer<PageAction> onOpen, onClose;

        /**
         * Sets the title of the page. If the provided title is null, it will be set to an empty string.
         *
         * @param title The new title for the page, or null to set it to an empty string.
         * @return The current Builder instance for method chaining.
         */
        public Builder title(@Nullable String title) {
            if(title == null) {
                return this;
            }

            return this.title(Component.literal(title));
        }

        /**
         * Sets the title of the page. If the provided title is null, it will be set to an empty Component.
         *
         * @param title The new title for the page, or null to set it to an empty Component.
         * @return The current Builder instance for method chaining.
         */
        public Builder title(@Nullable Component title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the template for the page. If the provided template is null, it will throw an IllegalArgumentException.
         *
         * @param template The Template to set for the page. Must not be null.
         * @return The current Builder instance for method chaining.
         * @throws IllegalArgumentException if the provided template is null.
         */
        public Builder template(@NotNull Template template) {
            if (template instanceof InventoryTemplate) {
                throw new IllegalArgumentException("you can not use an inventory template here!");
            }

            this.template = template;
            return this;
        }

        /**
         * Sets the inventory template for the page. If the provided template is null, it will clear any existing inventory template.
         *
         * @param template The InventoryTemplate to set for the page, or null to clear it.
         * @return The current Builder instance for method chaining.
         */
        public Builder inventory(@Nullable InventoryTemplate template) {
            this.inventoryTemplate = template;
            return this;
        }

        /**
         * Sets the onOpen behavior for the page. If the provided behaviour is null, it will clear any existing onOpen behavior.
         *
         * @param behaviour The PageAction to execute when the page is opened, or null to clear it.
         * @return The current Builder instance for method chaining.
         */
        public Builder onOpen(@Nullable Consumer<PageAction> behaviour) {
            this.onOpen = behaviour;
            return this;
        }

        /**
         * Sets the onOpen behavior for the page. If the provided behaviour is null, it will clear any existing onOpen behavior.
         *
         * @param behaviour The Runnable to execute when the page is opened, or null to clear it.
         * @return The current Builder instance for method chaining.
         */
        public Builder onOpen(@Nullable Runnable behaviour) {
            if (behaviour == null) {
                this.onOpen = null;
            } else {
                onOpen((action) -> behaviour.run());
            }
            return this;
        }

        /**
         * Sets the onClose behavior for the page. If the provided behaviour is null, it will clear any existing onClose behavior.
         *
         * @param behaviour The PageAction to execute when the page is closed, or null to clear it.
         * @return The current Builder instance for method chaining.
         */
        public Builder onClose(@Nullable Consumer<PageAction> behaviour) {
            this.onClose = behaviour;
            return this;
        }

        /**
         * Sets the onClose behavior for the page. If the provided behaviour is null, it will clear any existing onClose behavior.
         *
         * @param behaviour The PageAction to execute when the page is closed, or null to clear it.
         * @return The current Builder instance for method chaining.
         */
        public Builder onClose(@Nullable Runnable behaviour) {
            if (behaviour == null) {
                this.onClose = null;
            } else {
                onClose((action) -> behaviour.run());
            }
            return this;
        }

        /**
         * Builds and returns a new ServerPage instance based on the current state of the Builder.
         *
         * @return A new ServerPage instance.
         * @throws IllegalStateException if any required field is missing or invalid.
         */
        public ServerPage build() {
            validate();
            return new ServerPage(template, inventoryTemplate, title, onOpen, onClose);
        }

        /**
         * Validates the builder's state before building the ServerPage.
         * Ensures that the required fields are set and throws an exception if any required field is missing.
         *
         * @throws IllegalStateException if any required field is missing or invalid.
         */
        protected void validate() {
            if (template == null) {
                throw new IllegalStateException("template must be defined");
            }
        }

    }
}
