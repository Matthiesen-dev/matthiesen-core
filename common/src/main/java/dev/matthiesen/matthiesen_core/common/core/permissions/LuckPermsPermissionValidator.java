package dev.matthiesen.matthiesen_core.common.core.permissions;

import dev.matthiesen.matthiesen_core.common.api.permissions.Permission;
import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionValidator;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Implementation of the PermissionValidator interface that checks permissions using the LuckPerms API.
 * This validator will check if a player has the required permission to execute a command or perform an action based on the permissions defined in the LuckPerms system.
 */
public final class LuckPermsPermissionValidator implements PermissionValidator {
    /**
     * Creates a new instance of the LuckPermsPermissionValidator. This constructor does not perform any initialization, as there are no resources to set up for this validator.
     */
    public LuckPermsPermissionValidator() {}

    @Override
    public void initialize() {
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Booting LuckPermsPermissionValidator, permissions will be checked using the LuckPerms API. See https://luckperms.net/wiki/Home");
    }

    private LuckPermsHelper getHelper() {
        return LuckPermsHelper.INSTANCE;
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Permission permission) {
        return getHelper().hasPermissionNode(player, permission.getLiteral());
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission, int level) {
        return getHelper().hasPermissionNode(player, permission) || player.hasPermissions(level);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {
        if (source.getEntity() instanceof ServerPlayer player) {
            return hasPermission(player, permission);
        }
        return source.hasPermission(permission.getLevel().getLevel());
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, String permission, int level) {
        if (source.getEntity() instanceof ServerPlayer player) {
            return hasPermission(player, permission, level);
        }
        return source.hasPermission(level);
    }
}
