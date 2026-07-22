package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderEventsListeners;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

import java.util.function.Consumer;

/**
 * The FabricLoaderEventsListeners class implements the CommonLoaderEventsListeners interface and provides server-side event handling for the Fabric mod loader.
 */
public final class FabricLoaderEventsListeners implements CommonLoaderEventsListeners {
    @Override
    public void onServerStarting(Consumer<MinecraftServer> serverConsumer) {
        ServerLifecycleEvents.SERVER_STARTING.register(serverConsumer::accept);
    }

    @Override
    public void onServerStopping(Consumer<MinecraftServer> serverConsumer) {
        ServerLifecycleEvents.SERVER_STOPPING.register(serverConsumer::accept);
    }

    @Override
    public void onServerStartTick(Consumer<MinecraftServer> serverConsumer) {
        ServerTickEvents.START_SERVER_TICK.register(serverConsumer::accept);
    }

    @Override
    public void onServerEndTick(Consumer<MinecraftServer> serverConsumer) {
        ServerTickEvents.END_SERVER_TICK.register(serverConsumer::accept);
    }

    @Override
    public void onServerReload(Runnable runnable) {
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResources, success) -> runnable.run());
    }

    @Override
    public void onPlayerJoin(Consumer<ServerPlayer> playerConsumer) {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                playerConsumer.accept(handler.player));
    }

    @Override
    public void onPlayerLeave(Consumer<ServerPlayer> playerConsumer) {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                playerConsumer.accept(handler.player));
    }

    @Override
    public void onPlayerUseItemResult(PlayerUseItemResultListener listener) {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!(player instanceof ServerPlayer serverPlayer))
                return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
            InteractionResult result = listener.onPlayerUseItemResult(serverPlayer, world, hand);
            return new InteractionResultHolder<>(result, player.getItemInHand(hand));
        });
    }

    @Override
    public void onPlayerUseBlockResult(PlayerUseBlockResultListener listener) {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!(player instanceof ServerPlayer serverPlayer))
                return InteractionResult.PASS;
            return listener.onPlayerUseBlockResult(serverPlayer, world, hand, hitResult.getBlockPos());
        });
    }
}
