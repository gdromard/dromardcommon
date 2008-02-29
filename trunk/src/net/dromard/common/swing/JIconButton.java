package net.dromard.common.swing;

// Java
import java.net.*;

import java.awt.*;
import javax.swing.*;

/**
 * Spécialisation d'un Bouton pour qu'il gére les icones
 * @author Gabriel Dromard
 * @version 1.1
 */
public class JIconButton extends JButton {
	private static Dimension buttonSize = new Dimension(30, 30);
	/**
	 * This constructor make a normal JButton and add it for you normal Icon and roll Over Icon
	 * @param action       The action to execute when button is clicked.
	 * @param icon         The URL to the normal icon.
	 * @param rollOverIcon The URL to the roll over icon.
	 */
	public JIconButton(Action action, URL icon, URL rollOverIcon) {
		super(action);
		// Retrait du cadre de selection du bouton
		setFocusPainted(false);
		setContentAreaFilled(true);
		
		if(icon != null) {
			ImageIcon icon1 = new ImageIcon(icon);
			if(icon1 != null) {
				setIcon(icon1);
				if(rollOverIcon != null) {
					ImageIcon icon2 = new ImageIcon(icon);
					if(icon2 != null) setRolloverIcon(icon2);
					else setRolloverIcon(icon1);
				}
			}
		}
		
		setMaximumSize(buttonSize);
		setPreferredSize(buttonSize);
	}

	/**
	 * This constructor make a normal JButton and add it for you normal Icon and roll Over Icon
	 * @param icon         The URL to the normal icon.
	 * @param rollOverIcon The URL to the roll over icon.
	 */
	public JIconButton(URL icon, URL rollOverIcon) {
		this(null, icon, rollOverIcon);
	}
}