package dev.matthiesen.matthiesen_core.common.core.discord.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a Discord webhook message payload.
 */
@SuppressWarnings("unused")
public class WebhookMessage {
    private String content;
    private String username;
    @SerializedName("avatar_url")
    private String avatarUrl;
    private boolean tts;
    private List<Embed> embeds;
    private List<Component> components;
    private transient List<WebhookFile> files;

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
    public List<WebhookFile> getFiles() {
        return files;
    }

    /**
     * Sets file attachments associated with this message.
     *
     * @param files the attachment list
     */
    public void setFiles(List<WebhookFile> files) {
        this.files = files;
    }
}

