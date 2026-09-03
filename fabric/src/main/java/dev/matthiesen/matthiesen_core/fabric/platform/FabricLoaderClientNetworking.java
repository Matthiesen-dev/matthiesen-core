package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.core.network.PacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.BiConsumer;

/**
 * The FabricLoaderClientNetworking class provides client-side networking functionalities for the Fabric mod loader.
 * It is used internally by the FabricLoaderNetworking class to handle client-to-server and server-to-client packet registrations and transmissions.
 */
@Environment(EnvType.CLIENT)
final class FabricLoaderClientNetworking {
    /**
     * Constructs a new instance of FabricLoaderClientNetworking.
     * This class is not intended to be instantiated, as it provides static utility methods for client-side networking functionalities.
     */
    private FabricLoaderClientNetworking() {}

    /**
     * Registers a server-to-client packet receiver for the specified packet type.
     *
     * @param type    The type of the custom packet payload.
     * @param handler The handler to process the received packet and its context.
     * @param <T>     The type of the custom packet payload.
     */
    static <T extends CustomPacketPayload> void registerS2CReceiver(
            CustomPacketPayload.Type<T> type,
            BiConsumer<T, PacketContext> handler
    ) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> handler.accept(payload, new PacketContext(context.player(), () -> {
            context.client().execute(() -> {});
            return null;
        })));
    }

    /**
     * Sends a custom packet payload to the server.
     *
     * @param payload The custom packet payload to be sent to the server.
     */
    static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}

