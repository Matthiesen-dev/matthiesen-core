package dev.matthiesen.matthiesen_core.neoforge.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderEventsListeners;
import dev.matthiesen.matthiesen_core.neoforge.utility.RunnableReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.function.Consumer;

public final class NeoForgeLoaderEventsListeners implements CommonLoaderEventsListeners {
    @Override
    public void onServerStarting(Consumer<MinecraftServer> serverConsumer) {
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) ->
                serverConsumer.accept(event.getServer()));
    }

    @Override
    public void onServerStopping(Consumer<MinecraftServer> serverConsumer) {
        NeoForge.EVENT_BUS.addListener((ServerStoppingEvent event) ->
                serverConsumer.accept(event.getServer()));
    }

    @Override
    public void onServerStartTick(Consumer<MinecraftServer> serverConsumer) {
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Pre event) ->
                serverConsumer.accept(event.getServer()));
    }

    @Override
    public void onServerEndTick(Consumer<MinecraftServer> serverConsumer) {
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) ->
                serverConsumer.accept(event.getServer()));
    }

    @Override
    public void onServerReload(Runnable runnable) {
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) ->
                event.addListener(new RunnableReloadListener(runnable)));
    }

    @Override
    public void onPlayerJoin(Consumer<ServerPlayer> playerConsumer) {
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            var entity = event.getEntity();
            if (entity.level().isClientSide) return;
            if (!(entity instanceof ServerPlayer serverPlayer)) return;
            playerConsumer.accept(serverPlayer);
        });
    }

    @Override
    public void onPlayerLeave(Consumer<ServerPlayer> playerConsumer) {
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedOutEvent event) -> {
            var entity = event.getEntity();
            if (entity.level().isClientSide) return;
            if (!(entity instanceof ServerPlayer serverPlayer)) return;
            playerConsumer.accept(serverPlayer);
        });
    }

    @Override
    public void onPlayerUseItemResult(PlayerUseItemResultListener listener) {
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickItem event) -> {
            var entity = event.getEntity();
            if (entity.level().isClientSide) return;
            if (!(entity instanceof ServerPlayer serverPlayer)) return;
            InteractionResult result = listener.onPlayerUseItemResult(serverPlayer, event.getLevel(), event.getHand());
            if (result == InteractionResult.FAIL) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        });
    }

    @Override
    public void onPlayerUseBlockResult(PlayerUseBlockResultListener listener) {
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            var entity = event.getEntity();
            if (entity.level().isClientSide) return;
            if (!(entity instanceof ServerPlayer serverPlayer)) return;
            InteractionResult result = listener.onPlayerUseBlockResult(serverPlayer, event.getLevel(), event.getHand(), event.getPos());
            if (result == InteractionResult.FAIL) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        });
    }
}
