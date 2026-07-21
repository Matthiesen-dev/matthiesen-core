package dev.matthiesen.matthiesen_core.common.abstracts;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.Supplier;

/**
 * The AbstractRegistryBuilder class provides a convenient and standardized way for mod developers to register various game
 * elements such as items, blocks, block entities, creative mode tabs, sound events, criteria triggers, statistics, menu types,
 * data component types, enchantment entity effects, and features. It serves as a base class that can be extended by specific
 * mod implementations to facilitate the registration process while ensuring consistency and compatibility across different platforms.
 */
@SuppressWarnings("unused")
public abstract class AbstractRegistryBuilder {
    private final String modId;
    private final CommonLoaderRegistry registry;

    /**
     * Constructs a new AbstractRegistryBuilder for the specified mod ID. This constructor initializes the registry builder
     * with the provided mod ID and retrieves the common registry instance from MatthiesenCoreCommon.
     * @param modId The mod ID for which this registry builder is being created. This mod ID will be used to generate unique
     *              ResourceLocation identifiers for registered items, blocks, and other game elements.
     */
    protected AbstractRegistryBuilder(final String modId) {
        this.modId = modId;
        this.registry = MatthiesenCoreCommon.INSTANCE.getCommonRegistry();
        MatthiesenCoreCommon.INSTANCE.createDebugLog("Creating registry builder for mod: " + modId);
    }

