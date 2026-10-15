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

@SuppressWarnings("unused")
public class PaginationHelper {
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
