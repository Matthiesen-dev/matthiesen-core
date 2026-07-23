package dev.matthiesen.matthiesen_core.common.utility;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building chat tables. This is used to create tables in chat with a fluent API. It provides methods
 * for adding rows and columns, and it will automatically handle the formatting of the table. After setting the desired
 * properties on the ChatTableBuilder, the build() method should be called to get the final Component representing the
 * table. After calling build(), the ChatTableBuilder should not be used anymore, as it may cause unexpected behavior.
 */
@SuppressWarnings("unused")
public final class ChatTableBuilder {
    private final String title;
    private final List<Entry> entries = new ArrayList<>();
    private final Formatting formatting;

    /**
     * Creates a new ChatTableBuilder with the specified title and formatting. The title is the text that will be
     * displayed at the top of the table, and the formatting is used to specify the colors of the different parts of the table.
     * @param title The title of the table, which will be displayed at the top of the table. This is used to provide a
     *              heading for the table, and it will be formatted with the title color specified in the formatting.
     */
    public ChatTableBuilder(String title) {
        this(title, Formatting.DEFAULT);
    }

    /**
     * Creates a new ChatTableBuilder with the specified title and formatting. The title is the text that will be displayed
     * at the top of the table, and the formatting is used to specify the colors of the different parts of the table.
     * @param title The title of the table, which will be displayed at the top of the table. This is used to provide a
     *              heading for the table, and it will be formatted with the title color specified in the formatting.
     * @param formatting The formatting to use for the table, which specifies the colors of the different parts of the
     *                   table. This is used to customize the appearance of the table, and it will be used to format the
     *                   title, keys, values, separators, and default text in the table.
     */
    public ChatTableBuilder(String title, Formatting formatting) {
        this.title = title == null ? "Unknown" : title;
        this.formatting = formatting == null ? Formatting.DEFAULT : formatting;
    }

    /**
     * Adds an entry to the chat table. The entry will be displayed as "key: value" in the chat, with the key and value
     * formatted according to the formatting settings of the ChatTableBuilder.
     * @param key The key of the entry. This is the text that will be displayed before the value in the chat.
     * @param value The value of the entry. This is the text that will be displayed after the key in the chat.
     * @return The ChatTableBuilder instance, for chaining
     */
    public ChatTableBuilder addRow(String key, String value) {
        entries.add(new Row(safeText(key), safeText(value)));
        return this;
    }

    /**
     * Adds a blank row to the table. This can be used to create spacing between rows, or to separate different sections of the table.
     * @param sectionName The name of the section to create. This will be displayed in the first column of the table, and
     *                    it will be formatted with the section formatting.
     * @return The ChatTableBuilder instance, for chaining
     */
    public ChatTableBuilder addSection(String sectionName) {
        entries.add(new Section(safeText(sectionName)));
        return this;
    }

    /**
     * Builds the chat table as a Component. The title will be formatted with the title color, and each entry will be
     * formatted according to its type (row or section).
     * @return The built chat table as a Component.
     */
    public Component build() {
        int maxKeyWidth = 0;
        for (Entry entry : entries) {
            if (entry instanceof Row row) {
                maxKeyWidth = Math.max(maxKeyWidth, row.key().length());
            }
        }

        MutableComponent message = Component.empty();
        message.append(Component.literal("[ " + title + " ]").withStyle(formatting.title()));

        boolean firstSection = true;
        for (Entry entry : entries) {
            if (entry instanceof Section(String name)) {
                message.append(Component.literal(firstSection ? "\n" : "\n\n"));
                message.append(Component.literal("[" + name + "]").withStyle(formatting.sectionLabel()));
                firstSection = false;
                continue;
            }

            if (entry instanceof Row(String key, String value)) {
                message.append(Component.literal("\n- ").withStyle(formatting.rowMark()));
                message.append(Component.literal(padRight(key, maxKeyWidth)).withStyle(formatting.rowKey()));
                message.append(Component.literal(" : ").withStyle(formatting.rowSeparator()));
                message.append(Component.literal(value).withStyle(formatting.rowValue()));
            }
        }

        return message;
    }

    private static String safeText(String value) {
        if (value == null || value.isBlank()) {
            return "<unset>";
        }
        return value;
    }

    private static String padRight(String value, int width) {
        if (value.length() >= width) {
            return value;
        }

        return value + " ".repeat(width - value.length());
    }

    private sealed interface Entry permits Row, Section {
    }

    private record Row(String key, String value) implements Entry {
    }

    private record Section(String name) implements Entry {
    }

    /**
     * A class representing the formatting options for the chat table, including colors for the title, section labels,
     * row keys, row separators, and row values.
     * @param title The color for the title of the chat table.
     * @param sectionLabel The color for the section labels in the chat table.
     * @param rowMark The color for the mark before each row in the chat table.
     * @param rowKey The color for the keys in each row of the chat table.
     * @param rowSeparator The color for the separator between keys and values in each row of the chat table.
     * @param rowValue The color for the values in each row of the chat table.
     */
    public record Formatting(
            ChatFormatting title,
            ChatFormatting sectionLabel,
            ChatFormatting rowMark,
            ChatFormatting rowKey,
            ChatFormatting rowSeparator,
            ChatFormatting rowValue
    ) {
        /**
         * The default formatting for the chat table. The title is gold, section labels are aqua, row marks are dark gray,
         * row keys are yellow, row separators are gray, and row values are white.
         */
        public static Formatting DEFAULT = new Formatting(
                ChatFormatting.GOLD,
                ChatFormatting.AQUA,
                ChatFormatting.DARK_GRAY,
                ChatFormatting.YELLOW,
                ChatFormatting.GRAY,
                ChatFormatting.WHITE
        );
    }
}
