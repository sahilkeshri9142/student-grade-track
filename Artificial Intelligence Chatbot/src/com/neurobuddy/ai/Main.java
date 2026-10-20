package com.neurobuddy.ai;

import com.neurobuddy.ai.engine.ResponseEngine;
import com.neurobuddy.ai.gui.ChatbotGUI;
import com.neurobuddy.ai.model.UserProfile;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point for NeuroBuddy AI – Smart Conversational Assistant.
 */
public class Main {

    public static void main(String[] args) {
        // Enable high-DPI font antialiasing and subpixel rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            // Use native cross-platform UI settings
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default Swing Look & Feel
        }

        SwingUtilities.invokeLater(() -> {
            // Initialize User Profile with default nickname
            UserProfile profile = new UserProfile("Scholar");

            // Initialize Response and NLP Engine
            ResponseEngine engine = new ResponseEngine(profile);

            // Launch Main Modern GUI Window
            ChatbotGUI gui = new ChatbotGUI(engine);
            gui.setVisible(true);
        });
    }
}
