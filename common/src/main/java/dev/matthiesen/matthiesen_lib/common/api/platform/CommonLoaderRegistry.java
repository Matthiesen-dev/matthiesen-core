package dev.matthiesen.matthiesen_lib.common.api.platform;

import com.mojang.serialization.MapCodec;
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

public interface CommonLoaderRegistry {
    /**
     * Register a block entity type for the mod. This method allows you to register a new block entity type that can be used in your mod's content, such as for
     * custom blocks that have associated block entities. By registering a block entity type, you can create unique block entities that enhance the player's
     * experience and provide new gameplay mechanics. The registered block entity type can then be referenced in your code when defining the behavior of blocks
     * or other game objects that should interact with the block entity, allowing you to create custom interactions and features that are specific to your mod.
     * @param id The unique identifier for the block entity type. This should be a ResourceLocation that identifies the block entity type, and it will be used for
     *           registration and reference purposes. The ID should be unique to avoid conflicts with other mods or vanilla block entity types.
     * @param blockEntityType A supplier that provides the instance of the block entity type to be registered. This should return an instance of a subclass of
     *                        BlockEntityType that defines the specific properties and behavior of the block entity type you are creating. The supplier allows for
     *                        lazy initialization of the block entity type, which can help with performance and resource management during mod loading.
     * @return A supplier that provides the registered block entity type. This allows you to reference the block entity type in your code after it has been registered,
     * and it ensures that you are using the correct instance of the block entity type when interacting with it or checking for its presence.
     * @param <T> The type of the block entity type being registered. This should be a subclass of BlockEntityType that defines the specific properties and behavior
     *           of the block entity type you are creating. By using a generic type parameter, this method can be used to register any type of block entity type,
     *           allowing for flexibility and extensibility in your mod's content.
     */
    <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType);

    /**
     * Register a block for the mod. This method allows you to register a new block that can be used in your mod's content, such as for custom structures,
     * decorative blocks, or functional blocks with unique behavior. By registering a block, you can create unique building materials and interactive elements
     * that enhance the player's experience and provide new gameplay mechanics. The registered block can then be referenced in your code when defining the behavior
     * of entities, items, or other game objects that should interact with the block, allowing you to create custom interactions and features that are specific to your mod.
     * @param id The unique identifier for the block. This should be a ResourceLocation that identifies the block, and it will be used for registration and
     *           reference purposes. The ID should be unique to avoid conflicts with other mods or vanilla blocks.
     * @param block A supplier that provides the instance of the block to be registered. This should return an instance of a subclass of Block that defines the
     *              specific properties and behavior of the block you are creating. The supplier allows for lazy initialization of the block, which can help with
     *              performance and resource management during mod loading.
     * @return A supplier that provides the registered block. This allows you to reference the block in your code after it has been registered, and it ensures that
     * you are using the correct instance of the block when interacting with it or checking for its presence.
     * @param <T> The type of the block being registered. This should be a subclass of Block that defines the specific properties and behavior of the block you
     *           are creating. By using a generic type parameter, this method can be used to register any type of block, allowing for flexibility and extensibility
     *           in your mod's content.
     */
    <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block);

    /**
     * Register an item for the mod. This method allows you to register a new item that can be used in your mod's content, such as for custom blocks, tools,
     * or other items. By registering an item, you can create unique items that enhance the player's experience and provide new gameplay mechanics. The registered
     * item can then be referenced in your code when defining the behavior of blocks, entities, or other game objects that should interact with the item, allowing
     * you to create custom interactions and features that are specific to your mod.
     * @param id The unique identifier for the item. This should be a ResourceLocation that identifies the item, and it will be used for registration and
     *           reference purposes. The ID should be unique to avoid conflicts with other mods or vanilla items.
     * @param item A supplier that provides the instance of the item to be registered. This should return an instance of a subclass of Item that defines
     *             the specific properties and behavior of the item you are creating. The supplier allows for lazy initialization of the item, which can
     *             help with performance and resource management during mod loading.
     * @return A supplier that provides the registered item. This allows you to reference the item in your code after it has been registered, and it
     * ensures that you are using the correct instance of the item when interacting with it or checking for its presence.
     * @param <T> The type of the item being registered. This should be a subclass of Item that defines the specific properties and behavior of the
     *           item you are creating. By using a generic type parameter, this method can be used to register any type of item, allowing for flexibility
     *           and extensibility in your mod's content.
     */
    <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item);

    /**
     * Register a sound event for the mod. This method allows you to register a new sound event that can be used in your mod's content, such as for custom blocks,
     * items, or entities. By registering a sound event, you can create unique audio cues that enhance the player's experience and make your mod's content more immersive.
     * The registered sound event can then be referenced in your code when defining the behavior of blocks, items, or entities that should play the sound, allowing you to
     * create custom audio effects that are specific to your mod.
     * @param id The unique identifier for the sound event. This should be a ResourceLocation that identifies the sound event, and it will be used for registration
     *           and reference purposes. The ID should be unique to avoid conflicts with other mods or vanilla sound events.
     * @param sound A supplier that provides the instance of the sound event to be registered. This should return an instance of a subclass of SoundEvent that
     *              defines the specific properties and behavior of the sound event you are creating. The supplier allows for lazy initialization of the sound event,
     *              which can help with performance and resource management during mod loading.
     * @return A supplier that provides the registered sound event. This allows you to reference the sound event in your code after it has been registered, and it
     * ensures that you are using the correct instance of the sound event when playing it or checking for its presence.
     * @param <T> The type of the sound event being registered. This should be a subclass of SoundEvent that defines the specific properties and behavior of the
     *           sound event you are creating. By using a generic type parameter, this method can be used to register any type of sound event, allowing for flexibility
     *           and extensibility in your mod's audio content.
     */
    <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound);

    /**
     * Register a criterion trigger for the mod. This method allows you to register a new type of criterion trigger that can be used in advancements.
     * A criterion trigger is a condition that can be checked when certain events occur in the game, such as when a player performs a specific action or
     * achieves a certain milestone. By registering a criterion trigger, you can create custom advancements that are triggered by your mod's specific conditions,
     * allowing for more complex and unique advancement paths for players to explore.
     * @param id The unique identifier for the criterion trigger. This should be a ResourceLocation that identifies the trigger, and it will be used for
     *           registration and reference purposes. The ID should be unique to avoid conflicts with other mods or vanilla triggers.
     * @param criterionTrigger A supplier that provides the instance of the criterion trigger to be registered. This should return an instance of a subclass of
     *                         CriterionTrigger that defines the specific properties and behavior of the trigger you are creating. The supplier allows for lazy
     *                         initialization of the trigger, which can help with performance and resource management during mod loading.
     * @return A supplier that provides the registered criterion trigger. This allows you to reference the trigger in your code after it has been registered, and
     * it ensures that you are using the correct instance of the trigger when checking for conditions or triggering advancements.
     * @param <T> The type of the criterion trigger being registered. This should be a subclass of CriterionTrigger that defines the specific properties and behavior
     *           of the trigger you are creating. By using a generic type parameter, this method can be used to register any type of criterion trigger, allowing for
     *           flexibility and extensibility in your mod's advancement system.
     */
    <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger);

    /**
     * Register a stat for the mod. This method allows you to register a new type of stat that can be tracked and displayed in the game's statistics system. By
     * registering a stat, you can create custom statistics that track specific actions or events related to your mod, such as how many times a player has used a
     * certain item or how many entities they have killed with a specific weapon. The registered stat can then be used in your code to increment the statistic when
     * the relevant action occurs, and it will be displayed in the player's statistics screen along with other stats.
     * @param id The unique identifier for the stat. This should be a ResourceLocation that identifies the stat, and it will be used for registration and reference
     *           purposes. The ID should be unique to avoid conflicts with other mods or vanilla stats.
     * @param stats A supplier that provides the instance of the stat to be registered. This should return an instance of a subclass of ResourceLocation that defines
     *              the specific properties and behavior of the stat you are creating. The supplier allows for lazy initialization of the stat, which can help with
     *              performance and resource management during mod loading.
     * @return A supplier that provides the registered stat. This allows you to reference the stat in your code after it has been registered, and it ensures that you
     * are using the correct instance of the stat when incrementing it or displaying it in the statistics screen.
     * @param <T> The type of the stat being registered. This should be a subclass of ResourceLocation that defines the specific properties and behavior of the stat
     *           you are creating. By using a generic type parameter, this method can be used to register any type of stat, allowing for flexibility and extensibility
     *           in your mod's statistics system.
     */
    <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats);

    /**
     * Register a menu type for the mod. This method allows you to register a new type of menu that can be used in the game's GUI system. A menu type defines the
     * structure and behavior of a menu, including how it interacts with the player's inventory and how it is displayed on the screen. By registering a menu type,
     * you can create custom GUIs for your mod that provide unique functionality or interfaces for players to interact with. The registered menu type can then be
     * used in your code to create instances of the menu and display them to players as needed.
     * @param id The unique identifier for the menu type. This should be a ResourceLocation that identifies the menu type, and it will be used for registration and
     *           reference purposes. The ID should be unique to avoid conflicts with other mods or vanilla menu types.
     * @param menuType A supplier that provides the instance of the menu type to be registered. This should return an instance of a subclass of MenuType that defines
     *                 the specific properties and behavior of the menu you are creating. The supplier allows for lazy initialization of the menu type, which can help
     *                 with performance and resource management during mod loading.
     * @return A supplier that provides the registered menu type. This allows you to reference the menu type in your code after it has been registered, and it ensures
     * that you are using the correct instance of the menu type when creating instances of the menu or displaying it to players.
     * @param <T> The type of the menu type being registered. This should be a subclass of MenuType that defines the specific properties and behavior of the menu you
     *           are creating. By using a generic type parameter, this method can be used to register any type of menu, allowing for flexibility and extensibility in
     *           your mod's GUI system.
     */
    <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType);

    /**
     * Register a data component type for the mod. This method allows you to register a new type of data component that can be used in the game's data system.
     * Data components are used to store and manage custom data for entities, blocks, or other game objects, and they can be used to add new functionality or
     * behavior to these objects. By registering a data component type, you can create custom data structures that can be attached to entities or blocks, allowing
     * you to store additional information or implement new mechanics in your mod. The registered data component type can then be used in your code to create
     * instances of the component and attach them to the relevant game objects as needed.
     * @param id The unique identifier for the data component type. This should be a ResourceLocation that identifies the component type, and it will be used
     *           for registration and reference purposes. The ID should be unique to avoid conflicts with other mods or vanilla components.
     * @param component A supplier that provides the instance of the data component type to be registered. This should return an instance of a subclass of
     *                  DataComponentType that defines the specific properties and behavior of the data component you are creating. The supplier allows for
     *                  lazy initialization of the component type, which can help with performance and resource management during mod loading.
     * @return A supplier that provides the registered data component type. This allows you to reference the data component type in your code after it has been
     * registered, and it ensures that you are using the correct instance of the component type when creating instances of the component or attaching it to game objects.
     * @param <T> The type of the data component type being registered. This should be a subclass of DataComponentType that defines the specific properties and
     *           behavior of the data component you are creating. By using a generic type parameter, this method can be used to register any type of data component,
     *           allowing for flexibility and extensibility in your mod's data system.
     */
    <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component);

    /**
     * Register an entity effect for enchantments. This method allows you to register a new type of entity effect that can be applied by enchantments in the
     * game. The entity effect is defined by a MapCodec, which specifies how the effect should be serialized and deserialized for storage and network communication.
     * By registering an entity effect, you can create custom enchantments that apply unique effects to entities when they are hit or affected by the enchantment.
     * This adds more depth and customization options to the enchantment system in Minecraft.
     * @param name The unique name for the entity effect. This should be a ResourceLocation that identifies the effect, and it will be used for registration
     *             and reference purposes. The name should be unique to avoid conflicts with other mods or vanilla effects.
     * @param codec A supplier that provides the MapCodec for the entity effect. The MapCodec defines how the entity effect is serialized and deserialized,
     *              which is important for saving the effect data and sending it over the network. The codec should be designed to handle the specific properties
     *              and behavior of the entity effect you are creating.
     * @return A supplier that provides the registered entity effect. This allows you to reference the entity effect in your code after it has been registered,
     * and it ensures that you are using the correct instance of the effect when applying it to entities or checking for its presence.
     * @param <T> The type of the MapCodec for the entity effect. This should be a subclass of MapCodec that is specific to the type of entity effect you are
     *           creating, and it should be designed to handle the properties and behavior of that effect.
     */
    <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec);

    /**
     * Register a feature for the mod. This method allows you to register a new type of feature that can be used in world generation. Features are used to define
     * specific structures, terrain modifications, or other elements that can be generated in the world. By registering a feature, you can create custom world generation content that enhances the player's experience and adds unique elements to the game world. The registered feature can then be referenced in your code when defining world generation settings or when creating custom biomes, allowing you to control how and where the feature is generated in the world.
     * @param name The unique name for the feature. This should be a ResourceLocation that identifies the feature, and it will be used for registration and reference purposes.
     * @param feature A supplier that provides the instance of the feature to be registered. This should return an instance of a subclass of Feature that defines the specific properties and behavior of the feature you are creating. The supplier allows for lazy initialization of the feature, which can help with performance and resource management during mod loading.
     * @return A supplier that provides the registered feature. This allows you to reference the feature in your code after it has been registered, and it ensures that you are using the correct instance of the feature when generating it in the world or checking for its presence.
     * @param <T> The type of the feature being registered. This should be a subclass of Feature that defines the specific properties and behavior of the feature you are creating. By using a generic type parameter, this method can be used to register any type of feature, allowing for flexibility and extensibility in your mod's world generation content.
     */
    <T extends Feature<?>> Supplier<T> registerFeature(ResourceLocation name, Supplier<T> feature);

    /**
     * Register a creative mode tab for the mod. This method allows you to register a new creative mode tab that can be accessed in the creative mode inventory.
     * A creative mode tab is a category of items that can be organized and displayed together in the creative inventory, making it easier for players to find
     * and access items related to your mod. By registering a creative mode tab, you can create a dedicated section in the creative inventory for your mod's items,
     * enhancing the user experience and making it more convenient for players to explore and use the content provided by your mod.
     * @param id The unique identifier for the creative mode tab. This should be a ResourceLocation that identifies the tab, and it will be used for registration
     *           and reference purposes. The ID should be unique to avoid conflicts with other mods or vanilla tabs.
     * @param tab A supplier that provides the instance of the creative mode tab to be registered. This should return an instance of a subclass of CreativeModeTab
     *            that defines the specific properties and behavior of the tab you are creating. The supplier allows for lazy initialization of the tab, which
     *            can help with performance and resource management during mod loading.
     * @return A supplier that provides the registered creative mode tab. This allows you to reference the tab in your code after it has been registered, and
     * it ensures that you are using the correct instance of the tab when adding items to it or displaying it in the creative inventory.
     * @param <T> The type of the creative mode tab being registered. This should be a subclass of CreativeModeTab that defines the specific properties and
     *           behavior of the tab you are creating. By using a generic type parameter, this method can be used to register any type of creative mode tab,
     *           allowing for flexibility and extensibility in your mod's creative inventory organization.
     */
    <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab);

    /**
     * Register a permission validator for the mod. This method should be called during the mod initialization process to set up the permission system
     * for the mod. The implementation of this method may vary depending on the mod loader and the specific permission system being used, but it generally
     * involves registering a validator that can check if a player has certain permissions or access rights within the mod. This allows for more fine-grained
     * control over who can access certain features or perform certain actions within the mod, enhancing security and customization options for server administrators
     * and players.
     */
    void registerPermissionValidator();

    /**
     * Create a new CreativeModeTab.Builder instance. This method is used to create a new builder for creating a creative mode tab, which
     * is a category of items that can be accessed in the creative mode inventory. The builder allows you to set various properties of the
     * creative tab, such as its title, icon, and the items that should be included in the tab. This method abstracts away the differences
     * in how creative tabs are created and registered across different mod loaders, providing a unified way to create creative tabs in your mod.
     * @return A new instance of CreativeModeTab.Builder that can be used to create and register a creative mode tab.
     */
    CreativeModeTab.Builder newCreativeTabBuilder();
}
