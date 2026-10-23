package dev.matthiesen.matthiesen_core.common.core.discord.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a Discord webhook message payload.
 */
@SuppressWarnings("unused")
public final class WebhookMessage {
    private String content;
    private String username;
    @SerializedName("avatar_url")
    private String avatarUrl;
    private boolean tts;
    private List<Embed> embeds;
    private List<Component> components;
    private transient List<File> files;

    /**
     * Creates an empty webhook message.
     */
    public WebhookMessage() {
    }

    /**
     * Creates a webhook message with only content populated.
     *
     * @param content the plain message content
     */
    public WebhookMessage(String content) {
        this.content = content;
    }

    /**
     * Gets the plain message content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the plain message content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the display username override.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the display username override.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the avatar URL override.
     *
     * @return the avatar URL
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Sets the avatar URL override.
     *
     * @param avatarUrl the avatar URL
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * Gets whether Discord text-to-speech should be used.
     *
     * @return {@code true} if tts is enabled
     */
    public boolean isTts() {
        return tts;
    }

    /**
     * Sets whether Discord text-to-speech should be used.
     *
     * @param tts tts flag
     */
    public void setTts(boolean tts) {
        this.tts = tts;
    }

    /**
     * Gets the embed list.
     *
     * @return the embeds
     */
    public List<Embed> getEmbeds() {
        return embeds;
    }

    /**
     * Sets the embed list.
     *
     * @param embeds the embeds
     */
    public void setEmbeds(List<Embed> embeds) {
        this.embeds = embeds;
    }

    /**
     * Gets the component list.
     *
     * @return the components
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Sets the component list.
     *
     * @param components the components
     */
    public void setComponents(List<Component> components) {
        this.components = components;
    }

    /**
     * Gets file attachments associated with this message.
     *
     * @return the attachment list
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Sets file attachments associated with this message.
     *
     * @param files the attachment list
     */
    public void setFiles(List<File> files) {
        this.files = files;
    }

    /**
     * Represents a file attachment for a Discord webhook message.
     */
    public static class File {
        private String filename;
        private byte[] data;

        /**
         * Creates an empty webhook file instance.
         */
        public File() {
        }

        /**
         * Creates a webhook file instance with file metadata and data.
         *
         * @param filename the file name to expose to Discord
         * @param data the binary file payload
         */
        public File(String filename, byte[] data) {
            this.filename = filename;
            this.data = data;
        }

        /**
         * Gets the attachment file name.
         *
         * @return the file name
         */
        public String getFilename() {
            return filename;
        }

        /**
         * Sets the attachment file name.
         *
         * @param filename the file name
         */
        public void setFilename(String filename) {
            this.filename = filename;
        }

        /**
         * Gets the attachment binary data.
         *
         * @return the binary payload
         */
        public byte[] getData() {
            return data;
        }

        /**
         * Sets the attachment binary data.
         *
         * @param data the binary payload
         */
        public void setData(byte[] data) {
            this.data = data;
        }
    }

    public static class Builder {
        private String content;
        private String username;
        private String avatarUrl;
        private boolean tts;
        private List<Embed> embeds;
        private List<Component> components;
        private List<File> files;

        /**
         * Creates an empty webhook message builder.
         */
        public Builder() {}

        /**
         * Sets plain message content.
         *
         * @param content the content text
         * @return this builder
         */
        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        /**
         * Sets webhook username override.
         *
         * @param username the username
         * @return this builder
         */
        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        /**
         * Sets webhook avatar URL override.
         *
         * @param avatarUrl the avatar URL
         * @return this builder
         */
        public Builder withAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        /**
         * Sets whether Discord text-to-speech is enabled.
         *
         * @param tts tts flag
         * @return this builder
         */
        public Builder withTts(boolean tts) {
            this.tts = tts;
            return this;
        }

        /**
         * Replaces the embed list.
         *
         * @param embeds the embed list
         * @return this builder
         */
        public Builder withEmbeds(List<Embed> embeds) {
            this.embeds = embeds;
            return this;
        }

        /**
         * Appends an embed to the message.
         *
         * @param embed the embed
         * @return this builder
         */
        public Builder addEmbed(Embed embed) {
            if (this.embeds == null) {
                this.embeds = new ArrayList<>();
            }
            this.embeds.add(embed);
            return this;
        }

        /**
         * Appends an embed configured by a nested builder callback.
         *
         * @param embedBuilderCustomizer callback that customizes an {@link Embed.Builder}
         * @return this builder
         */
        public Builder addEmbed(Consumer<Embed.Builder> embedBuilderCustomizer) {
            Embed.Builder builder = new Embed.Builder();
            embedBuilderCustomizer.accept(builder);
            return addEmbed(builder.build());
        }

        /**
         * Replaces the component list.
         *
         * @param components the component list
         * @return this builder
         */
        public Builder withComponents(List<Component> components) {
            this.components = components;
            return this;
        }

        /**
         * Appends a component to the message.
         *
         * @param component the component
         * @return this builder
         */
        public Builder addComponent(Component component) {
            if (this.components == null) {
                this.components = new ArrayList<>();
            }
            this.components.add(component);
            return this;
        }

        /**
         * Replaces the file attachment list.
         *
         * @param files attachment list
         * @return this builder
         */
        public Builder withFiles(List<File> files) {
            this.files = files;
            return this;
        }

        /**
         * Appends a file attachment.
         *
         * @param file the attachment
         * @return this builder
         */
        public Builder addFile(File file) {
            if (this.files == null) {
                this.files = new ArrayList<>();
            }
            this.files.add(file);
            return this;
        }

        /**
         * Builds a new {@link WebhookMessage} instance.
         *
         * @return the built message
         */
        public WebhookMessage build() {
            WebhookMessage message = new WebhookMessage();
            message.setContent(content);
            message.setUsername(username);
            message.setAvatarUrl(avatarUrl);
            message.setTts(tts);
            message.setEmbeds(embeds);
            message.setComponents(components);
            message.setFiles(files);
            return message;
        }
    }
}

