package net.dromard.common.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class JCustomHeader extends JPanel {
	private static final long serialVersionUID = -6737583742663400113L;
	private String title;
	private String subTitle;
	private Color lines;
	private Font font;
	private Font subFont;
	
	public JCustomHeader(String title, String subTitle, Color background, Color foreground, Color lines) {
		super();
		this.title = title;
		this.subTitle = subTitle;
		this.lines = lines;
		setBackground(background);
		setForeground(foreground);
        setFont(Font.decode("Century Gothic-BOLD-60"));
        setSubTitleFont(Font.decode("Century Gothic-BOLD-30"));
		
		setPreferredSize(new Dimension(300, 100));
	}
	
	public Font getSubTitleFont() {
		if (subFont == null) subFont = getFont();
		return subFont;
	}
	
	public Font getTitleFont() {
		if (font == null) font = getFont();
		return font;
	}
	
	public void setSubTitleFont(Font subTitleFont) {
		subFont = subTitleFont;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		Insets insets = getInsets();
		int x = insets.left;
//        int y = insets.top;
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;
        
		Paint oldPainter = g2.getPaint();
		g2.setPaint(new GradientPaint(width/2, height, getBackground(), width, 0, lines));
		//g2.setColor(Color.BLUE);
		x = 0;
		while (x < width) {
			g2.drawLine(x, height, x + height, 0);
			x += 10;
		}
		g2.setPaint(oldPainter);
		
		
		// Print the text content
        g2.setColor(getForeground());
        g2.setFont(getTitleFont());
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.drawString(title, insets.left + 22, insets.top + (height / 2) + fontMetrics.getAscent() / 3);
        
        g2.setFont(getSubTitleFont());
        g2.drawString(subTitle, insets.left + 22, insets.top + (height / 7)*5 + fontMetrics.getAscent() / 3);
        
        /*
		int px = 16;
		g2.setFont(getFont().deriveFont(Font.BOLD, px));
		int len = SwingHelper.getUIStringWidth(this, "Fait Main", g2.getFont());
		while ((len = SwingHelper.getUIStringWidth(this, "Fait Main", g2.getFont())) > height)
			g2.setFont(getFont().deriveFont(Font.BOLD, --px));
		AffineTransform at = new AffineTransform();
	    at.setToRotation(-Math.PI/2.0, height/2, height/2);
	    g2.setTransform(at);
	    //System.out.println(len + " + " + px);
	    g2.drawString("Fait Main", height/2-len, px+2);
        */
        g2.dispose();
	}
}
