package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.core.network.PacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
final class FabricLoaderClientNetworking {
    private FabricLoaderClientNetworking() {}

    static <T extends CustomPacketPayload> void registerS2CReceiver(
            CustomPacketPayload.Type<T> type,
            BiConsumer<T, PacketContext> handler
    ) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> handler.accept(payload, new PacketContext(context.player(), () -> {
            context.client().execute(() -> {});
            return null;
        })));
    }

    static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}

