package dev.matthiesen.matthiesen_core.common.abstracts.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * A simple screen abstraction for screens that only need a background texture and no labels. This is useful for custom GUI implementations
 * that rely on other methods for displaying information to the player, such as tooltips or custom rendering. This class handles rendering the
 * background texture and suppressing the default labels, so that subclasses can focus on implementing the specific functionality of the screen
 * without having to worry about the basic rendering of the background and labels. Subclasses should provide the background texture and dimensions
 * by implementing the abstract methods, and they can override the render method to add custom rendering or tooltips as needed.
 *
 * @param <T> The type of the menu that this screen is associated with. This should be a subclass of AbstractContainerMenu, and it should
 *           match the menu type that is used for the screen's container. This allows the screen to access the menu's inventory and other data as needed.
 */
@SuppressWarnings("unused")
public abstract class AbstractSimpleScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    /**
     * Get the width of the background texture for this screen. This should return the width of the texture that is returned
     * by getBackgroundTexture(), and it will be used to set the width of the screen and to render the background texture correctly.
     * @return The width of the background texture for this screen.
     */
    protected abstract int getBgWidth();

    /**
     * Get the height of the background texture for this screen. This should return the height of the texture that is returned
     * by getBackgroundTexture(), and it will be used to set the height of the screen and to render the background texture correctly.
     * @return The height of the background texture for this screen.
     */
    protected abstract int getBgHeight();

    /**
     * Get the background texture for this screen. This should return a ResourceLocation that points to the texture to be used as the background for this screen.
     * @return A ResourceLocation that points to the texture to be used as the background for this screen.
     */
    protected abstract ResourceLocation getBackgroundTexture();

    private final int BG_WIDTH = getBgWidth();
    private final int BG_HEIGHT = getBgHeight();
    private final ResourceLocation BACKGROUND = getBackgroundTexture();

    /**
     * Default constructor for the AbstractSimpleScreen class. This constructor initializes the screen with the specified
     * menu, inventory, and title component,
     * @param abstractContainerMenu The menu that this screen is associated with. This should be an instance of a subclass of
     *                              AbstractContainerMenu that matches the menu type used for the screen's container.
     * @param inventory The player's inventory. This is used to display the player's inventory in the screen and to allow the player
     *                  to interact with it as needed.
     * @param component The title component for this screen. This is used to display the title of the screen, and it can be set to an
     *                  empty component if the screen does not need a title.
     */
    public AbstractSimpleScreen(T abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = BG_WIDTH;
        this.imageHeight = BG_HEIGHT;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, BG_WIDTH, BG_HEIGHT, BG_WIDTH, BG_HEIGHT);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Suppress default container-title and "Inventory" labels.
    }
}
