package dev.matthiesen.matthiesen_core.common.core.discord.model;

import dev.matthiesen.matthiesen_core.common.core.discord.DiscordColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for creating {@link Embed} instances.
 */
@SuppressWarnings("unused")
public final class EmbedBuilder {
    private String title;
    private String description;
    private String url;
    private Integer color;
    private Embed.Author author;
    private List<Embed.EmbedField> fields;
    private Embed.Footer footer;
    private Embed.Image image;
    private Embed.Thumbnail thumbnail;
    private String timestamp;

    /**
     * Creates an empty embed builder.
     */
    public EmbedBuilder() {}

    /**
     * Sets the embed title.
     *
     * @param title the title text
     * @return this builder
     */
    public EmbedBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the embed description.
     *
     * @param description the description text
     * @return this builder
     */
    public EmbedBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the embed URL.
     *
     * @param url the URL
     * @return this builder
     */
    public EmbedBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets the embed color value.
     *
     * @param color the decimal color value expected by Discord
     * @return this builder
     */
    public EmbedBuilder withColor(Integer color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the embed color from a predefined {@link DiscordColor}.
     *
     * @param color the predefined color
     * @return this builder
     */
    public EmbedBuilder withColor(DiscordColor color) {
        this.color = color.getValue();
        return this;
    }

    /**
     * Sets the embed author section.
     *
     * @param author the author object
     * @return this builder
     */
    public EmbedBuilder withAuthor(Embed.Author author) {
        this.author = author;
        return this;
    }

    /**
     * Replaces the full embed field list.
     *
     * @param fields the field list
     * @return this builder
     */
    public EmbedBuilder withFields(List<Embed.EmbedField> fields) {
        this.fields = fields;
        return this;
    }

    /**
     * Appends a single field to the embed.
     *
     * @param name the field name
     * @param value the field value
     * @param inline whether the field should be displayed inline
     * @return this builder
     */
    public EmbedBuilder addField(String name, String value, boolean inline) {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }
        Embed.EmbedField field = new Embed.EmbedField();
        field.setName(name);
        field.setValue(value);
        field.setInline(inline);
        this.fields.add(field);
        return this;
    }

    /**
     * Sets the embed author section from simple values.
     *
     * @param name the author display name
     * @param url the author URL
     * @param iconUrl the author icon URL
     * @return this builder
     */
    public EmbedBuilder withAuthor(String name, String url, String iconUrl) {
        Embed.Author builtAuthor = new Embed.Author();
        builtAuthor.setName(name);
        builtAuthor.setUrl(url);
        builtAuthor.setIconUrl(iconUrl);
        this.author = builtAuthor;
        return this;
    }

    /**
     * Sets the embed footer section.
     *
     * @param footer the footer object
     * @return this builder
     */
    public EmbedBuilder withFooter(Embed.Footer footer) {
        this.footer = footer;
        return this;
    }

    /**
     * Sets the embed footer section from simple values.
     *
     * @param text the footer text
     * @param iconUrl the footer icon URL
     * @return this builder
     */
    public EmbedBuilder withFooter(String text, String iconUrl) {
        Embed.Footer builtFooter = new Embed.Footer();
        builtFooter.setText(text);
        builtFooter.setIconUrl(iconUrl);
        this.footer = builtFooter;
        return this;
    }

    /**
     * Sets the embed image section.
     *
     * @param image the image object
     * @return this builder
     */
    public EmbedBuilder withImage(Embed.Image image) {
        this.image = image;
        return this;
    }

    /**
     * Sets the embed image URL.
     *
     * @param url the image URL
     * @return this builder
     */
    public EmbedBuilder withImageUrl(String url) {
        Embed.Image builtImage = new Embed.Image();
        builtImage.setUrl(url);
        this.image = builtImage;
        return this;
    }

    /**
     * Sets the embed thumbnail section.
     *
     * @param thumbnail the thumbnail object
     * @return this builder
     */
    public EmbedBuilder withThumbnail(Embed.Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    /**
     * Sets the embed thumbnail URL.
     *
     * @param url the thumbnail URL
     * @return this builder
     */
    public EmbedBuilder withThumbnailUrl(String url) {
        Embed.Thumbnail builtThumbnail = new Embed.Thumbnail();
        builtThumbnail.setUrl(url);
        this.thumbnail = builtThumbnail;
        return this;
    }

    /**
     * Sets the embed timestamp string.
     *
     * @param timestamp timestamp value (typically ISO-8601)
     * @return this builder
     */
    public EmbedBuilder withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Builds a new {@link Embed} instance.
     *
     * @return the built embed
     */
    public Embed build() {
        Embed embed = new Embed();
        embed.setTitle(title);
        embed.setDescription(description);
        embed.setUrl(url);
        embed.setColor(color);
        embed.setAuthor(author);
        embed.setFields(fields);
        embed.setFooter(footer);
        embed.setImage(image);
        embed.setThumbnail(thumbnail);
        embed.setTimestamp(timestamp);
        return embed;
    }
}

