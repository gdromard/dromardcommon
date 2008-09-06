package net.dromard.common.swing.themes.wave;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.*;

import net.dromard.common.swing.SwingHelper;

/**
 * This class describes a higher-contrast Metal Theme.
 *
 * @author Gabriel Dromard
 * @version 1.0
 */
public class WaveTheme extends DefaultMetalTheme {
	/** Look & Feels Classes */
	protected String[] lafClass = {
			"javax.swing.plaf.metal.MetalLookAndFeel",
			"com.sun.java.swing.plaf.mac.MacLookAndFeel",
			"com.sun.java.swing.plaf.motif.MotifLookAndFeel",
			"com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
	};
	
    // methods
    public String getName() { return "Wave Theme"; }

	/**
	 * To print all entries ...
	 * <code>
	 * for(Enumeration enum = table.keys(); enum.hasMoreElements();)
	 * System.out.println(enum.nextElement());
	 * </code>
	 */
	public void addCustomEntriesToTable(UIDefaults table) {
		super.addCustomEntriesToTable(table);
		WaveButtonUI btnUi = new WaveButtonUI();
		//System.out.println("[DEBUG] <addCustomEntriesToTable> "+btnUi.getClass().getName());
		table.put("ButtonUI", btnUi.getClass().getName());
	}
	
	/**
	 * Apply the Wave Theme to a Component
	 */
	public static void apply(Component component) {
	    DefaultMetalTheme theme = new WaveTheme();
		MetalLookAndFeel.setCurrentTheme(theme);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			SwingUtilities.updateComponentTreeUI(component);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public static void main(String[] args) {
		JPanel root = new JPanel();
		root.add(new JButton("Test of Wave UI"));
		WaveTheme.apply(root);
		SwingHelper.openInFrame(root, "WaveButtonUI");
	}
}
