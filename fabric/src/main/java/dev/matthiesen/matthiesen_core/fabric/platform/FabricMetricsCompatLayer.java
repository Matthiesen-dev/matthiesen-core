package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonMetricsCompatLayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

public final class FabricMetricsCompatLayer implements CommonMetricsCompatLayer {
    private @Nullable MinecraftServer server;

    @Override
    public void initServer() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> this.server = server);
    }

    @Override
    public boolean clientOnlineMode() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) return true;

        final var client = Minecraft.getInstance();
        return client.getUser().getXuid().isPresent();
    }

    @Override
    public int clientPlayerCount() {
        final var client = Minecraft.getInstance();
        final var connection = client.getConnection();
        if (connection != null) return connection.getOnlinePlayers().size();

        final var server = client.getSingleplayerServer();
        if (server != null) return server.getPlayerCount();

        return client.player == null ? 0 : 1;
    }

    @Override
    public boolean serverOnlineMode() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) return true;
        assert server != null : "Server not initialized";
        return server.usesAuthentication();
    }

    @Override
    public int serverPlayerCount() {
        assert server != null : "Server not initialized";
        return server.getPlayerCount();
    }

    @Override
    public String platformLabel() {
        var env = FabricLoader.getInstance().getEnvironmentType();
        return "fabric-" + env.name().toLowerCase();
    }
}
