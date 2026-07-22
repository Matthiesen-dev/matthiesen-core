package dev.matthiesen.matthiesen_core.common.api.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

/**
 * Utility interface for registering menu screens. This is used to avoid having to duplicate registration across platforms.
 */
@FunctionalInterface
public interface ScreenRegistrar {
    /**
     * Registers a menu screen for a given menu type. This should be called during client initialization to ensure that the screen
     * is properly registered and can be opened when needed.
     * @param <M> The type of the menu, which must extend AbstractContainerMenu.
     * @param <S> The type of the screen, which must extend Screen and implement MenuAccess for the menu type M.
     * @param menuType The MenuType for which the screen is being registered. This identifies the type of menu that the screen will be associated with.
     * @param screenConstructor A constructor reference for creating instances of the screen. This should be a functional interface
     *                          that takes in the menu and returns a new instance of the screen, allowing the platform to create the screen when the menu is opened.
     */
    <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void register(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor);

    /**
     * Registers a menu screen using a supplier for the menu type. This is useful when the menu type is exposed as a Supplier
     * from common registration code and allows callers to use the same register(...) style in bulk registration callbacks.
     * @param <M> The type of the menu, which must extend AbstractContainerMenu.
     * @param <S> The type of the screen, which must extend Screen and implement MenuAccess for the menu type M.
     * @param menuTypeSupplier A supplier that provides the MenuType for which the screen is being registered.
     * @param screenConstructor A constructor reference for creating instances of the screen.
     */
    default <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    void register(Supplier<? extends MenuType<? extends M>> menuTypeSupplier, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
        register(menuTypeSupplier.get(), screenConstructor);
    }
}
