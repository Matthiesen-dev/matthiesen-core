package dev.matthiesen.matthiesen_lib.fabric.platform;

import dev.matthiesen.matthiesen_lib.common.api.platform.Environment;
import dev.matthiesen.matthiesen_lib.common.api.platform.ModContainer;
import dev.matthiesen.matthiesen_lib.common.api.platform.Platform;
import dev.matthiesen.matthiesen_lib.common.api.platform.CommonLoaderUtils;
import dev.matthiesen.matthiesen_lib.fabric.MatthiesenLibFabric;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public final class FabricLoaderUtils implements CommonLoaderUtils {
    @Override
    public MinecraftServer getServer() {
        return MatthiesenLibFabric.SERVER_INSTANCE;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public Path getGameDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public Path getModConfig(String dir, String file) {
        return FabricLoader.getInstance().getConfigDir().resolve(dir).resolve(file);
    }

    @Override
    public Environment getEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT ? Environment.CLIENT : Environment.SERVER;
    }

    @Override
    public ModContainer getModContainer(String modId) {
        var fabricModContainer = FabricLoader.getInstance().getModContainer(modId);
        if (fabricModContainer.isEmpty()) return null;
        var loadedFabricModContainer = fabricModContainer.get();
        return new ModContainer() {
            @Override
            public String getModName() {
                return loadedFabricModContainer.getMetadata().getName();
            }

            @Override
            public String getModVersion() {
                return loadedFabricModContainer.getMetadata().getVersion().getFriendlyString();
            }

            @Override
            public Platform getPlatformData() {
                return Platform.FABRIC;
            }
        };
    }
}
