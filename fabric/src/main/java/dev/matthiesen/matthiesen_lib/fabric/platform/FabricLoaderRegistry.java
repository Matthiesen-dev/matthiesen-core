package dev.matthiesen.matthiesen_lib.fabric.platform;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import dev.matthiesen.matthiesen_lib.common.api.platform.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_lib.fabric.permissions.FabricPermissionValidator;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
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

public final class FabricLoaderRegistry implements CommonLoaderRegistry {
    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return registerSupplier(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return registerSupplier(BuiltInRegistries.BLOCK, id, block);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return registerSupplier(BuiltInRegistries.ITEM, id, item);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return registerSupplier(BuiltInRegistries.SOUND_EVENT, id, sound);
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return registerSupplier(BuiltInRegistries.TRIGGER_TYPES, id, criterionTrigger);
    }

    @Override
    public <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return registerSupplier(BuiltInRegistries.CUSTOM_STAT, id, stats);
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return registerSupplier(BuiltInRegistries.MENU, id, menuType);
    }

    @Override
    public <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return registerSupplier(BuiltInRegistries.DATA_COMPONENT_TYPE, id, component);
    }

    @Override
    public <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec) {
        return registerSupplier(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, name, codec);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(ResourceLocation name, Supplier<T> feature) {
        return registerSupplier(BuiltInRegistries.FEATURE, name, feature);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return registerSupplier(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab);
    }

    @Override
    public void registerPermissionValidator() {
        if (FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0")) {
            MatthiesenLibCommon.INSTANCE.getPermissionsManager().setPermissionValidator(new FabricPermissionValidator());
        }
    }

    @Override
    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return FabricItemGroup.builder();
    }

    @SuppressWarnings("unchecked")
    private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R registry, ResourceLocation id, Supplier<T> object) {
        final T registeredObject = Registry.register((Registry<T>) registry, id, object.get());
        return () -> registeredObject;
    }
}
