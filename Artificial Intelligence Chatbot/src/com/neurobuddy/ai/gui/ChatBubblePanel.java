package com.neurobuddy.ai.gui;

import com.neurobuddy.ai.model.ChatMessage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.function.Consumer;

/**
 * Modern chat bubble panel rendering user and bot messages with custom shapes,
 * avatar headers, formatted rich text, confidence badges, and copy-to-clipboard functionality.
 */
public class ChatBubblePanel extends JPanel {

    private final ChatMessage message;
    private final Consumer<String> onSuggestionClick;

    public ChatBubblePanel(ChatMessage message, String userNickname, Consumer<String> onSuggestionClick) {
        this.message = message;
        this.onSuggestionClick = onSuggestionClick;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        buildUI(userNickname);
    }

    private void buildUI(String userNickname) {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        if (message.isUser()) {
            buildUserBubble(container, userNickname);
            add(container, BorderLayout.EAST);
        } else {
            buildBotBubble(container);
            add(container, BorderLayout.WEST);
        }
    }

    private void buildUserBubble(JPanel container, String userNickname) {
        // Top label: Name + time
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        headerPanel.setOpaque(false);

        JLabel timeLabel = new JLabel(message.getFormattedTime());
        timeLabel.setFont(ThemeConstants.FONT_TINY);
        timeLabel.setForeground(ThemeConstants.TEXT_MUTED);

        JLabel nameLabel = new JLabel(userNickname != null ? userNickname : "You");
        nameLabel.setFont(ThemeConstants.FONT_BOLD);
        nameLabel.setForeground(ThemeConstants.PURPLE_LIGHT);

        headerPanel.add(timeLabel);
        headerPanel.add(Box.createHorizontalStrut(4));
        headerPanel.add(nameLabel);

        container.add(headerPanel);
        container.add(Box.createVerticalStrut(4));

        // Bubble Body
        RoundedBubblePanel bubble = new RoundedBubblePanel(true);
        bubble.setLayout(new BorderLayout());
        bubble.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JEditorPane textPane = createTextPane(formatUserText(message.getContent()), ThemeConstants.TEXT_ON_PURPLE);
        bubble.add(textPane, BorderLayout.CENTER);

        container.add(bubble);
    }

    private void buildBotBubble(JPanel container) {
        // Top label: Bot avatar + name + time
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        headerPanel.setOpaque(false);

        JLabel avatarLabel = new JLabel("🤖 NeuroBuddy AI");
        avatarLabel.setFont(ThemeConstants.FONT_BOLD);
        avatarLabel.setForeground(ThemeConstants.PURPLE_LIGHT);

        JLabel timeLabel = new JLabel(message.getFormattedTime());
        timeLabel.setFont(ThemeConstants.FONT_TINY);
        timeLabel.setForeground(ThemeConstants.TEXT_MUTED);

        headerPanel.add(avatarLabel);
        headerPanel.add(Box.createHorizontalStrut(6));
        headerPanel.add(timeLabel);

        container.add(headerPanel);
        container.add(Box.createVerticalStrut(4));

        // Bubble Body
        RoundedBubblePanel bubble = new RoundedBubblePanel(false);
        bubble.setLayout(new BorderLayout());
        bubble.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JEditorPane textPane = createTextPane(formatMarkdownToHtml(message.getContent()), ThemeConstants.TEXT_PRIMARY);
        bubble.add(textPane, BorderLayout.CENTER);

        // Footer panel (Confidence pill, Mood pill, Copy button)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        footerPanel.setOpaque(false);

        if (message.getMatchedIntent() != null && !message.getMatchedIntent().isEmpty()) {
            int conf = message.getConfidencePercentage();
            Color badgeBg = conf >= 75 ? new Color(0x06, 0x4E, 0x3B) :
                           (conf >= 50 ? new Color(0x1E, 0x3A, 0x5F) : new Color(0x45, 0x1A, 0x1A));
            Color badgeText = conf >= 75 ? ThemeConstants.SUCCESS_GREEN :
                             (conf >= 50 ? ThemeConstants.ACCENT_CYAN : ThemeConstants.WARNING_AMBER);

            JLabel confPill = new JLabel("🎯 " + conf + "% Match");
            confPill.setFont(ThemeConstants.FONT_TINY);
            confPill.setForeground(badgeText);
            confPill.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(badgeText, 1, true),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            footerPanel.add(confPill);
        }

        if (message.getMoodTag() != null && !message.getMoodTag().equals("NEUTRAL")) {
            JLabel moodPill = new JLabel("💬 " + message.getMoodTag());
            moodPill.setFont(ThemeConstants.FONT_TINY);
            moodPill.setForeground(ThemeConstants.PURPLE_LIGHT);
            moodPill.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ThemeConstants.PURPLE_LIGHT, 1, true),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            footerPanel.add(moodPill);
        }

