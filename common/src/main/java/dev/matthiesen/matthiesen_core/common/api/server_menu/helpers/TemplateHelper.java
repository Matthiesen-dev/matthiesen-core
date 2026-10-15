/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.helpers;

import dev.matthiesen.matthiesen_core.common.api.server_menu.template.slot.TemplateSlotDelegate;

public class TemplateHelper {

    public static TemplateSlotDelegate[] slotsOf(int size) {
        TemplateSlotDelegate[] elements = new TemplateSlotDelegate[size];
        for (int i = 0; i < size; i++) {
            elements[i] = new TemplateSlotDelegate(null, i);
        }
        return elements;
    }

}
