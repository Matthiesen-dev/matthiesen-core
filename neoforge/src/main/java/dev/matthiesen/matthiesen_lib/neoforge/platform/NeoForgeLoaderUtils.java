package dev.matthiesen.matthiesen_lib.neoforge.platform;

import dev.matthiesen.matthiesen_lib.common.api.platform.Environment;
import dev.matthiesen.matthiesen_lib.common.api.platform.ModContainer;
import dev.matthiesen.matthiesen_lib.common.api.platform.Platform;
import dev.matthiesen.matthiesen_lib.common.api.platform.CommonLoaderUtils;
import dev.matthiesen.matthiesen_lib.neoforge.MatthiesenLibNeoForge;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class NeoForgeLoaderUtils implements CommonLoaderUtils {
    @Override
    public MinecraftServer getServer() {
        return MatthiesenLibNeoForge.SERVER_INSTANCE;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public Path getGameDirectory() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getModConfig(String dir, String file) {
        Path configDir = FMLPaths.CONFIGDIR.get();
        return configDir.resolve(dir).resolve(file);
    }

    @Override
    public Environment getEnvironment() {
        return FMLEnvironment.dist.isClient() ? Environment.CLIENT : Environment.SERVER;
    }

    @Override
    public ModContainer getModContainer(String modId) {
        var neoForgeModContainer = ModList.get().getModContainerById(modId);
        if (neoForgeModContainer.isEmpty()) return null;
        var loadedNeoForgeModContainer = neoForgeModContainer.get();
        return new ModContainer() {
            @Override
            public String getModName() {
                return loadedNeoForgeModContainer.getModInfo().getDisplayName();
            }

            @Override
            public String getModVersion() {
                return loadedNeoForgeModContainer.getModInfo().getVersion().toString();
            }

            @Override
            public Platform getPlatformData() {
                return Platform.NEOFORGE;
            }
        };
    }
}
