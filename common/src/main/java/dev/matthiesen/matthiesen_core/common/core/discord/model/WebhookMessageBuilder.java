package dev.matthiesen.matthiesen_core.common.core.discord.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fluent builder for creating {@link WebhookMessage} instances.
 */
@SuppressWarnings("unused")
public final class WebhookMessageBuilder {
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private List<Embed> embeds;
    private List<Component> components;
    private List<WebhookFile> files;

    /**
     * Creates an empty webhook message builder.
     */
    public WebhookMessageBuilder() {}

    /**
     * Sets plain message content.
     *
     * @param content the content text
     * @return this builder
     */
    public WebhookMessageBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Sets webhook username override.
     *
     * @param username the username
     * @return this builder
     */
    public WebhookMessageBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets webhook avatar URL override.
     *
     * @param avatarUrl the avatar URL
     * @return this builder
     */
    public WebhookMessageBuilder withAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    /**
     * Sets whether Discord text-to-speech is enabled.
     *
     * @param tts tts flag
     * @return this builder
     */
    public WebhookMessageBuilder withTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    /**
     * Replaces the embed list.
     *
     * @param embeds the embed list
     * @return this builder
     */
    public WebhookMessageBuilder withEmbeds(List<Embed> embeds) {
        this.embeds = embeds;
        return this;
    }

    /**
     * Appends an embed to the message.
     *
     * @param embed the embed
     * @return this builder
     */
    public WebhookMessageBuilder addEmbed(Embed embed) {
        if (this.embeds == null) {
            this.embeds = new ArrayList<>();
        }
        this.embeds.add(embed);
        return this;
    }

    /**
     * Appends an embed configured by a nested builder callback.
     *
     * @param embedBuilderCustomizer callback that customizes an {@link EmbedBuilder}
     * @return this builder
     */
    public WebhookMessageBuilder addEmbed(Consumer<EmbedBuilder> embedBuilderCustomizer) {
        EmbedBuilder builder = new EmbedBuilder();
        embedBuilderCustomizer.accept(builder);
        return addEmbed(builder.build());
    }

    /**
     * Replaces the component list.
     *
     * @param components the component list
     * @return this builder
     */
    public WebhookMessageBuilder withComponents(List<Component> components) {
        this.components = components;
        return this;
    }

    /**
     * Appends a component to the message.
     *
     * @param component the component
     * @return this builder
     */
    public WebhookMessageBuilder addComponent(Component component) {
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
    public WebhookMessageBuilder withFiles(List<WebhookFile> files) {
        this.files = files;
        return this;
    }

    /**
     * Appends a file attachment.
     *
     * @param file the attachment
     * @return this builder
     */
    public WebhookMessageBuilder addFile(WebhookFile file) {
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

