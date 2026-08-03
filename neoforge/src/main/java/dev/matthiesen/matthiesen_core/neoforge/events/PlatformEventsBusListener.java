package dev.matthiesen.matthiesen_core.neoforge.events;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import dev.matthiesen.matthiesen_core.common.api.events.server.ServerEvent;
import dev.matthiesen.matthiesen_core.common.api.events.server.WorldEvent;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.neoforge.platform.helpers.NeoForgeReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * The PlatformEventsBusListener class is responsible for listening to various server and player events in the NeoForge
 * mod loader environment. It subscribes to events such as server starting, stopping, ticking, player joining, leaving,
 * and item/block interaction results. When these events occur, the corresponding methods in this class are invoked,
 * and they emit the appropriate events to the PlatformEvents system. This allows other parts of the mod to respond to
 * these events in a platform-agnostic manner.
 */
@EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID)
public final class PlatformEventsBusListener {
    /**
     * A volatile reference to the MinecraftServer instance, which is set when the server starts and cleared when the server stops.
     * This allows other parts of the mod to access the server instance safely across different threads.
     */
    public static volatile MinecraftServer SERVER_INSTANCE;

    // ================================================
    // Server Events
    // ================================================

    /**
     * Handles the common setup event for the mod. This method is called during the common setup phase of the mod loading
     * process and is used to perform any necessary initialization tasks that need to be executed on both the client and server sides.
     * @param event The FMLCommonSetupEvent event object, which provides context and information about the common setup phase of the mod loading process.
     */
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(MatthiesenCoreCommon.INSTANCE::onCommonServerSetup);
    }

    /**
     * Handles the server starting event in the NeoForge mod loader environment. This method is called when the server
     * is in the process of starting up, but before it has fully initialized. It emits a SERVER_STARTING event to the
     * PlatformEvents system, allowing other parts of the mod to respond to the server startup.
     * @param event The ServerStartingEvent event object, which provides context and information about the server startup
     *              event, including the server instance.
     */
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        SERVER_INSTANCE = event.getServer();
        PlatformEvents.SERVER_STARTING.emit(new ServerEvent.Starting(event.getServer()));
    }

    /**
     * Handles the server started event in the NeoForge mod loader environment. This method is called when the server
     * has successfully started and is ready to accept connections. It emits a SERVER_STARTED event to the PlatformEvents
     * system, allowing other parts of the mod to respond to the server startup.
     * @param event The ServerStartedEvent event object, which provides context and information about the server startup
     *              event, including the server instance.
     */
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        PlatformEvents.SERVER_STARTED.emit(new ServerEvent.Started(event.getServer()));
    }

    /**
     * Handles the server stopping event in the NeoForge mod loader environment. This method is called when the server
     * is in the process of shutting down, but before it has completely stopped. It emits a SERVER_STOPPING event to the
     * PlatformEvents system, allowing other parts of the mod to respond to the server shutdown.
     * @param event The ServerStoppingEvent event object, which provides context and information about the server
     *              shutdown event, including the server instance.
     */
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        SERVER_INSTANCE = null;
        PlatformEvents.SERVER_STOPPING.emit(new ServerEvent.Stopping(event.getServer()));
    }

    /**
     * Handles the server stopped event in the NeoForge mod loader environment. This method is called when the server
     * has completely stopped and is no longer running. It emits a SERVER_STOPPED event to the PlatformEvents system,
     * allowing other parts of the mod to respond to the server shutdown.
     * @param event The ServerStoppedEvent event object, which provides context and information about the server
     *              shutdown event, including the server instance.
     */
    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        PlatformEvents.SERVER_STOPPED.emit(new ServerEvent.Stopped(event.getServer()));
    }

    /**
     * Handles the server start tick event in the NeoForge mod loader environment. This method is called at the beginning
     * of each server tick, before any game logic is processed. It emits a SERVER_START_TICK event to the PlatformEvents
     * system, allowing other parts of the mod to respond to the start of the server tick.
     * @param event The ServerTickEvent.Pre event object, which provides context and information about the server tick
     *              event, including the server instance.
     */
    @SubscribeEvent
    public static void onServerStartTick(ServerTickEvent.Pre event) {
        PlatformEvents.SERVER_START_TICK.emit(new ServerEvent.StartTick(event.getServer()));
    }

    /**
     * Handles the server end tick event in the NeoForge mod loader environment. This method is called at the end of each
     * server tick, after all game logic has been processed. It emits a SERVER_END_TICK event to the PlatformEvents system,
     * allowing other parts of the mod to respond to the end of the server tick.
     * @param event The ServerTickEvent.Post event object, which provides context and information about the server tick
     *              event, including the server instance.
     */
    @SubscribeEvent
    public static void onServerEndTick(ServerTickEvent.Post event) {
        PlatformEvents.SERVER_END_TICK.emit(new ServerEvent.EndTick(event.getServer()));
    }

    /**
     * Handles the server reload event in the NeoForge mod loader environment. This method is called when the server
     * reloads its data packs and resources. It emits a SERVER_RELOAD event to the PlatformEvents system, allowing other
     * parts of the mod to respond to the server reload.
     * @param event The AddReloadListenerEvent event object, which provides context and information about the server
     *              reload event, including the ability to add custom reload listeners.
     */
    @SubscribeEvent
    public static void onServerReload(AddReloadListenerEvent event) {
        event.addListener(new NeoForgeReloadListener(() -> PlatformEvents.SERVER_RELOAD.emit(new ServerEvent.Reload())));
    }

    /**
     * Handles the world tick pre event in the NeoForge mod loader environment. This method is called at the beginning of each
     * world tick, before any game logic is processed. It emits a WORLD_START_TICK event to the PlatformEvents system, allowing
     * other parts of the mod to respond to the start of the world tick. The method takes a LevelTickEvent.Pre event object as
     * a parameter, which provides context and information about the world tick event, including the level (world) instance.
     * @param event The LevelTickEvent.Pre event object, which provides context and information about the world tick event, including the level (world) instance.
     */
    @SubscribeEvent
    public static void onWorldTickPre(LevelTickEvent.Pre event) {
        PlatformEvents.WORLD_START_TICK.emit(new WorldEvent.StartTick(event.getLevel()));
    }

    /**
     * Handles the world tick post event in the NeoForge mod loader environment. This method is called at the end of each
     * world tick, after all game logic has been processed. It emits a WORLD_END_TICK event to the PlatformEvents system,
     * allowing other parts of the mod to respond to the end of the world tick. The method takes a LevelTickEvent.Post event
     * object as a parameter, which provides context and information about the world tick event, including the level (world) instance.
     * @param event The LevelTickEvent.Post event object, which provides context and information about the world tick event, including the level (world) instance.
     */
    @SubscribeEvent
    public static void onWorldTickPost(LevelTickEvent.Post event) {
        PlatformEvents.WORLD_END_TICK.emit(new WorldEvent.EndTick(event.getLevel()));
    }

    /**
     * Handles the server chat event in the NeoForge mod loader environment. This method is called when a player sends a
     * chat message to the server. It emits a SERVER_CHAT event to the PlatformEvents system, allowing other parts of the
     * mod to respond to the chat message. The method takes a ServerChatEvent event object as a parameter, which provides
     * context and information about the chat event, including the player who sent the message and the raw text of the message.
     * If any listener returns true from the SERVER_CHAT event, the chat message is cancelled, preventing it from being processed further.
     * @param event The ServerChatEvent event object, which provides context and information about the chat event, including
     *              the player who sent the message and the raw text of the message.
     */
    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        if (PlatformEvents.SERVER_CHAT.emit(new ServerEvent.Chat(event.getPlayer(), event.getRawText())))
            event.setCanceled(true);
    }

    // ================================================
    // Player Events
    // ================================================

    /**
     * Handles the player join event in the NeoForge mod loader environment. This method is called when a player connects
     * to the server. It emits a PLAYER_JOIN event to the PlatformEvents system, allowing other parts of the mod to
     * respond to the player's connection. The method checks if the entity associated with the event is an instance of
     * ServerPlayer before emitting the event, ensuring that only server-side player entities trigger the event emission.
     * @param event The PlayerEvent.PlayerLoggedInEvent event object, which provides context and information about the
     *              player's connection event, including the player entity.
     */
    @SubscribeEvent
    public static void onPlayerJoin(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof ServerPlayer serverPlayer)) return;
        PlatformEvents.PLAYER_JOIN.emit(new PlayerEvent.Join(serverPlayer));
    }

    /**
     * Handles the player leave event in the NeoForge mod loader environment. This method is called when a player
     * disconnects from the server for any reason. It emits a PLAYER_LEAVE event to the PlatformEvents system, allowing
     * other parts of the mod to respond to the player's disconnection. The method checks if the entity associated with
     * the event is an instance of ServerPlayer before emitting the event, ensuring that only server-side player entities
     * trigger the event emission.
     * @param event The PlayerEvent.PlayerLoggedOutEvent event object, which provides context and information about the
     *              player's disconnection event, including the player entity.
     */
    @SubscribeEvent
    public static void onPlayerLeave(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof ServerPlayer serverPlayer)) return;
        PlatformEvents.PLAYER_LEAVE.emit(new PlayerEvent.Leave(serverPlayer));
    }

    /**
     * Handles the player pre-tick event in the NeoForge mod loader environment. This method is called at the beginning of each
     * server tick for each player, before any game logic is processed. It emits a PLAYER_PRE_TICK event to the PlatformEvents
     * system, allowing other parts of the mod to respond to the start of the player's tick. The method checks if the entity
     * associated with the event is an instance of ServerPlayer before emitting the event, ensuring that only server-side player
     * entities trigger the event emission.
     * @param event The PlayerTickEvent.Pre event object, which provides context and information about the player's tick event,
     *              including the player entity and the current tick phase.
     */
    @SubscribeEvent
    public static void onPlayerPreTick(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PlatformEvents.PLAYER_PRE_TICK.emit(new PlayerEvent.PreTick(serverPlayer));
        }
    }

    /**
     * Handles the player end tick event in the NeoForge mod loader environment. This method is called at the end of each
     * server tick for each player, after all game logic has been processed. It emits a PLAYER_END_TICK event to the PlatformEvents
     * system, allowing other parts of the mod to respond to the end of the player's tick. The method checks if the entity associated
     * with the event is an instance of ServerPlayer before emitting the event, ensuring that only server-side player entities trigger the event emission.
     * @param event The PlayerTickEvent.Post event object, which provides context and information about the player's tick event, including
     *              the player entity and the current tick phase.
     */
    @SubscribeEvent
    public static void onPlayerEndTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PlatformEvents.PLAYER_END_TICK.emit(new PlayerEvent.EndTick(serverPlayer));
        }
    }

    /**
     * Handles the player use item event in the NeoForge mod loader environment. This method is called when a server-side
     * player uses an item. It emits a PLAYER_USE_ITEM event to the PlatformEvents system, allowing other parts of the mod
     * to respond to the player's item usage. The method checks if the entity associated with the event is an instance of
     * ServerPlayer before emitting the event, ensuring that only server-side player entities trigger the event emission.
     * If the result of the event emission is InteractionResult.FAIL, the method cancels the event, preventing further
     * processing of the player's item usage.
     * @param event The PlayerInteractEvent.RightClickItem event object, which provides context and information about the
     *              player's item usage event, including the player entity, the level, and the hand used for the interaction.
     */
    @SubscribeEvent
    public static void onPlayerUseItem(PlayerInteractEvent.RightClickItem event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof ServerPlayer serverPlayer)) return;
        InteractionResult result = PlatformEvents.PLAYER_USE_ITEM.emit(
                new PlayerEvent.UseItem(
                        serverPlayer,
                        event.getLevel(),
                        event.getHand()
                )
        );
        if (result == InteractionResult.FAIL) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    /**
     * Handles the player use block event in the NeoForge mod loader environment. This method is called when a server-side
     * player right-clicks a block. It emits a PLAYER_USE_BLOCK event to the PlatformEvents system, allowing other parts
     * of the mod to respond to the player's block interaction. The method checks if the entity associated with the event
     * is an instance of ServerPlayer before emitting the event, ensuring that only server-side player entities trigger
     * the event emission. If the result of the event emission is InteractionResult.FAIL, the method cancels the event,
     * preventing further processing of the player's block interaction.
     * @param event The PlayerInteractEvent.RightClickBlock event object, which provides context and information about the
     *              player's block interaction event, including the player entity, the level, the hand used for the interaction,
     *              and the position of the block being interacted with.
     */
    @SubscribeEvent
    public static void onPlayerUseBlock(PlayerInteractEvent.RightClickBlock event) {
        var entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof ServerPlayer serverPlayer)) return;
        InteractionResult result = PlatformEvents.PLAYER_USE_BLOCK.emit(
                new PlayerEvent.UseBlock(
                        serverPlayer,
                        event.getLevel(),
                        event.getHand(),
                        event.getPos()
                )
        );
        if (result == InteractionResult.FAIL) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerPickup(ItemEntityPickupEvent.Pre event) {
        var entity = event.getPlayer();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof ServerPlayer serverPlayer)) return;
        boolean result = PlatformEvents.PLAYER_PICKUP_ITEM.emit(new PlayerEvent.PickupItem(serverPlayer, event.getItemEntity()));
        if (result) {
            event.setCanPickup(TriState.FALSE);
        }
    }
}
