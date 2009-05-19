package net.dromard.common.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * @author Gabriel Dromard
 * @version 1.0
 */
public class JImageDesktopPane extends JDesktopPane {
	private Image backgroundImage;
	private boolean scale = false;

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
	 * Redefinition de la methode pour que le composant intégre l'image de fond
	 * @param g Composant graphique
	 */
	public void paintComponent(Graphics g) {
		if (backgroundImage != null) {
			// Centrer l'image
			Dimension paneSize = this.getSize();
			Dimension imageSize = new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
			if (imageSize.width > paneSize.width || imageSize.height > paneSize.height) {
				if (scale) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
					AffineTransform af = new AffineTransform();
					// Get scale to map into container
					double scaleX = (double) getWidth() / (imageSize.width);
					double scaleY = (double) getHeight() / (imageSize.height);
					af.scale(scaleX, scaleY);
					BufferedImage temp = new BufferedImage((int) (imageSize.width*scaleX), (int) (imageSize.height*scaleY), BufferedImage.TYPE_INT_RGB);
					((Graphics2D) temp.getGraphics()).drawImage(backgroundImage, af, null);
					g.drawImage(temp, 0, 0, this);
				} else {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
					AffineTransform af = new AffineTransform();
					// Keep original ratio
					double ratio = (double) imageSize.width / imageSize.height;
					// Get scale to map into container
					double scaleX = (double) getWidth() / (imageSize.width);
					double scaleY = (double) getHeight() / (imageSize.height);
					
					// Scale the AffineTransform so as to paint the wanted image into container
					if (getWidth()*1.0/getHeight()*1.0 > ratio) {
						scaleX = scaleY;
					}
					af.scale(scaleX, scaleX);
	
					BufferedImage temp = new BufferedImage((int) (imageSize.width*scaleX), (int) (imageSize.height*scaleX), BufferedImage.TYPE_INT_RGB);
					((Graphics2D) temp.getGraphics()).drawImage(backgroundImage, af, null);
					g.drawImage(temp, (paneSize.width - temp.getWidth()) / 2, (paneSize.height - temp.getHeight()) / 2, this);
				}
			} else {
				g.drawImage(backgroundImage, (paneSize.width - imageSize.width) / 2, (paneSize.height - imageSize.height) / 2, this);
			}
		}
	}

	/**
	 * Ajout d'un composant au desktop
	 * @param c Composant à ajouter
	 */
	public Component add(Component c, int i) {
		this.setLayout(null);
		if(!(c instanceof JInternalFrame) && this.getComponentCount() <= 0) {
			this.setLayout(new BorderLayout());
		}
		c = super.add(c, i);
		//SwingUtilities.updateComponentTreeUI(this);
		return c;
	}

	/**
	 * Ajout d'un composant
	 * @param c Composant à ajouter
	 * @return Voir java.awt.Component
	 */
	public Component add(Component c) {
		return this.add(c, this.getComponents().length-1);
	}
}