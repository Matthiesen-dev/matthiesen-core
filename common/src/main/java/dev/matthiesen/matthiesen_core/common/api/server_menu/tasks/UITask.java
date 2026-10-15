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

public final class UITask {

    private final Consumer<UITask> consumer;
    private final long interval;
    private final long iterations;

    private long currentIteration;
    private long ticksRemaining;
    private boolean isExpired;

    public UITask(Consumer<UITask> consumer, long delay, long interval, long iterations) {
        this.consumer = consumer;
        this.interval = interval;
        this.iterations = iterations;

        if (delay > 0) {
            ticksRemaining = delay;
        }
        UITaskManager.getInstance().register(this);
    }

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

    public boolean isExpired() {
        return this.isExpired;
    }

    public void setExpired() {
        this.isExpired = true;
    }

    public static UITask.Builder builder() {
        return new UITask.Builder();
    }

    public final static class Builder {

        private Consumer<UITask> consumer;
        private long delay;
        private long interval;
        private long iterations = 1;

        public Builder execute(@NotNull Runnable runnable) {
            this.consumer = (task) -> runnable.run();
            return this;
        }

        public Builder execute(@NotNull Consumer<UITask> consumer) {
            this.consumer = consumer;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder delay(long delay) {
            if (delay < 0) {
                throw new IllegalArgumentException("delay must not be below 0");
            }
            this.delay = delay;
            return this;
        }

        public Builder interval(long interval) {
            if (interval < 0) {
                throw new IllegalArgumentException("interval must not be below 0");
            }
            this.interval = interval;
            return this;
        }

        public Builder iterations(long iterations) {
            if (iterations < -1) {
                throw new IllegalArgumentException("iterations must not be below -1");
            }
            this.iterations = iterations;
            return this;
        }

        public Builder infinite() {
            return iterations(-1);
        }

        public UITask build() {
            if (consumer == null) {
                throw new IllegalStateException("consumer must be set");
            }
            return new UITask(consumer, delay, interval, iterations);
        }

    }

}