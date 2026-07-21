package dev.matthiesen.matthiesen_core.common.core.discord.model;

import java.util.List;

/**
 * Represents a Discord embed object used in webhook messages.
 */
@SuppressWarnings("unused")
public class Embed {
    private String title;
    private String description;
    private String url;
    private Integer color;
    private Author author;
    private List<EmbedField> fields;
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
    public List<EmbedField> getFields() {
        return fields;
    }

    /**
     * Sets the embed field list.
     *
     * @param fields the field list
     */
    public void setFields(List<EmbedField> fields) {
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
    public static class EmbedField {
        private String name;
        private String value;
        private boolean inline;

        /**
         * Creates an empty embed field.
         */
        public EmbedField() {}

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
}

