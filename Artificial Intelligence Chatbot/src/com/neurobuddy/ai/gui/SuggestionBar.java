package com.neurobuddy.ai.gui;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.List;
import java.util.function.Consumer;

/**
 * Top or bottom horizontal prompt suggestion bar displaying interactive suggestion chips.
 */
public class SuggestionBar extends JPanel {

    private final Consumer<String> onSuggestionClick;

    public SuggestionBar(Consumer<String> onSuggestionClick) {
        this.onSuggestionClick = onSuggestionClick;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
    }

    public void updateSuggestions(List<String> suggestions) {
        removeAll();

        if (suggestions == null || suggestions.isEmpty()) {
            revalidate();
            repaint();
            return;
        }

        JLabel promptLabel = new JLabel("💡 Suggested:");
        promptLabel.setFont(ThemeConstants.FONT_SMALL);
        promptLabel.setForeground(ThemeConstants.PURPLE_LIGHT);
        add(promptLabel);
        add(Box.createHorizontalStrut(2));

        for (String suggestion : suggestions) {
            ModernButton chip = new ModernButton(suggestion, ModernButton.Style.CHIP, 8);
            chip.addActionListener(e -> {
                if (onSuggestionClick != null) {
                    onSuggestionClick.accept(suggestion);
                }
            });
            add(chip);
        }

        revalidate();
        repaint();
    }
}
