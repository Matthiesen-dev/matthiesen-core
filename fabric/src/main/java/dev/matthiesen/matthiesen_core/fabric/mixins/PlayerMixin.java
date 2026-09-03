package dev.matthiesen.matthiesen_core.fabric.mixins;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The PlayerMixin class is a Mixin that injects custom behavior into the Player class in Minecraft.
 * It listens for player tick events and emits corresponding events to the PlatformEvents event bus.
 */
@Mixin(Player.class)
public class PlayerMixin {

    /**
     * Injects custom behavior at the start of the player's tick method.
     * Emits a PlayerEvent.PreTick event to the PlatformEvents event bus.
     *
     * @param ci The CallbackInfo object provided by the Mixin framework.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void onPreTick(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (player instanceof ServerPlayer serverPlayer) {
            PlatformEvents.PLAYER_PRE_TICK.emit(new PlayerEvent.PreTick(serverPlayer));
        }
    }

    /**
     * Injects custom behavior at the end of the player's tick method.
     * Emits a PlayerEvent.EndTick event to the PlatformEvents event bus.
     *
     * @param ci The CallbackInfo object provided by the Mixin framework.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void onPostTick(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (player instanceof ServerPlayer serverPlayer) {
            PlatformEvents.PLAYER_END_TICK.emit(new PlayerEvent.EndTick(serverPlayer));
        }
    }
}
