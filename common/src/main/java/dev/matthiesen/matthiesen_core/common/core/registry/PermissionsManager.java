package dev.matthiesen.matthiesen_core.common.core.registry;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.permissions.Permission;
import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionValidator;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.common.core.permissions.VanillaPermissionValidator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The PermissionsManager class is responsible for managing the permission validation system within the Matthiesen Lib framework.
 * It provides methods to initialize the permission system, set and retrieve the current PermissionValidator instance.
 * This class is designed to be used as a singleton, ensuring that only one instance of the permission system is active at any given time.
 */
@SuppressWarnings("unused")
public final class PermissionsManager {
    private static final List<Permission> PERMISSIONS = new CopyOnWriteArrayList<>();
    private static final List<Permission> PENDING_PERMISSIONS = new CopyOnWriteArrayList<>();

    /**
     * Singleton instance of the PermissionsManager. This instance is used to manage permissions across the application.
     * It is initialized lazily and is thread-safe, ensuring that only one instance exists throughout the lifecycle of the application.
     */
    public static final PermissionsManager INSTANCE = new PermissionsManager();

    private boolean initialized;
    private PermissionValidator permissionValidator;

    private PermissionsManager() {}

    /**
     * Initializes the PermissionsManager with the provided CommonLoaderRegistry.
     * @param registry the CommonLoaderRegistry to use for registering the PermissionValidator
     */
    public synchronized void initialize(CommonLoaderRegistry registry) {
        if (initialized) return;

        MatthiesenCoreCommon.INSTANCE.createInfoLog("Initializing PermissionsManager");
        setPermissionValidator(new VanillaPermissionValidator());
        registry.registerPermissionValidator();

        initialized = true;
    }

    /**
     * Sets the PermissionValidator instance to be used for permission checks.
     * @param newPermissionValidator the new PermissionValidator instance to set
     */
    public synchronized void setPermissionValidator(PermissionValidator newPermissionValidator) {
        permissionValidator = newPermissionValidator;
        permissionValidator.initialize();
    }

    /**
     * Gets the current PermissionValidator instance.
     * @return the current PermissionValidator instance, or null if it has not been set.
     */
    public synchronized PermissionValidator getPermissionValidator() {
        return permissionValidator;
    }

    /**
     * Registers a permission. If the registrar is not yet available, the permission is queued for later registration.
     * Safe to call at any time.
     *
     * @param permission The Permission to register.
     */
    public synchronized void registerPermission(Permission permission) {
        PENDING_PERMISSIONS.add(permission);
    }

    /**
     * Internally adds a permission to the registry without triggering the registrar.
     *
     * @param permission The permission to add.
     */
    private synchronized void addPermission(Permission permission) {
        PERMISSIONS.add(permission);
    }

    /**
     * Retrieves all registered permissions.
     *
     * @return An unmodifiable list of all permissions.
     */
    public synchronized List<Permission> all() {
        for (Permission permission : PENDING_PERMISSIONS) addPermission(permission);
        PENDING_PERMISSIONS.clear();
        return new CopyOnWriteArrayList<>(PERMISSIONS);
    }

    /**
     * Gets the count of registered pending permissions.
     *
     * @return The number of permissions pending registration.
     */
    public synchronized int getPendingPermissionCount() {
        return PENDING_PERMISSIONS.size();
    }
}
