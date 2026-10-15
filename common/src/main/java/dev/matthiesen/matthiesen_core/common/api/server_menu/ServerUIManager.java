/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu;

import dev.matthiesen.matthiesen_core.common.api.server_menu.container.ServerContainer;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.tasks.UITask;
import dev.matthiesen.matthiesen_core.common.mixins.server.ServerPlayerAccessor;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class ServerUIManager {
    public static void openUIPassively(@NotNull ServerPlayer player, @NotNull Page page, long timeout, TimeUnit timeoutUnit) {
        AtomicLong timeOutTicks = new AtomicLong(timeoutUnit.convert(timeout, TimeUnit.SECONDS) * 20);
        UITask.builder()
                .execute((task) -> {
                    timeOutTicks.getAndDecrement();
                    if (player.containerMenu.containerId == ((ServerPlayerAccessor) player).matthiesenCore$getContainerCounter() || timeOutTicks.get() <= 0) {
                        openUIForcefully(player, page);
                        task.setExpired();
                    }
                })
                .infinite()
                .interval(1)
                .build();
    }

    public static void openUIForcefully(@NotNull ServerPlayer player, @NotNull Page page) {
        // Delay the open to allow sponge's annoying mixins to process previous container and not have aneurysm
        UITask.builder()
                .execute(() -> {
                    try {
                        ServerContainer container = new ServerContainer(player, page);
                        container.open();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                })
                .build();
    }

    public static void closeUI(@NotNull ServerPlayer player) {
        UITask.builder().execute(player::closeContainer).build();
    }
}
