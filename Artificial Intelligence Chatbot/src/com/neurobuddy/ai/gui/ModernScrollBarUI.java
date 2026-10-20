package com.neurobuddy.ai.gui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * Minimalist, modern dark scrollbar with rounded thumb and hidden track buttons.
 */
public class ModernScrollBarUI extends BasicScrollBarUI {

    private final Color thumbColor;
    private final Color hoverColor;

    public ModernScrollBarUI() {
        this.thumbColor = new Color(0x33, 0x41, 0x65, 180);
        this.hoverColor = new Color(0x47, 0x55, 0x89, 220);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // Transparent track
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = isThumbRollover() ? hoverColor : thumbColor;
        g2.setColor(color);

        int margin = 3;
        int width = Math.max(4, thumbBounds.width - (margin * 2));
        int height = thumbBounds.height;
        int x = thumbBounds.x + margin;
        int y = thumbBounds.y;

        g2.fillRoundRect(x, y, width, height, 8, 8);
        g2.dispose();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}
