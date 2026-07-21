package dev.matthiesen.matthiesen_core.neoforge.platform;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.api.command.CommandRegistry;
import dev.matthiesen.matthiesen_core.common.api.command.CoreCommand;
import dev.matthiesen.matthiesen_core.common.api.platform.services.CommonLoaderRegistry;
import dev.matthiesen.matthiesen_core.neoforge.utility.MatthiesenCoreNeoForgeRegistry;
import dev.matthiesen.matthiesen_core.neoforge.permissions.NeoForgePermissionValidator;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NeoForgeLoaderRegistry implements CommonLoaderRegistry {
    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return MatthiesenCoreNeoForgeRegistry.registerBlockEntity(id, blockEntityType);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return MatthiesenCoreNeoForgeRegistry.registerBlock(id, block);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return MatthiesenCoreNeoForgeRegistry.registerItem(id, item);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return MatthiesenCoreNeoForgeRegistry.registerSound(id, sound);
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return MatthiesenCoreNeoForgeRegistry.registerCriteriaTriggers(id, criterionTrigger);
    }

    @Override
    public <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return MatthiesenCoreNeoForgeRegistry.registerStats(id, stats);
    }

    @Override
    public <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return MatthiesenCoreNeoForgeRegistry.registerMenuType(id, menuType);
    }

    @Override
    public <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return MatthiesenCoreNeoForgeRegistry.registerDataComponentType(id, component);
    }

    @Override
    public <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec) {
        return MatthiesenCoreNeoForgeRegistry.registerEntityEffects(name, codec);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(ResourceLocation name, Supplier<T> feature) {
        return MatthiesenCoreNeoForgeRegistry.registerFeature(name, feature);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return MatthiesenCoreNeoForgeRegistry.registerCreativeModeTab(id, tab);
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
    public void registerCommands(Consumer<CommandRegistry> registrationHandler) {
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) ->
                registrationHandler.accept((CoreCommand command) -> command.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()))
        );
    }
}
