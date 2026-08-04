package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.loader.Environment;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModContainer;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.LoaderPlatformMeta;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.fabric.events.PlatformEventsBusListener;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;

import java.nio.file.Path;

/**
 * The FabricLoaderUtils class implements the CommonLoaderUtils interface and provides utility methods for interacting with the Fabric mod loader environment.
 */
public final class FabricLoaderUtils implements CommonLoaderUtils {
    @Override
    public MinecraftServer getServer() {
        return PlatformEventsBusListener.SERVER_INSTANCE;
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
    public LoaderPlatformMeta getLoaderMeta() {
        return LoaderPlatformMeta.FABRIC;
    }

    @Override
    public ModContainer getModContainer(String modId) {
        var fabricModContainer = FabricLoader.getInstance().getModContainer(modId);
        if (fabricModContainer.isEmpty()) return null;
        var loadedFabricModContainer = fabricModContainer.get();
        return new ModContainer() {
            @Override
            public String getModId() {
                return loadedFabricModContainer.getMetadata().getId();
            }

            @Override
            public String getModName() {
                return loadedFabricModContainer.getMetadata().getName();
            }

            @Override
            public String getModVersion() {
                return loadedFabricModContainer.getMetadata().getVersion().getFriendlyString();
            }

            @Override
            public LoaderPlatformMeta getPlatformData() {
                return LoaderPlatformMeta.FABRIC;
            }

            @Override
            public void registerConfig(ModConfigType type, IConfigSpec configSpec) {
                ModConfig.Type configType;
                switch (type) {
                    case COMMON -> configType = ModConfig.Type.COMMON;
                    case CLIENT -> configType = ModConfig.Type.CLIENT;
                    case SERVER -> configType = ModConfig.Type.SERVER;
                    default -> throw new IllegalArgumentException("Unknown config type: " + type);
                }
                NeoForgeConfigRegistry.INSTANCE.register(getModId(), configType, configSpec);
            }
        };
    }
}
