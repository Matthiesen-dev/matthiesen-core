package dev.matthiesen.matthiesen_core.common.core.economy.providers;

import dev.matthiesen.matthiesen_core.common.api.economy.EconomyProvider;
import dev.matthiesen.matthiesen_core.common.utility.item.ItemDecoder;
import dev.matthiesen.matthiesen_core.common.utility.player_data.ServerUser;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * An implementation of the EconomyProvider interface that represents an item-based economy provider.
 * This provider is responsible for managing the in-game currency system based on items, allowing players to
 * earn, spend, and manage their currency through item transactions.
 */
public final class ItemEconomyProvider implements EconomyProvider {
    /**
     * Retrieves the singleton instance of the ItemEconomyProvider. This instance is responsible for managing the in-game currency system based on items.
     */
    public static final ItemEconomyProvider INSTANCE = new ItemEconomyProvider();

    @Override
    public String providerId() {
        return "item_provider";
    }

    @Override
    public String providerDisplayName() {
        return "Item Provider";
    }

    @Override
    public int getBalance(ServerUser player, String currency) throws IllegalArgumentException {
        Item currencyItem = ItemDecoder.stringToItem(currency, Items.BARRIER);
        if (currencyItem == Items.AIR) {
            throw new IllegalArgumentException("Invalid currency item: " + currency);
        }
        int balance = 0;
        for (var itemStack : player.getOnlinePlayer().getInventory().items) {
            if (itemStack.getItem() == currencyItem) {
                balance += itemStack.getCount();
            }
        }
        return balance;
    }

    @Override
    public void deposit(ServerUser player, int amount, String currency) throws IllegalArgumentException {
        Item currencyItem = ItemDecoder.stringToItem(currency, Items.BARRIER);
        if (currencyItem == Items.AIR) {
            throw new IllegalArgumentException("Invalid currency item: " + currency);
        }
        ItemStack payment = new ItemStack(currencyItem, amount);
        boolean added = player.getOnlinePlayer().getInventory().add(payment);
        // Fallback: drop the item on the ground if inventory is full
        if (!added) {
            player.getOnlinePlayer().drop(payment, false);
        }
    }

    @Override
    public boolean withdraw(ServerUser player, int amount, String currency) throws IllegalArgumentException {
        Item currencyItem = ItemDecoder.stringToItem(currency, Items.BARRIER);
        if (currencyItem == Items.AIR) {
            throw new IllegalArgumentException("Invalid currency item: " + currency);
        }
        int balance = getBalance(player, currency);
        if (balance < amount) {
            return false;
        }
        subtractPlayerBalance(player.getOnlinePlayer(), currencyItem, amount);
        return true;
    }

    @Override
    public boolean hasEnough(ServerUser player, int amount, String currency) throws IllegalArgumentException {
        Item currencyItem = ItemDecoder.stringToItem(currency, Items.BARRIER);
        if (currencyItem == Items.AIR) {
            throw new IllegalArgumentException("Invalid currency item: " + currency);
        }
        int balance = getBalance(player, currency);
        return balance >= amount;
    }

    private void subtractPlayerBalance(ServerPlayer player, Item currencyItem, int amount) {
        int remaining = amount;
        for (var itemStack : player.getInventory().items) {
            if (itemStack.getItem() == currencyItem) {
                int count = itemStack.getCount();
                if (count >= remaining) {
                    itemStack.shrink(remaining);
                    break;
                } else {
                    itemStack.setCount(0);
                    remaining -= count;
                }
            }
        }
    }
}
