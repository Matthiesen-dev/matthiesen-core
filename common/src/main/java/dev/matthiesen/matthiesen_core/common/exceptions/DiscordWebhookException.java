package dev.matthiesen.matthiesen_core.common.exceptions;

/**
 * Exception thrown when an error occurs while sending a message to a Discord webhook.
 * This can include network errors, invalid webhook URLs, or any other issues that arise during the sending process.
 */
public class DiscordWebhookException extends Exception {

    /**
     * Constructs a new DiscordWebhookException with the specified detail message.
     * @param message A detailed message describing the error that occurred while sending a message to the Discord webhook.
     */
    public DiscordWebhookException(String message) {
        super(message);
    }

    /**
     * Constructs a new DiscordWebhookException with the specified detail message and cause.
     * @param message A detailed message describing the error that occurred while sending a message to the Discord webhook.
     * @param cause The underlying cause of the exception, which can be used for debugging and error handling purposes. This
     *              allows you to trace back to the original exception that led to this error, such as a network failure or an invalid URL format.
     */
    public DiscordWebhookException(String message, Throwable cause) {
        super(message, cause);
    }
}

