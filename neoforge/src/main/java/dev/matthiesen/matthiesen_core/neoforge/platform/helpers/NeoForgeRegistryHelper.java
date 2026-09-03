package dev.matthiesen.matthiesen_core.neoforge.platform.helpers;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
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
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * The NeoForgeRegistryHelper class provides utility methods for registering various game elements such as blocks, items, block entities,
 * sounds, creative mode tabs, criteria triggers, stats, menu types, features, data component types, and entity effects in the NeoForge
 * mod loader environment. It manages deferred registration of these elements and ensures that they are registered at the appropriate
 * time during the mod loading process.
 */
public final class NeoForgeRegistryHelper {
    private static final Map<String, DeferredRegister<?>> DEFERRED_REGISTERS = new ConcurrentHashMap<>();
    private static final AtomicBoolean CREATIVE_AUGMENTS_HOOK_INSTALLED = new AtomicBoolean(false);
    private static volatile IEventBus modBus;

    private NeoForgeRegistryHelper() {}

    /**
     * Initializes the NeoForgeRegistryHelper with the provided event bus. This method must be called before any registration methods are invoked
     * to ensure that the event bus is available for handling registration events.
     *
     * @param eventBus The event bus to be used for registrations.
     */
    public static void init(IEventBus eventBus) {
        modBus = eventBus;
    }

    /**
     * Registers a block entity with the specified resource location and supplier.
     *
     * @param id               The resource location representing the name of the block entity.
     * @param blockEntityType  A supplier that provides the block entity type to be registered.
     * @param <T>              The type of the block entity, which must extend BlockEntity.
     * @return A supplier that provides the registered block entity type.
     */
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return registerDeferred(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    /**
     * Registers a block with the specified resource location and supplier.
     *
     * @param id    The resource location representing the name of the block.
     * @param block A supplier that provides the block to be registered.
     * @param <T>   The type of the block, which must extend Block.
     * @return A supplier that provides the registered block.
     */
    public static <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return registerDeferred(Registries.BLOCK, id, block);
    }

    /**
     * Registers an item with the specified resource location and supplier.
     *
     * @param id   The resource location representing the name of the item.
     * @param item A supplier that provides the item to be registered.
     * @param <T>  The type of the item, which must extend Item.
     * @return A supplier that provides the registered item.
     */
    public static <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return registerDeferred(Registries.ITEM, id, item);
    }

    /**
     * Registers a sound event with the specified resource location and supplier.
     *
     * @param id    The resource location representing the name of the sound event.
     * @param sound A supplier that provides the sound event to be registered.
     * @param <T>   The type of the sound event, which must extend SoundEvent.
     * @return A supplier that provides the registered sound event.
     */
    public static <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return registerDeferred(Registries.SOUND_EVENT, id, sound);
    }

    /**
     * Registers a creative mode tab with the specified resource location and supplier.
     * @param id The resource location representing the name of the creative mode tab.
     * @param tab A supplier that provides the creative mode tab to be registered.
     * @return A supplier that provides the registered creative mode tab.
     * @param <T> The type of the creative mode tab, which must extend CreativeModeTab.
     */
    public static <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return registerDeferred(Registries.CREATIVE_MODE_TAB, id, tab);
    }

    /**
     * Initializes the creative mode tab augmentations by registering a listener for the BuildCreativeModeTabContentsEvent.
     * This method ensures that the augmentations are applied to the creative mode tabs during the appropriate phase of the mod loading process.
     * It is safe to call this method multiple times, but the augmentations will only be registered once.
     */
    public static void initializeCreativeModeTabAugmentations() {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeRegistryHelper has not been initialized yet");
        }
        if (!CREATIVE_AUGMENTS_HOOK_INSTALLED.compareAndSet(false, true)) {
            return;
        }

        eventBus.addListener((BuildCreativeModeTabContentsEvent event) ->
                event.acceptAll(MatthiesenCoreCommon.INSTANCE.getCreativeModeAugmentsManager().getAugmentationsForTab(event.getTabKey()))
        );
    }

    /**
     * Registers a supplier for a given criterion trigger with the specified resource location.
     *
     * @param id            The resource location representing the name of the entry.
     * @param criterionTrigger A supplier that provides the entry to be registered.
     * @param <T>           The type of the entry being registered.
     * @return A supplier that provides the registered entry.
     */
    public static <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return registerDeferred(Registries.TRIGGER_TYPE, id, criterionTrigger);
    }

    /**
     * Registers a stat with the specified resource location and supplier.
     *
     * @param id    The resource location representing the name of the stat.
     * @param stats A supplier that provides the stat to be registered.
     * @param <T>   The type of the stat, which must extend ResourceLocation.
     * @return A supplier that provides the registered stat.
     */
    public static <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return registerDeferred(Registries.CUSTOM_STAT, id, stats);
    }

    /**
     * Registers a menu type with the specified resource location and supplier.
     *
     * @param id       The resource location representing the name of the menu type.
     * @param menuType A supplier that provides the menu type to be registered.
     * @param <T>      The type of the menu, which must extend MenuType.
     * @return A supplier that provides the registered menu type.
     */
    public static <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return registerDeferred(Registries.MENU, id, menuType);
    }

    /**
     * Registers a feature with the specified resource location and supplier.
     *
     * @param id      The resource location representing the name of the feature.
     * @param feature A supplier that provides the feature to be registered.
     * @param <T>     The type of the feature, which must extend Feature.
     * @return A supplier that provides the registered feature.
     */
    public static <T extends Feature<?>> Supplier<T> registerFeature(ResourceLocation id, Supplier<T> feature) {
        return registerDeferred(Registries.FEATURE, id, feature);
    }

    /**
     * Registers a data component type with the specified resource location and supplier.
     *
     * @param id        The resource location representing the name of the data component type.
     * @param component A supplier that provides the data component type to be registered.
     * @param <T>       The type of the data component, which must extend DataComponentType.
     * @return A supplier that provides the registered data component type.
     */
    public static <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return registerDataComponentDeferred(id, component);
    }

    /**
     * Registers an entity effect with the specified name and codec.
     *
     * @param name  The resource location representing the name of the entity effect.
     * @param codec A supplier that provides the MapCodec for the entity effect.
     * @param <T>   The type of the entity effect, which must extend EnchantmentEntityEffect.
     * @return A supplier that provides the registered MapCodec for the entity effect.
     */
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
