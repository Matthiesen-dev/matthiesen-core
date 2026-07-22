package dev.matthiesen.matthiesen_core.common.api.client;

import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * Utility interface for registering HUD layers in a platform-neutral way.
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface HudRegistrar {
    /**
     * Registers a HUD layer with explicit ordering information.
     *
     * @param ordering Whether the new layer should render before or after {@code other}.
     * @param other The layer id to order against, or {@code null} to target the top or bottom of the stack.
     * @param key A unique id for the layer.
     * @param layer The layer implementation.
     */
    void register(HudOrdering ordering, @Nullable ResourceLocation other, ResourceLocation key, LayeredDraw.Layer layer);

    /**
     * Registers a HUD layer to render below all existing layers.
     * @param key A unique id for the layer.
     * @param layer The layer implementation.
     */
    default void registerBelowAll(ResourceLocation key, LayeredDraw.Layer layer) {
        register(HudOrdering.BEFORE, null, key, layer);
    }

    /**
     * Registers a HUD layer to render relative to an existing layer.
     * @param other The layer id to order against.
     * @param key A unique id for the layer.
     * @param layer The layer implementation.
     */
    default void registerBelow(ResourceLocation other, ResourceLocation key, LayeredDraw.Layer layer) {
        register(HudOrdering.BEFORE, other, key, layer);
    }

    /**
     * Registers a HUD layer to render above an existing layer.
     * @param other The layer id to order against.
     * @param key A unique id for the layer.
     * @param layer The layer implementation.
     */
    default void registerAbove(ResourceLocation other, ResourceLocation key, LayeredDraw.Layer layer) {
        register(HudOrdering.AFTER, other, key, layer);
    }

    /**
     * Registers a HUD layer to render above all existing layers.
     * @param key A unique id for the layer.
     * @param layer The layer implementation.
     */
    default void registerAboveAll(ResourceLocation key, LayeredDraw.Layer layer) {
        register(HudOrdering.AFTER, null, key, layer);
    }
}

