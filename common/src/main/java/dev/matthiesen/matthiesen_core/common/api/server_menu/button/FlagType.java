/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

@SuppressWarnings("unused")
public enum FlagType
{
    /**
     * ENCHANTS only hides tool enchants, as book ones are classified as stored enchantments.
     * EXTRAS hides multiple things, according to the minecraft wiki (<a href="https://minecraft.fandom.com/wiki/Tutorials/Command_NBT_tags">...</a>)
     */
    Reforged(0),
    Generations(0),
    Enchantments(1),
    Attribute_Modifiers(2),
    Unbreakable(4),
    Can_Destroy(8),
    Can_Place_On(16),
    Extras(32),
    Dyed_Leather(64),
    All(127);

    private final int value;

    FlagType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return this.value;
    }
}
