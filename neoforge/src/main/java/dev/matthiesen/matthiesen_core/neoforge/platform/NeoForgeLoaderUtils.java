package dev.matthiesen.matthiesen_core.neoforge.platform;

import dev.matthiesen.matthiesen_core.common.api.platform.loader.Environment;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModContainer;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.LoaderPlatformMeta;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.neoforge.events.PlatformEventsBusListener;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.nio.file.Path;

/**
 * The NeoForgeLoaderUtils class implements the CommonLoaderUtils interface and provides utility methods for interacting with
 * the NeoForge mod loader environment. It allows for retrieving the Minecraft server instance, checking if the environment
 * is a development environment, verifying if a specific mod is loaded, obtaining game directories and configuration paths,
 * determining the current environment (client or server), and accessing mod container information. This class serves as a
 * bridge between the common utility interface and the specific functionalities provided by NeoForge.
 */
public final class NeoForgeLoaderUtils implements CommonLoaderUtils {
    @Override
    public MinecraftServer getServer() {
        return PlatformEventsBusListener.SERVER_INSTANCE;
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
    public LoaderPlatformMeta getLoaderMeta() {
        return LoaderPlatformMeta.NEOFORGE;
    }

    @Override
    public ModContainer getModContainer(String modId) {
        var neoForgeModContainer = ModList.get().getModContainerById(modId);
        if (neoForgeModContainer.isEmpty()) return null;
        var loadedNeoForgeModContainer = neoForgeModContainer.get();
        return new ModContainer() {
            @Override
            public String getModId() {
                return loadedNeoForgeModContainer.getModInfo().getModId();
            }

            @Override
            public String getModName() {
                return loadedNeoForgeModContainer.getModInfo().getDisplayName();
            }

            @Override
            public String getModVersion() {
                return loadedNeoForgeModContainer.getModInfo().getVersion().toString();
            }

            @Override
            public LoaderPlatformMeta getPlatformData() {
                return LoaderPlatformMeta.NEOFORGE;
            }

            private boolean registeredConfigScreen = false;

            @Override
            public void registerConfig(ModConfigType type, IConfigSpec configSpec) {
                ModConfig.Type configType = parseConfigType(type);
                loadedNeoForgeModContainer.registerConfig(configType, configSpec);
                if (!registeredConfigScreen && getEnvironment() == Environment.CLIENT) {
                    loadedNeoForgeModContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
                    registeredConfigScreen = true;
                }
            }

            @Override
            public void registerConfig(ModConfigType type, IConfigSpec configSpec, String filename) {
                ModConfig.Type configType = parseConfigType(type);
                loadedNeoForgeModContainer.registerConfig(configType, configSpec, filename);
                if (!registeredConfigScreen && getEnvironment() == Environment.CLIENT) {
                    loadedNeoForgeModContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
                    registeredConfigScreen = true;
                }
            }

            private ModConfig.Type parseConfigType(ModConfigType configType) {
                return switch (configType) {
                    case COMMON -> ModConfig.Type.COMMON;
                    case CLIENT -> ModConfig.Type.CLIENT;
                    case SERVER -> ModConfig.Type.SERVER;
                    case STARTUP -> ModConfig.Type.STARTUP;
                };
            }
        };
    }
}
