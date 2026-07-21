package dev.matthiesen.matthiesen_lib.neoforge.platform;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import dev.matthiesen.matthiesen_lib.common.api.platform.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_lib.neoforge.MatthiesenLibNeoForgeRegistry;
import dev.matthiesen.matthiesen_lib.neoforge.permissions.NeoForgePermissionValidator;
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

public final class NeoForgeLoaderRegistry implements CommonLoaderRegistry {
    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return MatthiesenLibNeoForgeRegistry.registerBlockEntity(id, blockEntityType);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return MatthiesenLibNeoForgeRegistry.registerBlock(id, block);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return MatthiesenLibNeoForgeRegistry.registerItem(id, item);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return MatthiesenLibNeoForgeRegistry.registerSound(id, sound);
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return MatthiesenLibNeoForgeRegistry.registerCriteriaTriggers(id, criterionTrigger);
    }

    @Override
    public <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return MatthiesenLibNeoForgeRegistry.registerStats(id, stats);
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return MatthiesenLibNeoForgeRegistry.registerMenuType(id, menuType);
    }

    @Override
    public <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return MatthiesenLibNeoForgeRegistry.registerDataComponentType(id, component);
    }

    @Override
    public <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec) {
        return MatthiesenLibNeoForgeRegistry.registerEntityEffects(name, codec);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(ResourceLocation name, Supplier<T> feature) {
        return MatthiesenLibNeoForgeRegistry.registerFeature(name, feature);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return MatthiesenLibNeoForgeRegistry.registerCreativeModeTab(id, tab);
    }

    @Override
    public void registerPermissionValidator() {
        MatthiesenLibCommon.INSTANCE.getPermissionsManager().setPermissionValidator(new NeoForgePermissionValidator());
    }

    @Override
    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return CreativeModeTab.builder();
    }
}
