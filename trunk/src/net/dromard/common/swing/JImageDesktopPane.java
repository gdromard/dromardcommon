package net.dromard.common.swing;

import java.awt.*;
import javax.swing.*;

/**
 * @author Gabriel Dromard
 * @version 1.0
 */
public class JImageDesktopPane extends JDesktopPane {
	private Image backgroundImage;

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
		if(backgroundImage != null) {
			// Centrer l'image
			Dimension paneSize = this.getSize();
			Dimension imageSize = new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));

			g.drawImage(backgroundImage, (paneSize.width - imageSize.width) / 2, (paneSize.height - imageSize.height) / 2, this);
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