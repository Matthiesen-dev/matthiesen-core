package dev.matthiesen.matthiesen_core.common.core.discord;

/**
 * Enum representing the standard colors used in Discord embeds, with their corresponding integer values.
 * These values can be used when setting the color of an embed in a Discord message.
 */
@SuppressWarnings("unused")
public enum DiscordColor {
    /**
     * The color red is represented by the integer value 15158332, which corresponds to the hexadecimal RGB color code #FF0000.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a red color.
     */
    RED(15158332),
    /**
     * The color green is represented by the integer value 3066993, which corresponds to the hexadecimal RGB color code #2ECC71.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a green color.
     */
    GREEN(3066993),
    /**
     * The color blue is represented by the integer value 3447003, which corresponds to the hexadecimal RGB color code #3498DB.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a blue color.
     */
    BLUE(3447003),
    /**
     * The color purple is represented by the integer value 10181046, which corresponds to the hexadecimal RGB color code #800080.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a purple color.
     */
    PURPLE(10181046),
    /**
     * The color orange is represented by the integer value 15105570, which corresponds to the hexadecimal RGB color code #FFA500.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with an orange color.
     */
    ORANGE(15105570),
    /**
     * The color gold is represented by the integer value 15844367, which corresponds to the hexadecimal RGB color code #FFD700.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a gold color.
     */
    GOLD(15844367),
    /**
     * The color gray is represented by the integer value 9807270, which corresponds to the hexadecimal RGB color code #808080.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a gray color.
     */
    GREY(9807270),
    /**
     * The color dark red is represented by the integer value 10038562, which corresponds to the hexadecimal RGB color code #8B0000.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a dark red color.
     */
    DARK_RED(10038562),
    /**
     * The color dark green is represented by the integer value 5763719, which corresponds to the hexadecimal RGB color code #006400.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a dark green color.
     */
    DARK_GREEN(5763719),
    /**
     * The color dark blue is represented by the integer value 5793266, which corresponds to the hexadecimal RGB color code #00008B.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a dark blue color.
     */
    DARK_BLUE(5793266),
    /**
     * The color black is represented by the integer value 2303786, which corresponds to the hexadecimal RGB color code #000000.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a black color.
     */
    BLACK(2303786),
    /**
     * The color white is represented by the integer value 16777215, which corresponds to the hexadecimal RGB color code #FFFFFF.
     * This value can be used when setting the color of an embed in a Discord message to ensure that the embed displays with a white color.
     */
    WHITE(16777215);

    private final int value;

    /**
     * Constructs a new DiscordColor with the specified integer value.
     * @param value The integer value representing this color in the Discord API, which is typically a decimal representation
     *              of a hexadecimal RGB color code (e.g., 15158332 for red corresponds to #FF0000 in hex). This value can be
     *              used when setting the color of an embed in a Discord message to ensure that the embed displays with the
     *              intended color.
     */
    DiscordColor(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value associated with this DiscordColor, which can be used when setting the color of an embed in a
     * Discord message.
     * @return The integer value representing this color in the Discord API, which is typically a decimal representation of
     * a hexadecimal RGB color code (e.g., 15158332 for red corresponds to #FF0000 in hex).
     */
    public int getValue() {
        return value;
    }
}

