package dev.matthiesen.matthiesen_core.common.exceptions;

/**
 * Exception thrown when a Discord webhook delivery fails, such as when the Discord API returns an error status code. This
 * exception includes the HTTP status code returned by the API for better debugging and error handling.
 */
public class DiscordWebhookDeliveryException extends DiscordWebhookException {
    /**
     * The HTTP status code returned by the Discord API that caused the webhook delivery to fail. This status code can be used to
     * determine the specific reason for the error, such as 400 for bad request, 401 for unauthorized, 403 for forbidden, 404 for
     * not found, 500 for internal server error, etc. This information is crucial for debugging and handling errors when interacting
     * with the Discord API, as it provides insight into why the delivery failed and what steps may be needed to resolve the issue
     * (e.g., correcting the webhook URL, ensuring proper authentication, fixing message formatting, etc.).
     */
    private final int statusCode;

    /**
     * Constructs a new DiscordWebhookDeliveryException with the specified message and HTTP status code.
     * @param message A detailed message describing the error that occurred during webhook delivery.
     * @param statusCode The HTTP status code returned by the Discord API that indicates the reason for the delivery failure
     *                   (e.g., 400 for bad request, 401 for unauthorized, 403 for forbidden, 404 for not found, 500 for internal server error, etc.).
     */
    public DiscordWebhookDeliveryException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Retrieves the HTTP status code associated with this delivery failure, which can be used to determine the specific reason
     * for the error when interacting with the Discord API.
     * @return The HTTP status code returned by the Discord API that caused the webhook delivery to fail.
     */
    public int getStatusCode() {
        return statusCode;
    }
}

