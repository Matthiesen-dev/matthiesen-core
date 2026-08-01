package dev.matthiesen.matthiesen_core.common.registry;

import com.mojang.serialization.MapCodec;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.SupportedRegistries;
import dev.matthiesen.matthiesen_core.common.core.registry.RegistryBuilder;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

import java.util.function.Supplier;

/**
 * Convenience base class for registries that register enchantment entity effect codecs.
 *
 * <p>This type locks registration to the entity effect registry category by wiring
 * {@link SupportedRegistries#ENTITY_EFFECT} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractEntityEffectRegistry extends AbstractRegistry<MapCodec<? extends EnchantmentEntityEffect>> {
    /**
     * Creates an entity effect registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractEntityEffectRegistry(String modId) {
        super(modId, SupportedRegistries.ENTITY_EFFECT);
    }

    /**
     * Creates an entity effect registry using an existing {@link RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform entity effect registrations
     */
    protected AbstractEntityEffectRegistry(RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.ENTITY_EFFECT);
    }

    /**
     * Registers an entity effect codec with the given ID.
     * @param id the ID used to namespace the registration
     * @param codec the codec to register
     * @return a supplier for the registered codec
     * @param <T> the type of the entity effect
     */
    public <T extends EnchantmentEntityEffect> Supplier<MapCodec<T>> register(String id, MapCodec<T> codec) {
        return register(id, () -> codec);
    }
}

