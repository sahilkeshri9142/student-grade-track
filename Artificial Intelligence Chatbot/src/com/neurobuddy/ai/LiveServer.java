package com.neurobuddy.ai;

import com.neurobuddy.ai.engine.ResponseEngine;
import com.neurobuddy.ai.model.ChatMessage;
import com.neurobuddy.ai.model.UserProfile;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Built-in lightweight Live Server for NeuroBuddy AI using Java's built-in HttpServer.
 * Serves index.html, style.css, app.js, and provides the /api/chat REST API.
 */
public class LiveServer {

    private static final int PORT = 8080;
    private static ResponseEngine responseEngine;

    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("     NEUROBUDDY AI - LOCAL LIVE WEB SERVER                          ");
        System.out.println("====================================================================");

        UserProfile profile = new UserProfile("Scholar");
        responseEngine = new ResponseEngine(profile);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Static file handler
            server.createContext("/", new StaticFileHandler());

            // Chat API handler
            server.createContext("/api/chat", new ChatApiHandler());

            server.setExecutor(null); // default executor
            server.start();

            String url = "http://localhost:" + PORT;
            System.out.println("\n[SUCCESS] NeuroBuddy AI Live Server is running at: " + url);
            System.out.println("• You can open this link in any browser: " + url);
            System.out.println("• Or use VS Code Live Server extension by right-clicking index.html");
            System.out.println("• Press Ctrl+C in this terminal to stop the server.\n");

            // Attempt to open browser automatically
            try {
                if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                }
            } catch (Exception ignored) {
                // Headless or browse not supported
            }

        } catch (IOException e) {
            System.err.println("[ERROR] Failed to start Live Server on port " + PORT + ": " + e.getMessage());
            System.err.println("Make sure port " + PORT + " is not already in use by another program.");
        }
    }

    private static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            }

            // Remove leading slash
            String relativePath = path.startsWith("/") ? path.substring(1) : path;
            File file = new File(relativePath);

            if (!file.exists() || file.isDirectory()) {
                String notFound = "404 Not Found: " + path;
                exchange.sendResponseHeaders(404, notFound.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound.getBytes(StandardCharsets.UTF_8));
                }
                return;
            }

            String contentType = "text/plain";
            if (path.endsWith(".html")) contentType = "text/html; charset=UTF-8";
            else if (path.endsWith(".css")) contentType = "text/css; charset=UTF-8";
            else if (path.endsWith(".js")) contentType = "application/javascript; charset=UTF-8";
            else if (path.endsWith(".png")) contentType = "image/png";
            else if (path.endsWith(".svg")) contentType = "image/svg+xml";

            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, file.length());

            try (InputStream is = new FileInputStream(file);
                 OutputStream os = exchange.getResponseBody()) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    os.write(buffer, 0, count);
                }
            }
        }
    }

    private static class ChatApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String error = "Method Not Allowed";
                exchange.sendResponseHeaders(405, error.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(error.getBytes(StandardCharsets.UTF_8));
                }
                return;
            }

            // Read request body
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Simple robust JSON extractor without external dependencies
            String userQuery = extractJsonValue(body, "message");
            String nickname = extractJsonValue(body, "nickname");

            if (nickname != null && !nickname.isEmpty()) {
                responseEngine.getUserProfile().setNickname(nickname);
            }

            ChatMessage botReply = responseEngine.processUserInput(userQuery);

            StringBuilder json = new StringBuilder("{");
            json.append("\"reply\": \"").append(escapeJson(botReply.getContent())).append("\",");
            json.append("\"confidence\": ").append(botReply.getConfidence()).append(",");
            json.append("\"mood\": \"").append(escapeJson(botReply.getMoodTag())).append("\",");
            json.append("\"suggestions\": [");
            List<String> suggestions = botReply.getSuggestions();
            for (int i = 0; i < suggestions.size(); i++) {
                if (i > 0) json.append(",");
                json.append("\"").append(escapeJson(suggestions.get(i))).append("\"");
            }
            json.append("]}");

            byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private String extractJsonValue(String json, String key) {
            if (json == null) return "";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("[\"']?" + key + "[\"']?\\s*:\\s*[\"']([^\"']*)[\"']");
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) {
                return m.group(1).trim();
            }
            return "";
        }

        private String escapeJson(String s) {
            if (s == null) return "";
            return s.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
        }
    }
}
