/*
 * Created on 17 oct. 06
 * By Gabriel DROMARD
 */
package net.dromard.common.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
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

import net.dromard.common.widget.jform.JForm;

public class SwingHelper {

    /**
     * Retreive the string length in pixel.
     * @param component  The parent component on which the string will be draw.
     * @param string     The text.
     * @param stringFont The text font.
     */
    public static int getUIStringWidth(final JComponent component, final String string, final Font stringFont) {
        return component.getFontMetrics(stringFont).stringWidth(string);
    }

    /**
     * Open the given panel in a default frame.
     * @param component The inner component of the frame.
     */
    public static JFrame openInFrame(final JComponent component) {
        return SwingHelper.openInFrame(component, "");
    }

    /**
     * Open the given panel in a default frame.
     * @param component The inner component of the frame.
     * @param title The frame title.
     */
    public static JFrame openInFrame(final JComponent component, final String title) {
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
    public static JWindow openInWindow(final JComponent component) {
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
    public static void centerInScreen(final JFrame frame) {
        try {
            // Since 1.4
            frame.setLocationRelativeTo(null);
        } catch (Exception ex) {
            // Before 1.4
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gs = ge.getDefaultScreenDevice();

            // Get size of screen
            DisplayMode dm = gs.getDisplayMode();
            frame.setLocation(new Point((dm.getWidth() - frame.getWidth()) / 2, (dm.getHeight() - frame.getHeight()) / 2));
        }
    }

    /**
     * Display all environement fonts in a panel.
     * @return The panel containing all labels with all environement fonts
     */
    public static JPanel showAllFont() {
        JForm fonts = new JForm();
        // Get all font family names
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontNames[] = ge.getAvailableFontFamilyNames();

        // Iterate the font family names
        for (String fontName : fontNames) {
            System.out.println(fontName);
            JLabel lbl = new JLabel(fontName);
            Font font = new Font(fontName, Font.BOLD, 14);
            lbl.setFont(font);
            fonts.addLine(null, lbl, null);
        }
        return fonts;
    }

    /**
     * @param components The components to be filled in a FlowLayout container.
     * @return A FlowLayout panel with all given component in.
     */
    public static JPanel buildFlowLayoutPanel(final JComponent... components) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JComponent cpt : components) {
            p.add(cpt);
        }
        return p;
    }

    /**
     * Create an empty component width given size.
     * @param width  Width
     * @param height Height
     * @return The component with the given size.
     */
    public static JComponent createEmptyComponent(final int width, final int height) {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        Dimension size = new Dimension(width, height);
        emptyPanel.setSize(size);
        emptyPanel.setPreferredSize(size);
        emptyPanel.setMinimumSize(size);
        return emptyPanel;
    }

    /**
     * Show all environnement fonts in a frame.
     */
    public static void main(final String[] args) {
        SwingHelper.openInFrame(SwingHelper.showAllFont());
    }
}
