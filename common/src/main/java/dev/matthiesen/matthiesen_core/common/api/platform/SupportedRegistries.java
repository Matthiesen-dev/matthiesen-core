package dev.matthiesen.matthiesen_core.common.api.platform;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.matthiesen_core.common.abstracts.registry.AbstractRegistry;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
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
 * Enumerates the registry categories currently supported by {@link RegistryBuilder}.
 *
 * <p>Use one of the provided constants when constructing {@link AbstractRegistry} subclasses so the
 * generic type stays aligned with an actually supported registration target.</p>
 *
 * @param <T> the root type supported by the registry category
 */
@SuppressWarnings("unused")
public sealed interface SupportedRegistries<T>
        permits SupportedRegistries.BlockEntityRegistry,
        SupportedRegistries.BlockRegistry,
        SupportedRegistries.CreativeModeTabRegistry,
        SupportedRegistries.CriteriaTriggerRegistry,
        SupportedRegistries.DataComponentTypeRegistry,
        SupportedRegistries.EntityEffectsRegistry,
        SupportedRegistries.FeatureRegistry,
        SupportedRegistries.ItemRegistry,
        SupportedRegistries.MenuTypeRegistry,
        SupportedRegistries.SoundRegistry,
        SupportedRegistries.StatsRegistry {

    /**
     * Supported registry category for {@link Item} registrations.
     */
    SupportedRegistries<Item> ITEM = new ItemRegistry();
    /**
     * Supported registry category for {@link Block} registrations.
     */
    SupportedRegistries<Block> BLOCK = new BlockRegistry();
    /**
     * Supported registry category for {@link BlockEntityType} registrations.
     */
    SupportedRegistries<BlockEntityType<?>> BLOCK_ENTITY = new BlockEntityRegistry();
    /**
     * Supported registry category for {@link CreativeModeTab} registrations.
     */
    SupportedRegistries<CreativeModeTab> CREATIVE_MODE_TAB = new CreativeModeTabRegistry();
    /**
     * Supported registry category for {@link SoundEvent} registrations.
     */
    SupportedRegistries<SoundEvent> SOUND = new SoundRegistry();
    /**
     * Supported registry category for {@link CriterionTrigger} registrations.
     */
    SupportedRegistries<CriterionTrigger<?>> CRITERIA_TRIGGER = new CriteriaTriggerRegistry();
    /**
     * Supported registry category for custom statistic key ({@link ResourceLocation}) registrations.
     */
    SupportedRegistries<ResourceLocation> STAT = new StatsRegistry();
    /**
     * Supported registry category for {@link MenuType} registrations.
     */
    SupportedRegistries<MenuType<?>> MENU_TYPE = new MenuTypeRegistry();
    /**
     * Supported registry category for {@link DataComponentType} registrations.
     */
    SupportedRegistries<DataComponentType<?>> DATA_COMPONENT_TYPE = new DataComponentTypeRegistry();
    /**
     * Supported registry category for enchantment entity effect codec ({@link MapCodec}) registrations.
     */
    SupportedRegistries<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_EFFECT = new EntityEffectsRegistry();
    /**
     * Supported registry category for {@link Feature} registrations.
     */
    SupportedRegistries<Feature<?>> FEATURE = new FeatureRegistry();

    /**
     * Registers the provided entry through the matching {@link RegistryBuilder} method.
     *
     * @param registryBuilder the registry builder to use
     * @param name            the name of the entry to register
     * @param entry           the entry supplier
     * @param <R>             the concrete subtype being registered
     * @return a supplier for the registered object
     */
    <R extends T> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry);

    /**
     * Implementation for {@link #ITEM}.
     */
    final class ItemRegistry implements SupportedRegistries<Item> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public ItemRegistry() {}

        @Override
        public <R extends Item> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerItem(name, entry);
        }
    }

    /**
     * Implementation for {@link #BLOCK}.
     */
    final class BlockRegistry implements SupportedRegistries<Block> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public BlockRegistry() {}

        @Override
        public <R extends Block> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerBlock(name, entry);
        }
    }

    /**
     * Implementation for {@link #BLOCK_ENTITY}.
     */
    final class BlockEntityRegistry implements SupportedRegistries<BlockEntityType<?>> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public BlockEntityRegistry() {}

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public <R extends BlockEntityType<?>> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return (Supplier<R>) registerBlockEntityType(registryBuilder, name, (Supplier) entry);
        }

        /**
         * Bridges wildcarded block entity type suppliers to the strongly typed builder method.
         */
        private static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(
                RegistryBuilder registryBuilder,
                String name,
                Supplier<BlockEntityType<T>> entry
        ) {
            return registryBuilder.registerBlockEntity(name, entry);
        }
    }

    /**
     * Implementation for {@link #CREATIVE_MODE_TAB}.
     */
    final class CreativeModeTabRegistry implements SupportedRegistries<CreativeModeTab> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public CreativeModeTabRegistry() {}

        @Override
        public <R extends CreativeModeTab> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerCreativeModeTab(name, entry);
        }
    }

    /**
     * Implementation for {@link #SOUND}.
     */
    final class SoundRegistry implements SupportedRegistries<SoundEvent> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public SoundRegistry() {}

        @Override
        public <R extends SoundEvent> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerSound(name, entry);
        }
    }

    /**
     * Implementation for {@link #CRITERIA_TRIGGER}.
     */
    final class CriteriaTriggerRegistry implements SupportedRegistries<CriterionTrigger<?>> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public CriteriaTriggerRegistry() {}

        @Override
        public <R extends CriterionTrigger<?>> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerCriteriaTriggers(name, entry);
        }
    }

    /**
     * Implementation for {@link #STAT}.
     */
    final class StatsRegistry implements SupportedRegistries<ResourceLocation> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public StatsRegistry() {}

        @Override
        public <R extends ResourceLocation> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerStats(name, entry);
        }
    }

    /**
     * Implementation for {@link #MENU_TYPE}.
     */
    final class MenuTypeRegistry implements SupportedRegistries<MenuType<?>> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public MenuTypeRegistry() {}

        @Override
        public <R extends MenuType<?>> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerMenuType(name, entry);
        }
    }

    /**
     * Implementation for {@link #DATA_COMPONENT_TYPE}.
     */
    final class DataComponentTypeRegistry implements SupportedRegistries<DataComponentType<?>> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public DataComponentTypeRegistry() {}

        @Override
        public <R extends DataComponentType<?>> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerDataComponentType(name, entry);
        }
    }

    /**
     * Implementation for {@link #ENTITY_EFFECT}.
     */
    final class EntityEffectsRegistry implements SupportedRegistries<MapCodec<? extends EnchantmentEntityEffect>> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public EntityEffectsRegistry() {}

        @Override
        public <R extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerEntityEffects(name, entry);
        }
    }

    /**
     * Implementation for {@link #FEATURE}.
     */
    final class FeatureRegistry implements SupportedRegistries<Feature<?>> {
        /**
         * No-args constructor to prevent implicit one that would be public, since this type is only intended for internal use.
         */
        public FeatureRegistry() {}

        @Override
        public <R extends Feature<?>> Supplier<R> register(RegistryBuilder registryBuilder, String name, Supplier<R> entry) {
            return registryBuilder.registerFeature(name, entry);
        }
    }
}

