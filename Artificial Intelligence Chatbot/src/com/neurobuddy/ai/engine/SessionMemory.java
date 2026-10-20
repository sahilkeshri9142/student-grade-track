package com.neurobuddy.ai.engine;

import com.neurobuddy.ai.model.ChatMessage;
import com.neurobuddy.ai.model.StudyTopic;
import com.neurobuddy.ai.model.UserProfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages conversation memory during the active session:
 * maintains message logs, tracks current context topic, and handles file exports.
 */
public class SessionMemory {

    private final List<ChatMessage> history;
    private StudyTopic activeTopic;
    private final UserProfile userProfile;
    private static final int MAX_MEMORY_CAPACITY = 200;

    public SessionMemory(UserProfile userProfile) {
        this.userProfile = userProfile;
        this.history = new ArrayList<>();
        this.activeTopic = StudyTopic.GENERAL;
    }

    public synchronized void addMessage(ChatMessage message) {
        if (message == null) return;
        history.add(message);
        if (history.size() > MAX_MEMORY_CAPACITY) {
            history.remove(0);
        }
    }

    public synchronized List<ChatMessage> getHistory() {
        return Collections.unmodifiableList(new ArrayList<>(history));
    }

    public synchronized void clearHistory() {
        history.clear();
        activeTopic = StudyTopic.GENERAL;
    }

    public StudyTopic getActiveTopic() {
        return activeTopic;
    }

    public void setActiveTopic(StudyTopic activeTopic) {
        if (activeTopic != null) {
            this.activeTopic = activeTopic;
            userProfile.recordTopic(activeTopic);
        }
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    /**
     * Exports the conversation to a formatted text file.
     *
     * @param targetFile destination file
     * @return true if exported successfully, false otherwise
     */
    public boolean exportToFile(File targetFile) {
        if (targetFile == null) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write("====================================================\n");
            writer.write("   NeuroBuddy AI - Conversation Transcript\n");
            writer.write("====================================================\n");
            writer.write("User Nickname: " + userProfile.getNickname() + "\n");
            writer.write("Session Started: " + userProfile.getFormattedSessionStart() + "\n");
            writer.write("Exported At: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("Total Messages: " + history.size() + "\n");
            writer.write("Topics Explored: " + userProfile.getTopicsExploredCount() + "\n");
            writer.write("----------------------------------------------------\n\n");

            for (ChatMessage msg : history) {
                String senderLabel;
                if (msg.isUser()) {
                    senderLabel = userProfile.getNickname();
                } else if (msg.isBot()) {
                    senderLabel = "NeuroBuddy AI";
                } else {
                    senderLabel = "SYSTEM";
                }

                writer.write(String.format("[%s] %s:\n%s\n",
                        msg.getExportFormattedTime(), senderLabel, msg.getContent()));

                if (msg.isBot() && msg.getMatchedIntent() != null) {
                    writer.write(String.format("   (Intent: %s | Confidence: %d%%)\n",
                            msg.getMatchedIntent(), msg.getConfidencePercentage()));
                }
                writer.write("\n");
            }

            writer.write("====================================================\n");
            writer.write("End of Conversation Log - NeuroBuddy AI Assistant\n");
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting conversation: " + e.getMessage());
            return false;
        }
    }
}
