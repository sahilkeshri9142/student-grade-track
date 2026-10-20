package com.neurobuddy.ai.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Provides basic Natural Language Processing (NLP) capabilities:
 * text normalization, contraction expansion, tokenization, stopword filtering,
 * n-gram extraction, stem normalization, and fuzzy string similarity.
 */
public class NLPProcessor {

    // Common non-content stopwords (excluding question words that indicate intent)
    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "and", "or", "is", "are", "was", "were", "be", "been",
            "being", "have", "has", "had", "do", "does", "did", "to", "at", "by",
            "for", "with", "about", "into", "through", "during", "before", "after",
            "above", "below", "from", "up", "down", "in", "out", "on", "off", "over",
            "under", "again", "further", "then", "once", "here", "there", "any", "both",
            "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not",
            "only", "own", "same", "so", "than", "too", "very", "can", "will", "just",
            "should", "now", "please", "kindly", "tell", "give", "me", "know"
    ));

    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[^a-zA-Z0-9\\s]");
    private static final Pattern MULTI_SPACE_PATTERN = Pattern.compile("\\s+");

    /**
     * Cleans and normalizes raw user text:
     * 1. Converts to lowercase.
     * 2. Expands common conversational English contractions.
     * 3. Removes special punctuation.
     * 4. Normalizes whitespace.
     */
    public String cleanText(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "";
        }

        String text = rawText.toLowerCase(Locale.ENGLISH).trim();

        // Expand common contractions to preserve semantic meaning
        text = text.replace("what's", "what is")
                   .replace("who's", "who is")
                   .replace("how's", "how is")
                   .replace("it's", "it is")
                   .replace("that's", "that is")
                   .replace("i'm", "i am")
                   .replace("you're", "you are")
                   .replace("can't", "cannot")
                   .replace("won't", "will not")
                   .replace("don't", "do not")
                   .replace("doesn't", "does not")
                   .replace("didn't", "did not")
                   .replace("let's", "let us")
                   .replace("tell me", "explain")
                   .replace("explain to me", "explain")
                   .replace("dsa", "data structures and algorithms dsa");

        // Remove punctuation but retain alphanumerics and spaces
        text = PUNCTUATION_PATTERN.matcher(text).replaceAll(" ");
        text = MULTI_SPACE_PATTERN.matcher(text).replaceAll(" ").trim();

        return text;
    }

    /**
     * Splits text into individual word tokens.
     */
    public List<String> tokenize(String cleanedText) {
        if (cleanedText == null || cleanedText.isEmpty()) {
            return Collections.emptyList();
        }
        String[] parts = cleanedText.split("\\s+");
        List<String> tokens = new ArrayList<>(parts.length);
        for (String part : parts) {
            if (!part.isEmpty()) {
                tokens.add(part);
            }
        }
        return tokens;
    }

    /**
     * Filters out non-informative stopwords from token list.
     */
    public List<String> filterStopwords(List<String> tokens) {
        List<String> filtered = new ArrayList<>();
        for (String token : tokens) {
            if (!STOPWORDS.contains(token) && token.length() > 1) {
                filtered.add(token);
            }
        }
        return filtered;
    }

    /**
     * Normalizes words using simple rule-based suffix stemming
     * (e.g. databases -> database, loops -> loop, running -> run).
     */
    public String stem(String word) {
        if (word == null || word.length() <= 3) {
            return word;
        }

        // Handle plurals
        if (word.endsWith("ies") && word.length() > 4) {
            return word.substring(0, word.length() - 3) + "y";
        }
        if (word.endsWith("es") && (word.endsWith("sses") || word.endsWith("shes") || word.endsWith("ches"))) {
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("s") && !word.endsWith("ss") && !word.endsWith("is") && !word.endsWith("us")) {
            return word.substring(0, word.length() - 1);
        }

        // Handle -ing
        if (word.endsWith("ing") && word.length() > 5) {
            return word.substring(0, word.length() - 3);
        }

        // Handle -ed
        if (word.endsWith("ed") && word.length() > 4) {
            return word.substring(0, word.length() - 2);
        }

        return word;
    }

    /**
     * Extracts word stems from tokens.
     */
    public Set<String> getStemmedTokens(List<String> tokens) {
        Set<String> stems = new HashSet<>();
        for (String token : tokens) {
            stems.add(stem(token));
            stems.add(token); // keep original token as well
        }
        return stems;
    }

    /**
     * Extracts bigrams and trigrams to capture compound phrases
     * like "operating system", "linked list", "binary search".
     */
    public List<String> extractNGrams(List<String> tokens, int n) {
        List<String> nGrams = new ArrayList<>();
        if (tokens.size() < n) {
            return nGrams;
        }
        for (int i = 0; i <= tokens.size() - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j > 0) sb.append(" ");
                sb.append(tokens.get(i + j));
            }
            nGrams.add(sb.toString());
        }
        return nGrams;
    }

    /**
     * Computes Jaccard Similarity coefficient between two sets of tokens (0.0 to 1.0).
     */
    public double jaccardSimilarity(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) return 1.0;
        if (set1.isEmpty() || set2.isEmpty()) return 0.0;

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    /**
     * Levenshtein Distance for typo tolerance (e.g. pyton -> python).
     */
    public int levenshteinDistance(String s1, String s2) {
        if (s1.equals(s2)) return 0;
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[len1][len2];
    }

    /**
     * Checks if a token fuzzy matches a target word within allowed edit distance.
     */
    public boolean fuzzyMatch(String token, String target) {
        if (token.equalsIgnoreCase(target)) return true;
        int maxDist = target.length() >= 6 ? 2 : (target.length() >= 4 ? 1 : 0);
        return levenshteinDistance(token, target) <= maxDist;
    }
}
