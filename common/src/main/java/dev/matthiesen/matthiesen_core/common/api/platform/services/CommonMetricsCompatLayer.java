package dev.matthiesen.matthiesen_core.common.api.platform.services;

public interface CommonMetricsCompatLayer
{

    void initServer();

    boolean clientOnlineMode();

    int clientPlayerCount();

    boolean serverOnlineMode();

    int serverPlayerCount();

    String platformLabel();
}
