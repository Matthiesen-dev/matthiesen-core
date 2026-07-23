package dev.matthiesen.matthiesen_core.common.api.events;

/**
 * Defines the priority at which an event listener is dispatched relative to other listeners on the same
 * {@link EventObservable} or {@link ResultEventObservable}.
 *
 * <p>Listeners are invoked from {@link #HIGHEST} to {@link #LOWEST}. Within the same priority tier,
 * listeners are called in the order they were registered.</p>
 */
@SuppressWarnings("unused")
public enum EventPriority {

    /**
     * Dispatched first. Reserved for systems that must act before all others — for example,
     * security or permission checks.
     */
    HIGHEST(0),

    /** Dispatched before the default priority. Suitable for systems that need early access to an event. */
    HIGH(1),

    /** The default priority. Most mod listeners should use this. */
    NORMAL(2),

    /** Dispatched after the default priority. Useful for reactions that depend on normal-priority handlers. */
    LOW(3),

    /**
     * Dispatched last. Suitable for post-processing, logging, or cleanup that should happen after
     * everything else has had a chance to respond.
     */
    LOWEST(4);

    private final int value;

    EventPriority(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric ordering value. A lower value means higher dispatch priority (dispatched earlier).
     *
     * @return the ordering value
     */
    public int getValue() {
        return value;
    }
}

