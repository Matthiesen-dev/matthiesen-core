package dev.matthiesen.matthiesen_core.common.api.client.hud;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

/**
 * Identifiers for the vanilla {@link LayeredDraw.Layer}, in the order that they render.
 *
 * <p>The corresponding rendering code can be found in the source code of {@link Gui}.
 */
public final class NeoForgeVanillaGuiLayers {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private NeoForgeVanillaGuiLayers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Identifier for the camera overlays layer, which renders effects like pumpkin blur and spyglass overlay. This layer is rendered first, before all other layers.
     */
    public static final ResourceLocation CAMERA_OVERLAYS = ResourceLocation.withDefaultNamespace("camera_overlays");

    /**
     * Identifier for the crosshair layer, which renders the player's crosshair when they are not in spectator mode. This layer is rendered after camera overlays and before all other layers.
     */
    public static final ResourceLocation CROSSHAIR = ResourceLocation.withDefaultNamespace("crosshair");

    /**
     * Identifier for the hotbar layer, which renders the player's hotbar and offhand item. This layer is rendered after the crosshair and before all other layers.
     */
    public static final ResourceLocation HOTBAR = ResourceLocation.withDefaultNamespace("hotbar");

    /**
     * Identifier for the jump meter layer, which renders the jump meter when the player is flying with elytra. This layer is rendered after the hotbar and before all other layers.
     */
    public static final ResourceLocation JUMP_METER = ResourceLocation.withDefaultNamespace("jump_meter");

    /**
     * Identifier for the experience bar layer, which renders the player's experience bar and level. This layer is rendered after the jump meter and before all other layers.
     */
    public static final ResourceLocation EXPERIENCE_BAR = ResourceLocation.withDefaultNamespace("experience_bar");

    /**
     * Identifier for the player health layer, which renders the player's health bar and hearts. This layer is rendered after the experience bar and before all other layers.
     */
    public static final ResourceLocation PLAYER_HEALTH = ResourceLocation.withDefaultNamespace("player_health");

    /**
     * Identifier for the armor level layer, which renders the player's armor bar and icons. This layer is rendered after the player health and before all other layers.
     */
    public static final ResourceLocation ARMOR_LEVEL = ResourceLocation.withDefaultNamespace("armor_level");

    /**
     * Identifier for the food level layer, which renders the player's hunger bar and icons. This layer is rendered after the armor level and before all other layers.
     */
    public static final ResourceLocation FOOD_LEVEL = ResourceLocation.withDefaultNamespace("food_level");

    /**
     * Identifier for the vehicle health layer, which renders the health bar of the vehicle the player is currently riding (e.g. horse, boat, minecart). This layer is rendered after the food level and before all other layers.
     */
    public static final ResourceLocation VEHICLE_HEALTH = ResourceLocation.withDefaultNamespace("vehicle_health");

    /**
     * Identifier for the air level layer, which renders the player's air bubbles when they are underwater. This layer is rendered after the vehicle health and before all other layers.
     */
    public static final ResourceLocation AIR_LEVEL = ResourceLocation.withDefaultNamespace("air_level");

    /**
     * Identifier for the selected item name layer, which renders the name of the currently selected item in the hotbar when the player is not in spectator mode. This layer is rendered after the air level and before all other layers.
     */
    public static final ResourceLocation SELECTED_ITEM_NAME = ResourceLocation.withDefaultNamespace("selected_item_name");

    /**
     * Identifier for the spectator tooltip layer, which renders the tooltip of the entity the player is currently spectating when they are in spectator mode. This layer is rendered after the selected item name and before all other layers.
     */
    public static final ResourceLocation SPECTATOR_TOOLTIP = ResourceLocation.withDefaultNamespace("spectator_tooltip");

    /**
     * Identifier for the experience level layer, which renders the player's experience level number above the experience bar when they have at least one level. This layer is rendered after the spectator tooltip and before all other layers.
     */
    public static final ResourceLocation EXPERIENCE_LEVEL = ResourceLocation.withDefaultNamespace("experience_level");

    /**
     * Identifier for the effects layer, which renders the icons of the player's active status effects (e.g. potion effects) above the experience level when they have at least one active effect. This layer is rendered after the experience level and before all other layers.
     */
    public static final ResourceLocation EFFECTS = ResourceLocation.withDefaultNamespace("effects");

    /**
     * Identifier for the boss overlay layer, which renders the health bar and name of the boss mob the player is currently fighting (e.g. Ender Dragon, Wither) at the top of the screen. This layer is rendered after the effects and before all other layers.
     */
    public static final ResourceLocation BOSS_OVERLAY = ResourceLocation.withDefaultNamespace("boss_overlay");

    /**
     * Identifier for the sleep overlay layer, which renders the darkened screen effect when the player is sleeping in a bed. This layer is rendered after the boss overlay and before all other layers.
     */
    public static final ResourceLocation SLEEP_OVERLAY = ResourceLocation.withDefaultNamespace("sleep_overlay");

    /**
     * Identifier for the Demo overlay layer, which renders the demo screen overlay when the player is playing in demo mode. This layer is rendered after the sleep overlay and before all other layers.
     */
    public static final ResourceLocation DEMO_OVERLAY = ResourceLocation.withDefaultNamespace("demo_overlay");

    /**
     * Identifier for the debug overlay layer, which renders the debug information (e.g. coordinates, FPS, chunk info) when the player has the debug screen open. This layer is rendered after the demo overlay and before all other layers.
     */
    public static final ResourceLocation DEBUG_OVERLAY = ResourceLocation.withDefaultNamespace("debug_overlay");

    /**
     * Identifier for the scoreboard sidebar layer, which renders the sidebar scoreboard on the right side of the screen when it is active. This layer is rendered after the debug overlay and before all other layers.
     */
    public static final ResourceLocation SCOREBOARD_SIDEBAR = ResourceLocation.withDefaultNamespace("scoreboard_sidebar");

    /**
     * Identifier for the scoreboard list layer, which renders the list scoreboard at the top of the screen when it is active. This layer is rendered after the scoreboard sidebar and before all other layers.
     */
    public static final ResourceLocation OVERLAY_MESSAGE = ResourceLocation.withDefaultNamespace("overlay_message");

    /**
     * Identifier for the title layer, which renders the title and subtitle text in the center of the screen when they are active. This layer is rendered after the overlay message and before all other layers.
     */
    public static final ResourceLocation TITLE = ResourceLocation.withDefaultNamespace("title");

    /**
     * Identifier for the chat layer, which renders the chat messages and chat input box when the player has the chat open. This layer is rendered after the title and before all other layers.
     */
    public static final ResourceLocation CHAT = ResourceLocation.withDefaultNamespace("chat");

    /**
     * Identifier for the tab list layer, which renders the player list overlay when the player presses the tab key. This layer is rendered after the title and before all other layers.
     */
    public static final ResourceLocation TAB_LIST = ResourceLocation.withDefaultNamespace("tab_list");

    /**
     * Identifier for the subtitle overlay layer, which renders the subtitle text below the title in the center of the screen when it is active. This layer is rendered after the title and before all other layers.
     */
    public static final ResourceLocation SUBTITLE_OVERLAY = ResourceLocation.withDefaultNamespace("subtitle_overlay");

    /**
     * Identifier for the saving indicator layer, which renders the "Saving..." text in the bottom left corner of the screen when the game is saving. This layer is rendered last, after all other layers.
     */
    public static final ResourceLocation SAVING_INDICATOR = ResourceLocation.withDefaultNamespace("saving_indicator");
}
