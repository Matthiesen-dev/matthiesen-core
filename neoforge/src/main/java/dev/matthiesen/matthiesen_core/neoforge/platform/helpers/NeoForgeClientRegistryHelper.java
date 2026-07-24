package dev.matthiesen.matthiesen_core.neoforge.platform.helpers;

import dev.matthiesen.matthiesen_core.common.api.client.*;
import dev.matthiesen.matthiesen_core.common.api.client.block_outline.BlockOutlineContext;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudOrdering;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudRegistrar;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The NeoForgeClientRegistryHelper class provides utility methods for registering client-side components such as screens,
 * entity renderers, HUD elements, and key bindings in the NeoForge mod loader environment. It ensures that these registrations
 * are performed at the appropriate time during the mod loading process by utilizing the event bus system provided by NeoForge.
 */
public final class NeoForgeClientRegistryHelper {
    private static volatile IEventBus modBus;

    private NeoForgeClientRegistryHelper() {}

    public static void init(IEventBus eventBus) {
        modBus = eventBus;
    }

    public static void applyScreenRegistrations(Consumer<ScreenRegistrar> screenRegistrarConsumer) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeClientRegistryHelper has not been initialized.");
        }
        eventBus.addListener(EventPriority.LOWEST, (RegisterMenuScreensEvent event) ->
                screenRegistrarConsumer.accept(event::register));
    }

    @SuppressWarnings("rawtypes")
    public static void applyEntityRendererRegistrations(BiConsumer<
            BiConsumer<EntityType<? extends Entity>, EntityRendererProvider>,
            BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider>
            > entityRendererConsumer) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeClientRegistryHelper has not been initialized.");
        }
        eventBus.addListener(EventPriority.LOWEST, (EntityRenderersEvent.RegisterRenderers event) ->
                entityRendererConsumer.accept(event::registerEntityRenderer, event::registerBlockEntityRenderer));
    }

    public static void applyKeyBindingRegistrations(Consumer<KeyMappingRegistrar> registrar) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeClientRegistryHelper has not been initialized.");
        }
        eventBus.addListener(EventPriority.LOWEST, (RegisterKeyMappingsEvent event) ->
                registrar.accept(event::register));
    }

    public static void applyBlockHighlightOverrides(Function<ClientEvent.BlockHighlight, InteractionResult> blockHighlightEventHandler) {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, (RenderHighlightEvent.Block event) -> {
            if (Minecraft.getInstance().level == null) {
                return;
            }

            BlockOutlineContext context = new BlockOutlineContext(
                    Minecraft.getInstance().level,
                    event.getTarget(),
                    event.getPoseStack(),
                    event.getCamera(),
                    event.getMultiBufferSource()
            );

            InteractionResult result = blockHighlightEventHandler.apply(new ClientEvent.BlockHighlight(context));
            if (result == InteractionResult.FAIL) {
                event.setCanceled(true);
            }
        });
    }

    public static void applyHudRegistrations(Consumer<HudRegistrar> hudRegistrarConsumer) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeClientRegistryHelper has not been initialized.");
        }

        eventBus.addListener(EventPriority.NORMAL, (RegisterGuiLayersEvent event) ->
                hudRegistrarConsumer.accept((ordering, other, key, layer) -> {
                    if (ordering == HudOrdering.BEFORE) {
                        if (other == null) {
                            event.registerBelowAll(key, layer);
                        } else {
                            event.registerBelow(other, key, layer);
                        }
                    } else {
                        if (other == null) {
                            event.registerAboveAll(key, layer);
                        } else {
                            event.registerAbove(other, key, layer);
                        }
                    }
                }));
    }
}