        // Copy button
        JButton copyBtn = new JButton("📋 Copy");
        copyBtn.setFont(ThemeConstants.FONT_TINY);
        copyBtn.setForeground(ThemeConstants.TEXT_SECONDARY);
        copyBtn.setContentAreaFilled(false);
        copyBtn.setBorderPainted(false);
        copyBtn.setFocusPainted(false);
        copyBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        copyBtn.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(message.getContent()), null);
            copyBtn.setText("✓ Copied!");
            Timer t = new Timer(1500, evt -> copyBtn.setText("📋 Copy"));
            t.setRepeats(false);
            t.start();
        });
        footerPanel.add(copyBtn);

        bubble.add(footerPanel, BorderLayout.SOUTH);
        container.add(bubble);

        // If message includes follow-up suggestions, display them as interactive chips
        if (message.getSuggestions() != null && !message.getSuggestions().isEmpty() && onSuggestionClick != null) {
            container.add(Box.createVerticalStrut(6));
            JPanel chipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
            chipsPanel.setOpaque(false);

            for (String suggestion : message.getSuggestions()) {
                ModernButton chip = new ModernButton(suggestion, ModernButton.Style.CHIP, 8);
                chip.addActionListener(e -> onSuggestionClick.accept(suggestion));
                chipsPanel.add(chip);
            }
            container.add(chipsPanel);
        }
    }

    private JEditorPane createTextPane(String htmlContent, Color defaultColor) {
        JEditorPane pane = new JEditorPane("text/html", htmlContent);
        pane.setEditable(false);
        pane.setOpaque(false);
        pane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        pane.setFont(ThemeConstants.FONT_BODY);
        pane.setForeground(defaultColor);
        pane.setMaximumSize(new Dimension(650, Integer.MAX_VALUE));
        return pane;
    }

    private String formatUserText(String text) {
        return "<html><body style='font-family: Segoe UI, sans-serif; font-size: 13px; color: #FFFFFF; margin: 0; padding: 0;'>"
                + escapeHtml(text).replace("\n", "<br/>") + "</body></html>";
    }

    private String formatMarkdownToHtml(String markdown) {
        if (markdown == null) return "";

        String html = escapeHtml(markdown);

        // Convert bold: **text** -> <b>text</b>
        html = html.replaceAll("\\*\\*(.*?)\\*\\*", "<b style='color:#C4B5FD;'>$1</b>");

        // Convert inline code: `code` -> <code>...</code>
        html = html.replaceAll("`([^`]+)`",
                "<code style='background-color:#0B0F19; color:#A78BFA; padding:2px 5px; font-family:Consolas,monospace; border-radius:4px;'>$1</code>");

        // Convert markdown headers
        html = html.replaceAll("### (.*?)(<br/>|$)", "<h3 style='color:#C4B5FD; margin:4px 0;'>$1</h3>");

        // Convert newlines to HTML breaks
        html = html.replace("\n", "<br/>");

        return "<html><body style='font-family: Segoe UI, sans-serif; font-size: 13px; color: #F8FAFC; margin: 0; padding: 0; line-height: 1.45;'>"
                + html + "</body></html>";
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }

    /**
     * Inner custom rounded panel for chat bubbles with gradient or slate navy fills.
     */
    private static class RoundedBubblePanel extends JPanel {
        private final boolean isUser;

        RoundedBubblePanel(boolean isUser) {
            this.isUser = isUser;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (isUser) {
                g2.setPaint(new GradientPaint(
                        0, 0, ThemeConstants.USER_BUBBLE_TOP,
                        0, height, ThemeConstants.USER_BUBBLE_BOTTOM
                ));
                g2.fillRoundRect(0, 0, width, height, 16, 16);
            } else {
                g2.setColor(ThemeConstants.BOT_BUBBLE_BG);
                g2.fillRoundRect(0, 0, width, height, 16, 16);
                g2.setColor(ThemeConstants.BOT_BUBBLE_BORDER);
                g2.drawRoundRect(0, 0, width - 1, height - 1, 16, 16);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
