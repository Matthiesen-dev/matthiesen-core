/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.page;

import dev.matthiesen.matthiesen_core.common.api.server_menu.data.Subject;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.InventoryTemplate;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a page in a server menu system. A page is associated with a specific template and can have a title.
 * It provides methods to handle actions when the page is opened or closed.
 */
public interface Page extends Subject<Page> {

    /**
     * Returns the Template associated with this page.
     *
     * @return The Template associated with this page.
     */
    Template getTemplate();

    /**
     * Returns an Optional containing the InventoryTemplate associated with this page, if any.
     * If the page does not have an InventoryTemplate, an empty Optional is returned.
     *
     * @return An Optional containing the InventoryTemplate, or an empty Optional if none exists.
     */
    default Optional<InventoryTemplate> getInventoryTemplate() {
        return Optional.empty();
    }

    /**
     * Returns the title of the page as a Component. This title is typically displayed at the top of the server menu when the page is opened.
     *
     * @return The title of the page as a Component.
     */
    Component getTitle();

    /**
     * Called when the page is opened. This method can be overridden to perform any necessary setup or actions when the page is opened.
     *
     * @param action The PageAction that triggered the opening of the page.
     */
    default void onOpen(@NotNull PageAction action) {
    }

    /**
     * Called when the page is closed. This method can be overridden to perform any necessary cleanup or actions when the page is closed.
     *
     * @param action The PageAction that triggered the closure of the page.
     */
    default void onClose(@NotNull PageAction action) {
    }
}
