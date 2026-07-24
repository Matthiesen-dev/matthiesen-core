package dev.matthiesen.matthiesen_core.common.api.platform.registry;

import net.minecraft.network.chat.Component;

/**
 * Represents a definition for a resource pack in the modding framework. This record encapsulates the essential information
 * required to define a resource pack, including its associated mod ID, unique identifier, display name, and activation behavior.
 * Resource packs are used to modify or enhance the visual and audio aspects of the game, and this definition provides a structured
 * way to manage them within the modding environment.
 * @param modId The unique identifier for the mod that provides the resource pack. This is typically a string that uniquely
 *              identifies the mod within the modding framework.
 * @param id The unique identifier for the resource pack itself. This should be a string that uniquely identifies the resource
 *           pack within the context of the mod.
 * @param displayName The display name of the resource pack, represented as a Component. This is the name that will be shown to
 *                    users in the game's interface when selecting or managing resource packs.
 * @param activationBehaviour The activation behavior of the resource pack, represented by an instance of ResourcePackActivationBehaviour.
 *                            This defines how the resource pack is activated or applied within the game, such as whether it is automatically
 *                            enabled, requires user confirmation, or has other specific activation rules.
 */
public record ResourcePackDef(
        String modId,
        String id,
        Component displayName,
        ResourcePackActivationBehaviour activationBehaviour
) {
    /**
     * Creates a new ResourcePackDef instance with the specified parameters. This constructor allows for the creation of a resource
     * pack definition with a string display name, which is converted to a Component for internal use.
     * @param modId The unique identifier for the mod.
     * @param id The unique identifier for the resource pack.
     * @param displayName The display name of the resource pack.
     * @param activationBehaviour The activation behavior of the resource pack.
     */
    public ResourcePackDef(
            String modId,
            String id,
            String displayName,
            ResourcePackActivationBehaviour activationBehaviour
    ) {
        this(modId, id, Component.literal(displayName), activationBehaviour);
    }
}
