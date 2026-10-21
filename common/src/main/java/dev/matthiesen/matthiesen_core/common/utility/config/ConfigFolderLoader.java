package dev.matthiesen.matthiesen_core.common.utility.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A utility class for loading and managing .toml configuration files from a specified folder using NightConfig.
 * This class provides methods to load, clear, and retrieve configuration files in a structured manner.
 */
@SuppressWarnings("unused")
public final class ConfigFolderLoader {
    private final AbstractCommonMod mod;
    private final Path folder;
    private final Map<String, FileConfig> LOADED_CONFIGS = new HashMap<>();

    /**
     * Constructs a new ConfigFolderLoader for the specified mod and folder.
     *
     * @param mod    The mod instance for which the configuration files are being loaded.
     * @param folder The name of the folder containing the .toml configuration files.
     */
    public ConfigFolderLoader(AbstractCommonMod mod, String folder) {
        this.mod = mod;
        this.folder = mod.getCommonUtils().getModConfig(mod.getModId(), folder);
    }

    /**
     * Clears all loaded configurations and closes their associated FileConfig instances.
     */
    public void clearConfigs() {
        LOADED_CONFIGS.values().forEach(FileConfig::close);
        LOADED_CONFIGS.clear();
    }

    /**
     * Loads all .toml configuration files from the specified folder. If the folder does not exist, it will be created.
     * Any previously loaded configurations will be cleared before loading new ones.
     */
    public void loadConfigs() {
        loadConfigs(true);
    }

    /**
     * Loads all .toml configuration files from the specified folder. If the folder does not exist, it will be created.
     * Any previously loaded configurations will be cleared before loading new ones if {@code clearBeforeLoad} is true.
     *
     * @param clearBeforeLoad Whether to clear previously loaded configurations before loading new ones.
     */
    public void loadConfigs(boolean clearBeforeLoad) {
        if (clearBeforeLoad) {
            clearConfigs();
        }

        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                mod.createErrorLog("Failed to create config folder: " + e.getMessage(), e);
                return;
            }
        }

        try (Stream<Path> stream = Files.list(folder)) {
            stream.filter(path -> path.toString().endsWith(".toml"))
                    .forEach(this::loadConfigFile);
        } catch (IOException e) {
            mod.createErrorLog("Failed to load configs: " + e.getMessage(), e);
        }
    }

    /**
     * Returns a map of loaded configuration files, where the key is the configuration name (without the .toml extension) and the value is the corresponding FileConfig object.
     *
     * @return A map of loaded configuration files.
     */
    public Map<String, FileConfig> getLoadedConfigs() {
        return LOADED_CONFIGS;
    }

    private void loadConfigFile(Path path) {
        try {
            String fileName = path.getFileName().toString();
            String configName = fileName.substring(0, fileName.lastIndexOf('.'));
            FileConfig config = FileConfig.builder(path)
                    .autosave()
                    .preserveInsertionOrder()
                    .build();
            config.load();
            LOADED_CONFIGS.put(configName, config);
            mod.createInfoLog("Loaded config file: " + configName);
        } catch (Exception e) {
            mod.createErrorLog("Failed to load config file '" + path.getFileName() + "': " + e.getMessage(), e);
        }
    }
}
