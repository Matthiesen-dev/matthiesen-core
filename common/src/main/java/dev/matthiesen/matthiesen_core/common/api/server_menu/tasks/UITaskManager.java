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

/**
 * Manages UITasks, allowing for their registration and ticking. This class is a singleton and should be accessed via the getInstance() method.
 * It maintains a list of active tasks and a queue of pending tasks to be processed on each tick.
 */
public final class UITaskManager {

    private static UITaskManager INSTANCE;
    private final List<UITask> tasks = new ArrayList<>();
    private final Queue<UITask> pending = new ConcurrentLinkedQueue<>();

    /**
     * Registers a new UITask with the manager. The task will be added to the list of active tasks and will be processed on the next tick.
     *
     * @param task The UITask to register.
     */
    void register(UITask task) {
        if (task == null) return;
        this.pending.add(task);
    }

    /**
     * Ticks the UITaskManager, processing all registered tasks. This method should be called once per server tick.
     * It processes any pending tasks and ticks each active task, removing expired tasks from the list.
     */
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

    /**
     * Returns the singleton instance of UITaskManager. If the instance does not exist, it will be created.
     *
     * @return The singleton instance of UITaskManager.
     */
    public static UITaskManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UITaskManager();
        }
        return INSTANCE;
    }

}
