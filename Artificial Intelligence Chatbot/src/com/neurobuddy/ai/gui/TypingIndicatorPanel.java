package com.neurobuddy.ai.gui;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Animated three-dot typing indicator displaying "NeuroBuddy is thinking...".
 */
public class TypingIndicatorPanel extends JPanel {

    private int animationStep = 0;
    private final Timer timer;

    public TypingIndicatorPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(140, 36));

        timer = new Timer(150, e -> {
            animationStep = (animationStep + 1) % 4;
            repaint();
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            animationStep = 0;
            if (!timer.isRunning()) {
                timer.start();
            }
        } else {
            if (timer.isRunning()) {
                timer.stop();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isVisible()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw bubble background
        g2.setColor(ThemeConstants.BOT_BUBBLE_BG);
        g2.fillRoundRect(8, 4, 110, 28, 16, 16);
        g2.setColor(ThemeConstants.BOT_BUBBLE_BORDER);
        g2.drawRoundRect(8, 4, 110, 28, 16, 16);

        // Draw 3 bouncing/pulsing dots
        int startX = 38;
        int baseY = 19;
        int dotRadius = 6;
        int spacing = 16;

        for (int i = 0; i < 3; i++) {
            int yOffset = 0;
            Color dotColor;
            if (animationStep == i) {
                yOffset = -3;
                dotColor = ThemeConstants.PURPLE_LIGHT;
            } else {
                dotColor = new Color(0x64, 0x74, 0x8B);
            }

            g2.setColor(dotColor);
            g2.fillOval(startX + (i * spacing), baseY + yOffset, dotRadius, dotRadius);
        }

        g2.dispose();
    }
}
