/**
 * Code adapted from "GooeyLibs" by landonjw
 * Original Repository: https://github.com/NickImpact/GooeyLibs
 * Licensed under GNU Lesser General Public License v3.0
 * Copyright (c) 2024 landonjw
 * Modified Copyright (c) 2026 Adam Matthiesen (Matthiesen-dev)
 */
package dev.matthiesen.matthiesen_core.common.api.server_menu.button;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public class RateLimitedButton extends ButtonBase {

    private final Button button;
    private final int limit;
    private final long timeInterval;
    private final TimeUnit timeUnit;

    private final LinkedList<Instant> lastActionTimes = new LinkedList<>();

    protected RateLimitedButton(@NotNull Button button, int limit, long timeInterval, @NotNull TimeUnit timeUnit) {
        super(button.getDisplay());
        this.button = button;
        this.limit = limit;
        this.timeInterval = timeInterval;
        this.timeUnit = timeUnit;
    }

    @Override
    public void onClick(@NotNull ButtonAction action) {
        if (isRateLimited()) return;

        if (isTopElementAboveTimeThreshold()) lastActionTimes.remove();
        button.onClick(action);
        lastActionTimes.add(Instant.now());
    }

    public boolean isRateLimited() {
        if (lastActionTimes.size() < limit) return false;
        return !isTopElementAboveTimeThreshold();
    }

    public int getAllowedRemainingClicks() {
        AtomicInteger allowed = new AtomicInteger(limit);
        lastActionTimes.forEach(instant -> {
            if (Duration.between(instant, Instant.now()).toMillis() <= timeUnit.toMillis(timeInterval)) {
                allowed.getAndDecrement();
            }
        });
        return allowed.get();
    }

    private boolean isTopElementAboveTimeThreshold() {
        if (lastActionTimes.isEmpty()) return false;
        Instant earliestActionTime = lastActionTimes.peek();
        return Duration.between(earliestActionTime, Instant.now()).toMillis() > timeUnit.toMillis(timeInterval);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Button button;
        private int limit;
        private long timeInterval;
        private TimeUnit timeUnit;

        public Builder button(Button button) {
            this.button = button;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder interval(long time, @NotNull TimeUnit timeUnit) {
            this.timeInterval = time;
            this.timeUnit = timeUnit;
            return this;
        }

        public RateLimitedButton build() {
            return new RateLimitedButton(button, limit, timeInterval, timeUnit);
        }

    }

}
