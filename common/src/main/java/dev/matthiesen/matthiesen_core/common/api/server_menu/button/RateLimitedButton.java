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

/**
 * A RateLimitedButton is a wrapper around another Button that enforces a rate limit on the number of clicks allowed within a specified time interval.
 * This class is useful for preventing excessive clicking and ensuring that button actions are not triggered too frequently.
 */
@SuppressWarnings("unused")
public class RateLimitedButton extends ButtonBase {

    private final Button button;
    private final int limit;
    private final long timeInterval;
    private final TimeUnit timeUnit;

    private final LinkedList<Instant> lastActionTimes = new LinkedList<>();

    /**
     * Constructs a new RateLimitedButton that wraps the given button and enforces a rate limit on clicks.
     *
     * @param button       The button to be rate-limited.
     * @param limit        The maximum number of allowed clicks within the specified time interval.
     * @param timeInterval The time interval for the rate limit.
     * @param timeUnit     The unit of time for the interval.
     */
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

    /**
     * Checks if the button is currently rate-limited based on the defined limit and time interval.
     *
     * @return true if the button is rate-limited, false otherwise.
     */
    public boolean isRateLimited() {
        if (lastActionTimes.size() < limit) return false;
        return !isTopElementAboveTimeThreshold();
    }

    /**
     * Returns the number of remaining allowed clicks within the specified time interval.
     *
     * @return The number of remaining allowed clicks.
     */
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

    /**
     * Returns the underlying button that is being rate-limited.
     *
     * @return The wrapped Button instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing instances of RateLimitedButton.
     * This class provides a fluent API for setting various properties of the RateLimitedButton.
     */
    public static class Builder {

        private Button button;
        private int limit;
        private long timeInterval;
        private TimeUnit timeUnit;

        /**
         * Sets the button to be rate-limited.
         *
         * @param button The button to be rate-limited.
         * @return The current Builder instance for chaining.
         */
        public Builder button(Button button) {
            this.button = button;
            return this;
        }

        /**
         * Sets the maximum number of allowed clicks within the specified time interval.
         *
         * @param limit The maximum number of clicks allowed.
         * @return The current Builder instance for chaining.
         */
        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Sets the time interval for the rate limit.
         *
         * @param time     The time interval value.
         * @param timeUnit The unit of time for the interval.
         * @return The current Builder instance for chaining.
         */
        public Builder interval(long time, @NotNull TimeUnit timeUnit) {
            this.timeInterval = time;
            this.timeUnit = timeUnit;
            return this;
        }

        /**
         * Builds a new RateLimitedButton instance with the specified parameters.
         *
         * @return A new RateLimitedButton instance.
         */
        public RateLimitedButton build() {
            return new RateLimitedButton(button, limit, timeInterval, timeUnit);
        }

    }

}
