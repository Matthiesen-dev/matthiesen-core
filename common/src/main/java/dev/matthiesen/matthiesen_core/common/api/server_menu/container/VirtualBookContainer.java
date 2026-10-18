package dev.matthiesen.matthiesen_core.common.api.server_menu.container;

import dev.matthiesen.matthiesen_core.common.api.server_menu.virtual.VirtualLecternContainerData;
import dev.matthiesen.matthiesen_core.common.api.server_menu.virtual.VirtualLecternMenu;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.server_menu.MenuCallbackEngine;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a virtual book container that can be opened by players in a server menu system.
 * This class provides functionality for creating and managing virtual books with sections, titles, authors, and interactive buttons.
 */
public class VirtualBookContainer {
    /**
     * The default ItemStack representing a written book in Minecraft.
     */
    public static final ItemStack BOOK = new ItemStack(Items.WRITTEN_BOOK);
    public static final String DEFAULT_TITLE = "Virtual Book";
    public static final String DEFAULT_AUTHOR = "Server";
    public static final String PAGE_RULE = "----------------";
    private static final int MAX_BOOK_PAGES = 100;
    private static final int MAX_BOOK_CHARACTERS = 1023;
    private static final int MAX_BOOK_NEWLINES = 14;
    private static final int MAX_PAGE_NUMBER_DIGITS = 3;

    private String title = DEFAULT_TITLE;
    private String author = DEFAULT_AUTHOR;
    private int generation = 0;
    private boolean isBookRemovable = false;

    private final List<MutableComponent> rawSections = new ArrayList<>();
    private final List<String> generatedCallbacks = new ArrayList<>();

    /**
     * Default constructor for VirtualBookContainer.
     * Initializes an empty virtual book with default title and author.
     */
    public VirtualBookContainer() {}

    /**
     * Constructs a new VirtualBookContainer with the specified title and author.
     *
     * @param title  The title of the virtual book.
     * @param author The author of the virtual book.
     */
    public VirtualBookContainer(String title, String author) {
        this.title = title;
        this.author = author;
    }

    /**
     * Sets the title of the virtual book.
     *
     * @param title The title of the book.
     * @return The current instance of VirtualBookContainer for method chaining.
     */
    public VirtualBookContainer setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the author of the virtual book.
     *
     * @param author The name of the author.
     * @return The current instance of VirtualBookContainer for method chaining.
     */
    public VirtualBookContainer setAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * Adds a section to the virtual book.
     *
     * @param sectionContent The content of the section to add.
     * @return The current instance of VirtualBookContainer for method chaining.
     */
    public VirtualBookContainer addSection(MutableComponent sectionContent) {
        this.rawSections.add(sectionContent);
        return this;
    }

    /**
     * Sets the generation of the book. This can be used to indicate how many times the book has been copied.
     *
     * @param generation The generation of the book (0 for original, 1 for copy, etc.). (0-3 are valid values)
     * @return The current instance of VirtualBookContainer for method chaining.
     */
    public VirtualBookContainer setGeneration(int generation) {
        this.generation = generation;
        return this;
    }

    /**
     * Sets whether the book can be removed from the lectern by players.
     *
     * @param isBookRemovable A boolean indicating whether the book can be removed.
     * @return The current instance of VirtualBookContainer for method chaining.
     */
    public VirtualBookContainer setBookRemovable(boolean isBookRemovable) {
        this.isBookRemovable = isBookRemovable;
        return this;
    }

