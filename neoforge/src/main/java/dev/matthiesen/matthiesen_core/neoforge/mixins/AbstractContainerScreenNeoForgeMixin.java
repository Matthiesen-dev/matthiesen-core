package dev.matthiesen.matthiesen_core.neoforge.mixins;

import dev.matthiesen.matthiesen_core.common.core.item.CreativeTabSectionHeaderItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin class modifies the behavior of the AbstractContainerScreen class in the NeoForge mod loader environment.
 * It specifically targets the rendering of slot highlights, allowing for the suppression of the highlight effect for
 * CreativeTabSectionHeaderItem slots. This is achieved by injecting code into the renderSlotHighlight method, where it
 * checks if the slot being highlighted is a section marker stack and cancels the highlight rendering if so. This ensures
 * that the user interface behaves as intended when interacting with creative tab section headers.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenNeoForgeMixin {

    /**
     * Suppress the slot-hover highlight for CreativeTabSectionHeaderItem slots.
     * The {@code slot} parameter is the exact slot NeoForge is about to highlight,
     * so we only need to check its item stack — no shadow field access required.
     */
    @Inject(
            method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;IIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void skipSectionHeaderHoverHighlight(
            GuiGraphics guiGraphics, Slot slot, int x, int y, float partialTick, CallbackInfo ci) {
        if (slot != null && CreativeTabSectionHeaderItem.isSectionMarkerStack(slot.getItem())) {
            ci.cancel();
        }
    }
}
