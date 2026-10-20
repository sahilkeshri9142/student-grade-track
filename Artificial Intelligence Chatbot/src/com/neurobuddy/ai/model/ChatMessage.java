package com.neurobuddy.ai.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single message exchanged between user, bot, or system.
 */
public class ChatMessage {

    public enum Sender {
        USER,
        BOT,
        SYSTEM
    }

    private final String id;
    private final Sender sender;
    private final String content;
    private final LocalDateTime timestamp;
    private final double confidence;
    private final String matchedIntent;
    private final List<String> suggestions;
    private final String moodTag;

    private static final DateTimeFormatter DISPLAY_TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter EXPORT_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ChatMessage(Sender sender, String content) {
        this(sender, content, 1.0, null, Collections.emptyList(), null);
    }

    public ChatMessage(Sender sender, String content, double confidence, String matchedIntent,
                       List<String> suggestions, String moodTag) {
        this.id = String.valueOf(System.nanoTime());
        this.sender = sender;
        this.content = content != null ? content.trim() : "";
        this.timestamp = LocalDateTime.now();
        this.confidence = Math.max(0.0, Math.min(1.0, confidence));
        this.matchedIntent = matchedIntent;
        this.suggestions = suggestions != null ? new ArrayList<>(suggestions) : new ArrayList<>();
        this.moodTag = moodTag;
    }

    public String getId() {
        return id;
    }

    public Sender getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTime() {
        return timestamp.format(DISPLAY_TIME_FORMAT);
    }

    public String getExportFormattedTime() {
        return timestamp.format(EXPORT_TIME_FORMAT);
    }

    public double getConfidence() {
        return confidence;
    }

    public int getConfidencePercentage() {
        return (int) Math.round(confidence * 100);
    }

    public String getMatchedIntent() {
        return matchedIntent;
    }

    public List<String> getSuggestions() {
        return Collections.unmodifiableList(suggestions);
    }

    public String getMoodTag() {
        return moodTag;
    }

    public boolean isUser() {
        return sender == Sender.USER;
    }

    public boolean isBot() {
        return sender == Sender.BOT;
    }

    public boolean isSystem() {
        return sender == Sender.SYSTEM;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", getFormattedTime(), sender, content);
    }
}
