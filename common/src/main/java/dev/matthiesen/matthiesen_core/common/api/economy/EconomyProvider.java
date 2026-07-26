package dev.matthiesen.matthiesen_core.common.api.economy;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.utility.player_data.ServerUser;
import net.minecraft.server.level.ServerPlayer;

/**
 * This interface defines the contract for an economy provider in the mod. An economy provider is responsible for managing
 * the in-game currency system, including balance retrieval, deposits, withdrawals, and checking if a player has sufficient funds.
 * Implementations of this interface should provide the necessary logic to interact with the underlying economy system, whether
 * it's a custom currency, an external economy mod, or any other form of in-game currency management.
 */
@SuppressWarnings("unused")
public interface EconomyProvider {

    /**
     * Initializes the economy provider. This method should be called during the mod's initialization phase to set up any necessary configurations or connections.
     */
    default void initialize() {
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Registered economy provider " + providerDisplayName() + " with ID: " + providerId());
    }

    /**
     * Returns the unique identifier for the currency used by this economy provider.
     * @return A string representing the provider ID.
     */
    String providerId();

    /**
     * Returns the display name of the currency used by this economy provider.
     * @return A string representing the provider display name.
     */
    String providerDisplayName();

    // ==========================================================
    // Balance
    // ==========================================================

    /**
     * Returns the balance of the specified currency for the given player.
     * @param player The player whose balance is being queried.
     * @param currency The currency ID for which the balance is being queried.
     * @return The balance of the specified currency for the player.
     */
    int getBalance(ServerUser player, String currency) throws IllegalArgumentException;

    /**
     * Returns the balance of the specified currency for the given player.
     * @param player The player whose balance is being queried.
     * @param currency The currency ID for which the balance is being queried.
     * @return The balance of the specified currency for the player.
     */
    default int getBalance(ServerPlayer player, String currency) throws IllegalArgumentException {
        return getBalance(new ServerUser(player), currency);
    }

    // ==========================================================
    // Deposit
    // ==========================================================

    /**
     * Deposits the specified amount of the given currency into the player's account.
     * @param player The player to whom the currency will be deposited.
     * @param amount The amount of currency to deposit.
     * @param currency The currency ID of the currency to deposit.
     */
    void deposit(ServerUser player, int amount, String currency) throws IllegalArgumentException;

    /**
     * Deposits the specified amount of the given currency into the player's account.
     * @param player The player to whom the currency will be deposited.
     * @param amount The amount of currency to deposit.
     * @param currency The currency ID of the currency to deposit.
     */
    default void deposit(ServerPlayer player, int amount, String currency) throws IllegalArgumentException {
        deposit(new ServerUser(player), amount, currency);
    }

    // ==========================================================
    // Withdraw
    // ==========================================================

    /**
     * Withdraws the specified amount of the given currency from the player's account.
     * @param player The player from whom the currency will be withdrawn.
     * @param amount The amount of currency to withdraw.
     * @param currency The currency ID of the currency to withdraw.
     * @return True if the withdrawal was successful, false otherwise.
     */
    boolean withdraw(ServerUser player, int amount, String currency) throws IllegalArgumentException;

    /**
     * Withdraws the specified amount of the given currency from the player's account.
     * @param player The player from whom the currency will be withdrawn.
     * @param amount The amount of currency to withdraw.
     * @param currency The currency ID of the currency to withdraw.
     * @return True if the withdrawal was successful, false otherwise.
     */
    default boolean withdraw(ServerPlayer player, int amount, String currency) throws IllegalArgumentException {
        return withdraw(new ServerUser(player), amount, currency);
    }

    // ==========================================================
    // Has Enough Funds
    // ==========================================================

    /**
     * Checks if the player has enough of the specified currency to cover the given amount.
     * @param player The player whose balance is being checked.
     * @param amount The amount of currency to check against the player's balance.
     * @param currency The currency ID of the currency to check.
     * @return True if the player has enough of the specified currency, false otherwise.
     */
    boolean hasEnough(ServerUser player, int amount, String currency) throws IllegalArgumentException;

    /**
     * Checks if the player has enough of the specified currency to cover the given amount.
     * @param player The player whose balance is being checked.
     * @param amount The amount of currency to check against the player's balance.
     * @param currency The currency ID of the currency to check.
     * @return True if the player has enough of the specified currency, false otherwise.
     */
    default boolean hasEnough(ServerPlayer player, int amount, String currency) throws IllegalArgumentException {
        return hasEnough(new ServerUser(player), amount, currency);
    }
}
