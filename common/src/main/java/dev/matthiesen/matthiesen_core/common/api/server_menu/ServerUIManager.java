/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu;

import dev.matthiesen.matthiesen_core.common.api.server_menu.container.ServerContainer;
import dev.matthiesen.matthiesen_core.common.api.server_menu.container.VirtualBookContainer;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.tasks.UITask;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.mixins.server.ServerPlayerAccessor;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The ServerUIManager class provides methods to manage the opening and closing of user interfaces (UIs) for players on the server.
 * It ensures that UIs are opened only when players are not in a container, or forcefully opens them if necessary.
 */
@SuppressWarnings("unused")
public class ServerUIManager {
    /**
     * Opens the UI for the given player, but only if they are not already in a container.
     * If the player is in a container, it will wait until they are not, or until the timeout is reached.
     *
     * @param player      The player for whom the UI should be opened.
     * @param page        The page to be displayed in the UI.
     * @param timeout     The maximum time to wait for the player to be out of a container.
     * @param timeoutUnit The unit of time for the timeout.
     */
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

    /**
     * Opens the UI for the given player, regardless of their current container state.
     *
     * @param player The player for whom the UI should be opened.
     * @param book   The virtual book to be displayed in the UI.
     * @param timeout The maximum time to wait for the player to be out of a container.
     * @param timeoutUnit The unit of time for the timeout.
     */
    public static void openUIPassively(@NotNull ServerPlayer player, @NotNull VirtualBookContainer book, long timeout, TimeUnit timeoutUnit) {
        AtomicLong timeOutTicks = new AtomicLong(timeoutUnit.convert(timeout, TimeUnit.SECONDS) * 20);
        UITask.builder()
                .execute((task) -> {
                    timeOutTicks.getAndDecrement();
                    if (player.containerMenu.containerId == ((ServerPlayerAccessor) player).matthiesenCore$getContainerCounter() || timeOutTicks.get() <= 0) {
                        openUIForcefully(player, book);
                        task.setExpired();
                    }
                })
                .infinite()
                .interval(1)
                .build();
    }

    /**
     * Opens the UI for the given player, regardless of their current container state.
     *
     * @param player The player for whom the UI should be opened.
     * @param page   The page to be displayed in the UI.
     */
    public static void openUIForcefully(@NotNull ServerPlayer player, @NotNull Page page) {
        // Delay the open to allow sponge's annoying mixins to process previous container and not have aneurysm
        UITask.builder()
                .execute(() -> {
                    try {
                        ServerContainer container = new ServerContainer(player, page);
                        container.open();
                    } catch (Exception e) {
                        MatthiesenCoreCommon.INSTANCE.createErrorLog(e.getMessage(), e);
                    }
                })
                .build();
    }

    /**
     * Opens the UI for the given player, regardless of their current container state.
     *
     * @param player The player for whom the UI should be opened.
     * @param book   The virtual book to be displayed in the UI.
     */
    public static void openUIForcefully(@NotNull ServerPlayer player, @NotNull VirtualBookContainer book) {
        // Delay the open to allow sponge's annoying mixins to process previous container and not have aneurysm
        UITask.builder()
                .execute(() -> {
                    try {
                        book.openBookForPlayer(player);
                    } catch (Exception e) {
                        MatthiesenCoreCommon.INSTANCE.createErrorLog(e.getMessage(), e);
                    }
                })
                .build();
    }

    /**
     * Closes the UI for the given player.
     *
     * @param player The player for whom the UI should be closed.
     */
    public static void closeUI(@NotNull ServerPlayer player) {
        UITask.builder().execute(player::closeContainer).build();
    }
}
