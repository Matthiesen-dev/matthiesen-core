package dev.matthiesen.matthiesen_core.common.utility.chat;

import net.minecraft.ChatFormatting;

/**
 * Enum representing various chat colors and their corresponding Minecraft ChatFormatting.
 * This is intended as a simplified enum for just the ChatFormatting colors for use in various chat-related utilities and configs across different mods.
 */
@SuppressWarnings("unused")
public enum ChatColors {
    /** Represents the color black in MC's chat formatting. */
    BLACK("BLACK", "0", ChatFormatting.BLACK),
    /** Represents the color dark blue in MC's chat formatting. */
    DARK_BLUE("DARK_BLUE", "1", ChatFormatting.DARK_BLUE),
    /** Represents the color dark green in MC's chat formatting. */
    DARK_GREEN("DARK_GREEN", "2", ChatFormatting.DARK_GREEN),
    /** Represents the color dark aqua in MC's chat formatting. */
    DARK_AQUA("DARK_AQUA", "3", ChatFormatting.DARK_AQUA),
    /** Represents the color dark red in MC's chat formatting. */
    DARK_RED("DARK_RED", "4", ChatFormatting.DARK_RED),
    /** Represents the color dark purple in MC's chat formatting. */
    DARK_PURPLE("DARK_PURPLE", "5", ChatFormatting.DARK_PURPLE),
    /** Represents the color gold in MC's chat formatting. */
    GOLD("GOLD", "6", ChatFormatting.GOLD),
    /** Represents the color gray in MC's chat formatting. */
    GRAY("GRAY", "7", ChatFormatting.GRAY),
    /** Represents the color dark gray in MC's chat formatting. */
    DARK_GRAY("DARK_GRAY", "8", ChatFormatting.DARK_GRAY),
    /** Represents the color blue in MC's chat formatting. */
    BLUE("BLUE", "9", ChatFormatting.BLUE),
    /** Represents the color green in MC's chat formatting. */
    GREEN("GREEN", "a", ChatFormatting.GREEN),
    /** Represents the color aqua in MC's chat formatting. */
    AQUA("AQUA", "b", ChatFormatting.AQUA),
    /** Represents the color red in MC's chat formatting. */
    RED("RED", "c", ChatFormatting.RED),
    /** Represents the color light purple in MC's chat formatting. */
    LIGHT_PURPLE("LIGHT_PURPLE", "d", ChatFormatting.LIGHT_PURPLE),
    /** Represents the color yellow in MC's chat formatting. */
    YELLOW("YELLOW", "e", ChatFormatting.YELLOW),
    /** Represents the color white in MC's chat formatting. */
    WHITE("WHITE", "f", ChatFormatting.WHITE);

    private final String name;
    private final String code;
    private final ChatFormatting format;

    /**
     * Constructs a ChatColors enum constant with the specified ChatFormatting.
     * @param name The name of this color.
     * @param format The ChatFormatting associated with this color.
     */
    ChatColors(String name, String code, ChatFormatting format) {
        this.name = name;
        this.code = code;
        this.format = format;
    }

    /**
     * Returns the name of this ChatColors enum constant.
     * @return The name of this color.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the code of this ChatColors enum constant.
     * @return The code of this color.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the ChatFormatting associated with this ChatColors enum constant.
     * @return The ChatFormatting instance corresponding to this color.
     */
    public ChatFormatting getFormat() {
        return format;
    }

    /**
     * Returns the ChatColors enum constant corresponding to the given ChatFormatting.
     * @param format The ChatFormatting to look up.
     * @return The ChatColors enum constant with the specified ChatFormatting.
     * @throws IllegalArgumentException if no ChatColors constant with the given ChatFormatting exists.
     */
    public static ChatColors fromChatFormatting(ChatFormatting format) throws IllegalArgumentException {
        for (ChatColors color : ChatColors.values()) {
            if (color.getFormat() == format) {
                return color;
            }
        }
        throw new IllegalArgumentException("No ChatColors constant with ChatFormatting: " + format);
    }

    /**
     * Returns the ChatColors enum constant corresponding to the given name.
     * @param name The name of the color to retrieve.
     * @return The ChatColors enum constant with the specified name.
     * @throws IllegalArgumentException if no ChatColors constant with the given name exists.
     */
    public static ChatColors fromName(String name) throws IllegalArgumentException {
        for (ChatColors color : ChatColors.values()) {
            if (color.getName().equalsIgnoreCase(name)) {
                return color;
            }
        }
        throw new IllegalArgumentException("No ChatColors constant with name: " + name);
    }

    /**
     * Returns the ChatColors enum constant corresponding to the given code.
     * @param code The code of the color to retrieve.
     * @return The ChatColors enum constant with the specified code.
     * @throws IllegalArgumentException if no ChatColors constant with the given code exists.
     */
    public static ChatColors fromCode(String code) throws IllegalArgumentException {
        for (ChatColors color : ChatColors.values()) {
            if (color.getCode().equalsIgnoreCase(code)) {
                return color;
            }
        }
        throw new IllegalArgumentException("No ChatColors constant with code: " + code);
    }
}
