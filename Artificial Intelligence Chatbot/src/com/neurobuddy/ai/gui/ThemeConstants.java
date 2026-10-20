package com.neurobuddy.ai.gui;

import java.awt.Color;
import java.awt.Font;

/**
 * Visual design constants and color palette for NeuroBuddy AI.
 * Employs a dark navy and purple gradient-inspired modern UI aesthetic.
 */
public final class ThemeConstants {

    private ThemeConstants() {}

    // Backgrounds
    public static final Color WINDOW_BG = new Color(0x0F, 0x14, 0x22);
    public static final Color SIDEBAR_BG = new Color(0x13, 0x18, 0x2B);
    public static final Color SIDEBAR_CARD = new Color(0x1C, 0x23, 0x3D);
    public static final Color SIDEBAR_CARD_BORDER = new Color(0x2E, 0x38, 0x5C);
    public static final Color CHAT_BG = new Color(0x0B, 0x0F, 0x19);

    // Message Bubbles
    public static final Color USER_BUBBLE_TOP = new Color(0x8B, 0x5C, 0xF6);
    public static final Color USER_BUBBLE_BOTTOM = new Color(0x6D, 0x28, 0xD9);
    public static final Color BOT_BUBBLE_BG = new Color(0x19, 0x22, 0x36);
    public static final Color BOT_BUBBLE_BORDER = new Color(0x28, 0x36, 0x54);

    // Accent Colors
    public static final Color PURPLE_PRIMARY = new Color(0x7C, 0x3A, 0xED);
    public static final Color PURPLE_ACCENT = new Color(0x8B, 0x5C, 0xF6);
    public static final Color PURPLE_HOVER = new Color(0x6D, 0x28, 0xD9);
    public static final Color PURPLE_LIGHT = new Color(0xC4, 0xB5, 0xFD);
    public static final Color PURPLE_PILL_BG = new Color(0x3B, 0x1E, 0x6D);
    public static final Color ACCENT_CYAN = new Color(0x06, 0xB6, 0xD4);
    public static final Color SUCCESS_GREEN = new Color(0x10, 0xB9, 0x81);
    public static final Color WARNING_AMBER = new Color(0xF5, 0x9E, 0x0B);

    // Typography Colors
    public static final Color TEXT_PRIMARY = new Color(0xF8, 0xFA, 0xFC);
    public static final Color TEXT_SECONDARY = new Color(0x94, 0xA3, 0xB8);
    public static final Color TEXT_MUTED = new Color(0x64, 0x74, 0x8B);
    public static final Color TEXT_ON_PURPLE = new Color(0xFF, 0xFF, 0xFF);

    // Input & Interactive
    public static final Color INPUT_BG = new Color(0x1A, 0x22, 0x38);
    public static final Color INPUT_BORDER = new Color(0x33, 0x41, 0x65);
    public static final Color INPUT_BORDER_FOCUS = new Color(0x8B, 0x5C, 0xF6);
    public static final Color CHIP_BG = new Color(0x20, 0x28, 0x42);
    public static final Color CHIP_BORDER = new Color(0x38, 0x45, 0x6C);
    public static final Color CHIP_HOVER = new Color(0x2D, 0x39, 0x5E);

    // Fonts
    public static final Font FONT_LOGO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_TINY = new Font("Segoe UI", Font.PLAIN, 10);
    public static final Font FONT_CODE = new Font("Consolas", Font.PLAIN, 12);
}
