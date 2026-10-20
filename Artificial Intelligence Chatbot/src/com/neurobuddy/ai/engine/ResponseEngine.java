package com.neurobuddy.ai.engine;

import com.neurobuddy.ai.data.KnowledgeBase;
import com.neurobuddy.ai.model.ChatMessage;
import com.neurobuddy.ai.model.Intent;
import com.neurobuddy.ai.model.StudyTopic;
import com.neurobuddy.ai.model.UserProfile;
import com.neurobuddy.ai.nlp.IntentMatcher;
import com.neurobuddy.ai.nlp.NLPProcessor;
import com.neurobuddy.ai.nlp.SentimentAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Orchestrates the NLP pipeline: text preprocessing, sentiment detection,
 * intent recognition with confidence scoring, dynamic user personalization,
 * and contextual suggestion generation.
 */
public class ResponseEngine {

    private final NLPProcessor nlpProcessor;
    private final SentimentAnalyzer sentimentAnalyzer;
    private final IntentMatcher intentMatcher;
    private final KnowledgeBase knowledgeBase;
    private final SessionMemory sessionMemory;

    public ResponseEngine(UserProfile userProfile) {
        this.nlpProcessor = new NLPProcessor();
        this.sentimentAnalyzer = new SentimentAnalyzer();
        this.intentMatcher = new IntentMatcher(nlpProcessor);
        this.knowledgeBase = new KnowledgeBase();
        this.sessionMemory = new SessionMemory(userProfile);
    }

