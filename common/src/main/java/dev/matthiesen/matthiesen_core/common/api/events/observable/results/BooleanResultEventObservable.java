package dev.matthiesen.matthiesen_core.common.api.events.observable.results;

import dev.matthiesen.matthiesen_core.common.api.events.observable.EventObservable;
import dev.matthiesen.matthiesen_core.common.api.events.observable.EventPriority;
import dev.matthiesen.matthiesen_core.common.api.events.observable.EventSubscription;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A thread-safe, priority-ordered observable for events that return a cancellation flag.
 *
 * <h2>Dispatch behavior</h2>
 * <ul>
 *   <li>Listeners are invoked in {@link EventPriority#HIGHEST} -> {@link EventPriority#LOWEST} order.</li>
 *   <li>Within the same priority tier, listeners fire in the order they were registered.</li>
 *   <li>If any listener returns {@code true}, dispatch stops immediately and the emit call returns {@code true}.</li>
 *   <li>If no listener returns {@code true}, emit returns {@code false}.</li>
 *   <li>Exceptions thrown by listeners are logged and suppressed; dispatch continues to the next listener.</li>
 * </ul>
 *
 * <h2>Unsubscribe semantics</h2>
 * <p>Calling {@link EventSubscription#unsubscribe()} marks the listener for removal on the next emit cycle;
 * the current in-flight emit is not affected.</p>
 *
 * @param <T> the event type
 * @see EventObservable
 * @see PlatformEvents
 */
@SuppressWarnings("unused")
public final class BooleanResultEventObservable<T> extends ResultEventObservable<T, Boolean> {
    private static final Logger LOGGER = LogManager.getLogger(BooleanResultEventObservable.class);

    /**
     * Creates a new {@code BooleanResultEventObservable} instance.
     */
    public BooleanResultEventObservable() {}

    // -------------------------------------------------------------------------
    // Emit
    // -------------------------------------------------------------------------

    /**
     * Dispatches {@code event} to active subscribers in priority and registration order.
     *
     * @param event the event to dispatch; must not be {@code null}
     * @return {@code true} if the event was cancelled, otherwise {@code false}
     */
    @Override
    public Boolean emit(T event) {
        var entries = getEntries();
        List<Entry<T, Boolean>> snapshot = new ArrayList<>(entries.size());
        List<Entry<T, Boolean>> dead = new ArrayList<>();

        for (Entry<T, Boolean> e : entries) {
            if (e.active().get()) {
                snapshot.add(e);
            } else {
                dead.add(e);
            }
        }

        if (!dead.isEmpty()) {
            removeEntries(dead);
        }

        snapshot.sort(Comparator.comparingInt((Entry<T, Boolean> a) -> a.priority()).thenComparingLong(Entry::sequence));

        for (Entry<T, Boolean> entry : snapshot) {
            try {
                if (entry.listener().handle(event)) {
                    return true;
                }
            } catch (Throwable t) {
                LOGGER.error("[PlatformEvents] Unhandled exception in result listener for event '{}': {}",
                        event.getClass().getSimpleName(), t.getMessage(), t);
            }
        }

        return false;
    }
}

