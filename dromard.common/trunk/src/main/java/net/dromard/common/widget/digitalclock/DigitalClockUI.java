package net.dromard.common.widget.digitalclock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * The DigitalClock UI.
 * @author Gabriel Dromard
 * 2 oct. 2009
 */
class DigitalClockUI extends BasicLabelUI {

    /**
     * Create a (look and) feel object to render labels containing vertical text.
     * @param direction
     */
    public DigitalClockUI() {
        super();
    }

    /**
     * Javadoc copied from BasicLabelUI.paint
     * Paint the label text in the foreground color, if the label
     * is opaque then paint the entire background with the background
     * color.  The Label text is drawn by paintEnabledText() or
     * paintDisabledText().  The locations of the label parts are computed
     * by layoutCL.
     *
     * @see #paintEnabledText
     * @see #paintDisabledText
     * @see #layoutCL
     */
    @Override
    public void paint(final Graphics g, final JComponent c) {
        JLabel label = (JLabel) c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        if ((icon == null) && (text == null)) {
            return;
        }
        g.setFont(FontHelper.getFont("DS-DIGIT.TTF"));
        DigitalClockUI.deriveFont(g, label);
        FontMetrics fm = g.getFontMetrics();
        Insets paintViewInsets = c.getInsets();

        if (text != null) {
            int textX = paintViewInsets.left;
            int textY = paintViewInsets.top + fm.getAscent();

            int charWidth = g.getFontMetrics().charWidth('8');
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(new Color(230, 230, 230));
            g.drawString("8", textX + charWidth * 0, textY);
            g.drawString("8", textX + charWidth * 1, textY);
            g.drawString(":", textX + charWidth * 2 + (charWidth - g.getFontMetrics().charWidth(':')) / 2, textY);
            g.drawString("8", textX + charWidth * 3, textY);
            g.drawString("8", textX + charWidth * 4, textY);
            g.setColor(Color.BLACK.brighter());
            drawString(g, text, textX, textY, charWidth);
        }
    }

    private void drawString(final Graphics g, final String text, final int textX, final int textY, final int charWidth) {
        if (text != null && text.length() == 5) {
            float f = 1.20f;
            if (charWidth < 15) {
                f = 1f;
            }
            g.drawString(text.substring(0, 1), textX + charWidth * 0 + (int) ((charWidth - g.getFontMetrics().charWidth(text.charAt(0))) * f), textY);
            g.drawString(text.substring(1, 2), textX + charWidth * 1 + (int) ((charWidth - g.getFontMetrics().charWidth(text.charAt(1))) * f), textY);
            g.drawString(text.substring(2, 3), textX + charWidth * 2 + (charWidth - g.getFontMetrics().charWidth(text.charAt(2))) / 2, textY);
            g.drawString(text.substring(3, 4), textX + charWidth * 3 + (int) ((charWidth - g.getFontMetrics().charWidth(text.charAt(3))) * f), textY);
            g.drawString(text.substring(4, 5), textX + charWidth * 4 + (int) ((charWidth - g.getFontMetrics().charWidth(text.charAt(4))) * f), textY);
        }
    }

    private static void deriveFont(final Graphics g, final JLabel label) {
        float size = g.getFont().getSize();
        while (g.getFontMetrics().charWidth('0') * 5 <= label.getWidth() - label.getInsets().left - label.getInsets().right - 2 && g.getFontMetrics().getHeight() <= label.getHeight() - label.getInsets().top - label.getInsets().bottom - 2) {
            size += 1f;
            g.setFont(g.getFont().deriveFont(size));
        }
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame("Digital Label Test");
                f.setLayout(new BorderLayout());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel myVerticalLabel = null;
                myVerticalLabel = new JLabel("12:56", null, SwingConstants.CENTER);
                myVerticalLabel.setBackground(Color.WHITE);
                myVerticalLabel.setUI(new DigitalClockUI());
                f.add(myVerticalLabel, BorderLayout.CENTER);

                f.setSize(400, 300);
                f.setVisible(true);
            }
        });
    }
}
