package dev.matthiesen.matthiesen_core.common.core.permissions;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.permissions.Permission;
import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionValidator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Implementation of the PermissionValidator interface that checks permissions using Minecraft's built-in permission level system.
 * This validator will check if a player has the required permission level to execute a command or perform an action.
 */
public final class VanillaPermissionValidator implements PermissionValidator {
    /**
     * Creates a new instance of the VanillaPermissionValidator. This constructor does not perform any initialization, as there are no resources to set up for this validator.
     */
    public VanillaPermissionValidator() {}

    @Override
    public void initialize() {
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Booting VanillaPermissionValidator, permissions will be checked using Minecraft's permission level system, see https://minecraft.wiki/w/Permission_level");
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Permission permission) {
        return player.hasPermissions(permission.getLevel().getLevel());
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission, int level) {
        return player.hasPermissions(level);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {
        return source.hasPermission(permission.getLevel().getLevel());
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, String permission, int level) {
        return source.hasPermission(level);
    }
}
