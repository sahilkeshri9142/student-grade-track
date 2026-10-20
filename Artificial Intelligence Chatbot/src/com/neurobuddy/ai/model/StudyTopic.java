package com.neurobuddy.ai.model;

/**
 * Enumeration of study domains and conversation categories supported by NeuroBuddy AI.
 */
public enum StudyTopic {
    GENERAL("General", "Common conversations, bot identity, and greetings"),
    JAVA("Java", "Core Java, OOP, JVM, Collections, Multithreading"),
    PYTHON("Python", "Python syntax, data structures, GIL, decorators"),
    DBMS("DBMS", "Databases, SQL, Normalization, ACID properties, Indexing"),
    DSA("DSA", "Data structures, algorithms, Big-O complexity, trees, graphs"),
    OS("Operating Systems", "Processes, threads, deadlocks, memory management, CPU scheduling"),
    NETWORKS("Computer Networks", "OSI model, TCP/UDP, DNS, HTTP/HTTPS, IP/MAC"),
    MOTIVATION("Motivation & Wellness", "Study encouragement, stress management, productivity tips"),
    JOKES("Humor & Fun", "Programmer jokes, geek trivia, lighthearted laughs");

    private final String displayName;
    private final String description;

    StudyTopic(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
