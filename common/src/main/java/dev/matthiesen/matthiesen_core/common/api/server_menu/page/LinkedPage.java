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
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Represents a server page that can be opened and closed, with customizable behavior for these events.
 * This class extends PageBase and provides additional functionality for handling onOpen and onClose events.
 */
@SuppressWarnings("unused")
public class LinkedPage extends ServerPage {

    public static final String CURRENT_PAGE_PLACEHOLDER = "{current}";
    public static final String TOTAL_PAGES_PLACEHOLDER = "{total}";

    private LinkedPage previous;
    private LinkedPage next;

    /**
     * Constructs a new LinkedPage instance with the specified template, inventory template, title, event handlers, and linked pages.
     *
     * @param template          The template for this page. Must not be null.
     * @param inventoryTemplate The inventory template for this page. Can be null if no inventory is needed.
     * @param title             The title of the page as a Component. If null, an empty Component will be used.
     * @param onOpen            A PageAction to execute when the page is opened. Can be null if no action is needed.
     * @param onClose           A PageAction to execute when the page is closed. Can be null if no action is needed.
     * @param previous          The previous LinkedPage in the sequence. Can be null if this is the first page.
     * @param next              The next LinkedPage in the sequence. Can be null if this is the last page.
     */
    public LinkedPage(@NotNull Template template,
                      @Nullable InventoryTemplate inventoryTemplate,
                      @Nullable Component title,
                      @Nullable Consumer<PageAction> onOpen,
                      @Nullable Consumer<PageAction> onClose,
                      @Nullable LinkedPage previous,
                      @Nullable LinkedPage next) {
        super(template, inventoryTemplate, title, onOpen, onClose);
        this.previous = previous;
        this.next = next;
    }

    /**
     * Returns the previous page in the linked sequence.
     * If there is no previous page, this method returns null.
     * @return The previous LinkedPage, or null if there is no previous page.
     */
    public LinkedPage getPrevious() {
        return previous;
    }

    /**
     * Sets the previous page in the linked sequence and updates the current page's title to reflect the new total pages.
     *
     * @param previous The previous LinkedPage, or null if there is no previous page.
     */
    public void setPrevious(LinkedPage previous) {
        this.previous = previous;
        update();
    }

    /**
     * Returns the next page in the linked sequence.
     * If there is no next page, this method returns null.
     * @return The next LinkedPage, or null if there is no next page.
     */
    public LinkedPage getNext() {
        return next;
    }

    /**
     * Sets the next page in the linked sequence and updates the current page's title to reflect the new total pages.
     *
     * @param next The next LinkedPage, or null if there is no next page.
     */
    public void setNext(LinkedPage next) {
        this.next = next;
        update();
    }

    /**
     * Calculates and returns the current page number based on the previous pages.
     * If there is no previous page, the current page is considered to be 1.
     * @return The current page number.
     */
    public int getCurrentPage() {
        return (previous != null) ? previous.getCurrentPage() + 1 : 1;
    }

    /**
     * Calculates and returns the total number of pages in the linked sequence.
     * If there is no next page, the total number of pages is considered to be the current page number.
     * @return The total number of pages in the linked sequence.
     */
    public int getTotalPages() {
        return (next != null) ? next.getTotalPages() : getCurrentPage();
    }

    @Override
    public Component getTitle() {
        return replace(
                replace(super.getTitle(), Pattern.compile(CURRENT_PAGE_PLACEHOLDER, Pattern.LITERAL), "" + getCurrentPage()),
                Pattern.compile(TOTAL_PAGES_PLACEHOLDER, Pattern.LITERAL),
                "" + getTotalPages()
        );
    }

    @Override
    public Template getTemplate() {
        return super.getTemplate();
    }

    /**
     * Creates a new Builder instance for constructing a LinkedPage.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing instances of LinkedPage.
     * This class extends ServerPage.Builder and provides additional methods for setting previous and next pages.
     */
    public static class Builder extends ServerPage.Builder {

        protected LinkedPage previousPage;
        protected LinkedPage nextPage;

        @Override
        public Builder title(@Nullable String title) {
            super.title(title);
            return this;
        }

        @Override
        public Builder title(@Nullable Component title) {
            super.title(title);
            return this;
        }

        @Override
        public Builder template(@NotNull Template template) {
            super.template(template);
            return this;
        }

        @Override
        public Builder inventory(@Nullable InventoryTemplate template) {
            super.inventory(template);
            return this;
        }

        @Override
        public Builder onOpen(@Nullable Consumer<PageAction> behaviour) {
            super.onOpen(behaviour);
            return this;
        }

        @Override
        public Builder onOpen(@Nullable Runnable behaviour) {
            super.onOpen(behaviour);
            return this;
        }

        @Override
        public Builder onClose(@Nullable Consumer<PageAction> behaviour) {
            super.onClose(behaviour);
            return this;
        }

        @Override
        public Builder onClose(@Nullable Runnable behaviour) {
            super.onClose(behaviour);
            return this;
        }

        /**
         * Sets the next page for the LinkedPage being built.
         *
         * @param next The next LinkedPage, or null if there is no next page.
         * @return The current Builder instance for method chaining.
         */
        public Builder nextPage(@Nullable LinkedPage next) {
            this.nextPage = next;
            return this;
        }

        /**
         * Sets the previous page for the LinkedPage being built.
         *
         * @param previous The previous LinkedPage, or null if there is no previous page.
         * @return The current Builder instance for method chaining.
         */
        public Builder previousPage(@Nullable LinkedPage previous) {
            this.previousPage = previous;
            return this;
        }

        /**
         * Builds and returns a new LinkedPage instance based on the current state of the Builder.
         *
         * @return A new LinkedPage instance.
         */
        public LinkedPage build() {
            validate();
            return new LinkedPage(template, inventoryTemplate, title, onOpen, onClose, previousPage, nextPage);
        }

    }

    private Component replace(Component source, Pattern pattern, String replacement) {
        return this.replaceRecursive(Component.empty(), source, pattern, replacement);
    }

    private Component replaceRecursive(MutableComponent result, Component target, Pattern pattern, String replacement) {
        if (target instanceof MutableComponent mc) {
            ComponentContents contents = mc.getContents();
            if (contents instanceof PlainTextContents text) {
                if (!text.text().isEmpty()) {
                    String content = pattern.matcher(text.text()).replaceAll(replacement);
                    MutableComponent component = Component.literal(content).withStyle(target.getStyle());

                    for (Component child : target.getSiblings()) {
                        this.replaceRecursive(component, child, pattern, replacement);
                    }

                    result.append(component);
                } else {
                    MutableComponent empty = target.plainCopy().withStyle(target.getStyle());
                    for (Component child : target.getSiblings()) {
                        this.replaceRecursive(empty, child, pattern, replacement);
                    }

                    result.append(empty);
                }
            }

        } else {
            MutableComponent custom = target.plainCopy().withStyle(target.getStyle());
            for (Component child : target.getSiblings()) {
                this.replaceRecursive(custom, child, pattern, replacement);
            }

            result.append(custom);
        }

        return result;
    }
}
