package dev.matthiesen.matthiesen_core.common.core.economy.providers;

import dev.matthiesen.matthiesen_core.common.api.economy.BuiltInEconomyProviders;
import dev.matthiesen.matthiesen_core.common.api.economy.EconomyProvider;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.utility.player_data.ServerUser;
import net.impactdev.impactor.api.economy.EconomyService;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.impactdev.impactor.api.economy.currency.Currency;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * This class serves as an implementation of the EconomyProvider interface, specifically designed to integrate with the Impactor
 * economy system. It provides methods for managing player balances, deposits, withdrawals, and balance checks using the Impactor API.
 */
public final class ImpactorEconomyProvider implements EconomyProvider {

    /**
     * Singleton instance of the ImpactorEconomyProvider, ensuring that only one instance is used throughout the application.
     */
    public static final ImpactorEconomyProvider INSTANCE = new ImpactorEconomyProvider();

    private static EconomyService getEconomyService() {
        try {
            return EconomyService.instance();
        } catch (RuntimeException e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Impactor Economy Service is not available. Ensure that the Impactor mod is loaded and the economy service is properly initialized.", e);
            return null;
        }
    }

    private ImpactorEconomyProvider() {}

    @Override
    public String providerId() {
        return BuiltInEconomyProviders.IMPACTOR.getId();
    }

    @Override
    public String providerDisplayName() {
        return "Impactor Provider";
    }

    @Override
    public int getBalance(ServerUser player, String currency) throws IllegalArgumentException {
        try {
            Account account = getAccount(player.getOnlinePlayer().getUUID(), currency);
            return account.balance().intValue();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error retrieving balance for player " + player.getOnlinePlayer().getName().getString() + " with currency " + currency, e);
        }
    }

    @Override
    public void deposit(ServerUser player, int amount, String currency) throws IllegalArgumentException {
        try {
            Account account = getAccount(player.getOnlinePlayer().getUUID(), currency);
            account.deposit(new java.math.BigDecimal(amount));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error depositing " + amount + " to player " + player.getOnlinePlayer().getName().getString() + " with currency " + currency, e);
        }
    }

    @Override
    public boolean withdraw(ServerUser player, int amount, String currency) throws IllegalArgumentException {
        try {
            int balance = getBalance(player, currency);
            if (balance < amount) {
                throw new IllegalArgumentException("Player " + player.getOnlinePlayer().getName().getString() + " does not have enough balance to withdraw " + amount + " of currency " +currency);
            }
            Account account = getAccount(player.getOnlinePlayer().getUUID(), currency);
            return account.withdraw(new java.math.BigDecimal(amount)).successful();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error withdrawing " + amount + " from player " + player.getOnlinePlayer().getName().getString() + " with currency " + currency, e);
        }
    }

    @Override
    public boolean hasEnough(ServerUser player, int amount, String currency) throws IllegalArgumentException {
        try {
            Account account = getAccount(player.getOnlinePlayer().getUUID(), currency);
            return account.balance().intValue() >= amount;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error checking balance for player " + player.getOnlinePlayer().getName().getString() + " with currency " + currency, e);
        }
    }

    private Currency parseCurrency(@Subst("impactor:dollars") String currency) {
        var economyService = getEconomyService();
        if (economyService == null) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Impactor Economy Service is not available. Cannot parse currency: " + currency);
            return null;
        }
        Optional<Currency> currencyOptional = economyService.currencies().currency(Key.key(currency));
        if (currencyOptional.isEmpty()) {
            MatthiesenCoreCommon.INSTANCE.createWarnLog("Impactor currency " + currency + " not found, defaulting to primary currency");
            return economyService.currencies().primary();
        }
        return currencyOptional.get();
    }

    private Account getAccount(@NotNull UUID playerUUID, String currency) {
        var economyService = getEconomyService();
        if (economyService == null) {
            throw new IllegalStateException("Impactor Economy Service is not available. Cannot retrieve account for player UUID: " + playerUUID);
        }
        Currency parsedCurrency = parseCurrency(currency);
        return economyService.account(parsedCurrency, playerUUID).join();
    }
}