    /**
     * Compiles the raw sections into final book pages, automatically
     * injecting layout elements like headers and navigation footers.
     */
    private List<Component> compilePages() {
        List<Component> compiled = new ArrayList<>();
        List<Component> pageBodies = new ArrayList<>();

        int bodyCharLimit = Math.max(0, MAX_BOOK_CHARACTERS - buildHeaderText().length() - buildMaximumFooterText().length());
        int bodyNewlineLimit = Math.max(0, MAX_BOOK_NEWLINES - countNewlines(buildHeaderText()) - countNewlines(buildMaximumFooterText()));

        for (MutableComponent section : rawSections) {
            String sectionText = normalizeSectionText(section.getString());
            List<String> sectionChunks = splitSectionIntoChunks(sectionText, bodyCharLimit, bodyNewlineLimit);

            if (sectionChunks.size() == 1 && sectionChunks.getFirst().equals(sectionText)) {
                pageBodies.add(section.copy());
                continue;
            }

            for (String chunk : sectionChunks) {
                pageBodies.add(Component.literal(chunk).withStyle(section.getStyle()));
            }
        }

        if (pageBodies.size() > MAX_BOOK_PAGES) {
            int truncatedPages = pageBodies.size() - MAX_BOOK_PAGES;
            MatthiesenCoreCommon.INSTANCE.createWarnLog("Virtual book '" + this.title + "' exceeded the " + MAX_BOOK_PAGES + " page limit. Truncating " + truncatedPages + " page(s) of content.");
            pageBodies = new ArrayList<>(pageBodies.subList(0, MAX_BOOK_PAGES));
        }

        int totalPages = pageBodies.size();

        for (int i = 0; i < totalPages; i++) {
            int pageNum = i + 1;
            MutableComponent header = Component.literal(this.title + "\n")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withBold(true))
                    .append(Component.literal(PAGE_RULE + "\n").withStyle(ChatFormatting.GRAY));

            MutableComponent footer = Component.literal("\n").append(Component.literal(PAGE_RULE + "\n").withStyle(ChatFormatting.GRAY));
            if (pageNum > 1) {
                footer = footer.append(createPageButton("◀ Back", pageNum - 1));
            } else {
                footer = footer.append(Component.literal("    ")); // Spacer
            }
            footer = footer.append(Component.literal("  " + pageNum + "/" + totalPages + "  ").withStyle(ChatFormatting.DARK_GRAY));
            if (pageNum < totalPages) {
                footer = footer.append(createPageButton("Next ▶", pageNum + 1));
            }

            MutableComponent fullPage = Component.empty()
                    .append(header)
                    .append(pageBodies.get(i))
                    .append(footer);
            compiled.add(fullPage);
        }

