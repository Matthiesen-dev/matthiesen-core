package dev.matthiesen.matthiesen_core.neoforge.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonMetricsCompatLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.jetbrains.annotations.Nullable;

public final class NeoForgeMetricsCompatLayer implements CommonMetricsCompatLayer {
    private @Nullable MinecraftServer server;

    @Override
    public void initServer() {
        NeoForge.EVENT_BUS.addListener((final ServerStartedEvent event) -> this.server = event.getServer());
    }

    @Override
    public boolean clientOnlineMode() {
        if (!FMLEnvironment.production) return true;
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
        if (!FMLEnvironment.production) return true;
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
        return "neoforge-" + (FMLEnvironment.dist.isClient() ? "client" : "server");
    }
}
