package dev.matthiesen.matthiesen_core.neoforge.permissions;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.permissions.Permission;
import dev.matthiesen.matthiesen_core.common.api.permissions.PermissionValidator;
import dev.matthiesen.matthiesen_core.common.core.permissions.PermissionsManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The NeoForgePermissionValidator class is an implementation of the PermissionValidator interface for the NeoForge mod loader.
 * It provides methods to check permissions for players and command sources using NeoForge's PermissionAPI.
 * This class listens for the PermissionGatherEvent.Nodes event to gather permission nodes from the PermissionsManager
 * and register them with NeoForge's permission system. It also provides methods to check if a player or command source has a
 * specific permission, either by Permission object or by permission string and level.
 */
public final class NeoForgePermissionValidator implements PermissionValidator {
    private final Map<ResourceLocation, PermissionNode<Boolean>> nodes = new HashMap<>();

    /**
     * Constructs a new instance of the NeoForgePermissionValidator. This constructor registers the onPermissionGatherNodes method
     * as a listener for the PermissionGatherEvent.Nodes event, allowing it to gather permission nodes when the event is fired.
     * The constructor is private to enforce the singleton pattern, ensuring that only one instance of the NeoForgePermissionValidator exists throughout the application.
     * The instance is created and managed by the MatthiesenLibCommon class, which provides access to the permission validation functionality across the application.
     */
    public NeoForgePermissionValidator() {
        NeoForge.EVENT_BUS.addListener(this::onPermissionGatherNodes);
    }

    /**
     * This method is called when the PermissionGatherEvent.Nodes event is fired. It gathers all the permission nodes registered in the
     * PermissionsManager and adds them to the event.
     * This allows NeoForge to recognize and manage the permissions defined in the PermissionsManager.
     * @param event The PermissionGatherEvent.Nodes event that is fired when NeoForge is gathering permission nodes.
     */
    @SubscribeEvent
    public void onPermissionGatherNodes(PermissionGatherEvent.Nodes event) {
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Running NeoForge Permissions node gathering.");
        event.addNodes(createNodes());
    }

    @Override
    public void initialize() {
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Booting NeoForgePermissionValidator, player permissions will be checked using MinecraftForge' PermissionAPI, non player command sources will use Minecraft' permission level system, see https://docs.minecraftforge.net/en/latest/ and https://minecraft.fandom.com/wiki/Permission_level");
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Permission permission) {
        PermissionNode<Boolean> node = this.findNode(permission);
        if (node == null) {
            return player.hasPermissions(permission.getLevel().getLevel());
        }
        return PermissionAPI.getPermission(player, node);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission, int level) {
        String namespace = permission.split("\\.")[0];
        String path = permission.substring(permission.indexOf(".") + 1);
        PermissionNode<Boolean> node = new PermissionNode<>(
                namespace,
                path,
                PermissionTypes.BOOLEAN,
                (p, uuid, context) -> p != null && p.hasPermissions(level)
        );
        return PermissionAPI.getPermission(player, node);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {
        ServerPlayer player = this.extractPlayerFromSource(source);
        if (player == null) {
            return source.hasPermission(permission.getLevel().getLevel());
        }
        PermissionNode<Boolean> node = this.findNode(permission);
        if (node == null) {
            return source.hasPermission(permission.getLevel().getLevel());
        }
        return PermissionAPI.getPermission(player, node);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, String permission, int level) {
        ServerPlayer player = this.extractPlayerFromSource(source);
        if (player == null) {
            return source.hasPermission(4);
        }
        String namespace = permission.split("\\.")[0];
        String path = permission.substring(permission.indexOf(".") + 1);
        PermissionNode<Boolean> node = new PermissionNode<>(
                namespace,
                path,
                PermissionTypes.BOOLEAN,
                (p, uuid, context) -> p != null && p.hasPermissions(level)
        );
        return PermissionAPI.getPermission(player, node);
    }

    private List<PermissionNode<?>> createNodes() {
        PermissionsManager permissionsManager = MatthiesenCoreCommon.INSTANCE.getPermissionsManager();
        MatthiesenCoreCommon.INSTANCE.createInfoLog("Trying to Register " + permissionsManager.getPendingPermissionCount() + " NeoForge permission nodes");
        return permissionsManager.all().stream().map(permission -> {
            PermissionNode<Boolean> node = new PermissionNode<>(
                    permission.getIdentifier(),
                    PermissionTypes.BOOLEAN,
                    (player, uuid, context) -> player != null && player.hasPermissions(permission.getLevel().getLevel())
            );
            this.nodes.put(permission.getIdentifier(), node);
            return node;
        }).collect(Collectors.toList());
    }

    private PermissionNode<Boolean> findNode(Permission permission) {
        return this.nodes.get(permission.getIdentifier());
    }

    private ServerPlayer extractPlayerFromSource(CommandSourceStack source) {
        return source.getPlayer();
    }
}
