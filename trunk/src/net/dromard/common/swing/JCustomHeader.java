package net.dromard.common.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class JCustomHeader extends JPanel {
	private static final long serialVersionUID = -6737583742663400113L;
	private String title;
	private String subTitle;
	private Color startLinesColor;
	private Color endLinesColor;
	private Font subtitleFont;
	private Font titleFont;
	private Color titleColor;
	private Color subtitleColor;
	private Color startBgColor;
	private Color endBgColor;
	private Image backgroundImage;
	private Image titleImage;
	
	public JCustomHeader(String title, String subTitle) {
		super();
		this.title = title;
		this.subTitle = subTitle;
        setFont(Font.decode("Century Gothic-BOLD-60"));
        setSubTitleFont(Font.decode("Century Gothic-BOLD-30"));
		
		setPreferredSize(new Dimension(300, 100));
	}
	
	private Font getSubTitleFont() {
		if (subtitleFont == null) subtitleFont = getFont();
		return subtitleFont;
	}
	
	private Font getTitleFont() {
		if (titleFont == null) titleFont = getFont();
		return titleFont;
	}
	
	private Color getTitleColor() {
		if (titleColor == null) titleColor = getForeground();
		return titleColor;
	}
	
	private Color getSubTitleColor() {
		if (subtitleColor == null) subtitleColor = getForeground();
		return subtitleColor;
	}
	
	public void setSubTitleFont(Font subtitleFont) {
		this.subtitleFont = subtitleFont;
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}
	
	public void setTitleColor(Color titleColor) {
		this.titleColor = titleColor;
	}
	
	public void setSubTitleColor(Color subtitleColor) {
		this.subtitleColor = subtitleColor;
	}

	public void setLinesColors(Color startColor, Color endColor) {
		this.startLinesColor = startColor;
		this.endLinesColor = endColor;
	}

	public void setBackgroundColors(Color startColor, Color endColor) {
		this.startBgColor = startColor;
		this.endBgColor = endColor;
	}
	
	/**
	 * @param backgroundImage the backgroundImage to set
	 */
	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	
	/**
	 * @param titleImage the titleImage to set
	 */
	public void setTitleImage(Image titleImage) {
		this.titleImage = titleImage;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		Insets insets = getInsets();
		int x = 0;
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;
        
        // Draw background
        Paint oldPainter = g2.getPaint();
        if (startBgColor != null && endBgColor != null) {
    		g2.setColor(getBackground());
        	g2.setPaint(new GradientPaint(width, 0, startBgColor, width, height, endBgColor));
    		g2.fillRect(0, 0, width, height);
        }

        // Draw lines
        if (startLinesColor != null && endLinesColor != null) {
			g2.setPaint(new GradientPaint(width/2, height, startLinesColor, width, 0, endLinesColor));
			x = -height;
			while (x < width) {
				g2.drawLine(x, height, x + height, 0);
				x += 10;
			}
        }
        g2.setPaint(oldPainter);

		// Print the text content
        g2.setColor(getTitleColor());
        g2.setFont(getTitleFont());
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.drawString(title, insets.left + 22, insets.top + (height / 2) + fontMetrics.getAscent() / 3);
        
        g2.setColor(getSubTitleColor());
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

        if (backgroundImage != null) {
            // Center image
            Dimension paneSize = this.getSize();
            Dimension imageSize = new Dimension(backgroundImage.getWidth(null), backgroundImage.getHeight(null));
            width  = (paneSize.width  - imageSize.width);
            height = (paneSize.height - imageSize.height);
            
            g2.drawImage(backgroundImage, width, height, this);
        }
        if (titleImage != null) {
            // Centrer l'image
            Dimension paneSize = this.getSize();
            Dimension imageSize = new Dimension(backgroundImage.getWidth(null), backgroundImage.getHeight(null));
            
            g2.drawImage(titleImage, 0, (paneSize.height - imageSize.height), this);
        }
        g2.dispose();
	}
}
