package dev.matthiesen.matthiesen_core.common.core.discord.model;

import dev.matthiesen.matthiesen_core.common.api.discord.DiscordColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Discord embed object used in webhook messages.
 */
@SuppressWarnings("unused")
public final class Embed {
    private String title;
    private String description;
    private String url;
    private Integer color;
    private Author author;
    private List<Field> fields;
    private Footer footer;
    private Image image;
    private Thumbnail thumbnail;
    private String timestamp;

    /**
     * Creates an empty embed object.
     */
    public Embed() {}

    /**
     * Gets the embed title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the embed title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the embed description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the embed description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the embed URL.
     *
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the embed URL.
     *
     * @param url the URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the embed color as an integer.
     *
     * @return the color value
     */
    public Integer getColor() {
        return color;
    }

    /**
     * Sets the embed color as an integer.
     *
     * @param color the color value
     */
    public void setColor(Integer color) {
        this.color = color;
    }

    /**
     * Gets the embed author section.
     *
     * @return the author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Sets the embed author section.
     *
     * @param author the author
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * Gets the embed field list.
     *
     * @return the field list
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Sets the embed field list.
     *
     * @param fields the field list
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /**
     * Gets the embed footer section.
     *
     * @return the footer
     */
    public Footer getFooter() {
        return footer;
    }

    /**
     * Sets the embed footer section.
     *
     * @param footer the footer
     */
    public void setFooter(Footer footer) {
        this.footer = footer;
    }

    /**
     * Gets the embed image section.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the embed image section.
     *
     * @param image the image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Gets the embed thumbnail section.
     *
     * @return the thumbnail
     */
    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the embed thumbnail section.
     *
     * @param thumbnail the thumbnail
     */
    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Gets the embed timestamp string.
     *
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the embed timestamp string.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Embed author section.
     */
    public static class Author {
        private String name;
        private String url;
        private String icon_url;

        /**
         * Creates an empty author section.
         */
        public Author() {}

        /**
         * Gets the author name.
         *
         * @return the author name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the author name.
         *
         * @param name the author name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the author URL.
         *
         * @return the author URL
         */
        public String getUrl() {
            return url;
        }

        /**
         * Sets the author URL.
         *
         * @param url the author URL
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * Gets the author icon URL.
         *
         * @return the icon URL
         */
        public String getIconUrl() {
            return icon_url;
        }

        /**
         * Sets the author icon URL.
         *
         * @param iconUrl the icon URL
         */
        public void setIconUrl(String iconUrl) {
            this.icon_url = iconUrl;
        }
    }

    /**
     * Embed footer section.
     */
    public static class Footer {
        private String text;
        private String icon_url;

        /**
         * Creates an empty footer section.
         */
        public Footer() {}

        /**
         * Gets the footer text.
         *
         * @return the footer text
         */
        public String getText() {
            return text;
        }

        /**
         * Sets the footer text.
         *
         * @param text the footer text
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * Gets the footer icon URL.
         *
         * @return the icon URL
         */
        public String getIconUrl() {
            return icon_url;
        }

        /**
         * Sets the footer icon URL.
         *
         * @param iconUrl the icon URL
         */
        public void setIconUrl(String iconUrl) {
            this.icon_url = iconUrl;
        }
    }

    /**
     * Embed field entry.
     */
    public static class Field {
        private String name;
        private String value;
        private boolean inline;

        /**
         * Creates an empty embed field.
         */
        public Field() {}

        /**
         * Gets the field name.
         *
         * @return the field name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the field name.
         *
         * @param name the field name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the field value.
         *
         * @return the field value
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the field value.
         *
         * @param value the field value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets whether the field is displayed inline.
         *
         * @return {@code true} if inline
         */
        public boolean isInline() {
            return inline;
        }

        /**
         * Sets whether the field is displayed inline.
         *
         * @param inline inline flag
         */
        public void setInline(boolean inline) {
            this.inline = inline;
        }
    }

    /**
     * Embed image section.
     */
    public static class Image {
        private String url;

        /**
         * Creates an empty image section.
         */
        public Image() {}

        /**
         * Gets the image URL.
         *
         * @return the image URL
         */
        public String getUrl() {
            return url;
        }

        /**
         * Sets the image URL.
         *
         * @param url the image URL
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * Embed thumbnail section.
     */
    public static class Thumbnail {
        private String url;

        /**
         * Creates an empty thumbnail section.
         */
        public Thumbnail() {}

        /**
         * Gets the thumbnail URL.
         *
         * @return the thumbnail URL
         */
        public String getUrl() {
            return url;
        }

        /**
         * Sets the thumbnail URL.
         *
         * @param url the thumbnail URL
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * Builder class for constructing {@link Embed} instances.
     */
    public static class Builder {
        private String title;
        private String description;
        private String url;
        private Integer color;
        private Embed.Author author;
        private List<Field> fields;
        private Embed.Footer footer;
        private Embed.Image image;
        private Embed.Thumbnail thumbnail;
        private String timestamp;

        /**
         * Creates an empty embed builder.
         */
        public Builder() {}

        /**
         * Sets the embed title.
         *
         * @param title the title text
         * @return this builder
         */
        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the embed description.
         *
         * @param description the description text
         * @return this builder
         */
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the embed URL.
         *
         * @param url the URL
         * @return this builder
         */
        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Sets the embed color value.
         *
         * @param color the decimal color value expected by Discord
         * @return this builder
         */
        public Builder withColor(Integer color) {
            this.color = color;
            return this;
        }

        /**
         * Sets the embed color from a predefined {@link DiscordColor}.
         *
         * @param color the predefined color
         * @return this builder
         */
        public Builder withColor(DiscordColor color) {
            this.color = color.getValue();
            return this;
        }

        /**
         * Sets the embed author section.
         *
         * @param author the author object
         * @return this builder
         */
        public Builder withAuthor(Embed.Author author) {
            this.author = author;
            return this;
        }

        /**
         * Replaces the full embed field list.
         *
         * @param fields the field list
         * @return this builder
         */
        public Builder withFields(List<Field> fields) {
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
        public Builder addField(String name, String value, boolean inline) {
            if (this.fields == null) {
                this.fields = new ArrayList<>();
            }
            Field field = new Field();
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
        public Builder withAuthor(String name, String url, String iconUrl) {
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
        public Builder withFooter(Embed.Footer footer) {
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
        public Builder withFooter(String text, String iconUrl) {
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
        public Builder withImage(Embed.Image image) {
            this.image = image;
            return this;
        }

        /**
         * Sets the embed image URL.
         *
         * @param url the image URL
         * @return this builder
         */
        public Builder withImageUrl(String url) {
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
        public Builder withThumbnail(Embed.Thumbnail thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        /**
         * Sets the embed thumbnail URL.
         *
         * @param url the thumbnail URL
         * @return this builder
         */
        public Builder withThumbnailUrl(String url) {
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
        public Builder withTimestamp(String timestamp) {
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
}

