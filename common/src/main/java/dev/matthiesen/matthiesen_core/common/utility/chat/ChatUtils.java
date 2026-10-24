package dev.matthiesen.matthiesen_core.common.utility.chat;

import net.minecraft.ChatFormatting;

/**
 * Utility class for handling chat formatting and colors in Minecraft. This class provides methods to retrieve the appropriate
 * ChatFormatting based on color or formatting codes, allowing for consistent text styling across the server. It supports both
 * named colors and single-character codes, as well as various text formatting options such as bold, italic, underline, and more.
 */
@SuppressWarnings("unused")
public final class ChatUtils {
    private ChatUtils() {}

    /**
     * Returns the ChatFormatting corresponding to the given color code. This method handles both color names and single
     * character codes. If the code does not match any known color, an IllegalArgumentException is thrown.
     * @param code The string code representing the desired chat color. This can be a color name (e.g., "red") or a single
     *             character code (e.g., "c").
     * @return The ChatFormatting instance corresponding to the provided color code.
     * @throws IllegalArgumentException if the provided code does not match any known chat color code.
     */
    public static ChatFormatting getChatFormattingColor(String code) throws IllegalArgumentException {
        code = code.toLowerCase();
        if (code.length() == 1) {
            try {
                return ChatColors.fromCode(code).getFormat();
            } catch (Exception e) {
                throw new IllegalArgumentException("Unknown color value: " + code);
            }
        }
        try {
            return ChatColors.fromName(code).getFormat();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown color value: " + code);
        }
    }

    /**
     * Returns the ChatFormatting corresponding to the given code. This method handles both color and formatting codes, including obfuscated,
     * bold, strikethrough, underline, italic, and reset. If the code does not match any known formatting or color code, it will attempt to
     * retrieve the corresponding color using getChatFormattingColor.
     * @param code The string code representing the desired chat formatting or color. This can be a color name, a single character code, or a formatting name.
     * @return The ChatFormatting instance corresponding to the provided code. If the code is not recognized, an IllegalArgumentException is thrown.
     * @throws IllegalArgumentException if the provided code does not match any known chat formatting or color code.
     */
    public static ChatFormatting getChatFormatting(String code) throws IllegalArgumentException {
        code = code.toLowerCase();
        return switch (code) {
            case "obfuscated", "k" -> ChatFormatting.OBFUSCATED;
            case "bold", "l" -> ChatFormatting.BOLD;
            case "strikethrough", "m" -> ChatFormatting.STRIKETHROUGH;
            case "underline", "n" -> ChatFormatting.UNDERLINE;
            case "italic", "o" -> ChatFormatting.ITALIC;
            case "reset", "r" -> ChatFormatting.RESET;
            default -> getChatFormattingColor(code);
        };
    }
}
