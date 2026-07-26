package dev.matthiesen.matthiesen_core.common.api.economy;

/**
 * Represents the built-in economy providers available in the system. Each enum constant corresponds to a specific built-in
 * economy provider, which can be used to manage in-game currency and transactions.
 */
public enum BuiltInEconomyProviders {
    /**
     * Represents the Item economy provider, which is a built-in provider that uses in-game items as currency.
     */
    ITEM("item"),

    /**
     * Represents the Impactor economy provider, which is an optional built-in provider that can be used if the Impactor mod is present in the environment.
     */
    IMPACTOR("impactor");

    private final String id;

    /**
     * Constructs a BuiltInEconomyProviders enum constant with the specified unique identifier.
     * @param id The unique identifier associated with this built-in economy provider.
     */
    BuiltInEconomyProviders(String id) {
        this.id = id;
    }

    /**
     * Retrieves the unique identifier for this built-in economy provider.
     * @return The unique identifier associated with this built-in economy provider.
     */
    public String getId() {
        return this.id;
    }
}
