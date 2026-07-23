package dev.matthiesen.matthiesen_core.common.api.platform.registry;

import java.util.function.Supplier;

/**
 * Interface for a configurable registry that allows for the registration of objects with additional data. The init method
 * can be used to perform any necessary setup before registration, and the register method allows for the registration of
 * objects with a name and extra data provided as a Supplier.
 * @param <R> The type of objects being registered.
 */
@SuppressWarnings("unused")
public interface ConfigurableRegistry<R> {
    /**
     * Registers an object with the given name and extra data. The extra data is provided as a Supplier, allowing for lazy initialization if needed.
     * @param name The name of the object being registered.
     * @param entry A Supplier that provides additional data associated with the registered object. This allows for lazy initialization of the extra data if necessary.
     * @return A Supplier that provides the registered object. This allows for lazy retrieval of the registered object if needed.
     * @param <T> The type of the object being registered, which must be a subtype of R.
     */
    <T extends R> Supplier<T> register(String name, Supplier<T> entry);
}
