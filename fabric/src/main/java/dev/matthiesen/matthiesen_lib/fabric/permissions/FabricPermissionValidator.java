package dev.matthiesen.matthiesen_lib.fabric.permissions;

import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import dev.matthiesen.matthiesen_lib.common.api.permissions.Permission;
import dev.matthiesen.matthiesen_lib.common.api.permissions.PermissionValidator;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Implementation of the PermissionValidator interface that checks permissions using the fabric-permissions-api.
 * This validator will check if a player has the required permission level to execute a command or perform an action
 * using the fabric-permissions-api, which allows for integration with various permissions mods that support the API.
 * For more information on the fabric-permissions-api. See <a href="https://github.com/lucko/fabric-permissions-api">...</a>
 */
public final class FabricPermissionValidator implements PermissionValidator {
    /**
     * Creates a new instance of the FabricPermissionValidator. This constructor does not perform any initialization,
     * as the initialize method is called separately when the validator is registered.
     */
    public FabricPermissionValidator() {}

    @Override
    public void initialize() {
        MatthiesenLibCommon.INSTANCE.createInfoLog("Booting FabricPermissionValidator, permissions will be checked using fabric-permissions-api, see https://github.com/lucko/fabric-permissions-api");
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Permission permission) {
        return Permissions.check(player, permission.getLiteral(), permission.getLevel().getLevel());
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission, int level) {
        return Permissions.check(player, permission, level);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {
        return Permissions.check(source, permission.getLiteral(), permission.getLevel().getLevel());
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, String permission, int level) {
        return Permissions.check(source, permission, level);
    }
}
