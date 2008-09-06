/*
 * Created on 17 oct. 06
 * By Gabriel DROMARD
 */
package net.dromard.common.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class SwingHelper {
    

    /**
     * Retreive the string length in pixel.
     * @param component  The parent component on which the string will be draw.
     * @param string     The text.
     * @param stringFont The text font.
     */
    public static int getUIStringWidth(JComponent component, String string, Font stringFont) {
        return component.getFontMetrics(stringFont).stringWidth(string);
    }

    /**
     * Open the given panel in a default frame.
     * @param component The inner component of the frame.
     */
    public static JFrame openInFrame(JComponent component) {
    	return openInFrame(component, "");
    }
    
    /**
     * Open the given panel in a default frame.
     * @param component The inner component of the frame.
     * @param title The frame title.
     */
    public static JFrame openInFrame(JComponent component, String title) {
        final JFrame frame = new JFrame(title);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
        });
        return frame;
    }
    
    /**
     * Open the given panel in a default window.
     * @param component The inner component of the window.
     */
    public static JWindow openInWindow(JComponent component) {
        final JWindow window = new JWindow();
        window.getContentPane().setBackground(Color.WHITE);
        window.getContentPane().setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        window.getContentPane().add(scrollPane, BorderLayout.CENTER);
        window.pack();
        window.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                window.setVisible(true);
            }
        });        
        return window;
    }
    
    /**
     * Center the given frame at center of screen.
     * @param frame The frame to be centered.
     */
    public static void centerInScreen(JFrame frame) {
        try {
            // Since 1.4
            frame.setLocationRelativeTo(null);
        } catch(Exception ex) {
            // Before 1.4
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            
            // Get size of screen
            DisplayMode dm = gs.getDisplayMode();
            frame.setLocation(new Point((dm.getWidth()-frame.getWidth())/2, (dm.getHeight()-frame.getHeight())/2));
        }
    }
    
    /**
     * Display all environnement fonts in a panel.
     * @return The panel containing all labels with all environnement fonts
     */
    public static JPanel showAllFont() {
    	JForm fonts = new JForm();
        // Get all font family names
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontNames[] = ge.getAvailableFontFamilyNames();
        
        // Iterate the font family names
        for (int i=0; i<fontNames.length; i++) {
        	System.out.println(fontNames[i]);
        	JLabel lbl = new JLabel(fontNames[i]);
        	Font font = new Font(fontNames[i], Font.BOLD, 14);
        	lbl.setFont(font);
        	fonts.addLine(null, lbl, null);
        }
        return fonts;
    }
    
    /**
     * Show all environnement fonts in a frame.
     */
    public static void main(String[] args) {
		openInFrame(showAllFont());
	}
}
