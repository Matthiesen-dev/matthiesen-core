package dev.matthiesen.matthiesen_core.common.api.platform.services;

import dev.matthiesen.matthiesen_core.common.core.network.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;

public interface CommonLoaderNetworking {
    // Client-to-Server

    /**
     * Registers a client-to-server (C2S) packet type with the specified codec and handler.
     * @param type The custom packet type to register.
     * @param codec The codec for encoding and decoding the packet.
     * @param handler The handler to process the packet when received.
     * @param <T> The type of the custom packet payload.
     */
    <T extends CustomPacketPayload> void registerC2S(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    );

    // Server-to-Client

    /**
     * Registers a server-to-client (S2C) packet type with the specified codec and handler.
     * @param type The custom packet type to register.
     * @param codec The codec for encoding and decoding the packet.
     * @param handler The handler to process the packet when received.
     * @param <T> The type of the custom packet payload.
     */
    <T extends CustomPacketPayload> void registerS2C(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    );

    /**
     * Sends a custom packet payload to the server.
     * @param payload The custom packet payload to send.
     */
    void sendToServer(CustomPacketPayload payload);

    /**
     * Sends a custom packet payload to a specific player on the server.
     * @param player The player to send the packet to.
     * @param payload The custom packet payload to send.
     */
    void sendToPlayer(ServerPlayer player, CustomPacketPayload payload);
}
