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

public interface Page extends Subject<Page> {

    Template getTemplate();

    default Optional<InventoryTemplate> getInventoryTemplate() {
        return Optional.empty();
    }

    Component getTitle();

    default void onOpen(@NotNull PageAction action) {
    }

    default void onClose(@NotNull PageAction action) {
    }
}
