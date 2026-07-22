package dev.matthiesen.matthiesen_core.common.core.client;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.common.api.client.ScreenRegistrar;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Manages menu screen registrations in common code and replays queued registrations when platform hooks become available.
 *
 * <p>Registrations can be added before or after platform registration events; late registrations are applied immediately
 * once a platform registrar is active.</p>
 */
@SuppressWarnings("unused")
public final class ScreenManager {
	private static final List<ScreenRegistration> REGISTERED_SCREENS = new CopyOnWriteArrayList<>();

	/**
	 * Shared singleton instance used by common client bootstrap.
	 */
	public static final ScreenManager INSTANCE = new ScreenManager();

	private volatile ScreenRegistrar activeRegistrar;
	private int appliedRegistrations;
	private boolean initialized;

	private ScreenManager() {}

	/**
	 * Installs the platform screen registration callback once.
	 *
	 * @param clientEventsListeners platform event listener bridge
	 */
	public synchronized void initialize(CommonLoaderClientEventsListeners clientEventsListeners) {
		if (initialized) {
			return;
		}
		initialized = true;
		clientEventsListeners.applyScreenRegistrations(this::applyScreenRegistrations);
		MatthiesenCoreCommonClient.INSTANCE.createInfoLog("Initialized ScreenManager");
	}

	/**
	 * Registers multiple menu screens using a registrar callback.
	 *
	 * @param registrarConsumer callback that receives a screen registrar helper
	 */
	public void registerMenuScreens(Consumer<ScreenRegistrar> registrarConsumer) {
		registrarConsumer.accept(this::registerMenuScreen);
	}

	/**
	 * Queues a menu screen registration using a supplier-based menu type.
	 *
	 * @param menuTypeSupplier supplier for the menu type to bind
	 * @param screenConstructor screen constructor for that menu type
	 * @param <M> menu type
	 * @param <S> screen type
	 */
	public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
	void registerMenuScreen(Supplier<? extends MenuType<? extends M>> menuTypeSupplier, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
		registerMenuScreenInternal(registrar -> registrar.register(menuTypeSupplier, screenConstructor));
	}

	/**
	 * Queues a menu screen registration using a concrete menu type.
	 *
	 * @param menuType menu type to bind
	 * @param screenConstructor screen constructor for that menu type
	 * @param <M> menu type
	 * @param <S> screen type
	 */
	public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
	void registerMenuScreen(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, S> screenConstructor) {
		registerMenuScreenInternal(new ScreenEntry<>(menuType, screenConstructor));
	}

	private synchronized void applyScreenRegistrations(ScreenRegistrar registrar) {
		activeRegistrar = registrar;
		for (int i = appliedRegistrations; i < REGISTERED_SCREENS.size(); i++) {
			REGISTERED_SCREENS.get(i).apply(registrar);
		}
		appliedRegistrations = REGISTERED_SCREENS.size();
	}

	private synchronized void registerMenuScreenInternal(ScreenRegistration registration) {
		REGISTERED_SCREENS.add(registration);
		if (activeRegistrar != null) {
			registration.apply(activeRegistrar);
			appliedRegistrations = REGISTERED_SCREENS.size();
		}
	}

	@FunctionalInterface
	private interface ScreenRegistration {
		void apply(ScreenRegistrar registrar);
	}

	private record ScreenEntry<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>(
			MenuType<? extends M> menuType,
			MenuScreens.ScreenConstructor<M, S> screenConstructor
	) implements ScreenRegistration {
		@Override
		public void apply(ScreenRegistrar registrar) {
			registrar.register(menuType, screenConstructor);
		}
	}
}
