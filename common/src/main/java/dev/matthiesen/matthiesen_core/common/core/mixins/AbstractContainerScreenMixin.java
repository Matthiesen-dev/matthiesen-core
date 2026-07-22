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

/**
 * This mixin class modifies the behavior of the AbstractContainerScreen class in Minecraft to enhance the rendering of section headers in the creative inventory screen.
 * It provides functionality to skip the default slot highlight for section header slots, draw custom section banners for section
 * header slots, and hide tooltips for section header items. This allows for a more visually appealing and customizable presentation of section
 * headers in the creative inventory screen, improving the user experience by providing clear visual separation between different sections of items and
 * making it easier for players to navigate and find the items they are looking for.
 * <p>
 * The mixin class contains three main methods:
 * </p>
 * <p>
 * 1. skipSectionHeaderHoverHighlight: This method redirects the call to renderSlotHighlight in the render method of AbstractContainerScreen
 * to skip highlighting section header slots. It checks if the hovered slot is a section marker stack, and if so, it skips the call to
 * renderSlotHighlight, allowing the section banner to be drawn instead.
 * </p>
 * <p>
 * 2. drawSectionBanner: This method is injected into the renderSlot method of AbstractContainerScreen to draw a section banner for section
 * header slots. It checks if the current instance is a CreativeModeInventoryScreen and if the slot's item is a section marker stack. If so,
 * it cancels the original renderSlot method and draws the section banner instead, using the section metadata to determine the title, background
 * color, accent color, and title color. If a background image is specified in the section metadata, it is drawn instead of the color-based banner.
 * </p>
 * <p>
 * 3. hideSectionHeaderTooltip: This method is injected into the renderTooltip method of AbstractContainerScreen to hide the tooltip for section
 * header items in the creative inventory screen. It checks if the current instance is a CreativeModeInventoryScreen and if the hovered slot is a section
 * marker stack. If so, it cancels the original renderTooltip method, preventing the default tooltip from being displayed when hovering over section header
 * items, as they are not meant to be interacted with and do not have any associated functionality.
 * </p>
 */
@SuppressWarnings("ConstantConditions")
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Shadow
    protected Slot hoveredSlot;

    /**
     * Redirects the call to renderSlotHighlight in AbstractContainerScreen's render method to skip highlighting section header slots.
     * This is done to prevent the default slot highlight from being drawn over section header slots, which are not meant to be interacted
     * with and do not have any associated functionality. Instead, the section banner is drawn in the drawSectionBanner method, which provides
     * a more visually appealing and customizable section header in the creative inventory screen. This redirect checks if the hovered slot is
     * a section marker stack, and if so, it skips the call to renderSlotHighlight, allowing the section banner to be drawn instead. This enhances
     * the user experience in the creative inventory screen by providing clear visual separation between different sections of items, making it easier
     * for players to navigate and find the items they are looking for.
     *
     * <p>
     *     A NeoForge-specific mixin (AbstractContainerScreenNeoForgeMixin) handles this for NeoForge due to their own injection getting in the way.
     * </p>
     *
     * @param guiGraphics The graphics context used for rendering the slot highlight
     * @param i The x-coordinate of the slot
     * @param j The y-coordinate of the slot
     * @param k The z-index of the slot
     */
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

    /**
     * Draws a section banner for the given slot if it is a section marker stack. This method is injected into the renderSlot method of the AbstractContainerScreen class.
     * It checks if the current instance is a CreativeModeInventoryScreen and if the slot's item is a section marker stack. If so, it cancels the original
     * renderSlot method and draws the section banner instead. The section banner is drawn using the section metadata, including the title, background color,
     * accent color, and title color. If a background image is specified in the section metadata, it is drawn instead of the color-based banner. This allows
     * for a more visually appealing and customizable section header in the creative inventory screen. The method also handles cases where the background image
     * is missing or invalid, falling back to color-based rendering in those cases. The section banner is drawn at the slot's x and y coordinates, with a fixed
     * width and height. The title is drawn with the specified title color and shadow settings from the section metadata. This method enhances the user experience
     * in the creative inventory screen by providing clear visual separation between different sections of items, making it easier for players to navigate and find
     * the items they are looking for.
     * @param guiGraphics The graphics context used for rendering the section banner
     * @param slot The slot being rendered, which may contain a section marker stack
     * @param info The callback info for the injected method, allowing for cancellation of the original renderSlot method
     */
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
            CreativeModeTabSectionsManager.SectionData sectionData = CreativeModeTabSectionsManager.INSTANCE.getTabMetaData(creativeModeTabId, sectionId);

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

    /**
     * Hides the tooltip for section header items in the creative inventory screen. This prevents the default tooltip from being displayed
     * when hovering over section header items, as they are not meant to be interacted with and do not have any associated functionality.
     * @param guiGraphics The graphics context used for rendering the tooltip
     * @param i The x-coordinate of the mouse cursor
     * @param j The y-coordinate of the mouse cursor
     * @param ci The callback info for the injected method
     */
    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void hideSectionHeaderTooltip(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        if ((Object) this instanceof CreativeModeInventoryScreen
                && hoveredSlot != null
                && CreativeTabSectionHeaderItem.isSectionMarkerStack(hoveredSlot.getItem())) {
            ci.cancel();
        }
    }
}
