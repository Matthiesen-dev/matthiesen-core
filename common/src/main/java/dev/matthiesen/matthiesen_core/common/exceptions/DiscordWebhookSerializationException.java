package dev.matthiesen.matthiesen_core.common.exceptions;

/**
 * Exception thrown when there is an error during the serialization of a Discord webhook message.
 * This can occur when converting a WebhookMessage object into the format required for sending to the Discord API, such as
 * JSON serialization. This exception can be used to indicate issues with the message structure, missing required fields,
 * or any other problems that arise during the serialization process before the message is sent to the Discord webhook.
 */
public class DiscordWebhookSerializationException extends DiscordWebhookException {
    /**
     * Constructs a new DiscordWebhookSerializationException with the specified detail message.
     * @param message A detailed message describing the error that occurred during the serialization of a Discord webhook message.
     *                This message should provide information about what went wrong during the serialization process, such as missing
     *                required fields, invalid data formats, or any other issues that prevented the message from being properly serialized
     *                for sending to the Discord API.
     * @param cause The underlying cause of the exception, which can be used for debugging and error handling purposes. This
     *              allows you to trace back to the original exception that led to this serialization error, such as a JSON
     *              processing error, an invalid message structure, or any other issue that occurred during the conversion of
     *              the WebhookMessage object into the format required by the Discord API.
     */
    public DiscordWebhookSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}

