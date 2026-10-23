package dev.matthiesen.matthiesen_core.common.api.events.observable.results;

import dev.matthiesen.matthiesen_core.common.api.events.observable.EventPriority;
import dev.matthiesen.matthiesen_core.common.api.events.observable.EventSubscription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread-safe, priority-ordered observable for events that return a result of type {@code R}.
 *
 * <h2>Dispatch behavior</h2>
 * <ul>
 *   <li>Listeners are invoked in {@link EventPriority#HIGHEST} → {@link EventPriority#LOWEST} order.</li>
 *   <li>Within the same priority tier, listeners fire in the order they were registered.</li>
 *   <li>If any listener returns a non-null result, dispatch stops immediately and that result is returned to the caller.</li>
 *   <li>If all listeners return null, emit returns null.</li>
 *   <li>Exceptions thrown by listeners are logged and suppressed; dispatch continues to the next listener.</li>
 * </ul>
 *
 * <h2>Unsubscribe semantics</h2>
 * <p>Calling {@link EventSubscription#unsubscribe()} marks the listener for removal on the next emit cycle;
 * the current in-flight emit is not affected.</p>
 *
 * @param <T> the event type
 * @param <R> the result type
 */
@SuppressWarnings("unused")
public class ResultEventObservable<T, R> {
    private static final Logger LOGGER = LogManager.getLogger(ResultEventObservable.class);
    private final CopyOnWriteArrayList<Entry<T, R>> entries = new CopyOnWriteArrayList<>();
    private final AtomicLong sequenceCounter = new AtomicLong();

    /**
     * Creates a new {@code ResultEventObservable} instance.
     */
    public ResultEventObservable() {}

    /**
     * Returns the list of registered listener entries. This method is primarily intended for internal use and testing.
     *
     * @return the list of registered listener entries
     */
    public CopyOnWriteArrayList<Entry<T, R>> getEntries() {
        return entries;
    }

    /**
     * Removes the specified listener entries from the observable. This method is primarily intended for internal use and testing.
     *
     * @param dead the list of listener entries to remove
     */
    public void removeEntries(List<Entry<T, R>> dead) {
        entries.removeAll(dead);
    }

    /**
     * Subscribes a listener at {@link EventPriority#NORMAL} priority.
     *
     * @param listener the listener to register
     * @return an {@link EventSubscription} handle that can be used to remove the listener later
     */
    public EventSubscription subscribe(ResultListener<T, R> listener) {
        return subscribe(EventPriority.NORMAL, listener);
    }

    /**
     * Subscribes a listener at the specified priority.
     *
     * @param priority the priority at which to invoke the listener
     * @param listener the listener to register
     * @return an {@link EventSubscription} handle that can be used to remove the listener later
     */
    public EventSubscription subscribe(EventPriority priority, ResultListener<T, R> listener) {
        AtomicBoolean active = new AtomicBoolean(true);
        entries.add(new Entry<>(priority.getValue(), sequenceCounter.getAndIncrement(), listener, active));
        return EventSubscription.of(active);
    }

    /**
     * Emits an event to all registered listeners, returning the first non-null result.
     *
     * @param event the event to emit
     * @return the first non-null result returned by a listener, or null if all listeners returned null
     */
    public R emit(T event) {
        var entries = getEntries();
        List<Entry<T, R>> snapshot = new ArrayList<>(entries.size());
        List<Entry<T, R>> dead = new ArrayList<>();

        for (Entry<T, R> e : entries) {
            if (e.active().get()) {
                snapshot.add(e);
            } else {
                dead.add(e);
            }
        }

        if (!dead.isEmpty()) {
            removeEntries(dead);
        }

        snapshot.sort(Comparator.comparingInt((Entry<T, R> a) -> a.priority()).thenComparingLong(Entry::sequence));

        for (Entry<T, R> entry : snapshot) {
            try {
                R result = entry.listener().handle(event);
                if (result != null) {
                    return result;
                }
            } catch (Throwable t) {
                LOGGER.error("[PlatformEvents] Unhandled exception in result listener for event '{}': {}",
                        event.getClass().getSimpleName(), t.getMessage(), t);
            }
        }

        return null;
    }

    /**
     * A functional interface representing a listener that handles an event of type {@code T} and returns a result of type {@code R}.
     *
     * @param <T> the event type
     * @param <R> the result type
     */
    @FunctionalInterface
    public interface ResultListener<T, R> {
        /**
         * Handles the given event and returns a result.
         *
         * @param event the event to handle
         * @return a result of type {@code R}, or null if no result is produced
         */
        R handle(T event);
    }

    /**
     * A record representing a listener entry with its priority, sequence number, listener, and active state.
     */
    public record Entry<E, R>(int priority, long sequence, ResultListener<E, R> listener, AtomicBoolean active) {}
}
