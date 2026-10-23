package dev.matthiesen.matthiesen_core.common.api.events.observable;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.observable.results.InteractionResultEventObservable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * A thread-safe, priority-ordered observable for void (non-result) events.
 *
 * <h2>Dispatch behavior</h2>
 * <ul>
 *   <li>Listeners are invoked in {@link EventPriority#HIGHEST} → {@link EventPriority#LOWEST} order.</li>
 *   <li>Within the same priority tier, listeners fire in the order they were registered.</li>
 *   <li>If a listener throws any {@link Throwable}, the exception is logged and dispatch continues
 *       with the remaining listeners — one faulty mod cannot block others.</li>
 * </ul>
 *
 * <h2>Unsubscribe semantics</h2>
 * <p>Calling {@link EventObservable.Subscription#unsubscribe()} on the returned handle marks the listener as
 * inactive. The current in-flight emit is unaffected; the listener is removed before the next emit.</p>
 *
 * @param <T> the event type
 * @see InteractionResultEventObservable
 * @see PlatformEvents
 */
@SuppressWarnings("unused")
public final class EventObservable<T> {

    private static final Logger LOGGER = LogManager.getLogger(EventObservable.class);

    private final CopyOnWriteArrayList<Entry<T>> entries = new CopyOnWriteArrayList<>();
    private final AtomicLong sequenceCounter = new AtomicLong();

    /**
     * Creates a new {@code EventObservable} instance.
     */
    public EventObservable() {}

    /**
     * Subscribes a listener at {@link EventPriority#NORMAL} priority.
     *
     * @param listener the listener to register
     * @return an {@link EventObservable.Subscription} handle that can be used to remove the listener later
     */
    public Subscription subscribe(Consumer<T> listener) {
        return subscribe(EventPriority.NORMAL, listener);
    }

    /**
     * Subscribes a listener at the specified priority.
     *
     * @param priority the dispatch priority; lower enum ordinals fire first
     * @param listener the listener to register
     * @return an {@link EventObservable.Subscription} handle that can be used to remove the listener later
     */
    public Subscription subscribe(EventPriority priority, Consumer<T> listener) {
        AtomicBoolean active = new AtomicBoolean(true);
        entries.add(new Entry<>(priority.getValue(), sequenceCounter.getAndIncrement(), listener, active));
        return Subscription.of(active);
    }

    /**
     * Dispatches {@code event} to all currently active subscribers in priority and registration order.
     *
     * <p>Any listeners whose {@link EventObservable.Subscription} was unsubscribed before this call will be
     * removed from the internal list during this emit and will not be invoked.</p>
     *
     * @param event the event to dispatch; must not be {@code null}
     */
    public void emit(T event) {
        List<Entry<T>> snapshot = new ArrayList<>(entries.size());
        List<Entry<T>> dead = new ArrayList<>();

        for (Entry<T> e : entries) {
            if (e.active().get()) {
                snapshot.add(e);
            } else {
                dead.add(e);
            }
        }

        if (!dead.isEmpty()) {
            entries.removeAll(dead);
        }

        snapshot.sort(Comparator.comparingInt((Entry<T> a) -> a.priority()).thenComparingLong(Entry::sequence));

        for (Entry<T> entry : snapshot) {
            try {
                entry.listener().accept(event);
            } catch (Throwable t) {
                LOGGER.error("[PlatformEvents] Unhandled exception in listener for event '{}': {}",
                        event.getClass().getSimpleName(), t.getMessage(), t);
            }
        }
    }

    /**
     * An entry in the observable's listener list, containing the listener, its priority, and its active state.
     *
     * @param <E> the event type
     */
    public record Entry<E>(int priority, long sequence, Consumer<E> listener, AtomicBoolean active) {}

    /**
     * A handle to an event listener subscription that can be used to unsubscribe the listener.
     */
    public interface Subscription {
        /**
         * Unsubscribes the listener associated with this subscription. The listener will be removed
         * from the observable before the next emit cycle.
         */
        void unsubscribe();
        /**
         * Returns whether the listener associated with this subscription is still active.
         *
         * @return {@code true} if the listener is still active, {@code false} if it has been unsubscribed
         */
        boolean isSubscribed();

        /**
         * Creates a new {@link Subscription} instance that wraps the given {@link AtomicBoolean}.
         *
         * @param active the atomic boolean representing the subscription state
         * @return a new {@link Subscription} instance
         */
        static Subscription of(AtomicBoolean active) {
            return new Subscription() {
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
}

