package net.dromard.common.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;


public class JTextItem extends JComponent {
    private String text;

    public JTextItem(String text) {
        setText(text);
        setPreferredSize(new Dimension(text.length() * 10, 30));
    }
    
    public void setText(String text) {
    	this.text = text;
		SwingUtilities.updateComponentTreeUI(this);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;
        
        g2.setFont(getFont().deriveFont(Font.BOLD, height / 1.8f));

        // top gradient
        g2.setPaint(new GradientPaint(0.0f, y,
                                      new Color(220, 220, 220, 140),
                                      0.0f, y + height / 2.0f,
                                      new Color(220, 220, 220, 80)));
        g2.fillRect(x, y, width, height / 2);

        // bottom gradient
        g2.setPaint(new GradientPaint(0.0f, y + height / 2.0f + 5.0f,
                                      new Color(255, 255, 255, 0),
                                      0.0f, y + height,
                                      new Color(255, 255, 255, 70)));
        g2.fillRect(x, y + height / 2, width, height / 2);
/*
        // border
        g2.setPaint(Color.LIGHT_GRAY);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(x, y, width, height);
*/
        // text
        g2.setColor(Color.WHITE);
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.drawString(text, x + 10, y + (height / 2) + fontMetrics.getAscent() / 3);

        g2.dispose();
    }
    
    public static void main(String[] args) {
        JCachedPanel panel = new JCachedPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        JTextItem item = new JTextItem("Anti-aliased Text");
        item.setPreferredSize(new Dimension(400, 60));
        panel.add(item);
        SwingHelper.openInFrame(panel);
    }
}
