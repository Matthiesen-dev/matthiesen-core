/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.tasks;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a task that can be scheduled to run on the server's main thread, with support for delays, intervals, and iterations.
 * This class is designed to be used in conjunction with the UITaskManager, which handles the ticking of tasks.
 */
public final class UITask {

    private final Consumer<UITask> consumer;
    private final long interval;
    private final long iterations;

    private long currentIteration;
    private long ticksRemaining;
    private boolean isExpired;

    /**
     * Constructs a new UITask with the specified consumer, delay, interval, and iterations.
     * The task will execute the consumer after the specified delay, then continue executing at the specified interval for the given number of iterations.
     *
     * @param consumer   The consumer to execute when the task ticks.
     * @param delay      The initial delay before the task starts executing (in ticks).
     * @param interval   The interval between executions of the task (in ticks).
     * @param iterations The number of times to execute the task. A value of -1 indicates infinite iterations.
     */
    public UITask(Consumer<UITask> consumer, long delay, long interval, long iterations) {
        this.consumer = consumer;
        this.interval = interval;
        this.iterations = iterations;

        if (delay > 0) {
            ticksRemaining = delay;
        }
        UITaskManager.getInstance().register(this);
    }

    /**
     * Ticks this UITask, executing its consumer if the delay has elapsed and managing its iterations and expiration.
     * This method is called by the UITaskManager on each server tick.
     */
    void tick() {
        if (isExpired) return;

        this.ticksRemaining--;
        if (ticksRemaining > 0) return;

        consumer.accept(this);
        currentIteration++;

        if (interval > 0 && (currentIteration < iterations || iterations == -1 )) {
            this.ticksRemaining = interval;
        }
        else {
            this.isExpired = true;
        }
    }

    /**
     * Checks if this UITask has expired, meaning it will no longer be executed in future ticks.
     *
     * @return true if the task is expired, false otherwise.
     */
    public boolean isExpired() {
        return this.isExpired;
    }

    /**
     * Marks this UITask as expired, preventing it from being executed in future ticks.
     * Once a task is marked as expired, it will be removed from the UITaskManager's active task list on the next tick.
     */
    public void setExpired() {
        this.isExpired = true;
    }

    /**
     * Returns a new Builder instance for creating UITask objects with a fluent API.
     *
     * @return A new UITask.Builder instance.
     */
    public static UITask.Builder builder() {
        return new UITask.Builder();
    }

    /**
     * Builder class for creating UITask instances with a fluent API.
     */
    public final static class Builder {

        private Consumer<UITask> consumer;
        private long delay;
        private long interval;
        private long iterations = 1;

        /**
         * Sets the consumer to be executed when the task ticks. This method is a convenience method for setting a Runnable.
         *
         * @param runnable The Runnable to execute on each tick.
         * @return The current Builder instance for method chaining.
         */
        public Builder execute(@NotNull Runnable runnable) {
            this.consumer = (task) -> runnable.run();
            return this;
        }

        /**
         * Sets the consumer to be executed when the task ticks.
         *
         * @param consumer The consumer to execute on each tick.
         * @return The current Builder instance for method chaining.
         */
        public Builder execute(@NotNull Consumer<UITask> consumer) {
            this.consumer = consumer;
            return this;
        }

        /**
         * Sets the delay before the task starts executing. A value of 0 means no delay.
         *
         * @param delay The delay in ticks before the task starts executing.
         * @return The current Builder instance for method chaining.
         * @throws IllegalArgumentException if delay is less than 0.
         */
        @SuppressWarnings("unused")
        public Builder delay(long delay) {
            if (delay < 0) {
                throw new IllegalArgumentException("delay must not be below 0");
            }
            this.delay = delay;
            return this;
        }

        /**
         * Sets the interval between task executions. A value of 0 indicates that the task should execute every tick.
         *
         * @param interval The interval in ticks between executions.
         * @return The current Builder instance for method chaining.
         * @throws IllegalArgumentException if interval is less than 0.
         */
        public Builder interval(long interval) {
            if (interval < 0) {
                throw new IllegalArgumentException("interval must not be below 0");
            }
            this.interval = interval;
            return this;
        }

        /**
         * Sets the number of iterations for the task. A value of -1 indicates that the task should run indefinitely.
         *
         * @param iterations The number of iterations to run, or -1 for infinite iterations.
         * @return The current Builder instance for method chaining.
         * @throws IllegalArgumentException if iterations is less than -1.
         */
        public Builder iterations(long iterations) {
            if (iterations < -1) {
                throw new IllegalArgumentException("iterations must not be below -1");
            }
            this.iterations = iterations;
            return this;
        }

        /**
         * Sets the task to run indefinitely, ignoring the iterations count.
         *
         * @return The current Builder instance for method chaining.
         */
        public Builder infinite() {
            return iterations(-1);
        }

        /**
         * Builds and returns a new UITask instance based on the configured parameters.
         *
         * @return A new UITask instance.
         * @throws IllegalStateException if the consumer is not set.
         */
        public UITask build() {
            if (consumer == null) {
                throw new IllegalStateException("consumer must be set");
            }
            return new UITask(consumer, delay, interval, iterations);
        }

    }

}