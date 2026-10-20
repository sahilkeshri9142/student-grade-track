package com.neurobuddy.ai.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Represents a conversational intent with training patterns, keywords,
 * varied response templates, and suggested follow-up queries.
 */
public class Intent {

    private final String id;
    private final StudyTopic topic;
    private final String description;
    private final List<String> sampleQuestions;
    private final Set<String> requiredKeywords;
    private final Set<String> secondaryKeywords;
    private final List<String> responseTemplates;
    private final List<String> suggestedFollowUps;
    private final double confidenceThreshold;
    private static final Random RANDOM = new Random();

    public Intent(String id, StudyTopic topic, String description,
                  List<String> sampleQuestions,
                  Set<String> requiredKeywords,
                  Set<String> secondaryKeywords,
                  List<String> responseTemplates,
                  List<String> suggestedFollowUps,
                  double confidenceThreshold) {
        this.id = id;
        this.topic = topic != null ? topic : StudyTopic.GENERAL;
        this.description = description;
        this.sampleQuestions = sampleQuestions != null ? new ArrayList<>(sampleQuestions) : new ArrayList<>();
        this.requiredKeywords = requiredKeywords != null ? new HashSet<>(requiredKeywords) : new HashSet<>();
        this.secondaryKeywords = secondaryKeywords != null ? new HashSet<>(secondaryKeywords) : new HashSet<>();
        this.responseTemplates = responseTemplates != null ? new ArrayList<>(responseTemplates) : new ArrayList<>();
        this.suggestedFollowUps = suggestedFollowUps != null ? new ArrayList<>(suggestedFollowUps) : new ArrayList<>();
        this.confidenceThreshold = confidenceThreshold > 0 ? confidenceThreshold : 0.45;
    }

    public String getId() {
        return id;
    }

    public StudyTopic getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getSampleQuestions() {
        return Collections.unmodifiableList(sampleQuestions);
    }

    public Set<String> getRequiredKeywords() {
        return Collections.unmodifiableSet(requiredKeywords);
    }

    public Set<String> getSecondaryKeywords() {
        return Collections.unmodifiableSet(secondaryKeywords);
    }

    public List<String> getResponseTemplates() {
        return Collections.unmodifiableList(responseTemplates);
    }

    public List<String> getSuggestedFollowUps() {
        return Collections.unmodifiableList(suggestedFollowUps);
    }

    public double getConfidenceThreshold() {
        return confidenceThreshold;
    }

    /**
     * Retrieves a random response template from the available choices.
     */
    public String getRandomResponse() {
        if (responseTemplates.isEmpty()) {
            return "I understand your question about " + description + ", but no response template is configured.";
        }
        return responseTemplates.get(RANDOM.nextInt(responseTemplates.size()));
    }

    /**
     * Checks if user tokens contain any required keyword.
     */
    public boolean containsAnyRequiredKeyword(Set<String> tokens) {
        if (requiredKeywords.isEmpty()) {
            return true;
        }
        for (String req : requiredKeywords) {
            if (tokens.contains(req)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Counts how many secondary keywords are matched.
     */
    public int countSecondaryMatches(Set<String> tokens) {
        int count = 0;
        for (String sec : secondaryKeywords) {
            if (tokens.contains(sec)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "Intent{" + id + ", topic=" + topic + "}";
    }
}
