package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderNetworking;
import dev.matthiesen.matthiesen_core.common.core.network.PacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;

/**
 * The FabricLoaderNetworking class implements the CommonLoaderNetworking interface and provides networking functionalities for the Fabric mod loader.
 */
public final class FabricLoaderNetworking implements CommonLoaderNetworking {
    @Override
    public <T extends CustomPacketPayload> void registerC2S(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        // 1. Register the codec to Fabric's networking registry
        PayloadTypeRegistry.playC2S().register(type, codec);

        // 2. Bind the receiver to execute on Minecraft's server thread
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> handler.accept(payload, new PacketContext(context.player(), () -> {
            context.server().execute(() -> {}); // Force sync
            return null;
        })));
    }

    @Override
    public <T extends CustomPacketPayload> void registerS2C(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        // 1. Register codec to Fabric's S2C Registry
        PayloadTypeRegistry.playS2C().register(type, codec);

        // 2. Register client-side handler only when running in a client environment.
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            FabricLoaderClientNetworking.registerS2CReceiver(type, handler);
        }
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            throw new IllegalStateException("sendToServer is only available in the Fabric client environment.");
        }
        FabricLoaderClientNetworking.sendToServer(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }
}
