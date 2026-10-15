/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button.linked;

import dev.matthiesen.matthiesen_core.common.api.server_menu.ServerUIManager;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ButtonAction;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.LinkedPage;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class LinkedPageButton extends ServerButton {

    private final LinkType linkType;

    protected LinkedPageButton(@NotNull ItemStack display,
                               @Nullable Consumer<ButtonAction> onClick,
                               @NotNull LinkType linkType) {
        super(display, onClick);
        this.linkType = linkType;
    }

    @Override
    public void onClick(@NotNull ButtonAction action) {
        super.onClick(action);
        if (action.getPage() instanceof LinkedPage linkedPage) {
            Page targetPage = (linkType == LinkType.Previous) ? linkedPage.getPrevious() : linkedPage.getNext();
            if (targetPage != null) {
                ServerUIManager.openUIForcefully(action.getPlayer(), targetPage);
            }
        }
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ServerButton.Builder {

        private LinkType linkType;

        @Override
        public Builder display(@NotNull ItemStack display) {
            super.display(display);
            return this;
        }

        @Override
        public Builder onClick(@Nullable Runnable behaviour) {
            super.onClick(behaviour);
            return this;
        }

        @Override
        public Builder onClick(@Nullable Consumer<ButtonAction> behaviour) {
            super.onClick(behaviour);
            return this;
        }

        public Builder linkType(@NotNull LinkType linkType) {
            this.linkType = linkType;
            return this;
        }

        @Override
        public <T> Builder with(DataComponentType<T> type, T value) {
            super.with(type, value);
            return this;
        }

        @Override
        public LinkedPageButton build() {
            validate();
            return new LinkedPageButton(this.display, onClick, linkType);
        }

        @Override
        protected void validate() {
            super.validate();
            if (linkType == null) {
                throw new IllegalStateException("link type must be defined!");
            }
        }
    }

}
