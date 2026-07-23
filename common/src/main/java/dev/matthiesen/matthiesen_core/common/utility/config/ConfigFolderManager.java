package dev.matthiesen.matthiesen_core.common.utility.config;

import com.google.gson.*;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.*;

/**
 * A generic configuration manager for handling multiple JSON-based config files inside a single folder.
 * Each file is identified by its filename without the {@code .json} extension.
 *
 * @param <T> The type of the config class to manage
 */
@SuppressWarnings("unused")
public final class ConfigFolderManager<T> {
    private static final String JSON_EXTENSION = ".json";
    private static final Logger LOGGER = LogManager.getLogger(MatthiesenCoreCommon.MOD_NAME);

    private final Class<T> configClass;
    private final String folderName;
    private final String modId;
    private final Gson gson;
    private final Map<String, T> configs = new LinkedHashMap<>();
    private boolean loaded;

    /**
     * Creates a new ConfigFolderManager for the specified config class, folder name, and mod id namespace.
     *
     * @param configClass The class of the config to manage
     * @param folderName The folder that contains config files for this manager
     * @param modId The mod id namespace used for the config folder
     */
    public ConfigFolderManager(Class<T> configClass, String folderName, String modId) {
        this.configClass = configClass;
        this.folderName = folderName;
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
            LOGGER.debug("No GSON field found in {}, using default Gson instance", configClass.getSimpleName());
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
            LOGGER.error("Failed to create default instance of {}", configClass.getSimpleName(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads all config files from the configured folder and stores them using their filename (without {@code .json}) as the id.
     *
     * @return An unmodifiable view of the loaded configs keyed by config id
     */
    public Map<String, T> loadConfigs() {
        File configFolder = getConfigFolder();
        LOGGER.debug("Loading config folder found at: {}", configFolder.getAbsolutePath());
        ensureConfigDirectoryExists(configFolder);

        configs.clear();

        File[] configFiles = configFolder.listFiles(file ->
                file.isFile() && file.getName().toLowerCase(Locale.ROOT).endsWith(JSON_EXTENSION)
        );

        if (configFiles != null) {
            Arrays.sort(configFiles, Comparator.comparing(File::getName));

            for (File configFile : configFiles) {
                String configId = getConfigIdFromFileName(configFile.getName());
                configs.put(configId, readConfig(configFile, configId));
                saveConfig(configId);
            }
        }

        loaded = true;
        return Collections.unmodifiableMap(configs);
    }

    /**
     * Loads a single config by id. The id maps directly to the config filename without the {@code .json} extension.
     * If the file does not exist, a default config is created, cached, and saved to disk.
     *
     * @param configId The config id / filename without extension
     * @return The loaded config instance
     */
    public T loadConfig(String configId) {
        String normalizedConfigId = normalizeConfigId(configId);
        File configFile = getConfigFile(normalizedConfigId);
        LOGGER.debug("Loading config file found at: {}", configFile.getAbsolutePath());
        ensureConfigDirectoryExists(configFile.getParentFile());

        T config = readConfig(configFile, normalizedConfigId);
        configs.put(normalizedConfigId, config);
        loaded = true;
        saveConfig(normalizedConfigId);
        return config;
    }

    /**
     * Merges the default config with the file config. If a key is missing in the file config, it will be added from the default config.
     * If a key is present in both configs and is a nested object, it will recursively merge them.
     *
     * @param defaultConfig The default config as a JsonObject
     * @param fileConfig The file config as a JsonObject
     * @return The merged config as a JsonObject
     */
    private JsonObject mergeConfigs(JsonObject defaultConfig, JsonObject fileConfig) {
        LOGGER.debug("Checking for config merge.");
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
     * Saves all loaded configs to the file system.
     */
    public void saveConfigs() {
        if (!loaded) {
            loadConfigs();
        }

        for (String configId : configs.keySet()) {
            saveConfig(configId);
        }
    }

    /**
     * Saves a specific config to the file system.
     *
     * @param configId The config id / filename without extension
     */
    public void saveConfig(String configId) {
        String normalizedConfigId = normalizeConfigId(configId);
        T config = configs.get(normalizedConfigId);

        if (config == null) {
            LOGGER.error("Failed to save config '{}' because it has not been loaded or set yet.", normalizedConfigId);
            return;
        }

        try {
            File configFile = getConfigFile(normalizedConfigId);
            ensureConfigDirectoryExists(configFile.getParentFile());
            LOGGER.debug("Saving config to: {}", configFile.getAbsolutePath());

            try (FileWriter fileWriter = new FileWriter(configFile)) {
                gson.toJson(config, fileWriter);
                fileWriter.flush();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save config '{}'", normalizedConfigId, e);
        }
    }

    /**
     * Gets a config by id. If it has not been loaded yet, the manager will attempt to load it from disk.
     *
     * @param configId The config id / filename without extension
     * @return The config instance for the given id
     */
    public T getConfig(String configId) {
        String normalizedConfigId = normalizeConfigId(configId);

        if (!loaded) {
            loadConfigs();
        }

        if (!configs.containsKey(normalizedConfigId)) {
            return loadConfig(normalizedConfigId);
        }

        return configs.get(normalizedConfigId);
    }

    /**
     * Gets all loaded configs keyed by their config id. If configs have not yet been loaded, the entire folder will be scanned first.
     *
     * @return An unmodifiable view of the loaded configs
     */
    public Map<String, T> getConfigs() {
        if (!loaded) {
            return loadConfigs();
        }

        return Collections.unmodifiableMap(configs);
    }

    /**
     * Checks whether a config with the given id has already been loaded or exists on disk.
     *
     * @param configId The config id / filename without extension
     * @return {@code true} if the config exists in memory or on disk
     */
    public boolean hasConfig(String configId) {
        String normalizedConfigId = normalizeConfigId(configId);

        if (configs.containsKey(normalizedConfigId)) {
            return true;
        }

        return getConfigFile(normalizedConfigId).exists();
    }

    /**
     * Sets a config for the given id. This will not automatically save the config to the file system, so you should call {@link #saveConfig(String)}
     * or {@link #saveConfigs()} after setting it if you want to persist it.
     *
     * @param configId The config id / filename without extension
     * @param config The new config to set
     */
    public void setConfig(String configId, T config) {
        String normalizedConfigId = normalizeConfigId(configId);
        configs.put(normalizedConfigId, Objects.requireNonNull(config, "config cannot be null"));
        loaded = true;
    }

    /**
     * Reloads all configs from disk, discarding any in-memory state.
     *
     * @return An unmodifiable view of the reloaded configs keyed by config id
     */
    public Map<String, T> reloadConfigs() {
        loaded = false;
        return loadConfigs();
    }

    /**
     * Reloads a single config from disk, replacing any cached in-memory value.
     * If the file does not exist, a default config is created and saved.
     *
     * @param configId The config id / filename without extension
     * @return The reloaded config instance
     */
    public T reloadConfig(String configId) {
        String normalizedConfigId = normalizeConfigId(configId);
        configs.remove(normalizedConfigId);
        return loadConfig(normalizedConfigId);
    }

    /**
     * Deletes a config both from memory and from disk.
     * Does nothing (and logs a warning) if the config has not been loaded and the file does not exist.
     *
     * @param configId The config id / filename without extension
     * @return {@code true} if the file was successfully deleted or did not exist on disk; {@code false} if deletion failed
     */
    public boolean deleteConfig(String configId) {
        String normalizedConfigId = normalizeConfigId(configId);
        configs.remove(normalizedConfigId);

        File configFile = getConfigFile(normalizedConfigId);

        if (!configFile.exists()) {
            LOGGER.debug("Config '{}' does not exist on disk, nothing to delete.", normalizedConfigId);
            return true;
        }

        boolean deleted = configFile.delete();

        if (deleted) {
            LOGGER.debug("Deleted config file: {}", configFile.getAbsolutePath());
        } else {
            LOGGER.error("Failed to delete config file: {}", configFile.getAbsolutePath());
        }

        return deleted;
    }

    private T readConfig(File configFile, String configId) {
        if (configFile.exists()) {
            try (FileReader fileReader = new FileReader(configFile)) {
                T defaultConfig = createDefaultConfig();
                JsonObject defaultConfigObject = gson.toJsonTree(defaultConfig).getAsJsonObject();
                var fileConfigElement = JsonParser.parseReader(fileReader);

                if (!fileConfigElement.isJsonObject()) {
                    throw new JsonParseException("Config file must contain a JSON object");
                }

                JsonObject mergedConfig = mergeConfigs(defaultConfigObject, fileConfigElement.getAsJsonObject());
                return gson.fromJson(mergedConfig, configClass);
            } catch (Exception e) {
                LOGGER.error("Failed to load config '{}'! Using default config as fallback", configId, e);
                return createDefaultConfig();
            }
        }

        LOGGER.debug("Config '{}' not found, creating default config.", configId);
        return createDefaultConfig();
    }

    private void ensureConfigDirectoryExists(File configDirectory) {
        boolean madeDir = configDirectory.mkdirs();

        if (madeDir) {
            LOGGER.debug("Config Directory exists");
        }
    }

    private String normalizeConfigId(String configId) {
        String normalizedConfigId = Objects.requireNonNull(configId, "configId cannot be null").trim();

        if (normalizedConfigId.endsWith(JSON_EXTENSION)) {
            normalizedConfigId = normalizedConfigId.substring(0, normalizedConfigId.length() - JSON_EXTENSION.length());
        }

        if (normalizedConfigId.isEmpty()) {
            throw new IllegalArgumentException("configId cannot be blank");
        }

        if (normalizedConfigId.contains("/") || normalizedConfigId.contains("\\") || normalizedConfigId.contains("..")) {
            throw new IllegalArgumentException("configId cannot contain path separators or traversal sequences");
        }

        return normalizedConfigId;
    }

    private String getConfigIdFromFileName(String fileName) {
        return fileName.substring(0, fileName.length() - JSON_EXTENSION.length());
    }

    private File getConfigFolder() {
        return new File(System.getProperty("user.dir")
                + File.separator
                + "config"
                + File.separator
                + modId
                + File.separator
                + folderName);
    }

    private File getConfigFile(String configId) {
        return new File(getConfigFolder(), normalizeConfigId(configId) + JSON_EXTENSION);
    }
}

