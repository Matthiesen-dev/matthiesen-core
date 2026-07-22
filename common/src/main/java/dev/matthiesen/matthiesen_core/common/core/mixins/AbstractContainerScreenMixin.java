package dev.matthiesen.matthiesen_core.common.core.mixins;

import dev.matthiesen.matthiesen_core.common.core.item.CreativeTabSectionHeaderItem;
import dev.matthiesen.matthiesen_core.common.core.registry.CreativeModeTabSectionsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Shadow
    protected Slot hoveredSlot;

    // require = 0: NeoForge patches AbstractContainerScreen#render to call a new instance
    // renderSlotHighlight(GuiGraphics, Slot, int, int, float) instead of the vanilla static
    // renderSlotHighlight(GuiGraphics, int, int, int), so this call-site no longer exists there.
    // A NeoForge-specific mixin (AbstractContainerScreenNeoForgeMixin) handles that platform.
    @Redirect(
            method = "render",
            require = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;III)V"
            )
    )
    private void skipSectionHeaderHoverHighlight(GuiGraphics guiGraphics, int i, int j, int k) {
        if (hoveredSlot != null && CreativeTabSectionHeaderItem.isSectionMarkerStack(hoveredSlot.getItem())) {
            return;
        }
        AbstractContainerScreen.renderSlotHighlight(guiGraphics, i, j, k);
    }

    @Inject(method = "renderSlot", at = @At("HEAD"), cancellable = true)
    private void drawSectionBanner(GuiGraphics guiGraphics, Slot slot, CallbackInfo info) {
        if ((Object) this instanceof CreativeModeInventoryScreen && CreativeTabSectionHeaderItem.isSectionMarkerStack(slot.getItem())) {
            info.cancel();

            if (CreativeTabSectionHeaderItem.isPlaceholder(slot.getItem())) {
                return;
            }

            int x = slot.x;
            int y = slot.y;

            ResourceLocation creativeModeTabId = CreativeTabSectionHeaderItem.getCreativeModeTabId(slot.getItem());
            ResourceLocation sectionId = CreativeTabSectionHeaderItem.getSectionId(slot.getItem());
            CreativeModeTabSectionsManager.SectionData sectionData = CreativeModeTabSectionsManager.getTabMetaData(creativeModeTabId, sectionId);

            if (sectionData != null) {
                int barWidth = 160;
                int barHeight = 16;
                var sectionMeta = sectionData.meta();

                boolean renderedImage = false;
                ResourceLocation backgroundImage = sectionMeta.getSectionBackgroundImage();
                if (backgroundImage != null) {
                    try {
                        guiGraphics.blit(backgroundImage, x, y, 0, 0, barWidth, barHeight, barWidth, barHeight);
                        renderedImage = true;
                    } catch (RuntimeException ignored) {
                        // Fall back to color-based rendering when a texture is missing or invalid.
                    }
                }

                if (!renderedImage) {
                    // Draw a simple 9-slot-wide header bar so section headers work even without external textures.
                    guiGraphics.fill(x, y, x + barWidth, y + barHeight, sectionMeta.getSectionBackgroundColor());
                    guiGraphics.fill(x, y, x + barWidth, y + 2, sectionMeta.getSectionTitleAccentColor());
                }

                guiGraphics.drawString(
                        Minecraft.getInstance().font,
                        sectionData.title(),
                        x + 5,
                        y + 4,
                        sectionMeta.getSectionTitleColor(),
                        sectionMeta.getSectionTitleShadow()
                );
            }
        }
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void hideSectionHeaderTooltip(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        if ((Object) this instanceof CreativeModeInventoryScreen
                && hoveredSlot != null
                && CreativeTabSectionHeaderItem.isSectionMarkerStack(hoveredSlot.getItem())) {
            ci.cancel();
        }
    }
}
