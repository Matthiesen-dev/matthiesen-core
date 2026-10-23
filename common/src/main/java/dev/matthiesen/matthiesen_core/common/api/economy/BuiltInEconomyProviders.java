package dev.matthiesen.matthiesen_core.common.api.economy;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;

/**
 * Represents the built-in economy providers available in the system. Each enum constant corresponds to a specific built-in
 * economy provider, which can be used to manage in-game currency and transactions.
 */
public enum BuiltInEconomyProviders {
    /**
     * Represents the Item economy provider, which is a built-in provider that uses in-game items as currency.
     */
    ITEM("item", Type.VANILLA),

    /**
     * Represents the Impactor economy provider, which is an optional built-in provider that can be used if the Impactor mod is present in the environment.
     */
    IMPACTOR("impactor", Type.MOD);

    private final String id;
    private final Type type;

    /**
     * Constructs a BuiltInEconomyProviders enum constant with the specified unique identifier and type.
     * @param id The unique identifier associated with this built-in economy provider.
     * @param type The type of the built-in economy provider (VANILLA or MOD).
     */
    BuiltInEconomyProviders(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Retrieves the unique identifier for this built-in economy provider.
     * @return The unique identifier associated with this built-in economy provider.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Checks if this built-in economy provider is available for use. The availability is determined based on the type of the provider.
     * For VANILLA providers, it always returns true. For MOD providers, it checks if the corresponding mod is loaded in the environment.
     * @return true if the built-in economy provider is available; false otherwise.
     */
    public boolean isAvailable() {
        return switch (this.type) {
            case VANILLA -> true;
            case MOD -> MatthiesenCoreCommon.INSTANCE.getCommonUtils().isModLoaded(this.id);
        };
    }

    /**
     * Represents the type of the built-in economy provider (VANILLA or MOD).
     */
    public enum Type {
        VANILLA,
        MOD
    }
}
