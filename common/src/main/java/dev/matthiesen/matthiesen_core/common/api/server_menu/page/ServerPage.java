/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.page;

import dev.matthiesen.matthiesen_core.common.api.server_menu.template.Template;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.InventoryTemplate;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ServerPage extends PageBase {

    private final Consumer<PageAction> onOpen, onClose;

    public ServerPage(@NotNull Template template,
                     @Nullable InventoryTemplate inventoryTemplate,
                     @Nullable Component title,
                     @Nullable Consumer<PageAction> onOpen,
                     @Nullable Consumer<PageAction> onClose) {
        super(template, inventoryTemplate, title);
        this.onOpen = onOpen;
        this.onClose = onClose;
    }

    @Override
    public void onOpen(@NotNull PageAction action) {
        if (onOpen != null) onOpen.accept(action);
    }

    @Override
    public void onClose(@NotNull PageAction action) {
        if (onClose != null) onClose.accept(action);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        protected Component title;
        protected Template template;
        protected InventoryTemplate inventoryTemplate;
        protected Consumer<PageAction> onOpen, onClose;

        public Builder title(@Nullable String title) {
            if(title == null) {
                return this;
            }

            return this.title(Component.literal(title));
        }

        public Builder title(@Nullable Component title) {
            this.title = title;
            return this;
        }

        public Builder template(@NotNull Template template) {
            if (template instanceof InventoryTemplate) {
                throw new IllegalArgumentException("you can not use an inventory template here!");
            }

            this.template = template;
            return this;
        }

        public Builder inventory(@Nullable InventoryTemplate template) {
            this.inventoryTemplate = template;
            return this;
        }

        public Builder onOpen(@Nullable Consumer<PageAction> behaviour) {
            this.onOpen = behaviour;
            return this;
        }

        public Builder onOpen(@Nullable Runnable behaviour) {
            if (behaviour == null) {
                this.onOpen = null;
            } else {
                onOpen((action) -> behaviour.run());
            }
            return this;
        }

        public Builder onClose(@Nullable Consumer<PageAction> behaviour) {
            this.onClose = behaviour;
            return this;
        }

        public Builder onClose(@Nullable Runnable behaviour) {
            if (behaviour == null) {
                this.onClose = null;
            } else {
                onClose((action) -> behaviour.run());
            }
            return this;
        }

        public ServerPage build() {
            validate();
            return new ServerPage(template, inventoryTemplate, title, onOpen, onClose);
        }

        protected void validate() {
            if (template == null) {
                throw new IllegalStateException("template must be defined");
            }
        }

    }
}
