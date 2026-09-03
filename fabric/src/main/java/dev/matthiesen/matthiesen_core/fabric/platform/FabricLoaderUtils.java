package dev.matthiesen.matthiesen_core.fabric.platform;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.config.ConfigEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.Environment;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModContainer;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.LoaderPlatformMeta;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.fabric.events.PlatformEventsBusListener;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;

import java.nio.file.Path;

/**
 * The FabricLoaderUtils class implements the CommonLoaderUtils interface and provides utility methods for interacting with the Fabric mod loader environment.
 */
public final class FabricLoaderUtils implements CommonLoaderUtils {

    /**
     * Constructs a new instance of FabricLoaderUtils.
     * This class is not intended to be instantiated, as it provides static utility methods for interacting with the Fabric mod loader environment.
     */
    public FabricLoaderUtils() {}

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
                ModConfig.Type configType = parseConfigType(type);
                NeoForgeConfigRegistry.INSTANCE.register(getModId(), configType, configSpec);
            }

            @Override
            public void registerConfig(ModConfigType type, IConfigSpec configSpec, String filename) {
                ModConfig.Type configType = parseConfigType(type);
                NeoForgeConfigRegistry.INSTANCE.register(getModId(), configType, configSpec, filename);
            }

            @Override
            public void registerConfigLoadingListener() {
                NeoForgeModConfigEvents.loading(getModId()).register(config ->
                        PlatformEvents.CONFIG_LOADING(getModId()).emit(new ConfigEvent.Loading(toCommonConfig(config))));
            }

            @Override
            public void registerConfigUnloadingListener() {
                NeoForgeModConfigEvents.unloading(getModId()).register(config ->
                        PlatformEvents.CONFIG_UNLOADING(getModId()).emit(new ConfigEvent.Unloading(toCommonConfig(config))));
            }

            @Override
            public void registerConfigReloadingListener() {
                NeoForgeModConfigEvents.reloading(getModId()).register(config ->
                        PlatformEvents.CONFIG_RELOADING(getModId()).emit(new ConfigEvent.Reloading(toCommonConfig(config))));
            }

            private ModConfig.Type parseConfigType(ModConfigType configType) {
                return switch (configType) {
                    case COMMON -> ModConfig.Type.COMMON;
                    case CLIENT -> ModConfig.Type.CLIENT;
                    case SERVER -> ModConfig.Type.SERVER;
                    case STARTUP -> ModConfig.Type.STARTUP;
                };
            }

            private ModConfigType parseConfigType(ModConfig.Type configType) {
                return switch (configType) {
                    case COMMON -> ModConfigType.COMMON;
                    case CLIENT -> ModConfigType.CLIENT;
                    case SERVER -> ModConfigType.SERVER;
                    case STARTUP -> ModConfigType.STARTUP;
                };
            }

            private dev.matthiesen.matthiesen_core.common.api.events.config.ModConfig toCommonConfig(ModConfig config) {
                return new dev.matthiesen.matthiesen_core.common.api.events.config.ModConfig(
                        parseConfigType(config.getType()),
                        config.getSpec(),
                        config.getFileName(),
                        config.getModId(),
                        config.getLoadedConfig()
                );
            }
        };
    }
}
