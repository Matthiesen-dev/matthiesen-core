package dev.matthiesen.matthiesen_core.neoforge.utility;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class MatthiesenCoreNeoForgeRegistry {
    private static final Map<String, DeferredRegister<?>> DEFERRED_REGISTERS = new ConcurrentHashMap<>();
    private static volatile IEventBus modBus;

    private MatthiesenCoreNeoForgeRegistry() {}

    public static void init(IEventBus eventBus) {
        modBus = eventBus;
    }

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return registerDeferred(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    public static <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return registerDeferred(Registries.BLOCK, id, block);
    }

    public static <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return registerDeferred(Registries.ITEM, id, item);
    }

    public static <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return registerDeferred(Registries.SOUND_EVENT, id, sound);
    }

    public static <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return registerDeferred(Registries.CREATIVE_MODE_TAB, id, tab);
    }

    public static <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return registerDeferred(Registries.TRIGGER_TYPE, id, criterionTrigger);
    }

    public static <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return registerDeferred(Registries.CUSTOM_STAT, id, stats);
    }

    public static <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return registerDeferred(Registries.MENU, id, menuType);
    }

    public static <T extends Feature<?>> Supplier<T> registerFeature(ResourceLocation id, Supplier<T> feature) {
        return registerDeferred(Registries.FEATURE, id, feature);
    }

    public static <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return registerDataComponentDeferred(id, component);
    }

    public static <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec) {
        return registerDeferred(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, name, codec);
    }

    private static <T> Supplier<T> registerDeferred(Object registryKey, ResourceLocation id, Supplier<T> entrySupplier) {
        DeferredRegister<T> deferredRegister = getOrCreateDeferredRegister(registryKey, id.getNamespace());
        return deferredRegister.register(id.getPath(), entrySupplier);
    }

    private static <T> Supplier<T> registerDataComponentDeferred(ResourceLocation id, Supplier<T> entrySupplier) {
        DeferredRegister<T> deferredRegister = getOrCreateDataComponentDeferredRegister(id.getNamespace());
        return deferredRegister.register(id.getPath(), entrySupplier);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> DeferredRegister<T> getOrCreateDeferredRegister(Object registryKey, String namespace) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeRegistryHelper has not been initialized yet");
        }

        String cacheKey = registryKey.toString() + "|" + namespace;
        return (DeferredRegister<T>) DEFERRED_REGISTERS.computeIfAbsent(cacheKey, key -> {
            DeferredRegister<T> deferredRegister = DeferredRegister.create((net.minecraft.resources.ResourceKey) registryKey, namespace);
            deferredRegister.register(eventBus);
            return deferredRegister;
        });
    }

    @SuppressWarnings({"unchecked"})
    private static <T> DeferredRegister<T> getOrCreateDataComponentDeferredRegister(String namespace) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeRegistryHelper has not been initialized yet");
        }

        String cacheKey = Registries.DATA_COMPONENT_TYPE + "|" + namespace;
        return (DeferredRegister<T>) DEFERRED_REGISTERS.computeIfAbsent(cacheKey, key -> {
            DeferredRegister<T> deferredRegister = (DeferredRegister<T>) DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, namespace);
            deferredRegister.register(eventBus);
            return deferredRegister;
        });
    }
}
