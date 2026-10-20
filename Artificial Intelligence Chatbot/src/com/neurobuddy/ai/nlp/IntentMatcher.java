package com.neurobuddy.ai.nlp;

import com.neurobuddy.ai.model.Intent;
import com.neurobuddy.ai.model.StudyTopic;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Recognizes user intent by combining token overlap, Jaccard similarity against
 * sample questions, required/secondary keyword matching, n-gram detection,
 * and conversational context weighting.
 */
public class IntentMatcher {

    public static class MatchResult {
        private final Intent intent;
        private final double confidence;
        private final boolean confident;
        private final List<Intent> topCandidates;

        public MatchResult(Intent intent, double confidence, boolean confident, List<Intent> topCandidates) {
            this.intent = intent;
            this.confidence = Math.min(1.0, Math.max(0.0, confidence));
            this.confident = confident;
            this.topCandidates = topCandidates != null ? topCandidates : Collections.emptyList();
        }

        public Intent getIntent() {
            return intent;
        }

        public double getConfidence() {
            return confidence;
        }

        public boolean isConfident() {
            return confident;
        }

        public List<Intent> getTopCandidates() {
            return topCandidates;
        }
    }

    private final NLPProcessor nlpProcessor;
    private static final double MIN_ACCEPTABLE_CONFIDENCE = 0.40;

    public IntentMatcher(NLPProcessor nlpProcessor) {
        this.nlpProcessor = nlpProcessor;
    }

    /**
     * Finds the best matching intent from the knowledge base for a given input.
     *
     * @param cleanedText     normalized input string
     * @param tokens          list of words
     * @param intents         all available intents
     * @param activeContext   active study topic from conversation history
     * @return MatchResult containing the best intent and confidence score
     */
    public MatchResult match(String cleanedText, List<String> tokens,
                              List<Intent> intents, StudyTopic activeContext) {
        if (tokens == null || tokens.isEmpty() || intents == null || intents.isEmpty()) {
            return new MatchResult(null, 0.0, false, Collections.emptyList());
        }

        Set<String> tokenSet = new HashSet<>(tokens);
        Set<String> stemSet = nlpProcessor.getStemmedTokens(tokens);
        List<String> bigrams = nlpProcessor.extractNGrams(tokens, 2);
        List<String> trigrams = nlpProcessor.extractNGrams(tokens, 3);

        Intent bestIntent = null;
        double bestScore = 0.0;

        for (Intent intent : intents) {
            double score = computeIntentScore(cleanedText, tokenSet, stemSet, bigrams, trigrams, intent, activeContext);
            if (score > bestScore) {
                bestScore = score;
                bestIntent = intent;
            }
        }

        // Cap confidence at 1.0
        double finalConfidence = Math.min(1.0, bestScore);
        boolean isConfident = (bestIntent != null) && (finalConfidence >= bestIntent.getConfidenceThreshold());

        return new MatchResult(bestIntent, finalConfidence, isConfident, Collections.emptyList());
    }

    /**
     * Calculates the confidence score for a candidate intent.
     */
    private double computeIntentScore(String cleanedText,
                                      Set<String> tokenSet,
                                      Set<String> stemSet,
                                      List<String> bigrams,
                                      List<String> trigrams,
                                      Intent intent,
                                      StudyTopic activeContext) {
        double score = 0.0;

        // 1. Required keywords check
        if (!intent.getRequiredKeywords().isEmpty()) {
            boolean matchedRequired = false;
            for (String req : intent.getRequiredKeywords()) {
                if (tokenSet.contains(req) || stemSet.contains(req) || cleanedText.contains(req)) {
                    matchedRequired = true;
                    score += 0.35; // strong base confidence
                    break;
                }
            }
            // If required keywords exist and none matched, penalize heavily
            if (!matchedRequired) {
                return 0.0;
            }
        }

        // 2. Secondary keywords matching
        int secondaryMatches = 0;
        for (String sec : intent.getSecondaryKeywords()) {
            if (tokenSet.contains(sec) || stemSet.contains(sec) || cleanedText.contains(sec)) {
                secondaryMatches++;
            }
        }
        if (!intent.getSecondaryKeywords().isEmpty()) {
            double ratio = (double) secondaryMatches / intent.getSecondaryKeywords().size();
            score += ratio * 0.30;
        }

        // 3. Question pattern similarity (Jaccard against sample questions)
        double maxSampleSim = 0.0;
        for (String sample : intent.getSampleQuestions()) {
            String cleanSample = nlpProcessor.cleanText(sample);
            List<String> sampleTokens = nlpProcessor.tokenize(cleanSample);
            Set<String> sampleTokenSet = new HashSet<>(sampleTokens);

            double jaccard = nlpProcessor.jaccardSimilarity(tokenSet, sampleTokenSet);
            if (jaccard > maxSampleSim) {
                maxSampleSim = jaccard;
            }

            // Exact match boost
            if (cleanedText.equalsIgnoreCase(cleanSample)) {
                maxSampleSim = 1.0;
                break;
            }
        }
        score += maxSampleSim * 0.40;

        // 4. Exact phrase / N-gram matching
        for (String bigram : bigrams) {
            if (intent.getRequiredKeywords().contains(bigram) || intent.getSecondaryKeywords().contains(bigram)) {
                score += 0.15;
            }
        }
        for (String trigram : trigrams) {
            if (intent.getRequiredKeywords().contains(trigram) || intent.getSecondaryKeywords().contains(trigram)) {
                score += 0.20;
            }
        }

        // 5. Context Topic Continuity Boost
        if (activeContext != null && activeContext == intent.getTopic() && activeContext != StudyTopic.GENERAL) {
            score += 0.08; // slight boost for following up on the same study topic
        }

        return score;
    }
}