        return compiled;
    }

    private String buildHeaderText() {
        return this.title + "\n" + PAGE_RULE + "\n";
    }

    private String buildMaximumFooterText() {
        String maxPageNumber = "9".repeat(MAX_PAGE_NUMBER_DIGITS);
        return "\n" + PAGE_RULE + "\n" + "◀ Back" + "  " + maxPageNumber + "/" + maxPageNumber + "  " + "Next ▶";
    }

    private static int countNewlines(String value) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '\n') {
                count++;
            }
        }
        return count;
    }

    private static String normalizeSectionText(String value) {
        return value.replace("\r\n", "\n").replace('\r', '\n');
    }

    private List<String> splitSectionIntoChunks(String text, int charLimit, int newlineLimit) {
        List<String> chunks = new ArrayList<>();
        if (text.isEmpty()) {
            chunks.add("");
            return chunks;
        }

        String[] lines = text.split("\n", -1);
        StringBuilder current = new StringBuilder();
        int currentNewlines = 0;
        boolean warnedAboutOversizedWord = false;

        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            String line = lines[lineIndex];
            if (!line.isBlank()) {
                String[] words = line.trim().split("\\s+");
                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }

                    int requiredLength = word.length();
                    if (!current.isEmpty() && current.charAt(current.length() - 1) != '\n') {
                        requiredLength++;
                    }

                    if (!current.isEmpty() && current.length() + requiredLength > charLimit) {
                        chunks.add(current.toString());
                        current = new StringBuilder();
                        currentNewlines = 0;
                    }

                    if (word.length() > charLimit && charLimit > 0) {
                        if (!warnedAboutOversizedWord) {
                            MatthiesenCoreCommon.INSTANCE.createWarnLog("A single word in virtual book '" + this.title + "' exceeds the page body limit. Splitting the word as a fallback.");
                            warnedAboutOversizedWord = true;
                        }

                        int offset = 0;
                        while (offset < word.length()) {
                            int nextOffset = Math.min(word.length(), offset + charLimit);
                            String piece = word.substring(offset, nextOffset);

                            if (!current.isEmpty()) {
                                chunks.add(current.toString());
                                current = new StringBuilder();
                                currentNewlines = 0;
                            }

                            current.append(piece);
                            offset = nextOffset;

                            if (offset < word.length()) {
                                chunks.add(current.toString());
                                current = new StringBuilder();
                                currentNewlines = 0;
                            }
                        }
                        continue;
                    }

                    if (!current.isEmpty() && current.charAt(current.length() - 1) != '\n') {
                        current.append(' ');
                    }
                    current.append(word);
                }
            }

            if (lineIndex < lines.length - 1) {
                if (currentNewlines + 1 > newlineLimit) {
                    if (!current.isEmpty()) {
                        chunks.add(current.toString());
                        current = new StringBuilder();
                        currentNewlines = 0;
                    }
                } else {
                    if (!current.isEmpty() && current.length() + 1 > charLimit) {
                        chunks.add(current.toString());
                        current = new StringBuilder();
                        currentNewlines = 0;
                    }

                    current.append('\n');
                    currentNewlines++;
                }
            }
        }

        if (!current.isEmpty() || chunks.isEmpty()) {
            chunks.add(current.toString());
        }

        return chunks;
    }

    /**
     * Creates a clickable button component that runs any backend server-side Java logic when pressed.
     *
     * @param label The text displayed on the button.
     * @param action A lambda expression executing code on the server thread.
     * @return A Component representing the clickable button.
     */
    public Component createActionButton(String label, Consumer<ServerPlayer> action) {
        return createActionButton(label, action, ChatFormatting.DARK_GREEN);
    }

    /**
     * Creates a clickable button component that runs any backend server-side Java logic when pressed.
     *
     * @param label The text displayed on the button.
     * @param action A lambda expression executing code on the server thread.
     * @param color The color of the button text.
     * @return A Component representing the clickable button.
     */
    public Component createActionButton(String label, Consumer<ServerPlayer> action, ChatFormatting color) {
        String executionCommand = MenuCallbackEngine.registerCallback(action);
        this.generatedCallbacks.add(executionCommand);
        return Component.literal(label).withStyle(style -> style
                .withColor(color)
                .withBold(true)
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, executionCommand))
        );
    }

    /**
     * Creates a clickable button component that navigates to a specific page in the virtual book.
     *
     * @param label The text displayed on the button.
     * @param targetPageNum The page number to navigate to when clicked.
     * @return A Component representing the clickable button.
     */
    public Component createPageButton(String label, int targetPageNum) {
        return Component.literal(label).withStyle(style -> style
                .withColor(ChatFormatting.BLUE)
                .withBold(true)
                .withClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, String.valueOf(targetPageNum)))
        );
    }

    /**
     * Returns the WrittenBookContent representing the virtual book, including title, author, and pages.
     *
     * @return A WrittenBookContent object for the virtual book.
     */
    public WrittenBookContent getBookContent() {
        List<Component> finalPages = compilePages();
        return new WrittenBookContent(
                Filterable.passThrough(title),
                author,
                generation,
                finalPages.stream().map(Filterable::passThrough).toList(),
                false
        );
    }

    /**
     * Returns an ItemStack representing the virtual book, ready to be given to a player.
     *
     * @return An ItemStack of the virtual book.
     */
    public ItemStack getBook() {
        ItemStack book = BOOK.copy();
        book.set(DataComponents.WRITTEN_BOOK_CONTENT, getBookContent());
        return book;
    }

    /**
     * Opens the virtual book for the specified player, allowing them to view and interact with it.
     *
     * @param player The ServerPlayer who will view the book.
     */
    public void openBookForPlayer(ServerPlayer player) {
        ItemStack book = getBook();
        SimpleContainer container = new SimpleContainer(book);
        ContainerData lecternData = new VirtualLecternContainerData();

        player.openMenu(new SimpleMenuProvider(
                (id, inventory, p) -> new VirtualLecternMenu(id, container, lecternData, this.generatedCallbacks, this.isBookRemovable),
                Component.literal(title)
        ));
    }
}
