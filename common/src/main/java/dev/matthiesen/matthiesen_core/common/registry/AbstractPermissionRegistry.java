package dev.matthiesen.matthiesen_core.common.registry;

import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.permissions.Permission;

/**
 * An abstract implementation of a permission registry that provides basic functionality for registering permissions with the MatthiesenCoreCommon permissions manager.
 * Subclasses should extend this class and implement the necessary methods to define and register their own permissions.
 */
@SuppressWarnings("unused")
public abstract class AbstractPermissionRegistry {
    /**
     * Constructs a new AbstractPermissionRegistry. This constructor is protected to prevent direct instantiation of the abstract class. Subclasses
     * should call this constructor to initialize the registry.
     */
    protected AbstractPermissionRegistry() {}

    /**
     * Registers a permission with the MatthiesenCoreCommon permissions manager.
     * @param permission the permission to register
     */
    protected final void register(Permission permission) {
        MatthiesenCoreCommon.INSTANCE.getPermissionsManager().registerPermission(permission);
    }
}

