package dev.matthiesen.matthiesen_core.neoforge.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderNetworking;
import dev.matthiesen.matthiesen_core.common.core.network.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public final class NeoForgeLoaderNetworking implements CommonLoaderNetworking {
    // Defer processing until the lifecycle event runs
    public static final List<PayloadRegistration<?, ?>> PENDING_C2S = new CopyOnWriteArrayList<>();
    public static final List<PayloadRegistration<?, ?>> PENDING_S2C = new CopyOnWriteArrayList<>();

    @Override
    public <T extends CustomPacketPayload> void registerC2S(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        PENDING_C2S.add(new PayloadRegistration<>(type, codec, handler));
    }

    @Override
    public <T extends CustomPacketPayload> void registerS2C(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        PENDING_S2C.add(new PayloadRegistration<>(type, codec, handler));
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    // Immutable record to store cross-thread cache data
    public record PayloadRegistration<T extends CustomPacketPayload, B extends RegistryFriendlyByteBuf>(
            CustomPacketPayload.Type<T> type,
            StreamCodec<B, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {}
}
