package net.dromard.common.swing.themes.white;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.*;

/**
 * This class describes a higher-contrast Metal Theme.
 *
 * @author Gabriel Dromard
 * @version 1.0
 */
public class WhiteBlankTheme extends DefaultMetalTheme {
    public static final Color BLUE       = new Color(187, 208, 225);
    public static final Color DARK_BLUE  = new Color(30,  49,  116);
    public static final Color LIGHT_BLUE = new Color(215, 226, 243);
    public static final Color ORANGE     = new Color(246, 145, 0);
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
    
    // primary colors
    private final ColorUIResource primary1 					= new ColorUIResource(DARK_BLUE);
    private final ColorUIResource primary2 					= new ColorUIResource(BLUE);
    private final ColorUIResource primary3 					= new ColorUIResource(LIGHT_BLUE);
    // secondary colors
    private final ColorUIResource secondary1 				= new ColorUIResource(DARK_BLUE);
    private final ColorUIResource secondary2				= new ColorUIResource(BLUE);
    private final ColorUIResource secondary3 				= new ColorUIResource(WHITE);
    // Other colors
    private final ColorUIResource primaryControlHighlight 	= new ColorUIResource(LIGHT_BLUE);
    private final ColorUIResource controlHighlight 			= new ColorUIResource(LIGHT_BLUE);
    private final ColorUIResource focusColor 				= new ColorUIResource(LIGHT_BLUE);
    private final ColorUIResource textHighlightColor 		= new ColorUIResource(DARK_BLUE);
    private final ColorUIResource highlightedTextColor 		= new ColorUIResource(WHITE);
    
    // methods
    public String getName() { return "White Blank Theme"; }

    protected ColorUIResource getPrimary1() { return primary1; }
    protected ColorUIResource getPrimary2() { return primary2; }
    protected ColorUIResource getPrimary3() { return primary3; }

    protected ColorUIResource getSecondary1() { return secondary1; }
    protected ColorUIResource getSecondary2() { return secondary2; }
    protected ColorUIResource getSecondary3() { return secondary3; }

	public ColorUIResource getPrimaryControlHighlight() { return primaryControlHighlight; }
	public ColorUIResource getControlHighlight() 		{ return controlHighlight; }
	public ColorUIResource getFocusColor() 				{ return focusColor; }

	public ColorUIResource getTextHighlightColor() 		{ return textHighlightColor; }
	public ColorUIResource getHighlightedTextColor() 	{ return highlightedTextColor; }

	/**
	 * To print all entries ...
	 * <code>
	 * for(Enumeration enum = table.keys(); enum.hasMoreElements();)
	 * System.out.println(enum.nextElement());
	 * </code>
	 */
	public void addCustomEntriesToTable(UIDefaults table) {
		super.addCustomEntriesToTable(table);
		table.put( "Button.background", LIGHT_BLUE);
		table.put( "Button.foreground", DARK_BLUE);
		
		table.put( "TextField.background", LIGHT_BLUE);
		table.put( "TextField.foreground", DARK_BLUE);
		table.put( "TextField.border", BORDER_GRAY);
		
		table.put( "PasswordField.background", LIGHT_BLUE);
		table.put( "PasswordField.foreground", DARK_BLUE);
		table.put( "PasswordField.border", BORDER_GRAY);
	}
	
	/**
	 * Apply the White blank Theme to a Component
	 */
	public static void apply(Component component) {
	    DefaultMetalTheme theme = new WhiteBlankTheme();
		MetalLookAndFeel.setCurrentTheme(theme);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"/*UIManager.getLookAndFeel()*/);
			SwingUtilities.updateComponentTreeUI(component);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
