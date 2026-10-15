/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public class ButtonAction {

    private final ServerPlayer player;
    private final ButtonClick clickType;
    private final Button button;
    private final Template template;
    private final Page page;
    private final int slot;

    public ButtonAction(@NotNull ServerPlayer player,
                        @NotNull ButtonClick clickType,
                        @NotNull Button button,
                        @NotNull Template template,
                        @NotNull Page page,
                        int slot) {
        this.player = Objects.requireNonNull(player);
        this.clickType = Objects.requireNonNull(clickType);
        this.button = Objects.requireNonNull(button);
        this.template = Objects.requireNonNull(template);
        this.page = Objects.requireNonNull(page);
        this.slot = slot;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public ButtonClick getClickType() {
        return clickType;
    }

    public Button getButton() {
        return button;
    }

    public Template getTemplate() {
        return template;
    }

    public Page getPage() {
        return page;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isSlotInInventory() {
        return page.getInventoryTemplate().isPresent() && slot >= template.getSize();
    }

    public Optional<Integer> getInventorySlot() {
        if (isSlotInInventory()) {
            return Optional.of(slot - template.getSize());
        } else {
            return Optional.empty();
        }
    }

}
