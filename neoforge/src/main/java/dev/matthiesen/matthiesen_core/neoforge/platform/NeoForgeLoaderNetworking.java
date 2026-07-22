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

/**
 * The NeoForgeLoaderNetworking class implements the CommonLoaderNetworking interface and provides networking functionalities
 * for the NeoForge mod loader. It allows for the registration of custom packet payloads for both client-to-server (C2S) and
 * server-to-client (S2C) communication, as well as sending packets to the server or specific players. The class maintains lists
 * of pending payload registrations that are processed during the appropriate lifecycle events, ensuring that networking is set
 * up correctly within the NeoForge environment.
 */
public final class NeoForgeLoaderNetworking implements CommonLoaderNetworking {
    // Defer processing until the lifecycle event runs
    /**
     * A thread-safe list of pending client-to-server (C2S) payload registrations. Each entry in the list represents a
     * registration of a custom packet payload type, along with its associated codec and handler. These registrations are
     * processed during the appropriate lifecycle events to ensure that networking is set up correctly.
     */
    public static final List<PayloadRegistration<?, ?>> PENDING_C2S = new CopyOnWriteArrayList<>();

    /**
     * A thread-safe list of pending server-to-client (S2C) payload registrations. Each entry in the list represents a
     * registration of a custom packet payload type, along with its associated codec and handler. These registrations are
     * processed during the appropriate lifecycle events to ensure that networking is set up correctly.
     */
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

    /**
     * Represents a registration of a custom packet payload type, along with its associated codec and handler. This record is
     * used to store the necessary information for processing payload registrations in a thread-safe manner. The type parameter
     * T represents the specific custom packet payload type, while B represents the type of the byte buffer used for encoding
     * and decoding the payload.
     * @param type The custom packet payload type being registered. This is used to identify the specific type of payload
     *             for which the registration is being made.
     * @param codec The codec used for encoding and decoding the custom packet payload. This is responsible for converting
     *              the payload to and from a byte buffer representation.
     * @param handler The handler that processes the custom packet payload when it is received. This is a BiConsumer that
     *                takes the payload and a PacketContext, allowing for appropriate handling of the payload in the
     *                context of the network communication.
     * @param <T> The specific custom packet payload type being registered. This type must extend CustomPacketPayload to ensure
     *           that it conforms to the expected structure and behavior of custom packet payloads.
     * @param <B> The type of the byte buffer used for encoding and decoding the custom packet payload. This type must extend
     *           RegistryFriendlyByteBuf to ensure that it is compatible with the expected byte buffer structure used in the NeoForge networking system.
     */
    public record PayloadRegistration<T extends CustomPacketPayload, B extends RegistryFriendlyByteBuf>(
            CustomPacketPayload.Type<T> type,
            StreamCodec<B, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {}
}
