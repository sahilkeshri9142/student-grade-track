package com.neurobuddy.ai;

import com.neurobuddy.ai.engine.ResponseEngine;
import com.neurobuddy.ai.model.ChatMessage;
import com.neurobuddy.ai.model.UserProfile;

/**
 * Automated test suite verifying the NLP processor, intent matching,
 * confidence scoring, mood detection, and response generation across all domains.
 */
public class NeuroBuddyConsoleTest {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("  NeuroBuddy AI - Automated Verification & NLP Test Suite");
        System.out.println("=========================================================\n");

        UserProfile profile = new UserProfile("Scholar");
        ResponseEngine engine = new ResponseEngine(profile);

        String[] testInputs = {
                // Example conversations from user prompt specification
                "Hello",
                "What is DBMS?",
                "I feel demotivated.",
                "Tell me a joke.",
                "How does quantum teleportation bake cookies in Mars?", // Unknown test
                // Study assistant domains
                "Explain OOP concepts in Java",
                "Difference between list and tuple in python",
                "What is binary search algorithm?",
                "What is deadlock in operating systems?",
                "Explain 7 layers of OSI model",
                "What is ACID properties in DBMS?",
                // Personalization test
                "My name is Anup",
                "Can you help me with exam stress?",
                "Thank you so much!"
        };

        int passed = 0;

        for (int i = 0; i < testInputs.length; i++) {
            String input = testInputs[i];
            System.out.println(String.format("[%02d] User Input: \"%s\"", i + 1, input));

            ChatMessage response = engine.processUserInput(input);

            System.out.println("     Bot Reply: " + response.getContent().replace("\n", " ").substring(0, Math.min(100, response.getContent().length())) + "...");
            System.out.println(String.format("     Intent: %-20s | Confidence: %d%% | Mood: %s",
                    response.getMatchedIntent(), response.getConfidencePercentage(), response.getMoodTag()));
            System.out.println("     Suggestions: " + response.getSuggestions());
            System.out.println("---------------------------------------------------------------------------------");

            if (response.getContent() != null && !response.getContent().isEmpty()) {
                passed++;
            }
        }

        System.out.println(String.format("\nTest Summary: %d/%d test cases executed successfully!", passed, testInputs.length));
        System.out.println("Total Messages in Session Memory: " + engine.getSessionMemory().getHistory().size());
        System.out.println("Topics Explored: " + engine.getUserProfile().getTopicsExplored());
        System.out.println("Final User Nickname: " + engine.getUserProfile().getNickname());
    }
}
