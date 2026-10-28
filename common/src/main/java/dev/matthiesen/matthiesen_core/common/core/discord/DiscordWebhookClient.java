package dev.matthiesen.matthiesen_core.common.core.discord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.matthiesen.matthiesen_core.common.api.discord.WebhookClient;
import dev.matthiesen.matthiesen_core.common.api.discord.exceptions.DiscordWebhookDeliveryException;
import dev.matthiesen.matthiesen_core.common.api.discord.exceptions.DiscordWebhookException;
import dev.matthiesen.matthiesen_core.common.api.discord.exceptions.DiscordWebhookSerializationException;
import dev.matthiesen.matthiesen_core.common.core.discord.model.Message;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A client for sending messages to Discord webhooks.
 *
 * <p>This class provides methods to send messages to Discord webhooks using HTTP POST requests.</p>
 */
public final class DiscordWebhookClient implements WebhookClient {
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 5000;

    private final Gson gson;

    /**
     * Constructs a new DiscordWebhookClient with a Gson instance for JSON serialization.
     */
    public DiscordWebhookClient() {
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override
    public void sendMessage(String webhookUrl, Message message) throws DiscordWebhookException {
        if (webhookUrl == null || webhookUrl.isBlank()) {
            throw new DiscordWebhookException("Webhook URL must not be blank");
        }
        if (message == null) {
            throw new DiscordWebhookException("Webhook message must not be null");
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URI(webhookUrl).toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            connection.setDoOutput(true);

            String payload;
            try {
                payload = gson.toJson(message);
            } catch (Exception e) {
                throw new DiscordWebhookSerializationException("Failed to serialize webhook message", e);
            }

            List<Message.File> files = message.getFiles();
            if (files != null && !files.isEmpty()) {
                writeMultipartPayload(connection, payload, files);
            } else {
                writeJsonPayload(connection, payload);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                String responseBody = readResponseBody(connection);
                throw new DiscordWebhookDeliveryException(
                        "Discord webhook responded with HTTP status " + responseCode + ": " + responseBody,
                        responseCode
                );
            }
        } catch (DiscordWebhookException e) {
            throw e;
        } catch (Exception e) {
            throw new DiscordWebhookException("Unexpected error while sending webhook", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void writeJsonPayload(HttpURLConnection connection, String payload) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }

    private static void writeMultipartPayload(HttpURLConnection connection, String payload, List<Message.File> files) throws IOException {
        String boundary = "===" + System.currentTimeMillis() + "===";
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream outputStream = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(outputStream, true, StandardCharsets.UTF_8)) {

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"payload_json\"\r\n");
            writer.append("Content-Type: application/json; charset=UTF-8\r\n\r\n");
            writer.append(payload).append("\r\n");
            writer.flush();

            for (int i = 0; i < files.size(); i++) {
                Message.File file = files.get(i);
                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"files[").append(String.valueOf(i)).append("]\"; filename=\"")
                        .append(file.getFilename()).append("\"\r\n");
                writer.append("Content-Type: application/octet-stream\r\n\r\n");
                writer.flush();

                outputStream.write(file.getData());
                outputStream.flush();

                writer.append("\r\n");
                writer.flush();
            }

            writer.append("--").append(boundary).append("--\r\n");
            writer.flush();
        }
    }

    private static String readResponseBody(HttpURLConnection connection) {
        InputStream errorStream = connection.getErrorStream();
        if (errorStream == null) {
            return "<empty>";
        }

        try (InputStream stream = errorStream; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            stream.transferTo(output);
            return output.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "<unreadable>";
        }
    }
}
