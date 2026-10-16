/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.helpers;

import dev.matthiesen.matthiesen_core.common.api.server_menu.template.slot.TemplateSlotDelegate;

/**
 * A utility class that provides helper methods for working with server menu templates.
 * This class includes methods for creating arrays of TemplateSlotDelegate objects, which are used to represent slots in various server menu templates.
 */
public class TemplateHelper {

    /**
     * Creates an array of TemplateSlotDelegate objects of the specified size, initializing each slot with a null button and its corresponding index.
     *
     * @param size The number of slots to create.
     * @return An array of TemplateSlotDelegate objects.
     */
    public static TemplateSlotDelegate[] slotsOf(int size) {
        TemplateSlotDelegate[] elements = new TemplateSlotDelegate[size];
        for (int i = 0; i < size; i++) {
            elements[i] = new TemplateSlotDelegate(null, i);
        }
        return elements;
    }
}
