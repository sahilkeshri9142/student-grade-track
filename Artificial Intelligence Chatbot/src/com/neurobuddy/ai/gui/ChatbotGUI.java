package com.neurobuddy.ai.gui;

import com.neurobuddy.ai.engine.ResponseEngine;
import com.neurobuddy.ai.model.ChatMessage;
import com.neurobuddy.ai.model.UserProfile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

/**
 * Main Graphical User Interface for NeuroBuddy AI.
 * Built with Java Swing using a dark navy & purple theme, dynamic message bubbles,
 * typing indicators, suggestions, and real-time NLP interactions.
 */
public class ChatbotGUI extends JFrame {

    private final ResponseEngine responseEngine;
    private final UserProfile userProfile;

    private SidebarPanel sidebarPanel;
    private JPanel chatMessagesPanel;
    private JScrollPane chatScrollPane;
    private TypingIndicatorPanel typingIndicator;
    private SuggestionBar suggestionBar;
    private JTextField inputField;
    private ModernButton sendButton;

    public ChatbotGUI(ResponseEngine responseEngine) {
        super("NeuroBuddy AI – Smart Conversational Assistant");
        this.responseEngine = responseEngine;
        this.userProfile = responseEngine.getUserProfile();

        initWindow();
        buildUI();
        displayWelcomeMessage();
    }

