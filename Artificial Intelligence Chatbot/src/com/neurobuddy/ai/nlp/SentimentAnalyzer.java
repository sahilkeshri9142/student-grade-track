package com.neurobuddy.ai.nlp;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Detects basic sentiment states (Positive, Frustrated, Demotivated/Stressed, Confused, Neutral)
 * using transparent rule-based lexicon matching, enabling supportive and mood-aware replies.
 */
public class SentimentAnalyzer {

    public enum SentimentType {
        POSITIVE,
        FRUSTRATED,
        STRESSED_DEMOTIVATED,
        CONFUSED,
        NEUTRAL
    }

    public static class SentimentResult {
        private final SentimentType type;
        private final double score; // -1.0 to 1.0
        private final String supportiveMessage;

        public SentimentResult(SentimentType type, double score, String supportiveMessage) {
            this.type = type;
            this.score = score;
            this.supportiveMessage = supportiveMessage;
        }

        public SentimentType getType() {
            return type;
        }

        public double getScore() {
            return score;
        }

        public String getSupportiveMessage() {
            return supportiveMessage;
        }

        public boolean isMoodDetected() {
            return type != SentimentType.NEUTRAL;
        }
    }

    private static final Set<String> POSITIVE_WORDS = new HashSet<>(Arrays.asList(
            "happy", "great", "awesome", "good", "amazing", "love", "excited",
            "excellent", "wonderful", "cool", "fantastic", "yay", "super",
            "glad", "enjoy", "brilliant", "perfect", "pleased", "thank"
    ));

    private static final Set<String> FRUSTRATED_WORDS = new HashSet<>(Arrays.asList(
            "frustrated", "bug", "bugs", "error", "errors", "hate", "annoying", "annoyed",
            "angry", "mad", "broken", "failing", "fails", "crash", "crashed", "crap",
            "sucks", "awful", "terrible", "horrible", "stuck", "fed up", "waste"
    ));

    private static final Set<String> STRESSED_WORDS = new HashSet<>(Arrays.asList(
            "demotivated", "depressed", "sad", "stressed", "stress", "overwhelmed",
            "exhausted", "tired", "quit", "give up", "giving up", "hopeless",
            "nervous", "anxious", "scared", "fear", "failure", "imposter", "burnout"
    ));

    private static final Set<String> CONFUSED_WORDS = new HashSet<>(Arrays.asList(
            "confused", "lost", "complicated", "unclear", "hard", "difficult",
            "tough", "clueless", "puzzled", "baffled"
    ));

    /**
     * Evaluates user tokens against sentiment lexicons.
     */
    public SentimentResult analyze(String cleanedText, List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return new SentimentResult(SentimentType.NEUTRAL, 0.0, null);
        }

        int posCount = 0;
        int frustCount = 0;
        int stressCount = 0;
        int confCount = 0;

        for (String token : tokens) {
            if (POSITIVE_WORDS.contains(token)) posCount++;
            if (FRUSTRATED_WORDS.contains(token)) frustCount++;
            if (STRESSED_WORDS.contains(token)) stressCount++;
            if (CONFUSED_WORDS.contains(token)) confCount++;
        }

        // Check for common multi-word expressions in cleanedText
        if (cleanedText.contains("give up") || cleanedText.contains("giving up") || cleanedText.contains("feel down")) {
            stressCount += 2;
        }
        if (cleanedText.contains("not working") || cleanedText.contains("doesn't work") || cleanedText.contains("wont work")) {
            frustCount += 2;
        }
        if (cleanedText.contains("do not understand") || cleanedText.contains("cant get this")) {
            confCount += 2;
        }

        // Determine dominant sentiment
        if (stressCount > 0 && stressCount >= frustCount && stressCount >= posCount) {
            return new SentimentResult(
                    SentimentType.STRESSED_DEMOTIVATED,
                    -0.75,
                    "I hear that you might be feeling overwhelmed or demotivated. Take a deep breath — learning takes time, and every programmer experiences tough days. You've got this!"
            );
        }

        if (frustCount > 0 && frustCount >= posCount) {
            return new SentimentResult(
                    SentimentType.FRUSTRATED,
                    -0.65,
                    "Debugging and code errors can definitely feel frustrating! Let's tackle it methodically together. What specific part is causing issues?"
            );
        }

        if (confCount > 0 && confCount >= posCount) {
            return new SentimentResult(
                    SentimentType.CONFUSED,
                    -0.30,
                    "Computer science topics can feel like a maze at first glance! No worries at all — we can break it down into simple, digestible steps."
            );
        }

        if (posCount > 0) {
            return new SentimentResult(
                    SentimentType.POSITIVE,
                    0.80,
                    "I love that positive energy! Keep that learning momentum going!"
            );
        }

        return new SentimentResult(SentimentType.NEUTRAL, 0.0, null);
    }
}
