package dev.matthiesen.matthiesen_core.common.api.client;

import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackDef;

/**
 * The ResourcePackRegistrar interface defines a contract for registering resource pack definitions with the client.
 * Implementations of this interface are responsible for handling the registration of resource packs, allowing for
 * the addition of custom resources to the game.
 */
@FunctionalInterface
public interface ResourcePackRegistrar {

    /**
     * Registers a resource pack definition with the client.
     * @param resourcePackDef The resource pack definition to register.
     */
    void register(ResourcePackDef resourcePackDef);
}
