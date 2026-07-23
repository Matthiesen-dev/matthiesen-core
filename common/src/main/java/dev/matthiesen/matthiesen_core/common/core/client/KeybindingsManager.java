package dev.matthiesen.matthiesen_core.common.core.client;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeybindMapping;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeybindRegistrar;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderClientEventsListeners;
import net.minecraft.client.KeyMapping;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Manages keybinding registrations and per-tick keybind callbacks in common code.
 *
 * <p>The manager queues key registrations until a platform registrar is active, then replays pending entries and
 * executes registered keybind tick handlers on each end-client-tick event.</p>
 */
@SuppressWarnings("unused")
public final class KeybindingsManager {
	private static final List<KeybindRegistration> REGISTERED_KEYBINDS = new CopyOnWriteArrayList<>();

	/**
	 * Shared singleton instance used by common client bootstrap.
	 */
	public static final KeybindingsManager INSTANCE = new KeybindingsManager();

	private volatile KeyMappingRegistrar activeRegistrar;
	private int appliedRegistrations;
	private boolean initialized;

	private KeybindingsManager() {}

	/**
	 * Installs keybinding registration and per-tick keybind processing callbacks once.
	 *
	 * @param clientEventsListeners platform event listener bridge
	 */
	public synchronized void initialize(CommonLoaderClientEventsListeners clientEventsListeners) {
		if (initialized) {
			return;
		}

		initialized = true;
		clientEventsListeners.applyKeyBindingRegistrations(this::applyKeybindRegistrations);
		clientEventsListeners.endClientTick(this::tickKeybinds);
		MatthiesenCoreCommonClient.INSTANCE.createInfoLog("Initialized KeybindingsManager");
	}

	/**
	 * Registers multiple keybinds using a registrar callback.
	 *
	 * @param registrarConsumer callback that receives a keybind registrar helper
	 */
	public void registerKeybinds(Consumer<KeybindRegistrar> registrarConsumer) {
		registrarConsumer.accept(this::registerKeybind);
	}

	/**
	 * Registers a named keybind mapping.
	 *
	 * @param name unique keybind name
	 * @param keybind keybind mapping with optional tick handler
	 */
	public void registerKeybind(String name, KeybindMapping keybind) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(keybind, "keybind");
		registerKeybindInternal(new KeybindRegistration(name, keybind));
	}

	/**
	 * Registers a named key mapping with no per-tick callback.
	 *
	 * @param name unique keybind name
	 * @param keyMapping key mapping to register
	 */
	public void registerKeybind(String name, KeyMapping keyMapping) {
		registerKeybind(name, keyMapping, () -> {
		});
	}

	/**
	 * Registers a named key mapping with a per-tick callback.
	 *
	 * @param name unique keybind name
	 * @param keyMapping key mapping to register
	 * @param onClientTick callback invoked on each end-client-tick
	 */
	public void registerKeybind(String name, KeyMapping keyMapping, Runnable onClientTick) {
		Objects.requireNonNull(keyMapping, "keyMapping");
		Objects.requireNonNull(onClientTick, "onClientTick");
		registerKeybind(name, new KeybindMapping() {
			@Override
			public KeyMapping getKeybind() {
				return keyMapping;
			}

			@Override
			public void onClientTick() {
				onClientTick.run();
			}
		});
	}

	/**
	 * Applies all queued keybind registrations to the provided platform registrar.
	 *
	 * @param registrar platform key mapping registrar
	 */
	public synchronized void applyKeybindRegistrations(KeyMappingRegistrar registrar) {
		activeRegistrar = registrar;

		for (int i = appliedRegistrations; i < REGISTERED_KEYBINDS.size(); i++) {
			REGISTERED_KEYBINDS.get(i).apply(registrar);
		}

		appliedRegistrations = REGISTERED_KEYBINDS.size();
	}

	/**
	 * Invokes per-tick handlers for all registered keybind mappings.
	 */
	public void tickKeybinds() {
		for (KeybindRegistration registration : REGISTERED_KEYBINDS) {
			try {
				registration.keybind().onClientTick();
			} catch (Throwable throwable) {
				MatthiesenCoreCommonClient.INSTANCE.createErrorLog("Exception while handling keybind tick for " + registration.name(), throwable);
			}
		}
	}

	private synchronized void registerKeybindInternal(KeybindRegistration registration) {
		for (KeybindRegistration existingRegistration : REGISTERED_KEYBINDS) {
			if (existingRegistration.name().equals(registration.name())) {
				throw new IllegalArgumentException("Keybind already registered: " + registration.name());
			}
		}

		REGISTERED_KEYBINDS.add(registration);

		if (activeRegistrar != null) {
			registration.apply(activeRegistrar);
			appliedRegistrations = REGISTERED_KEYBINDS.size();
		}
	}

	private record KeybindRegistration(String name, KeybindMapping keybind) {
		void apply(KeyMappingRegistrar registrar) {
			registrar.register(keybind.getKeybind());
		}
	}
}
