package dev.matthiesen.matthiesen_core.neoforge.mixins;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents;
import dev.matthiesen.matthiesen_core.common.api.events.client.ClientEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The MinecraftClientShutdownMixin class is a Mixin that injects into the Minecraft client shutdown process to emit a client stopping event.
 * It is used to ensure that any necessary cleanup or finalization tasks are performed before the client fully shuts down, allowing
 * for proper resource management and event handling in the Matthiesen Core mod.
 */
@Mixin(Minecraft.class)
public class MinecraftClientShutdownMixin {

    /**
     * This method is injected into the Minecraft client shutdown process to emit a client stopping event.
     * It is called at the head of the stop method, allowing for any necessary cleanup or finalization tasks to be performed before the client fully shuts down.
     * @param ci The CallbackInfo object that provides information about the method being injected into.
     */
    @Inject(method = "stop", at = @At("HEAD"))
    private void onClientShutdown(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        PlatformClientEvents.CLIENT_STOPPING.emit(new ClientEvent.Stopping(client));
    }
}
