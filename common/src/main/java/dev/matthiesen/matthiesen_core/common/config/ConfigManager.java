package dev.matthiesen.matthiesen_core.common.config;

import com.google.gson.*;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;

/**
 * A generic configuration manager for handling JSON-based config files.
 * It supports loading, saving, and merging default values with existing config files.
 * @param <T> The type of the config class to manage
 */
@SuppressWarnings("unused")
public final class ConfigManager<T> {
    private static final Logger LOGGER = LogManager.getLogger(MatthiesenCoreCommon.MOD_NAME);

    private final Class<T> configClass;
    private final String configName;
    private final String modId;
    private final Gson gson;
    private T config;

    /**
     * Creates a new ConfigManager for the specified config class, name, and mod id namespace.
     *
     * @param configClass The class of the config to manage
     * @param configName The name of the config file (without .json extension)
     * @param modId The mod id namespace used for the config folder
     */
    public ConfigManager(Class<T> configClass, String configName, String modId) {
        this.configClass = configClass;
        this.configName = configName;
        this.modId = modId;
        this.gson = getGsonFromConfigClass();
    }

    /**
     * Attempts to get the GSON instance from the config class.
     * Falls back to creating a new Gson instance if not available.
     */
    private Gson getGsonFromConfigClass() {
        try {
            Field gsonField = configClass.getDeclaredField("GSON");
            gsonField.setAccessible(true);
            return (Gson) gsonField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            return new GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create();
        }
    }

    /**
     * Creates a new instance of the config class.
     */
    private T createDefaultConfig() {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the config from the file system. If the config file does not exist, it will create a new one with default values.
     * @return The loaded config instance
     */
    public T loadConfig() {
        String configFileLoc = getConfigFileLocation();
        LOGGER.debug("Loading config file found at: {}", configFileLoc);
        File configFile = new File(configFileLoc);
        boolean madeDir = configFile.getParentFile().mkdirs();

        if (madeDir) {
            LOGGER.debug("Config Directory exists");
        }

        if (configFile.exists()) {
            try (FileReader fileReader = new FileReader(configFile)) {
                T defaultConfig = createDefaultConfig();
                String defaultConfigJson = gson.toJson(defaultConfig);

                JsonElement fileConfigElement = JsonParser.parseReader(fileReader);
                JsonElement defaultConfigElement = JsonParser.parseString(defaultConfigJson);

                JsonElement mergedConfigElement = mergeConfigs(
                        defaultConfigElement.getAsJsonObject(),
                        fileConfigElement.getAsJsonObject()
                );

                config = gson.fromJson(mergedConfigElement, configClass);
            } catch (Exception e) {
                LOGGER.error("Failed to load the config! Using default config as fallback", e);
                config = createDefaultConfig();
            }
        } else {
            config = createDefaultConfig();
        }

        saveConfig();
        return config;
    }

    /**
     * Merges the default config with the file config. If a key is missing in the file config, it will be added from the default config.
     * If a key is present in both configs and is a nested object, it will recursively merge them.
     * @param defaultConfig The default config as a JsonObject
     * @param fileConfig The file config as a JsonObject
     * @return The merged config as a JsonElement
     */
    private JsonElement mergeConfigs(JsonObject defaultConfig, JsonObject fileConfig) {
        MatthiesenCoreCommon.INSTANCE.createDebugLog("Checking for config merge.");
        boolean merged = false;

        for (String key : defaultConfig.keySet()) {
            if (!fileConfig.has(key)) {
                fileConfig.add(key, defaultConfig.get(key));
                LOGGER.debug("{} not found in file config, adding from default.", key);
                merged = true;
            } else if (defaultConfig.get(key).isJsonObject() && fileConfig.get(key).isJsonObject()) {
                mergeConfigs(defaultConfig.getAsJsonObject(key), fileConfig.getAsJsonObject(key));
            }
        }

        if (merged) {
            LOGGER.debug("Successfully merged config.");
        }

        return fileConfig;
    }

    /**
     * Saves the current config to the file system. If the config is null, it will not save and log an error.
     */
    public void saveConfig() {
        try {
            String configFileLoc = getConfigFileLocation();
            LOGGER.debug("Saving config to: {}", configFileLoc);
            File configFile = new File(configFileLoc);
            try (FileWriter fileWriter = new FileWriter(configFile)) {
                gson.toJson(config, fileWriter);
                fileWriter.flush();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    /**
     * Gets the current config. If the config is null, it will attempt to load it from the file system.
     * @return The current config instance
     */
    public T getConfig() {
        if (config == null) {
            return loadConfig();
        }
        return config;
    }

    /**
     * Sets the current config. This will not automatically save the config to the file system, so you should call saveConfig()
     * after setting the config if you want to persist it.
     *
     * @param config The new config to set
     */
    public void setConfig(T config) {
        this.config = config;
    }

    private String getConfigFileLocation() {
        return System.getProperty("user.dir")
                + File.separator
                + "config"
                + File.separator
                + modId
                + File.separator
                + configName
                + ".json";
    }
}

