package dev.matthiesen.matthiesen_core.common.api.server_menu.container;

import dev.matthiesen.matthiesen_core.common.api.server_menu.virtual.VirtualLecternContainerData;
import dev.matthiesen.matthiesen_core.common.api.server_menu.virtual.VirtualLecternMenu;
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

    private String title = DEFAULT_TITLE;
    private String author = DEFAULT_AUTHOR;
    private int generation = 0;

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
     * Compiles the raw sections into final book pages, automatically
     * injecting layout elements like headers and navigation footers.
     */
    private List<Component> compilePages() {
        List<Component> compiled = new ArrayList<>();
        int totalPages = rawSections.size();

        for (int i = 0; i < totalPages; i++) {
            int pageNum = i + 1;
            MutableComponent body = rawSections.get(i);

            MutableComponent header = Component.literal(this.title + "\n")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withBold(true))
                    .append(Component.literal(PAGE_RULE + "\n\n").withStyle(ChatFormatting.GRAY));

            MutableComponent footer = Component.literal("\n\n").append(Component.literal(PAGE_RULE + "\n").withStyle(ChatFormatting.GRAY));
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
                    .append(body)
                    .append(footer);
            compiled.add(fullPage);
        }

        return compiled;
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
                (id, inventory, p) -> new VirtualLecternMenu(id, container, lecternData, this.generatedCallbacks),
                Component.literal(title)
        ));
    }
}