    /**
     * Creates a new CreativeModeTab.Builder instance using the platform-specific implementation provided by the CommonPlatform service.
     * @return a new CreativeModeTab.Builder instance
     */
    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return registry.newCreativeTabBuilder();
    }

    /**
     * Registers a new item with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the item being registered. This should be a subclass of Item, and it will be used to create instances of the item when needed.
     * @param name The name to register the item under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param item A Supplier that provides an instance of the Item to register. This supplier will be called when the item needs
     *             to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered Item. This allows other parts of the mod to access the item after it has
     * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return registry.registerItem(ResourceLocation.fromNamespaceAndPath(modId, name), item);
    }

    /**
     * Registers a new block with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the block being registered. This should be a subclass of Block, and it will be used to create instances of the block when needed.
     * @param name The name to register the block under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param block A Supplier that provides an instance of the Block to register. This supplier will be called when the block
     *              needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered Block. This allows other parts of the mod to access the block after it has
     * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return registry.registerBlock(ResourceLocation.fromNamespaceAndPath(modId, name), block);
    }

    /**
     * Registers a new block entity type with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the block entity being registered. This should be a subclass of BlockEntity, and it will be
     *           used to create instances of the block entity when needed.
     * @param name The name to register the block entity type under. This should be unique within the mod and should follow the standard
     *             format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param blockEntitySupplier A Supplier that provides an instance of the BlockEntityType to register. This supplier will be called when the block entity type needs to be created,
     *                            allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered BlockEntityType. This allows other parts of the mod to access the block entity type after it has been registered,
     *                        and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType<T>> blockEntitySupplier) {
        return registry.registerBlockEntity(ResourceLocation.fromNamespaceAndPath(modId, name), blockEntitySupplier);
    }

    /**
     * Registers a new creative mode tab with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the creative mode tab being registered. This should be a subclass of CreativeModeTab, and it will be
     *           used to create instances of the creative mode tab when needed.
     * @param name The name to register the creative mode tab under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param tab A Supplier that provides an instance of the CreativeModeTab to register. This supplier will be called when the
     *            creative mode tab needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered CreativeModeTab. This allows other parts of the mod to access the creative
     * mode tab after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String name, Supplier<T> tab) {
        return registry.registerCreativeModeTab(ResourceLocation.fromNamespaceAndPath(modId, name), tab);
    }

    /**
     * Registers a new sound event with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the sound event being registered. This should be a subclass of SoundEvent, and it will be used to create instances of the sound event when needed.
     * @param name The name to register the sound event under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param sound A Supplier that provides an instance of the SoundEvent to register. This supplier will be called when the sound
     *              event needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered SoundEvent. This allows other parts of the mod to access the sound event after
     * it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends SoundEvent> Supplier<T> registerSound(String name, Supplier<T> sound) {
        return registry.registerSound(ResourceLocation.fromNamespaceAndPath(modId, name), sound);
    }

    /**
     * Registers a new custom criterion trigger with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the criterion trigger being registered. This should be a subclass of CriterionTrigger, and it will be
     *           used to create instances of the criterion trigger when needed.
     * @param name The name to register the criterion trigger under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param criterionTrigger A Supplier that provides an instance of the CriterionTrigger to register. This supplier will be called
     *                         when the criterion trigger needs to be created, allowing for lazy initialization and avoiding potential
     *                         issues with static initialization order.
     * @return A Supplier that provides the registered CriterionTrigger. This allows other parts of the mod to access the criterion
     * trigger after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(String name, Supplier<T> criterionTrigger) {
        return registry.registerCriteriaTriggers(ResourceLocation.fromNamespaceAndPath(modId, name), criterionTrigger);
    }

    /**
     * Registers a new custom statistic with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the statistic being registered. This should be a subclass of ResourceLocation, and it will be used to
     *           create instances of the statistic when needed.
     * @param name The name to register the statistic under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param stats A Supplier that provides an instance of the statistic to register. This supplier will be called when the statistic
     *              needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered statistic. This allows other parts of the mod to access the statistic after it
     * has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends ResourceLocation> Supplier<T> registerStats(String name, Supplier<T> stats) {
        return registry.registerStats(ResourceLocation.fromNamespaceAndPath(modId, name), stats);
    }

    /**
     * Registers a new MenuType with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the menu being registered. This should be a subclass of MenuType, and it will be used to create instances of the menu when needed.
     * @param name The name to register the menu under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param menuType A Supplier that provides an instance of the MenuType to register. This supplier will be called when the menu type
     *                 needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered MenuType. This allows other parts of the mod to access the menu type after it has
     * been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends MenuType<?>> Supplier<T> registerMenuType(String name, Supplier<T> menuType) {
        return registry.registerMenuType(ResourceLocation.fromNamespaceAndPath(modId, name), menuType);
    }

    /**
     * Registers a new DataComponentType with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the data component being registered. This should be a subclass of DataComponentType, and it will be used to create instances of the data component when needed.
     * @param name The name to register the data component type under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param component A Supplier that provides an instance of the DataComponentType to register. This supplier will be called when the
     *                  data component type needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered DataComponentType. This allows other parts of the mod to access the data component
     * type after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     */
    public <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(String name, Supplier<T> component) {
        return registry.registerDataComponentType(ResourceLocation.fromNamespaceAndPath(modId, name), component);
    }

    /**
     * Registers a new EnchantmentEntityEffect type with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     *
     * @param <T> The type of the enchantment entity effect being registered. This should be a subclass of MapCodec that produces
     *           instances of EnchantmentEntityEffect, and it will be used to create instances of the enchantment entity effect when needed.
     * @param name The name to register the enchantment entity effect under. This should be unique within the mod and should follow the standard
     *             format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param codec A Supplier that provides an instance of the MapCodec to register for the enchantment entity effect. This supplier
     *              will be called when the enchantment entity effect needs to be created, allowing for lazy initialization and avoiding
     *              potential issues with static initialization order.
     * @return A Supplier that provides the registered MapCodec for the enchantment entity effect. This allows other parts of the mod to
     * access the enchantment entity effect codec after it has been registered, and it will return the correct instance regardless of when
     * it is called during the mod's initialization process.
     */
    public <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(String name, Supplier<T> codec) {
        return registry.registerEntityEffects(ResourceLocation.fromNamespaceAndPath(modId, name), codec);
    }

    /**
     * Registers a new feature with the given ResourceLocation ID and Supplier using the platform-specific implementation provided by the CommonPlatform service.
     * @param name The name to register the feature under. This should be unique within the mod and should follow the standard format of "name" (without the mod ID, as it will be prefixed automatically).
     * @param feature A Supplier that provides an instance of the Feature to register. This supplier will be called when the feature needs to be created, allowing for lazy initialization and avoiding potential issues with static initialization order.
     * @return A Supplier that provides the registered Feature. This allows other parts of the mod to access the feature after it has been registered, and it will return the correct instance regardless of when it is called during the mod's initialization process.
     * @param <T> The type of the feature being registered. This should be a subclass of Feature, and it will be used to create instances of the feature when needed.
     */
    public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
        return registry.registerFeature(ResourceLocation.fromNamespaceAndPath(modId, name), feature);
    }
}
