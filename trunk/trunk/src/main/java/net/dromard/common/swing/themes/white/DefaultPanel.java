/*
 * Created on 15 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.swing.themes.white;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author Gabriel DROMARD
 *
 * TODO Change this comment
 */
public class DefaultPanel extends JPanel {
	private Image backgroundImage;
	private int hAlign = CENTER;
	private int vAlign = CENTER;
	public static final int CENTER = 0000;
	public static final int LEFT   = 1000;
	public static final int RIGHT  = 0001;
	public static final int TOP    = LEFT;
	public static final int BOTTOM = RIGHT;
	
    public DefaultPanel() {
        super();
        setBackground(DefaultCommon.BACKGROUND);
    }
    
    public DefaultPanel(Image backgroundImage) { this(); setBackgroundImage(backgroundImage); }
    public DefaultPanel(URL backgroundImage) { this(); setBackgroundImage(backgroundImage); }
        
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
	    this.backgroundImage = new ImageIcon(backgroundImage).getImage();
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
