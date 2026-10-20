package com.neurobuddy.ai.gui;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern custom JButton with rounded corners, gradient highlights, and hover transitions.
 */
public class ModernButton extends JButton {

    public enum Style {
        PRIMARY_PURPLE,
        SECONDARY_DARK,
        CHIP,
        DANGER_SUBTLE
    }

    private final Style style;
    private final int cornerRadius;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text, Style style) {
        this(text, style, 10);
    }

    public ModernButton(String text, Style style, int cornerRadius) {
        super(text);
        this.style = style;
        this.cornerRadius = cornerRadius;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        applyStyleProperties();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    private void applyStyleProperties() {
        switch (style) {
            case PRIMARY_PURPLE:
                setForeground(ThemeConstants.TEXT_ON_PURPLE);
                setFont(ThemeConstants.FONT_BOLD);
                break;
            case SECONDARY_DARK:
                setForeground(ThemeConstants.TEXT_PRIMARY);
                setFont(ThemeConstants.FONT_BODY);
                break;
            case CHIP:
                setForeground(ThemeConstants.PURPLE_LIGHT);
                setFont(ThemeConstants.FONT_SMALL);
                break;
            case DANGER_SUBTLE:
                setForeground(new Color(0xF8, 0x71, 0x71));
                setFont(ThemeConstants.FONT_SMALL);
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        switch (style) {
            case PRIMARY_PURPLE:
                Color top = isPressed ? ThemeConstants.PURPLE_HOVER :
                            (isHovered ? new Color(0x93, 0x33, 0xEA) : ThemeConstants.PURPLE_PRIMARY);
                Color bot = isPressed ? new Color(0x58, 0x1C, 0x87) :
                            (isHovered ? ThemeConstants.PURPLE_PRIMARY : ThemeConstants.PURPLE_HOVER);
                g2.setPaint(new GradientPaint(0, 0, top, 0, height, bot));
                g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
                break;

            case SECONDARY_DARK:
                Color bg = isPressed ? new Color(0x13, 0x1A, 0x2A) :
                           (isHovered ? new Color(0x28, 0x33, 0x4F) : ThemeConstants.SIDEBAR_CARD);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
                g2.setColor(isHovered ? ThemeConstants.PURPLE_ACCENT : ThemeConstants.SIDEBAR_CARD_BORDER);
                g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
                break;

            case CHIP:
                Color chipBg = isPressed ? new Color(0x18, 0x20, 0x35) :
                               (isHovered ? ThemeConstants.CHIP_HOVER : ThemeConstants.CHIP_BG);
                g2.setColor(chipBg);
                g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
                g2.setColor(isHovered ? ThemeConstants.PURPLE_LIGHT : ThemeConstants.CHIP_BORDER);
                g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
                break;

            case DANGER_SUBTLE:
                Color dangerBg = isPressed ? new Color(0x45, 0x12, 0x16) :
                                 (isHovered ? new Color(0x3B, 0x18, 0x1D) : new Color(0x24, 0x15, 0x1B));
                g2.setColor(dangerBg);
                g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
                g2.setColor(isHovered ? new Color(0xEF, 0x44, 0x44) : new Color(0x7F, 0x1D, 0x1D));
                g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
                break;
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
