package dev.matthiesen.matthiesen_core.neoforge;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.network.PacketContext;
import dev.matthiesen.matthiesen_core.neoforge.platform.NeoForgeLoaderNetworking;
import dev.matthiesen.matthiesen_core.neoforge.platform.helpers.NeoForgeRegistryHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * The MatthiesenCoreNeoForge class serves as the main entry point for the Matthiesen Core mod when running on the NeoForge mod loader.
 */
@Mod(MatthiesenCoreCommon.MOD_ID)
public final class MatthiesenCoreNeoForge {
    /**
     * A reference to the MatthiesenCoreCommon instance, which is initialized during the construction of this class. This instance
     * is used to perform common initialization tasks and logging.
     */
    public static MatthiesenCoreCommon INSTANCE;

    /**
     * A volatile reference to the MinecraftServer instance, which is set when the server starts and cleared when the server stops.
     * This allows other parts of the mod to access the server instance safely across different threads.
     */
    public static volatile MinecraftServer SERVER_INSTANCE;

    /**
     * Constructs a new instance of the MatthiesenCoreNeoForge class. This constructor initializes the MatthiesenCoreCommon instance,
     * sets up the NeoForge registry helper, and performs common initialization tasks for the mod.
     * @param modBus The event bus used for registering mod events and listeners. This is provided by the NeoForge mod loader and
     *               is used to register various components and event handlers for the mod.
     */
    public MatthiesenCoreNeoForge(IEventBus modBus) {
        INSTANCE = MatthiesenCoreCommon.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        NeoForgeRegistryHelper.init(modBus);
        INSTANCE.initialize();
    }

    /**
     * The ServerEventsSubscriber class is an inner static class that subscribes to server-related events in the NeoForge mod
     * loader environment. It handles common setup, server starting, server stopping, and payload registration events.
     */
    @EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID)
    public static class ServerEventsSubscriber {

        /**
         * Handles the common setup event for the mod. This method is called during the common setup phase of the mod loading
         * process and is used to perform any necessary initialization tasks that need to be executed on both the client and server sides.
         * @param event The FMLCommonSetupEvent event object, which provides context and information about the common setup phase of the mod loading process.
         */
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(INSTANCE::onCommonServerSetup);
        }

        /**
         * Handles the server starting event for the mod. This method is called when the Minecraft server is starting, and it sets
         * the SERVER_INSTANCE reference to the current server instance. This allows other parts of the mod to access the server
         * instance safely across different threads.
         * @param event The ServerStartingEvent event object, which provides context and information about the server starting phase of the mod loading process.
         */
        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            SERVER_INSTANCE = event.getServer();
        }

        /**
         * Handles the server stopped event for the mod. This method is called when the Minecraft server has stopped, and it
         * clears the SERVER_INSTANCE reference to null. This ensures that other parts of the mod do not hold onto a reference
         * to a server instance that is no longer valid.
         * @param event The ServerStoppedEvent event object, which provides context and information about the server stopped phase of the mod loading process.
         */
        @SubscribeEvent
        public static void onServerStopped(ServerStoppedEvent event) {
            SERVER_INSTANCE = null;
        }

        /**
         * Handles the payload registration event for the mod. This method is called when the mod loader is registering custom packet payloads,
         * and it registers any pending client-to-server (C2S) and server-to-client (S2C) payloads that have been queued up for registration.
         * After registering the payloads, it clears the pending lists to ensure that they are not registered again in future events.
         * @param event The RegisterPayloadHandlersEvent event object, which provides context and information about the payload
         *              registration phase of the mod loading process.
         */
        @SubscribeEvent
        public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1.0.0");
            registerAndClearPending(NeoForgeLoaderNetworking.PENDING_C2S, MatthiesenCoreNeoForge::registerC2S, registrar);
            registerAndClearPending(NeoForgeLoaderNetworking.PENDING_S2C, MatthiesenCoreNeoForge::registerS2C, registrar);
        }

    }

    private static void registerAndClearPending(
            List<NeoForgeLoaderNetworking.PayloadRegistration<?, ?>> pendingList,
            BiConsumer<PayloadRegistrar, NeoForgeLoaderNetworking.PayloadRegistration<?, ?>> registerFunction,
            PayloadRegistrar registrar
    ) {
        for (var reg : pendingList) {
            registerFunction.accept(registrar, reg);
        }
        pendingList.clear();
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> void registerC2S(PayloadRegistrar registrar, NeoForgeLoaderNetworking.PayloadRegistration<?, ?> reg) {
        var type = (CustomPacketPayload.Type<T>) reg.type();
        var codec = (net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, T>) reg.codec();
        var handler = (java.util.function.BiConsumer<T, PacketContext>) reg.handler();

        registrar.playToServer(type, codec, (payload, context) -> {
            PacketContext packetContext = new PacketContext(context.player(), () -> {
                context.enqueueWork(() -> {});
                return null;
            });
            handler.accept(payload, packetContext);
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> void registerS2C(PayloadRegistrar registrar, NeoForgeLoaderNetworking.PayloadRegistration<?, ?> reg) {
        var type = (CustomPacketPayload.Type<T>) reg.type();
        var codec = (net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, T>) reg.codec();
        var handler = (java.util.function.BiConsumer<T, PacketContext>) reg.handler();

        registrar.playToClient(type, codec, (payload, context) -> {
            PacketContext packetContext = new PacketContext(context.player(), () -> {
                context.enqueueWork(() -> {});
                return null;
            });
            handler.accept(payload, packetContext);
        });
    }
}
