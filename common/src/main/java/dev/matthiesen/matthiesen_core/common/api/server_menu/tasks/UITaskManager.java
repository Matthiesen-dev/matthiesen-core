/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class UITaskManager {

    private static UITaskManager INSTANCE;
    private final List<UITask> tasks = new ArrayList<>();
    private final Queue<UITask> pending = new ConcurrentLinkedQueue<>();

    void register(UITask task) {
        if (task == null) return;
        this.pending.add(task);
    }

    public void tick() {
        UITask newTask;
        while ((newTask = pending.poll()) != null) {
            tasks.add(newTask);
        }

        var iterator = tasks.iterator();
        while (iterator.hasNext()) {
            var task = iterator.next();
            if (task == null) {
                iterator.remove();
                continue;
            }

            task.tick();

            if (task.isExpired()) {
                iterator.remove();
            }
        }
    }

    public static UITaskManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UITaskManager();
        }
        return INSTANCE;
    }

}
