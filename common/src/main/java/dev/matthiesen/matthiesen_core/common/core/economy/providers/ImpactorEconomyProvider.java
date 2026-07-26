package dev.matthiesen.matthiesen_core.common.core.economy.providers;

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
    public static final ImpactorEconomyProvider INSTANCE = new ImpactorEconomyProvider();
    private static final EconomyService SERVICE = EconomyService.instance();

    @Override
    public String providerId() {
        return "impactor";
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
        Optional<Currency> currencyOptional = SERVICE.currencies().currency(Key.key(currency));
        if (currencyOptional.isEmpty()) {
            MatthiesenCoreCommon.INSTANCE.createWarnLog("Impactor currency " + currency + " not found, defaulting to primary currency");
            return SERVICE.currencies().primary();
        }
        return currencyOptional.get();
    }

    private Account getAccount(@NotNull UUID playerUUID, String currency) {
        Currency parsedCurrency = parseCurrency(currency);
        return SERVICE.account(parsedCurrency, playerUUID).join();
    }
}
