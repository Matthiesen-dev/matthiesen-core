package dev.matthiesen.matthiesen_core.common.core.economy;

import dev.matthiesen.matthiesen_core.common.api.economy.EconomyProvider;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.economy.providers.ImpactorEconomyProvider;
import dev.matthiesen.matthiesen_core.common.core.economy.providers.ItemEconomyProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The EconomyManager class is a singleton responsible for managing the in-game economy, including currency providers, balance management,
 * deposits, withdrawals, and checking if a player has sufficient funds.
 * It allows for the registration and retrieval of different economy providers, enabling flexibility in how the in-game currency system is
 * implemented. This class should be initialized during the mod's initialization phase and provides methods to interact with the registered economy providers.
 */
@SuppressWarnings("unused")
public final class EconomyManager {
    /**
     * Retrieves the singleton instance of the EconomyManager. This instance is responsible for managing the in-game economy, including currency providers, balance management,
     * deposits, withdrawals, and checking if a player has sufficient funds.
     */
    public static final EconomyManager INSTANCE = new EconomyManager();

    private static final Map<String, EconomyProvider> providers = new ConcurrentHashMap<>();

    private boolean initialized;
    private MatthiesenCoreCommon modInstance;

    private EconomyManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initializes the EconomyManager with the provided mod instance. This method should be called once during the mod's initialization phase.
     * @param modInstance The instance of the mod that is initializing the EconomyManager.
     */
    public void initialize(MatthiesenCoreCommon modInstance) {
        if (initialized) return;
        initialized = true;
        this.modInstance = modInstance;
        modInstance.createInfoLog("Initializing Economy Manager");

        registerEconomyProvider(ItemEconomyProvider.INSTANCE);

        if (modInstance.getCommonUtils().isModLoaded("impactor")) {
            registerEconomyProvider(ImpactorEconomyProvider.INSTANCE);
        }
    }

    /**
     * Registers an economy provider with the EconomyManager. If a provider with the same ID is already registered, this method will not register it again.
     * @param provider The economy provider to register.
     */
    public void registerEconomyProvider(EconomyProvider provider) {
        String id = provider.providerId();
        if (isEconomyProviderRegistered(id)) return;
        provider.initialize();
        providers.put(id, provider);
    }

    /**
     * Retrieves the registered economy provider by its unique identifier.
     * @param id The unique identifier of the economy provider to retrieve.
     * @return The registered economy provider associated with the given ID, or null if no provider is registered with that ID.
     */
    public EconomyProvider getEconomyProvider(String id) {
        EconomyProvider provider = providers.get(id);
        if (provider == null) {
            modInstance.createErrorLog("Economy provider with ID '" + id + "' is not registered. Returning null.");
            return null;
        }
        return provider;
    }

    /**
     * Checks if an economy provider is registered with the given ID.
     * @param id The unique identifier of the economy provider to check.
     * @return true if the provider is registered, false otherwise.
     */
    public boolean isEconomyProviderRegistered(String id) {
        return providers.containsKey(id);
    }
}
