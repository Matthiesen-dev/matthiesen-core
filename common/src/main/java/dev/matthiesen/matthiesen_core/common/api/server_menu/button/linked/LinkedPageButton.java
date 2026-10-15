/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button.linked;

import dev.matthiesen.matthiesen_core.common.api.server_menu.ServerUIManager;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ButtonAction;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.LinkedPage;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a button that links to another page in a server menu, either the previous or next page.
 * This button is designed to be used within a LinkedPage context, allowing navigation between linked pages.
 */
@SuppressWarnings("unused")
public class LinkedPageButton extends ServerButton {

    private final LinkType linkType;

    /**
     * Constructs a new LinkedPageButton with the specified display item stack, onClick behavior, and link type.
     *
     * @param display   The ItemStack to be used as the button's display.
     * @param onClick   A Consumer<ButtonAction> that defines the behavior when the button is clicked. Can be null for no action.
     * @param linkType  The LinkType indicating whether this button links to the previous or next page.
     */
    protected LinkedPageButton(@NotNull ItemStack display,
                               @Nullable Consumer<ButtonAction> onClick,
                               @NotNull LinkType linkType) {
        super(display, onClick);
        this.linkType = linkType;
    }

    @Override
    public void onClick(@NotNull ButtonAction action) {
        super.onClick(action);
        if (action.getPage() instanceof LinkedPage linkedPage) {
            Page targetPage = (linkType == LinkType.Previous) ? linkedPage.getPrevious() : linkedPage.getNext();
            if (targetPage != null) {
                ServerUIManager.openUIForcefully(action.getPlayer(), targetPage);
            }
        }
    }

    /**
     * Returns the link type of this LinkedPageButton, indicating whether it links to the previous or next page.
     *
     * @return The LinkType of this button.
     */
    public LinkType getLinkType() {
        return linkType;
    }

    /**
     * Creates a new Builder instance for constructing a LinkedPageButton.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing instances of LinkedPageButton.
     * This builder allows for a fluent API to set properties and create a LinkedPageButton.
     */
    public static class Builder extends ServerButton.Builder {

        private LinkType linkType;

        @Override
        public Builder display(@NotNull ItemStack display) {
            super.display(display);
            return this;
        }

        @Override
        public Builder onClick(@Nullable Runnable behaviour) {
            super.onClick(behaviour);
            return this;
        }

        @Override
        public Builder onClick(@Nullable Consumer<ButtonAction> behaviour) {
            super.onClick(behaviour);
            return this;
        }

        /**
         * Sets the link type for the LinkedPageButton being built.
         *
         * @param linkType The LinkType to set (Previous or Next).
         * @return The current Builder instance for method chaining.
         */
        public Builder linkType(@NotNull LinkType linkType) {
            this.linkType = linkType;
            return this;
        }

        /**
         * Builds and returns a new LinkedPageButton instance based on the current state of the Builder.
         * Validates the required fields before creating the LinkedPageButton.
         *
         * @return A new LinkedPageButton instance.
         * @throws IllegalStateException if any required field is missing or invalid.
         */
        @Override
        public <T> Builder with(DataComponentType<T> type, T value) {
            super.with(type, value);
            return this;
        }

        /**
         * Builds and returns a new LinkedPageButton instance based on the current state of the Builder.
         * Validates the required fields before creating the LinkedPageButton.
         *
         * @return A new LinkedPageButton instance.
         * @throws IllegalStateException if any required field is missing or invalid.
         */
        @Override
        public LinkedPageButton build() {
            validate();
            return new LinkedPageButton(this.display, onClick, linkType);
        }

        /**
         * Validates the builder's state before building the LinkedPageButton.
         * Ensures that the required fields are set and throws an exception if any required field is missing.
         *
         * @throws IllegalStateException if any required field is missing or invalid.
         */
        @Override
        protected void validate() {
            super.validate();
            if (linkType == null) {
                throw new IllegalStateException("link type must be defined!");
            }
        }
    }

}
