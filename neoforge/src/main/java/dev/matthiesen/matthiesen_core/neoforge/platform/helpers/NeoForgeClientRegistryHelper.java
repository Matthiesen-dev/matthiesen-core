package dev.matthiesen.matthiesen_core.neoforge.platform.helpers;

import dev.matthiesen.matthiesen_core.common.api.client.*;
import dev.matthiesen.matthiesen_core.common.api.client.block_outline.BlockOutlineContext;
import dev.matthiesen.matthiesen_core.common.api.client.keybinds.KeyMappingRegistrar;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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

    public static void onHudRender(Consumer<ClientEvent.HudRender> hudRenderEventConsumer) {
        NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, (RenderGuiLayerEvent.Post event) -> {
            hudRenderEventConsumer.accept(new ClientEvent.HudRender(
                    event.getGuiGraphics(),
                    event.getPartialTick()
            ));
        });
    }

    public static void applyBlockHighlightOverrides(Consumer<ClientEvent.BlockHighlight> blockHighlightEventConsumer) {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, (RenderHighlightEvent.Block event) -> {
            if (Minecraft.getInstance().level == null) return;

            BlockOutlineContext context = new BlockOutlineContext(
                    Minecraft.getInstance().level,
                    event.getTarget(),
                    event.getPoseStack(),
                    event.getCamera(),
                    event.getMultiBufferSource()
            );

            blockHighlightEventConsumer.accept(new ClientEvent.BlockHighlight(context));
        });
    }
}
