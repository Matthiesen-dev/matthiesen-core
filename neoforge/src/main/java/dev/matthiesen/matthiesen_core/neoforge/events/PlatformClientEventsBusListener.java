package dev.matthiesen.matthiesen_core.neoforge.events;

import dev.matthiesen.matthiesen_core.common.api.client.BlockOutlineContext;
import dev.matthiesen.matthiesen_core.common.api.client.hud.HudOrdering;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackActivationBehaviour;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommonClient;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.InteractionResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.Optional;

/**
 * The PlatformClientEventsBusListener class is responsible for handling client-side events in the NeoForge mod loader environment.
 */
@EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID, value = Dist.CLIENT)
public final class PlatformClientEventsBusListener {
    /**
     * This method is called during the client setup phase of the mod loading process. It is responsible for performing
     * any necessary client-side initialization tasks, such as setting up renderers, key bindings, and other client-specific features.
     * @param event The FMLClientSetupEvent event that triggers this method.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void clientSetup(FMLClientSetupEvent event) {
        MatthiesenCoreCommonClient.INSTANCE.onClientSetup();
    }

    /**
     * This method is called at the end of each client tick. It checks if the player instance is not null and then executes
     * all the Runnables that have been registered to run at the end of the client tick. This allows for scheduling tasks
     * that need to be performed after the main client tick processing is complete.
     * @param event The ClientTickEvent.Post event that triggers this method.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClientEndTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().player != null) {
            PlatformClientEvents.CLIENT_END_TICK.emit(new ClientEvent.EndTick());
        }
    }

    /**
     * This method is called when the game is registering GUI layers. It allows for the registration of custom HUD layers
     * by consuming the HudRegistrar provided by the event. The method uses the applyHudLayerRegistrations method from
     * PlatformClientEvents to register the HUD layers with the appropriate ordering and keys.
     * @param event The RegisterGuiLayersEvent event that triggers this method.
     */
    @SubscribeEvent
    public static void onRegisterGuiLayersEvent(RegisterGuiLayersEvent event) {
        PlatformClientEvents.applyHudLayerRegistrations((ordering, other, key, layer) -> {
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
        });
    }

    /**
     * This method is called when the game is rendering block highlights. It creates a BlockOutlineContext based on the current
     * game state and emits a BlockHighlight event. If the result of the event is InteractionResult.FAIL, the method cancels
     * the default block highlight rendering, allowing for custom behavior to be implemented.
     * @param event The RenderHighlightEvent.Block event that triggers this method.
     */
    @SubscribeEvent
    public static void onBlockHighlight(RenderHighlightEvent.Block event) {
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

        InteractionResult result = PlatformClientEvents.emitBlockHighlight(new ClientEvent.BlockHighlight(context));
        if (result == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }

    /**
     * This method is called when the game is adding resource pack finders. It allows for the registration of custom resource packs
     * by consuming the ResourcePackRegistrar provided by the event. The method uses the applyResourcePackRegistrations method
     * from PlatformClientEvents to register the resource packs with the appropriate mod ID, display name, and activation behavior.
     * @param event The AddPackFindersEvent event that triggers this method.
     */
    @SubscribeEvent
    public static void onRegisterResourcePack(AddPackFindersEvent event) {
        PlatformClientEvents.applyResourcePackRegistrations((packDef) -> {
            var modContainer = ModList.get().getModContainerById(packDef.modId());
            if (modContainer.isEmpty()) {
                throw new IllegalArgumentException("Mod ID " + packDef.modId() + " not found in mod list.");
            }

            var modFile = modContainer.get().getModInfo();
            var packLocation = ResourceLocation.fromNamespaceAndPath(packDef.modId(), "resourcepacks/" + packDef.id());
            var resourcePath = modFile.getOwningFile().getFile().findResource(packLocation.getPath());
            var version = modFile.getVersion();

            var pack = Pack.readMetaAndCreate(
                    new PackLocationInfo(
                            "mod/" + packLocation,
                            packDef.displayName(),
                            PackSource.BUILT_IN,
                            Optional.of(new KnownPack("neoforge", "mod/" + packLocation, version.toString()))
                    ),
                    BuiltInPackSource.fromName((info) -> new PathPackResources(info, resourcePath)),
                    PackType.CLIENT_RESOURCES,
                    new PackSelectionConfig(
                            packDef.activationBehaviour() == ResourcePackActivationBehaviour.ALWAYS_ENABLED,
                            Pack.Position.TOP,
                            false
                    )
            );

            if (pack == null) {
                throw new IllegalStateException("Failed to create resource pack for mod " + packDef.modId() + " with ID " + packDef.id());
            }

            event.addRepositorySource(it -> it.accept(pack));
        });
    }
}
