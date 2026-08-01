package dev.matthiesen.matthiesen_core.common.core;

import dev.matthiesen.matthiesen_core.common.api.discord.WebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.platform.CommonServerMod;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderUtils;
import dev.matthiesen.matthiesen_core.common.core.data.SavedPlayerData;
import dev.matthiesen.matthiesen_core.common.core.discord.no_op.NoOpWebhookNotifierService;
import dev.matthiesen.matthiesen_core.common.core.economy.EconomyManager;
import dev.matthiesen.matthiesen_core.common.core.permissions.LuckPermsHelper;
import dev.matthiesen.matthiesen_core.common.core.metric.MatthiesenCoreMetrics;
import dev.matthiesen.matthiesen_core.common.core.network.NetworkingManager;
import dev.matthiesen.matthiesen_core.common.core.registry.PermissionsManager;
import dev.matthiesen.matthiesen_core.common.core.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * MatthiesenCoreCommon is a singleton class that provides core functionalities and services for the Matthiesen Lib mod.
 * It manages initialization, logging, metrics, and access to various managers and services used throughout the application.
 * This class is designed to be thread-safe and ensures that only one instance exists throughout the lifecycle of the application.
 * It provides methods for logging, error tracking, and access to common utilities and registries. It also handles the initialization
 * of various managers responsible for permissions, commands, events, networking, and text parsing. The class is intended to be used
 * by mods that depend on the Matthiesen Lib framework, providing a centralized point of access to core functionalities and services.
 */
public final class MatthiesenCoreCommon implements CommonServerMod {
    /**
     * The unique identifier for the mod, used for registration and identification purposes. This constant is used throughout
     * the application to refer to the mod in a consistent manner.
     */
    public static final String MOD_ID = "matthiesen_core";

    /**
     * The name of the mod, used for logging and identification purposes. This constant is used throughout the application
     * to refer to the mod in a consistent manner.
     */
    public static final String MOD_NAME = "Matthiesen Core";

    private static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    private static final CommonLoaderUtils COMMON_UTILS =
            ServiceLoader.load(CommonLoaderUtils.class).findFirst()
                    .orElseThrow(() -> new IllegalStateException("No CommonLoaderUtils implementation found"));
    private static final CommonLoaderRegistry COMMON_REGISTRY =
            ServiceLoader.load(CommonLoaderRegistry.class).findFirst()
                    .orElseThrow(() -> new IllegalStateException("No CommonLoaderRegistry implementation found"));

    private static final WebhookNotifierService WEBHOOK_NOTIFIER_SERVICE =
            ServiceLoader.load(WebhookNotifierService.class).findFirst().orElse(new NoOpWebhookNotifierService());

    /**
     * Singleton instance of the MatthiesenLibCommon. This instance is used to manage the common utilities and registry across the application.
     * It is initialized lazily and is thread-safe, ensuring that only one instance exists throughout the lifecycle of the application.
     */
    public static final MatthiesenCoreCommon INSTANCE = new MatthiesenCoreCommon();

    private boolean initialized;

    public MatthiesenCoreCommon() {}

    public static ResourceLocation modResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    /**
     * Initializes the MatthiesenLibCommon instance. This method sets up the PermissionsManager and marks the instance as initialized.
     */
    @Override
    public void initialize() {
        if (initialized) return;

        MatthiesenCoreMetrics.initialize();
        WEBHOOK_NOTIFIER_SERVICE.initialize();

        PermissionsManager.INSTANCE.initialize(COMMON_REGISTRY);
        CommandsRegistryManager.INSTANCE.initialize(COMMON_REGISTRY);
        PlatformEvents.initialize();
        NetworkingManager.INSTANCE.initialize(INSTANCE);
        TextParserRegistryManager.INSTANCE.initialize(INSTANCE);
        CreativeModeTabSectionsManager.INSTANCE.initialize(INSTANCE);
        CreativeModeAugmentsManager.INSTANCE.initialize(INSTANCE);
        EconomyManager.INSTANCE.initialize(INSTANCE);
        BuiltInCreativeModeSection.INSTANCE.initialize();

        PlatformEvents.PLAYER_JOIN.subscribe(SavedPlayerData::verifyPlayerData);

        if (getCommonUtils().isModLoaded("luckperms")) {
            LuckPermsHelper.loadCompat();
        }

        initialized = true;
        createInfoLog("Initialized Common");
    }

    /**
     * Registers a mod to the metrics system. This method allows mods to be registered with the metrics system, enabling the collection of
     * usage data and other relevant metrics for the registered mod.
     * @param modId The unique identifier of the mod to be registered. This should be a string that uniquely identifies the mod within the
     *              application, and is used to associate collected metrics with the correct mod.
     */
    public void registerModToMetrics(String modId) {
        getCoreMetrics().registerModToMetrics(modId);
    }

    /**
     * Called during the common server setup phase. This method is responsible for running auto-registrations for creative mode tab sections.
     */
    public void onCommonServerSetup() {
        CreativeModeTabSectionsManager.INSTANCE.runAutoRegistrations();
    }

    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public String getModName() {
        return MOD_NAME;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void trackError(Throwable throwable) {
        getCoreMetrics().trackError(throwable);
    }

    @Override
    public RegistryBuilder getRegistryBuilder() {
        return new RegistryBuilder(MOD_ID);
    }

    @Override
    public PermissionsManager getPermissionsManager() {
        return PermissionsManager.INSTANCE;
    }

    @Override
    public CommandsRegistryManager getCommandsRegistryManager() {
        return CommandsRegistryManager.INSTANCE;
    }

    @Override
    public WebhookNotifierService getWebhookService() {
        return WEBHOOK_NOTIFIER_SERVICE;
    }

    @Override
    public CommonLoaderUtils getCommonUtils() {
        return COMMON_UTILS;
    }

    @Override
    public CommonLoaderRegistry getCommonRegistry() {
        return COMMON_REGISTRY;
    }

    @Override
    public TextParserRegistryManager getTextParserManager() {
        return TextParserRegistryManager.INSTANCE;
    }

    @Override
    public MatthiesenCoreMetrics getCoreMetrics() {
        return MatthiesenCoreMetrics.INSTANCE;
    }

    @Override
    public NetworkingManager getNetworkingManager() {
        return NetworkingManager.INSTANCE;
    }

    @Override
    public CreativeModeAugmentsManager getCreativeModeAugmentsManager() {
        return CreativeModeAugmentsManager.INSTANCE;
    }

    @Override
    public EconomyManager getEconomyManager() {
        return EconomyManager.INSTANCE;
    }

    /**
     * Registers an item to a miscellaneous creative mode tab section.
     * @param registrationKey The unique identifier of the miscellaneous creative mode tab section to which the item should be registered.
     * @param itemSupplier A {@link Supplier} that provides the item to be registered to the specified section.
     */
    public void registerItemToMiscTab(BuiltInCreativeModeSection.RegistrationKey registrationKey, Supplier<Item> itemSupplier) {
        BuiltInCreativeModeSection.INSTANCE.registerItemToSection(registrationKey, itemSupplier);
    }

    /**
     * Registers multiple items to a miscellaneous creative mode tab section.
     * @param registrationKey The unique identifier of the miscellaneous creative mode tab section to which the items should be registered.
     * @param itemSuppliers A list of {@link Supplier} instances that provide the items to be registered to the specified section.
     */
    public void registerItemsToMiscTab(BuiltInCreativeModeSection.RegistrationKey registrationKey, List<Supplier<Item>> itemSuppliers) {
        BuiltInCreativeModeSection.INSTANCE.registerSectionWithItems(registrationKey, itemSuppliers);
    }
}