    private void initWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1060, 720);
        setMinimumSize(new Dimension(880, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeConstants.WINDOW_BG);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // 1. Left Sidebar
        sidebarPanel = new SidebarPanel(
                userProfile,
                this::handleUserSend,
                this::confirmAndClearChat,
                this::handleExportChat,
                this::handleNicknameUpdated
        );
        add(sidebarPanel, BorderLayout.WEST);

        // 2. Right Main Chat Area
        JPanel mainChatContainer = new JPanel(new BorderLayout());
        mainChatContainer.setBackground(ThemeConstants.CHAT_BG);

        // Top Suggestion Bar
        suggestionBar = new SuggestionBar(this::handleUserSend);
        suggestionBar.setBorder(BorderFactory.createEmptyBorder(6, 16, 4, 16));
        suggestionBar.updateSuggestions(responseEngine.getDefaultSuggestions());
        mainChatContainer.add(suggestionBar, BorderLayout.NORTH);

        // Center Chat Messages Scrollable Area
        chatMessagesPanel = new JPanel();
        chatMessagesPanel.setLayout(new BoxLayout(chatMessagesPanel, BoxLayout.Y_AXIS));
        chatMessagesPanel.setBackground(ThemeConstants.CHAT_BG);
        chatMessagesPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Typing Indicator Panel (initially hidden)
        typingIndicator = new TypingIndicatorPanel();
        typingIndicator.setVisible(false);

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setBackground(ThemeConstants.CHAT_BG);
        scrollWrapper.add(chatMessagesPanel, BorderLayout.NORTH);
        scrollWrapper.add(typingIndicator, BorderLayout.CENTER);

        chatScrollPane = new JScrollPane(scrollWrapper);
        chatScrollPane.setBackground(ThemeConstants.CHAT_BG);
        chatScrollPane.getViewport().setBackground(ThemeConstants.CHAT_BG);
        chatScrollPane.setBorder(null);
        chatScrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        chatScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainChatContainer.add(chatScrollPane, BorderLayout.CENTER);

        // Bottom Input Area
        JPanel bottomBar = buildInputBottomBar();
        mainChatContainer.add(bottomBar, BorderLayout.SOUTH);

        add(mainChatContainer, BorderLayout.CENTER);
    }

    private JPanel buildInputBottomBar() {
        JPanel bottomContainer = new JPanel(new BorderLayout(10, 0));
        bottomContainer.setBackground(ThemeConstants.CHAT_BG);
        bottomContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, ThemeConstants.BOT_BUBBLE_BORDER),
                BorderFactory.createEmptyBorder(12, 16, 14, 16)
        ));

        // Rounded Input Box Wrapper
        RoundedInputBox inputWrapper = new RoundedInputBox(12);
        inputWrapper.setLayout(new BorderLayout(8, 0));
        inputWrapper.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));

        inputField = new JTextField();
        inputField.setFont(ThemeConstants.FONT_BODY);
        inputField.setForeground(ThemeConstants.TEXT_PRIMARY);
        inputField.setCaretColor(ThemeConstants.PURPLE_LIGHT);
        inputField.setBackground(new Color(0, 0, 0, 0));
        inputField.setOpaque(false);
        inputField.setBorder(null);

        // Enter key listener
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleUserSend(inputField.getText());
                }
            }
        });

        inputWrapper.add(inputField, BorderLayout.CENTER);

        // Send Button
        sendButton = new ModernButton("Send  ➤", ModernButton.Style.PRIMARY_PURPLE, 12);
        sendButton.setPreferredSize(new Dimension(100, 42));
        sendButton.addActionListener(e -> handleUserSend(inputField.getText()));

        bottomContainer.add(inputWrapper, BorderLayout.CENTER);
        bottomContainer.add(sendButton, BorderLayout.EAST);

        return bottomContainer;
    }

    private void displayWelcomeMessage() {
        String welcomeText = String.format(
                "Hey **%s**! Welcome to **NeuroBuddy AI** – your intelligent conversational assistant for Computer Science and programming!\n\n" +
                "I can assist you with:\n" +
                "• ☕ **Java**: Core concepts, OOP, JVM, Collections, Multithreading\n" +
                "• 🐍 **Python**: Lists vs Tuples, GIL, Decorators, Deep/Shallow Copy\n" +
                "• 🗄️ **DBMS**: Relational models, ACID properties, Normalization (1NF-BCNF), SQL vs NoSQL, Indexing\n" +
                "• 🌲 **DSA**: Complexity analysis (Big-O), Arrays, Stacks/Queues, Binary Search, BST, BFS/DFS\n" +
                "• 💻 **Operating Systems**: Process vs Thread, Deadlocks, Virtual Memory, CPU Scheduling\n" +
                "• 🌐 **Computer Networks**: OSI 7-Layer Model, TCP vs UDP, DNS, HTTP/HTTPS, IP/MAC\n" +
                "• 💡 **Study Motivation & Humor**: Encouragement when stuck, and programmer jokes!\n\n" +
                "Ask me any question below, or click any suggested prompt to begin!",
                userProfile.getNickname()
        );

        ChatMessage welcomeMsg = new ChatMessage(
                ChatMessage.Sender.BOT,
                welcomeText,
                1.0,
                "welcome",
                responseEngine.getDefaultSuggestions(),
                null
        );

        responseEngine.getSessionMemory().addMessage(welcomeMsg);
        addMessageBubble(welcomeMsg);
    }

    /**
     * Handles processing and displaying a message from the user.
     */
    public void handleUserSend(String text) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        String query = text.trim();
        inputField.setText("");

        // 1. Add User Message
        ChatMessage userMsg = new ChatMessage(ChatMessage.Sender.USER, query);
        responseEngine.getSessionMemory().addMessage(userMsg);
        addMessageBubble(userMsg);

        // 2. Disable input temporarily & show typing indicator
        inputField.setEnabled(false);
        sendButton.setEnabled(false);
        typingIndicator.setVisible(true);
        scrollToBottom();

        // 3. Process response in background SwingWorker for natural simulated bot response
        SwingWorker<ChatMessage, Void> worker = new SwingWorker<ChatMessage, Void>() {
            @Override
            protected ChatMessage doInBackground() throws Exception {
                // Natural simulated reading & thinking delay (450ms)
                Thread.sleep(450);
                return responseEngine.processUserInput(query);
            }

            @Override
            protected void done() {
                try {
                    ChatMessage botResponse = get();
                    typingIndicator.setVisible(false);
                    addMessageBubble(botResponse);

                    // Update suggestions and sidebar stats
                    suggestionBar.updateSuggestions(botResponse.getSuggestions());
                    sidebarPanel.updateStats();

                } catch (Exception ex) {
                    typingIndicator.setVisible(false);
                    ChatMessage errorMsg = new ChatMessage(
                            ChatMessage.Sender.BOT,
                            "An unexpected error occurred while processing your question: " + ex.getMessage()
                    );
                    addMessageBubble(errorMsg);
                } finally {
                    inputField.setEnabled(true);
                    sendButton.setEnabled(true);
                    inputField.requestFocusInWindow();
                    scrollToBottom();
                }
            }
        };

        worker.execute();
    }

    private void addMessageBubble(ChatMessage message) {
        ChatBubblePanel bubble = new ChatBubblePanel(message, userProfile.getNickname(), this::handleUserSend);
        chatMessagesPanel.add(bubble);
        chatMessagesPanel.add(Box.createVerticalStrut(6));
        chatMessagesPanel.revalidate();
        chatMessagesPanel.repaint();
        scrollToBottom();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = chatScrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }

    private void confirmAndClearChat() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear the conversation history?",
                "Clear Conversation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            responseEngine.getSessionMemory().clearHistory();
            chatMessagesPanel.removeAll();
            chatMessagesPanel.revalidate();
            chatMessagesPanel.repaint();
            suggestionBar.updateSuggestions(responseEngine.getDefaultSuggestions());
            displayWelcomeMessage();
            sidebarPanel.updateStats();
        }
    }

    private void handleExportChat() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Conversation Transcript");
        fileChooser.setSelectedFile(new File("neurobuddy_chat_history.txt"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".txt");
            }

            boolean success = responseEngine.getSessionMemory().exportToFile(selectedFile);
            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Conversation history exported successfully to:\n" + selectedFile.getAbsolutePath(),
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to save conversation to disk.",
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void handleNicknameUpdated(String newName) {
        JOptionPane.showMessageDialog(
                this,
                "Nickname updated to \"" + newName + "\"! NeuroBuddy will personalize all future replies.",
                "Profile Updated",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Custom text input container with rounded dark navy borders.
     */
    private static class RoundedInputBox extends JPanel {
        private final int radius;

        RoundedInputBox(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(ThemeConstants.INPUT_BG);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(ThemeConstants.INPUT_BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
