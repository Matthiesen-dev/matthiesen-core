package dev.matthiesen.matthiesen_core.common.api.events;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A handle returned when subscribing a listener to an {@link EventObservable} or
 * {@link ResultEventObservable}.
 *
 * <p>Calling {@link #unsubscribe()} marks the listener for removal. The listener will still
 * receive the current in-flight emit if it is already being dispatched, but will be excluded
 * from every subsequent emit cycle.</p>
 *
 * @see EventObservable#subscribe(java.util.function.Consumer)
 * @see ResultEventObservable#subscribe(ResultEventObservable.ResultListener)
 */
@SuppressWarnings("unused")
public interface EventSubscription {

    /**
     * Marks this subscription for removal. Takes effect on the next emit cycle.
     * Calling this method more than once has no additional effect.
     */
    void unsubscribe();

    /**
     * Returns whether this subscription is currently active (i.e. {@link #unsubscribe()} has
     * not yet been called).
     *
     * @return {@code true} if the listener is still subscribed
     */
    boolean isSubscribed();

    /**
     * Creates a simple {@code EventSubscription} backed by the provided {@link AtomicBoolean}.
     * Setting the boolean to {@code false} (via {@link #unsubscribe()}) will exclude this
     * listener from subsequent emit cycles.
     *
     * @param active the shared flag controlling whether the subscription is active
     * @return a new {@code EventSubscription} backed by {@code active}
     */
    static EventSubscription of(AtomicBoolean active) {
        return new EventSubscription() {
            @Override
            public void unsubscribe() {
                active.set(false);
            }

            @Override
            public boolean isSubscribed() {
                return active.get();
            }
        };
    }
}

