package dev.matthiesen.matthiesen_core.common.core.discord.model;

/**
 * Represents a binary attachment sent with a Discord webhook request.
 */
@SuppressWarnings("unused")
public class WebhookFile {
    private String filename;
    private byte[] data;

    /**
     * Creates an empty webhook file instance.
     */
    public WebhookFile() {
    }

    /**
     * Creates a webhook file instance with file metadata and data.
     *
     * @param filename the file name to expose to Discord
     * @param data the binary file payload
     */
    public WebhookFile(String filename, byte[] data) {
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