    public NLPProcessor getNlpProcessor() {
        return nlpProcessor;
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public SessionMemory getSessionMemory() {
        return sessionMemory;
    }

    public UserProfile getUserProfile() {
        return sessionMemory.getUserProfile();
    }

    /**
     * Processes a user's input string and produces an intelligent, formatted ChatMessage response.
     */
    public ChatMessage processUserInput(String rawInput) {
        UserProfile userProfile = sessionMemory.getUserProfile();

        if (rawInput == null || rawInput.trim().isEmpty()) {
            return new ChatMessage(
                    ChatMessage.Sender.BOT,
                    "It looks like you sent an empty message! Feel free to ask me any question about CS or hit one of the suggestion chips below.",
                    1.0,
                    "empty_input",
                    getDefaultSuggestions(),
                    null
            );
        }

        userProfile.incrementUserMessages();

        // 1. Text Preprocessing
        String cleanedText = nlpProcessor.cleanText(rawInput);
        List<String> tokens = nlpProcessor.tokenize(cleanedText);

        // 2. Sentiment / Mood Detection
        SentimentAnalyzer.SentimentResult sentiment = sentimentAnalyzer.analyze(cleanedText, tokens);
        userProfile.setLastDetectedMood(sentiment.getType().name());

        // 3. Check for direct nickname setting command
        if (cleanedText.startsWith("my name is ") || cleanedText.startsWith("call me ") || cleanedText.startsWith("i am ")) {
            String extractedName = extractNickname(cleanedText);
            if (!extractedName.isEmpty()) {
                userProfile.setNickname(extractedName);
                String botReply = String.format("Pleasure to meet you, **%s**! I'll call you %s from now on. What would you like to study or discuss?",
                        extractedName, extractedName);
                ChatMessage botMsg = new ChatMessage(
                        ChatMessage.Sender.BOT,
                        botReply,
                        0.98,
                        "set_name",
                        Arrays.asList("What is DBMS?", "Explain OOP concepts in Java", "Tell me a joke"),
                        sentiment.getType().name()
                );
                sessionMemory.addMessage(botMsg);
                userProfile.incrementBotResponses();
                return botMsg;
            }
        }

        // 4. Intent Recognition with Confidence Scoring
        StudyTopic activeTopic = sessionMemory.getActiveTopic();
        IntentMatcher.MatchResult matchResult = intentMatcher.match(
                cleanedText, tokens, knowledgeBase.getIntents(), activeTopic);

        // 5. Build Response
        ChatMessage responseMessage;

        if (matchResult.isConfident() && matchResult.getIntent() != null) {
            Intent intent = matchResult.getIntent();

            // Update session topic memory
            sessionMemory.setActiveTopic(intent.getTopic());

            // Retrieve and personalize response template
            String rawTemplate = intent.getRandomResponse();
            String personalizedResponse = rawTemplate.replace("{name}", userProfile.getNickname());

            // If a strong emotion was detected and wasn't already a motivation/joke intent, append gentle supportive note
            StringBuilder finalResponse = new StringBuilder();
            if (sentiment.isMoodDetected() &&
                sentiment.getSupportiveMessage() != null &&
                intent.getTopic() != StudyTopic.MOTIVATION &&
                intent.getTopic() != StudyTopic.JOKES) {
                finalResponse.append(sentiment.getSupportiveMessage()).append("\n\n");
            }
            finalResponse.append(personalizedResponse);

            responseMessage = new ChatMessage(
                    ChatMessage.Sender.BOT,
                    finalResponse.toString(),
                    matchResult.getConfidence(),
                    intent.getId(),
                    intent.getSuggestedFollowUps(),
                    sentiment.getType().name()
            );

        } else {
            // Low-confidence or unknown fallback response
            String fallbackText = buildFallbackMessage(cleanedText, userProfile.getNickname(), sentiment);
            List<String> suggestedOptions = getContextualFallbacks(matchResult);

            responseMessage = new ChatMessage(
                    ChatMessage.Sender.BOT,
                    fallbackText,
                    matchResult.getConfidence(),
                    "unknown_fallback",
                    suggestedOptions,
                    sentiment.getType().name()
            );
        }

        sessionMemory.addMessage(responseMessage);
        userProfile.incrementBotResponses();
        return responseMessage;
    }

    private String extractNickname(String cleaned) {
        String name = "";
        if (cleaned.startsWith("my name is ")) {
            name = cleaned.substring("my name is ".length()).trim();
        } else if (cleaned.startsWith("call me ")) {
            name = cleaned.substring("call me ".length()).trim();
        } else if (cleaned.startsWith("i am ")) {
            name = cleaned.substring("i am ".length()).trim();
        }

        // Capitalize first letter of words
        if (!name.isEmpty()) {
            String[] words = name.split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (String w : words) {
                if (!w.isEmpty()) {
                    sb.append(Character.toUpperCase(w.charAt(0)))
                      .append(w.substring(1).toLowerCase())
                      .append(" ");
                }
            }
            return sb.toString().trim();
        }
        return name;
    }

    private String buildFallbackMessage(String cleanedText, String nickname, SentimentAnalyzer.SentimentResult sentiment) {
        StringBuilder sb = new StringBuilder();

        if (sentiment.isMoodDetected() && sentiment.getSupportiveMessage() != null) {
            sb.append(sentiment.getSupportiveMessage()).append("\n\n");
        }

        sb.append(String.format("I'm still learning how to handle that specific question, %s! ", nickname));
        sb.append("Could you ask it in another way, or choose one of the study topics below?\n\n");
        sb.append("💡 *Tip*: I specialize in **Java, Python, DBMS, DSA, Operating Systems, Computer Networks**, and **Study Motivation**.");

        return sb.toString();
    }

    private List<String> getContextualFallbacks(IntentMatcher.MatchResult matchResult) {
        List<String> fallbacks = new ArrayList<>();
        if (matchResult != null && matchResult.getIntent() != null) {
            fallbacks.addAll(matchResult.getIntent().getSuggestedFollowUps());
        }
        if (fallbacks.size() < 4) {
            for (String def : getDefaultSuggestions()) {
                if (!fallbacks.contains(def) && fallbacks.size() < 4) {
                    fallbacks.add(def);
                }
            }
        }
        return fallbacks;
    }

    public List<String> getDefaultSuggestions() {
        return Arrays.asList(
                "What is DBMS?",
                "Explain OOP concepts in Java",
                "Difference between TCP and UDP",
                "Tell me a joke"
        );
    }
}
