package com.neurobuddy.ai.gui;

import com.neurobuddy.ai.model.UserProfile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Consumer;

/**
 * Left sidebar panel providing branding, live status indicator, user profile card,
 * quick action buttons, session controls, and exploration metrics.
 */
public class SidebarPanel extends JPanel {

    private final UserProfile userProfile;
    private final Consumer<String> onActionTriggered;
    private final Runnable onClearChat;
    private final Runnable onExportChat;
    private final Consumer<String> onNameChanged;

    private JLabel nicknameLabel;
    private JLabel statsLabel;

    public SidebarPanel(UserProfile userProfile,
                        Consumer<String> onActionTriggered,
                        Runnable onClearChat,
                        Runnable onExportChat,
                        Consumer<String> onNameChanged) {
        this.userProfile = userProfile;
        this.onActionTriggered = onActionTriggered;
        this.onClearChat = onClearChat;
        this.onExportChat = onExportChat;
        this.onNameChanged = onNameChanged;

        setPreferredSize(new Dimension(260, 0));
        setMinimumSize(new Dimension(240, 0));
        setBackground(ThemeConstants.SIDEBAR_BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ThemeConstants.SIDEBAR_CARD_BORDER));

        buildContent();
    }

    private void buildContent() {
        JPanel scrollContent = new JPanel();
        scrollContent.setOpaque(false);
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 16));

        // 1. Branding Header
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel logoIconLabel = new JLabel("🧠 NeuroBuddy AI");
        logoIconLabel.setFont(ThemeConstants.FONT_LOGO);
        logoIconLabel.setForeground(ThemeConstants.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Smart Conversational Assistant");
        subtitleLabel.setFont(ThemeConstants.FONT_SMALL);
        subtitleLabel.setForeground(ThemeConstants.PURPLE_LIGHT);

        // Status badge
        JPanel statusBadge = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        statusBadge.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dot.setForeground(ThemeConstants.SUCCESS_GREEN);
        JLabel statusText = new JLabel("Online • Offline NLP Engine");
        statusText.setFont(ThemeConstants.FONT_TINY);
        statusText.setForeground(ThemeConstants.SUCCESS_GREEN);
        statusBadge.add(dot);
        statusBadge.add(statusText);

        brandPanel.add(logoIconLabel);
        brandPanel.add(Box.createVerticalStrut(3));
        brandPanel.add(subtitleLabel);
        brandPanel.add(Box.createVerticalStrut(6));
        brandPanel.add(statusBadge);

        scrollContent.add(brandPanel);
        scrollContent.add(Box.createVerticalStrut(16));

        // 2. User Profile Card
        JPanel profileCard = new RoundedCardPanel(12);
        profileCard.setLayout(new BorderLayout(8, 0));
        profileCard.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        profileCard.setAlignmentX(LEFT_ALIGNMENT);
        profileCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));

        nicknameLabel = new JLabel(userProfile.getNickname());
        nicknameLabel.setFont(ThemeConstants.FONT_BOLD);
        nicknameLabel.setForeground(ThemeConstants.TEXT_PRIMARY);

        JLabel studentLabel = new JLabel("Active Learner");
        studentLabel.setFont(ThemeConstants.FONT_TINY);
        studentLabel.setForeground(ThemeConstants.TEXT_MUTED);

        namePanel.add(nicknameLabel);
        namePanel.add(studentLabel);

        JButton editBtn = new JButton("✏️ Edit");
        editBtn.setFont(ThemeConstants.FONT_TINY);
        editBtn.setForeground(ThemeConstants.PURPLE_LIGHT);
        editBtn.setContentAreaFilled(false);
        editBtn.setBorderPainted(false);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> promptNameChange());

        profileCard.add(userIcon, BorderLayout.WEST);
        profileCard.add(namePanel, BorderLayout.CENTER);
        profileCard.add(editBtn, BorderLayout.EAST);

        scrollContent.add(profileCard);
        scrollContent.add(Box.createVerticalStrut(20));

        // 3. Quick Actions Section
        JLabel actionsHeader = new JLabel("QUICK ACTIONS");
        actionsHeader.setFont(ThemeConstants.FONT_TINY);
        actionsHeader.setForeground(ThemeConstants.TEXT_MUTED);
        actionsHeader.setAlignmentX(LEFT_ALIGNMENT);
        scrollContent.add(actionsHeader);
        scrollContent.add(Box.createVerticalStrut(8));

        scrollContent.add(createActionButton("📚 Study Help", "Show me the study topics I can learn", ModernButton.Style.SECONDARY_DARK));
        scrollContent.add(Box.createVerticalStrut(8));
        scrollContent.add(createActionButton("💡 Motivation", "I feel demotivated", ModernButton.Style.SECONDARY_DARK));
        scrollContent.add(Box.createVerticalStrut(8));
        scrollContent.add(createActionButton("😄 Tell a Joke", "Tell me a joke", ModernButton.Style.SECONDARY_DARK));
        scrollContent.add(Box.createVerticalStrut(8));
        scrollContent.add(createActionButton("❓ General Questions", "What can you do?", ModernButton.Style.SECONDARY_DARK));

        scrollContent.add(Box.createVerticalStrut(20));

        // 4. Session Controls
        JLabel sessionHeader = new JLabel("CHAT MANAGEMENT");
        sessionHeader.setFont(ThemeConstants.FONT_TINY);
        sessionHeader.setForeground(ThemeConstants.TEXT_MUTED);
        sessionHeader.setAlignmentX(LEFT_ALIGNMENT);
        scrollContent.add(sessionHeader);
        scrollContent.add(Box.createVerticalStrut(8));

        ModernButton exportBtn = new ModernButton("📥 Export Chat History", ModernButton.Style.SECONDARY_DARK, 10);
        exportBtn.setAlignmentX(LEFT_ALIGNMENT);
        exportBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        exportBtn.addActionListener(e -> onExportChat.run());
        scrollContent.add(exportBtn);

        scrollContent.add(Box.createVerticalStrut(8));

        ModernButton clearBtn = new ModernButton("🗑️ Clear Chat", ModernButton.Style.DANGER_SUBTLE, 10);
        clearBtn.setAlignmentX(LEFT_ALIGNMENT);
        clearBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        clearBtn.addActionListener(e -> onClearChat.run());
        scrollContent.add(clearBtn);

        add(scrollContent, BorderLayout.NORTH);

        // 5. Footer Stats
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

        statsLabel = new JLabel("Messages: 0 | Topics: 0");
        statsLabel.setFont(ThemeConstants.FONT_TINY);
        statsLabel.setForeground(ThemeConstants.TEXT_MUTED);
        statsLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel versionLabel = new JLabel("NeuroBuddy AI • College Portfolio");
        versionLabel.setFont(ThemeConstants.FONT_TINY);
        versionLabel.setForeground(new Color(0x47, 0x55, 0x69));
        versionLabel.setAlignmentX(CENTER_ALIGNMENT);

        footerPanel.add(statsLabel);
        footerPanel.add(Box.createVerticalStrut(4));
        footerPanel.add(versionLabel);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private ModernButton createActionButton(String label, String query, ModernButton.Style style) {
        ModernButton btn = new ModernButton(label, style, 10);
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setHorizontalAlignment(JButton.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        btn.addActionListener(e -> onActionTriggered.accept(query));
        return btn;
    }

    private void promptNameChange() {
        String newName = JOptionPane.showInputDialog(
                this,
                "What would you like NeuroBuddy AI to call you?",
                userProfile.getNickname()
        );
        if (newName != null && !newName.trim().isEmpty()) {
            userProfile.setNickname(newName.trim());
            nicknameLabel.setText(userProfile.getNickname());
            if (onNameChanged != null) {
                onNameChanged.accept(userProfile.getNickname());
            }
        }
    }

    public void updateStats() {
        if (statsLabel != null) {
            statsLabel.setText(String.format("Messages: %d | Topics: %d",
                    userProfile.getUserMessageCount(),
                    userProfile.getTopicsExploredCount()));
        }
        if (nicknameLabel != null) {
            nicknameLabel.setText(userProfile.getNickname());
        }
    }

    /**
     * Card container panel with rounded corners and subtle border.
     */
    private static class RoundedCardPanel extends JPanel {
        private final int radius;

        RoundedCardPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(ThemeConstants.SIDEBAR_CARD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(ThemeConstants.SIDEBAR_CARD_BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
