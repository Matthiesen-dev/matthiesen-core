/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.template;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Enum representing different types of templates for server menus.
 * Each template type is associated with a specific container type.
 */
public enum TemplateType {
    CHEST(template -> switch (template.getSize() / 9) {
        case 1 -> MenuType.GENERIC_9x1;
        case 2 -> MenuType.GENERIC_9x2;
        case 3 -> MenuType.GENERIC_9x3;
        case 4 -> MenuType.GENERIC_9x4;
        case 5 -> MenuType.GENERIC_9x5;
        default -> MenuType.GENERIC_9x6;
    }),
    FURNACE(template -> MenuType.FURNACE),
    BREWING_STAND(template -> MenuType.BREWING_STAND),
    HOPPER(template -> MenuType.HOPPER),
    DISPENSER(template -> MenuType.GENERIC_3x3),
    CRAFTING_TABLE(template -> MenuType.CRAFTING);

    private final Function<Template, MenuType<? extends AbstractContainerMenu>> containerTypeSupplier;

    /**
     * Constructs a TemplateType with the specified container type supplier.
     *
     * @param containerTypeSupplier A function that takes a Template and returns the corresponding MenuType.
     */
    TemplateType(@NotNull Function<Template, MenuType<? extends AbstractContainerMenu>> containerTypeSupplier) {
        this.containerTypeSupplier = containerTypeSupplier;
    }

    /**
     * Returns the MenuType associated with this TemplateType for the given Template.
     *
     * @param template The Template for which to get the MenuType.
     * @return The MenuType corresponding to this TemplateType and the provided Template.
     */
    public MenuType<? extends AbstractContainerMenu> getContainerType(@NotNull Template template) {
        return containerTypeSupplier.apply(template);
    }

}
