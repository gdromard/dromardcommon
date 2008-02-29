package net.dromard.common.swing;

import java.awt.*;
import java.net.URL;

import javax.swing.*;

import net.dromard.common.util.ImageHelper;


/**
 * @author Gabriel Dromard
 * @version 1.0
 */
public class JImagePanel extends JPanel {
	private Image backgroundImage;
	private int hAlign = CENTER;
	private int vAlign = CENTER;
	public static final int CENTER = 0000;
	public static final int LEFT   = 1000;
	public static final int RIGHT  = 0001;
	public static final int TOP    = LEFT;
	public static final int BOTTOM = RIGHT;
	

    public JImagePanel() { super(); }
    public JImagePanel(Image backgroundImage) { this(); setBackgroundImage(backgroundImage); }
    public JImagePanel(URL backgroundImage) { this(); setBackgroundImage(backgroundImage); }
        
	/**
	 * Recupération de l'image de fond du composant
	 * @return L'image de fond du composant
	 */
	public Image getBackgroundImage() {return backgroundImage;}

	/**
	 * Initialisation de l'image de fond du composant
	 * @param backgroundImage L'image de fond du composant
	 */
	public void setBackgroundImage(Image backgroundImage) {this.backgroundImage = backgroundImage;}

	/**
	 * Initialisation de l'image de fond du composant
	 * @param backgroundImage L'image de fond du composant
	 */
	public void setBackgroundImage(URL backgroundImage) {
	    this.backgroundImage = ImageHelper.loadImage(backgroundImage);
	}

	/**
	 * Redefinition de la methode pour que le composant intégre l'image de fond
	 * @param g Composant graphique
	 */
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
		if(backgroundImage != null) {
			// Centrer l'image
			Dimension paneSize = this.getSize();
			Dimension imageSize = new Dimension(backgroundImage.getWidth(null), backgroundImage.getHeight(null));
		    int width  = (paneSize.width  - imageSize.width)  / 2;
		    int height = (paneSize.height - imageSize.height) / 2;
			
			if(hAlign == LEFT) width = 0;
			else if(hAlign == RIGHT) width = paneSize.width - imageSize.width;
			
			if(vAlign == TOP) height = 0;
			else if(vAlign == BOTTOM) height = paneSize.height - imageSize.height;
			
			g.drawImage(backgroundImage, width, height, this);
		}
	}
    /**
     * @return Returns the hAlign.
     */
    public int getHAlign() {
        return hAlign;
    }
    /**
     * @return Returns the vAlign.
     */
    public int getVAlign() {
        return vAlign;
    }
    /**
     * @param align The hAlign to set.
     */
    public void setHAlign(int align) {
        hAlign = align;
    }
    /**
     * @param align The vAlign to set.
     */
    public void setVAlign(int align) {
        vAlign = align;
    }
}