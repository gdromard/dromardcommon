/*
 * Created on 15 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.swing.themes.white;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * @author Gabriel DROMARD
 *
 * TODO Change this comment
 */
public class DefaultCommon {
    public static final Color BLUE       = new Color(187, 208, 225);
    public static final Color DARK_BLUE  = new Color(30,  49,  116);
    public static final Color LIGHT_BLUE = new Color(215, 226, 243);
    public static final Color ORANGE     = new java.awt.Color(246, 145, 0);
    public static final Color WHITE      = Color.white;
    public static final Color BACKGROUND = WHITE;
    
    public static final Border BORDER_BLUE       = new LineBorder(BLUE, 1);
    public static final Border BORDER_DARK_BLUE  = new LineBorder(DARK_BLUE, 1);
    public static final Border BORDER_LIGHT_BLUE = new LineBorder(LIGHT_BLUE, 1);
    public static final Border BORDER_ORANGE     = new LineBorder(ORANGE, 1);
    
    public static final Border BORDER_GRAY    = new LineBorder(Color.gray);
    public static final Border BORDER_FOCUSED = new LineBorder(DARK_BLUE);
    
    public static final Dimension DIMENSION_FIELDS  = new Dimension(130, 22);
    public static final Dimension DIMENSION_BUTTONS = new Dimension(60, 25);
    public static final Dimension DIMENSION_LABLELS = new Dimension(130, 22);
    
    public static final Font TITLE1_FONT = new java.awt.Font("Century Gothic", 1, 18);
    public static final Dimension TITLE1_DIMENSION = new java.awt.Dimension(260, 30);
}
