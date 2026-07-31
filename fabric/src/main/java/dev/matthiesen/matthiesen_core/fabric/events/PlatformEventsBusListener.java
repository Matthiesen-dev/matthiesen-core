package dev.matthiesen.matthiesen_core.fabric.events;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import dev.matthiesen.matthiesen_core.common.api.events.server.ServerEvent;
import dev.matthiesen.matthiesen_core.common.api.events.server.WorldEvent;
import dev.matthiesen.matthiesen_core.fabric.MatthiesenCoreFabric;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

/**
 * The PlatformEventsBusListener class serves as a bridge between the Fabric mod loader and the common event observables
 * defined in the Matthiesen Core library. It registers Fabric-specific event callbacks and translates them into the
 * corresponding common events, allowing developers to handle server lifecycle events, player interactions, and other
 * game-related events in a consistent manner across different mod loaders.
 */
public final class PlatformEventsBusListener {
    /**
     * A volatile reference to the MinecraftServer instance, which is set when the server starts and cleared when the server stops.
     * This allows other parts of the mod to access the server instance safely across different threads.
     */
    public static volatile MinecraftServer SERVER_INSTANCE;

    /**
     * Wires Fabric-specific loader callbacks into the common event observables.
     */
    public static void initialize() {

        // Run Common Setup first
        MatthiesenCoreFabric.INSTANCE.onCommonServerSetup();

        // ================================================
        // Server Events
        // ================================================

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            SERVER_INSTANCE = server;
            PlatformEvents.SERVER_STARTING.emit(new ServerEvent.Starting(server));
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server ->
                PlatformEvents.SERVER_STARTED.emit(new ServerEvent.Started(server)));

        ServerLifecycleEvents.SERVER_STOPPING.register(server ->
                PlatformEvents.SERVER_STOPPING.emit(new ServerEvent.Stopping(server)));

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            PlatformEvents.SERVER_STOPPED.emit(new ServerEvent.Stopped(server));
            SERVER_INSTANCE = null;
        });

        ServerTickEvents.START_SERVER_TICK.register(server ->
                PlatformEvents.SERVER_START_TICK.emit(new ServerEvent.StartTick(server)));

        ServerTickEvents.END_SERVER_TICK.register(server ->
                PlatformEvents.SERVER_END_TICK.emit(new ServerEvent.EndTick(server)));

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResources, success) ->
                PlatformEvents.SERVER_RELOAD.emit(new ServerEvent.Reload()));

        ServerTickEvents.START_WORLD_TICK.register((serverLevel) ->
                PlatformEvents.WORLD_START_TICK.emit(new WorldEvent.StartTick(serverLevel)));
        ServerTickEvents.END_WORLD_TICK.register((serverLevel) ->
                PlatformEvents.WORLD_END_TICK.emit(new WorldEvent.EndTick(serverLevel)));

        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(((message, sender, params) ->
                !PlatformEvents.SERVER_CHAT.emit(new ServerEvent.Chat(sender, message.signedBody().content()))));

        // ================================================
        // Player Events
        // ================================================

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                PlatformEvents.PLAYER_JOIN.emit(new PlayerEvent.Join(handler.player)));

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                PlatformEvents.PLAYER_LEAVE.emit(new PlayerEvent.Leave(handler.player)));

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!(player instanceof ServerPlayer serverPlayer))
                return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
            InteractionResult result = PlatformEvents.PLAYER_USE_ITEM.emit(new PlayerEvent.UseItem(serverPlayer, world, hand));
            return new InteractionResultHolder<>(result, player.getItemInHand(hand));
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!(player instanceof ServerPlayer serverPlayer))
                return InteractionResult.PASS;
            return PlatformEvents.PLAYER_USE_BLOCK.emit(new PlayerEvent.UseBlock(serverPlayer, world, hand, hitResult.getBlockPos()));
        });
    }
}
