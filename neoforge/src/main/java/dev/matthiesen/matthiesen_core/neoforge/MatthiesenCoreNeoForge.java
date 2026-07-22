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

@Mod(MatthiesenCoreCommon.MOD_ID)
public final class MatthiesenCoreNeoForge {
    public static MatthiesenCoreCommon INSTANCE;
    public static volatile MinecraftServer SERVER_INSTANCE;

    public MatthiesenCoreNeoForge(IEventBus modBus) {
        INSTANCE = MatthiesenCoreCommon.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        NeoForgeRegistryHelper.init(modBus);
        INSTANCE.initialize();
    }

    @EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID)
    public static class ServerEventsSubscriber {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(INSTANCE::onCommonServerSetup);
        }

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            SERVER_INSTANCE = event.getServer();
        }

        @SubscribeEvent
        public static void onServerStopped(ServerStoppedEvent event) {
            SERVER_INSTANCE = null;
        }

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
