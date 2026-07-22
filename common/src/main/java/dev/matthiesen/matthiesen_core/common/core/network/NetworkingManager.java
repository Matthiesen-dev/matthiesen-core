package dev.matthiesen.matthiesen_core.common.core.network;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;

/**
 * The NetworkingManager class is a singleton responsible for managing network communications within the application.
 * It provides methods to register custom packet types for both client-to-server (C2S) and server-to-client (S2C) communications,
 * as well as methods to send packets to the server or specific players. This class utilizes the CommonLoaderNetworking service
 * to handle the underlying networking operations.
 */
@SuppressWarnings("unused")
public final class NetworkingManager {
    private static final CommonLoaderNetworking NETWORKING_SERVICE =
            ServiceLoader.load(CommonLoaderNetworking.class).findFirst().orElseThrow();

    /**
     * Retrieves the singleton instance of the NetworkingManager.
     * This instance is used to manage network communications, including registering packet types and sending packets.
     */
    public static final NetworkingManager INSTANCE = new NetworkingManager();

    private NetworkingManager() {}

    private boolean initialized;

    /**
     * Initializes the NetworkingManager, setting up necessary configurations and preparing it for use.
     * This method should be called during the mod's initialization phase to ensure that the networking system is
     * ready for registering packet types and handling network communications.
     */
    public void initialize(MatthiesenCoreCommon modInstance) {
        if (initialized) return;
        initialized = true;
        modInstance.createInfoLog("Initializing NetworkingManager");
    }

    /**
     * Registers a client-to-server (C2S) packet type with the specified codec and handler.
     * @param type The custom packet type to register.
     * @param codec The codec for encoding and decoding the packet.
     * @param handler The handler to process the packet when received.
     * @param <T> The type of the custom packet payload.
     */
    public <T extends CustomPacketPayload> void registerC2S(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        NETWORKING_SERVICE.registerC2S(type, codec, handler);
    }

    /**
     * Registers a server-to-client (S2C) packet type with the specified codec and handler.
     * @param type The custom packet type to register.
     * @param codec The codec for encoding and decoding the packet.
     * @param handler The handler to process the packet when received.
     * @param <T> The type of the custom packet payload.
     */
    public <T extends CustomPacketPayload> void registerS2C(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        NETWORKING_SERVICE.registerS2C(type, codec, handler);
    }

    /**
     * Sends a custom packet payload to the server.
     * @param payload The custom packet payload to send.
     */
    public void sendToServer(CustomPacketPayload payload) {
        NETWORKING_SERVICE.sendToServer(payload);
    }

    /**
     * Sends a custom packet payload to a specific player on the server.
     * @param player The player to send the packet to.
     * @param payload The custom packet payload to send.
     */
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        NETWORKING_SERVICE.sendToPlayer(player, payload);
    }
}
