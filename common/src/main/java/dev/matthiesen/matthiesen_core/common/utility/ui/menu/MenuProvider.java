package dev.matthiesen.matthiesen_core.common.utility.ui.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuConstructor;

/**
 * Utility class for creating and opening menus. This is used to avoid having to duplicate menu creation and opening across platforms.
 * It provides a method for creating a SimpleMenuProvider from a MenuConstructor and a title, and a method for opening a
 * menu for a player using a SimpleMenuProvider.
 */
@SuppressWarnings("unused")
public final class MenuProvider {

    /**
     * Default constructor for MenuProvider. This constructor is private to prevent instantiation, as this class is intended
     * to be used as a utility class with static methods only.
     */
    private MenuProvider() {}

    /**
     * Creates a SimpleMenuProvider from a MenuConstructor and a title. This is used to avoid having to duplicate code
     * for creating SimpleMenuProviders across platforms.
     *
     * @param constructor The MenuConstructor to use for creating the menu. This is a functional interface, so it can be
     *                    implemented using a lambda expression or method reference.
     * @param title The title of the menu. This is displayed at the top of the menu when it is opened.
     * @return A SimpleMenuProvider that can be used to open the menu for a player. This should be passed to the openMenu
     * method to actually open the menu.
     */
    public static SimpleMenuProvider createProvider(MenuConstructor constructor, Component title) {
        return new SimpleMenuProvider(constructor, title);
    }

    /**
     * Opens a menu for a player using a SimpleMenuProvider. This is used to avoid having to duplicate code for opening menus across platforms.
     *
     * @param player The player to open the menu for. This should be a ServerPlayer, as menus can only be opened for players on the server side.
     * @param provider The SimpleMenuProvider to use for opening the menu. This should be created using the createProvider method,
     *                 or it can be created manually if needed.
     */
    public static void openMenu(ServerPlayer player, SimpleMenuProvider provider) {
        player.openMenu(provider);
    }
}
