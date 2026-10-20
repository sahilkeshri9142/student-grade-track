package com.neurobuddy.ai.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the user's profile, including their preferred nickname, session
 * statistics, and topics explored during the conversation.
 */
public class UserProfile {

    private String nickname;
    private final LocalDateTime sessionStart;
    private int userMessageCount;
    private int botResponseCount;
    private final Set<StudyTopic> topicsExplored;
    private String lastDetectedMood;

    public UserProfile(String initialNickname) {
        this.nickname = (initialNickname != null && !initialNickname.trim().isEmpty())
                ? initialNickname.trim()
                : "Friend";
        this.sessionStart = LocalDateTime.now();
        this.userMessageCount = 0;
        this.botResponseCount = 0;
        this.topicsExplored = new HashSet<>();
        this.lastDetectedMood = "Neutral";
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (nickname != null && !nickname.trim().isEmpty()) {
            this.nickname = nickname.trim();
        }
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public String getFormattedSessionStart() {
        return sessionStart.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public int getUserMessageCount() {
        return userMessageCount;
    }

    public void incrementUserMessages() {
        this.userMessageCount++;
    }

    public int getBotResponseCount() {
        return botResponseCount;
    }

    public void incrementBotResponses() {
        this.botResponseCount++;
    }

    public void recordTopic(StudyTopic topic) {
        if (topic != null) {
            this.topicsExplored.add(topic);
        }
    }

    public Set<StudyTopic> getTopicsExplored() {
        return Collections.unmodifiableSet(topicsExplored);
    }

    public int getTopicsExploredCount() {
        return topicsExplored.size();
    }

    public String getLastDetectedMood() {
        return lastDetectedMood;
    }

    public void setLastDetectedMood(String lastDetectedMood) {
        this.lastDetectedMood = lastDetectedMood;
    }
}
