/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.helpers;

import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.PlaceholderButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.LinkedPage;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that provides methods for creating and linking paginated menus in a server menu system.
 * This class is designed to work with templates containing placeholder buttons, allowing for dynamic generation of pages based on a list of buttons.
 */
@SuppressWarnings("unused")
public class PaginationHelper {
    /**
     * Links a list of LinkedPage instances together in order, setting the previous and next references for each page.
     *
     * @param pages The list of LinkedPage instances to link together.
     */
    public static void linkPagesTogether(@NotNull List<LinkedPage> pages) {
        for (int i = 0; i < pages.size(); i++) {
            if (i != 0) {
                pages.get(i).setPrevious(pages.get(i - 1));
            }
            if (i != pages.size() - 1) {
                pages.get(i).setNext(pages.get(i + 1));
            }
        }
    }

    /**
     * Creates a series of linked pages from a template containing placeholder buttons, replacing them with buttons from the provided list.
     * Each page will contain as many buttons as there are placeholders in the template, and the pages will be linked together in order.
     *
     * @param template    The template containing placeholder buttons to be replaced.
     * @param toReplace   The list of buttons to replace the placeholders with.
     * @param pageBuilder An optional builder for customizing the creation of each page. If null, a default builder will be used.
     * @return The first page in the linked series of pages created from the template and button list.
     * @throws IllegalStateException if no placeholders are defined in the supplied template.
     */
    public static LinkedPage createPagesFromPlaceholders(@NotNull Template template,
                                                         @NotNull List<Button> toReplace,
                                                         @Nullable LinkedPage.Builder pageBuilder) {
        // Get all the indexes of placeholder buttons.
        List<Integer> placeholderIndexes = new ArrayList<>();

        for (int i = 0; i < template.getSize(); i++) {
            if (template.getSlot(i).getButton().orElse(null) instanceof PlaceholderButton) {
                placeholderIndexes.add(i);
            }
        }

        if (placeholderIndexes.isEmpty()) {
            throw new IllegalStateException("no placeholders defined in supplied template");
        }

        // If page builder isn't specified, just create a default builder.
        LinkedPage.Builder builder = (pageBuilder != null) ? pageBuilder : LinkedPage.builder();

        List<LinkedPage> pages = new ArrayList<>();
        int currentIndex = 0; // Stores the index of the current button being replaced

        if (toReplace.isEmpty()) {
            return builder.template(template.clone()).build();
        }

        while (currentIndex < toReplace.size()) {
            // Clones the template, and replaces all placeholders with buttons from toReplace.
            Template replacement = template.clone();
            for (int targetIndex : placeholderIndexes) {
                if (currentIndex >= toReplace.size()) {
                    break;
                }
                replacement.getSlot(targetIndex).setButton(toReplace.get(currentIndex));
                currentIndex++;
            }
            pages.add(builder.template(replacement).build());
        }

        linkPagesTogether(pages);
        return pages.getFirst();
    }
}
