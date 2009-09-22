package net.dromard.common.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import net.dromard.common.widget.jform.JForm;
import net.dromard.common.widget.memorymonitor.JMemoryMonitor;

/**
 * This is a J2LProd's JTaskPane like component <a href="http://www.L2FProd.com">see L2FProd</a>.
 * Building task oriented applications
 * Lot of recent applications bring contextual item lists from which you can pick tasks related to 
 * the current selection or context. 
 * The JTaskPane <!--and JTaskPaneGroup--> deliver this feature to java applications.
 * 
 * @author Gabriel Dromard
 */
public class JTaskPane extends JPanel {
    /** The serialVersionUID. */
    private static final long serialVersionUID = -723009677479608179L;
    protected static int BUTTON_WIDTH = 18;
    protected Color borderColor = Color.GRAY;
    protected Color buttonBorderColor = Color.DARK_GRAY.brighter();
    protected Color titleGradientBeginColor = Color.WHITE;
    protected Color titleGradientEndColor = Color.LIGHT_GRAY;
    protected JPanel content;
    protected MyJLabel label;

    @SuppressWarnings("serial")
    public JTaskPane(final String title, final JComponent component) {
        super(new BorderLayout());
        setOpaque(false);
        // Title
        label = new MyJLabel(title);
        label.setBorder(BorderFactory.createLineBorder(borderColor));
        add(label, BorderLayout.NORTH);
        // Content Panel
        content = new JPanel(new BorderLayout()) {
            @Override
            public void paintComponent(final Graphics g) {
                super.paintComponent(g);
                g.setColor(borderColor);
                g.drawRect(0, -1, getWidth() - 1, getHeight());
            }
        };
        content.setBorder(new LineBorder(borderColor) {
            @Override
            public Insets getBorderInsets(final Component c) {
                Insets insets = super.getBorderInsets(c);
                insets.top = insets.top - 1;
                return insets;
            }
        });
        content.add(component, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
        // Hide/show content
        label.setButtonActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                content.setVisible(!content.isVisible());
            }
        });
    }

    @Override
    public void paintComponent(final Graphics g) {
        // Set the background color of the content panel
        content.setBackground(JTaskPane.this.getBackground());
        super.paintComponent(g);
    }

    /**
     * Set the JTaskPane opened or closed
     */
    public void setOpened(final boolean opened) {
        label.button.up = opened;
        content.setVisible(opened);
    }

    /**
     * Is the JTaskPane opened ?
     * @return True if it is opened
     */
    public boolean isOpened() {
        return content.isVisible();
    }

    /**
     * @return the titleGradientBeginColor
     */
    public Color getTitleGradientBeginColor() {
        return titleGradientBeginColor;
    }

    /**
     * @param titleGradientBeginColor the titleGradientBeginColor to set
     */
    public void setTitleGradientBeginColor(final Color titleGradientBeginColor) {
        this.titleGradientBeginColor = titleGradientBeginColor;
    }

    /**
     * @return the titleGradientEndColor
     */
    public Color getTitleGradientEndColor() {
        return titleGradientEndColor;
    }

    /**
     * @param titleGradientEndColor the titleGradientEndColor to set
     */
    public void setTitleGradientEndColor(final Color titleGradientEndColor) {
        this.titleGradientEndColor = titleGradientEndColor;
    }

    /**
     * Title of JTaskPane
     */
    class MyJLabel extends JLabel {
        /** The serialVersionUID. */
        private static final long serialVersionUID = 5020028000388191120L;
        protected MyJButton button;
        protected ActionListener currentActionListener;
        protected String text;

        public MyJLabel(final String title) {
            super(" " + title);
            text = title;
            setPreferredSize(new Dimension(getWidth(), 30));
            button = new MyJButton();
            add(button);
            int yPos = (getHeight() - JTaskPane.BUTTON_WIDTH) / 2;
            int xPos = getWidth() - yPos - JTaskPane.BUTTON_WIDTH;
            button.setBounds(xPos, yPos, JTaskPane.BUTTON_WIDTH, JTaskPane.BUTTON_WIDTH);
        }

        public void setButtonActionListener(final ActionListener action) {
            // Remove action listeners
            button.removeActionListener(currentActionListener);
            button.addActionListener(action);
            currentActionListener = action;
        }

        @Override
        protected void paintComponent(final Graphics g) {
            // Re set position of button
            int yPos = (getHeight() - JTaskPane.BUTTON_WIDTH) / 2;
            int xPos = getWidth() - yPos - JTaskPane.BUTTON_WIDTH;
            button.setBounds(xPos, yPos, JTaskPane.BUTTON_WIDTH, JTaskPane.BUTTON_WIDTH);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            Paint oldPainter = g2.getPaint();
            g2.setPaint(new GradientPaint(0, 0, titleGradientBeginColor, (float) getSize().getWidth(), (float) getSize().getHeight(), titleGradientEndColor));
            //g2.fill(new RoundRectangle2D.Double(0, 0, (double)getWidth(), (double)getHeight(), 12, 12));
            g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
            g2.setPaint(oldPainter);

            super.paintComponent(g);
        }
    }

    /**
     * Button on left of JTaskPane title 
     */
    class MyJButton extends JButton {
        /** The serialVersionUID. */
        private static final long serialVersionUID = -2936011039771596471L;
        /** The up. */
        protected boolean up = true;

        public MyJButton() {
            super();
            setOpaque(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    up = !up;
                }
            });
        }

        @Override
        public void paintComponent(final Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            Paint oldPainter = g2.getPaint();
            g2.setPaint(new GradientPaint(0, 0, titleGradientBeginColor, getWidth() / 2, getHeight() / 2, titleGradientEndColor, true));
            g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
            g2.setColor(buttonBorderColor);
            g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            g2.setColor(Color.DARK_GRAY);
            if (up) {
                paintUpArrows(g2);
            } else {
                paintDownArrows(g2);
            }
            g2.setPaint(oldPainter);
        }

        public void paintUpArrows(final Graphics2D g2) {
            g2.translate(0, -1);
            g2.drawLine(5, getHeight() / 2, getWidth() / 2, 5);
            g2.drawLine(6, getHeight() / 2, getWidth() / 2, 6);
            g2.drawLine(getWidth() / 2, 5, getWidth() - 5, getHeight() / 2);
            g2.drawLine(getWidth() / 2, 6, getWidth() - 6, getHeight() / 2);
            g2.translate(0, +1);

            g2.translate(0, getHeight() / 4 - 1);
            g2.drawLine(5, getHeight() / 2, getWidth() / 2, 5);
            g2.drawLine(6, getHeight() / 2, getWidth() / 2, 6);
            g2.drawLine(getWidth() / 2, 5, getWidth() - 5, getHeight() / 2);
            g2.drawLine(getWidth() / 2, 6, getWidth() - 6, getHeight() / 2);
            g2.translate(0, -getHeight() / 4 + 1);
        }

        public void paintDownArrows(final Graphics2D g2) {
            g2.drawLine(5, getHeight() / 2, getWidth() / 2, getHeight() - 5);
            g2.drawLine(6, getHeight() / 2, getWidth() / 2, getHeight() - 6);
            g2.drawLine(getWidth() / 2, getHeight() - 5, getWidth() - 5, getHeight() / 2);
            g2.drawLine(getWidth() / 2, getHeight() - 6, getWidth() - 6, getHeight() / 2);

            g2.translate(0, -getHeight() / 4);
            g2.drawLine(5, getHeight() / 2, getWidth() / 2, getHeight() - 5);
            g2.drawLine(6, getHeight() / 2, getWidth() / 2, getHeight() - 6);
            g2.drawLine(getWidth() / 2, getHeight() - 5, getWidth() - 5, getHeight() / 2);
            g2.drawLine(getWidth() / 2, getHeight() - 6, getWidth() - 6, getHeight() / 2);
            g2.translate(0, +getHeight() / 4);
        }
    }

    /**
     * Demo main methods that display JTaskPane, JForm, ShadowBorder 
     */
    public static void main(final String[] args) {
        //
        JForm root = new JForm(10, 10);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //root.setBackground(Color.WHITE);
        // Initialize JForm
        JForm form1 = new JForm(5, 5);
        form1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        form1.setBackground(Color.WHITE);

        //form.setOpaque(false);
        for (int i = 0; i < 10; ++i) {
            JTextField txt = new JTextField();
            txt.setPreferredSize(new Dimension(150, (int) txt.getPreferredSize().getHeight()));
            if (i == 3) {
                form1.addLine(new JLabel("label plus long" + i), txt, new JButton("Select" + i));
            } else {
                form1.addLine(new JLabel("label" + i), txt, new JButton("Select" + i));
            }
        }
        JForm form2 = new JForm(5, 5);
        form2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        form2.setBackground(Color.WHITE);
        for (int i = 0; i < 5; ++i) {
            JTextField txt = new JTextField();
            txt.setPreferredSize(new Dimension(150, (int) txt.getPreferredSize().getHeight()));
            if (i >= 3) {
                form2.addLine(new JLabel("label plus long" + i), txt, null);
            } else {
                form2.addLine(new JLabel("label" + i), txt, new JButton("Select" + i));
            }
        }
        // Add form in panel
        JPanel shadowPanel1 = new JShadowPanel(new BorderLayout());
        JPanel myPanel1 = new JTaskPane("JTaskPane containing a JForm", form1);
        shadowPanel1.add(myPanel1, BorderLayout.CENTER);

        // Add form in panel
        JShadowPanel shadowPanel2 = new JShadowPanel(new BorderLayout());
        shadowPanel2.setType(JShadowPanel.TOP_LEFT);
        JTaskPane myPanel2 = new JTaskPane("Same with different options", form2);
        myPanel2.titleGradientBeginColor = Color.LIGHT_GRAY;
        myPanel2.titleGradientEndColor = Color.WHITE;
        shadowPanel2.add(myPanel2, BorderLayout.CENTER);
        //myPanel.setBackground(Color.WHITE);
        root.addLine(null, shadowPanel1, null);
        root.addLine(null, shadowPanel2, null);
        JMemoryMonitor mem = new JMemoryMonitor();
        mem.getComponent().setPreferredSize(new Dimension(200, 200));
        root.addLine(null, mem.getComponent(), null);
        // Cached Text Item
        JCachedPanel panel = new JCachedPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        JTextItem item = new JTextItem("Swing Demo");
        panel.add(item, BorderLayout.NORTH);
        panel.add(root, BorderLayout.CENTER);
        item.setPreferredSize(new Dimension(200, 30));
        // Show frame
        JFrame frame = SwingHelper.openInFrame(panel);
        frame.setSize(frame.getWidth() + 300, frame.getHeight());
        myPanel2.setOpened(false);
        SwingUtilities.updateComponentTreeUI(frame);
    }
}