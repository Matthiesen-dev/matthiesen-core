package dev.matthiesen.matthiesen_core.neoforge.platform;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.command.CommandRegistry;
import dev.matthiesen.matthiesen_core.common.api.command.CoreCommand;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.neoforge.permissions.NeoForgePermissionValidator;
import dev.matthiesen.matthiesen_core.neoforge.platform.helpers.NeoForgeRegistryHelper;
import net.minecraft.advancements.CriterionTrigger;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.callback.AddCallback;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The NeoForgeLoaderRegistry class implements the CommonLoaderRegistry interface and provides methods for registering various
 * game elements such as blocks, items, block entities, sounds, creative mode tabs, criteria triggers, stats, menu types, features,
 * data component types, and entity effects in the NeoForge mod loader environment. It serves as a bridge between the common
 * registration interface and the specific registration system provided by NeoForge.
 */
public final class NeoForgeLoaderRegistry implements CommonLoaderRegistry {
    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return NeoForgeRegistryHelper.registerBlockEntity(id, blockEntityType);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return NeoForgeRegistryHelper.registerBlock(id, block);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return NeoForgeRegistryHelper.registerItem(id, item);
    }

    @Override
    public void registerItemRegistryCallback(Consumer<Item> itemConsumer) {
        BuiltInRegistries.ITEM.addCallback((AddCallback<Item>) (registry, i, key, item) -> itemConsumer.accept(item));
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return NeoForgeRegistryHelper.registerSound(id, sound);
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return NeoForgeRegistryHelper.registerCriteriaTriggers(id, criterionTrigger);
    }

    @Override
    public <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return NeoForgeRegistryHelper.registerStats(id, stats);
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return NeoForgeRegistryHelper.registerMenuType(id, menuType);
    }

    @Override
    public <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return NeoForgeRegistryHelper.registerDataComponentType(id, component);
    }

    @Override
    public <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec) {
        return NeoForgeRegistryHelper.registerEntityEffects(name, codec);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(ResourceLocation name, Supplier<T> feature) {
        return NeoForgeRegistryHelper.registerFeature(name, feature);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return NeoForgeRegistryHelper.registerCreativeModeTab(id, tab);
    }

    @Override
    public void registerPermissionValidator() {
        MatthiesenCoreCommon.INSTANCE.getPermissionsManager().setPermissionValidator(new NeoForgePermissionValidator());
    }

    @Override
    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return CreativeModeTab.builder();
    }

    @Override
    public void initializeCreativeModeTabAugmentations() {
        NeoForgeRegistryHelper.initializeCreativeModeTabAugmentations();
    }

    @Override
    public void registerCommands(Consumer<CommandRegistry> registrationHandler) {
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) ->
                registrationHandler.accept((CoreCommand command) -> command.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()))
        );
    }
}
