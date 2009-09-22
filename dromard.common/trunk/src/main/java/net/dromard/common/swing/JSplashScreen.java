package net.dromard.common.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JWindow;

/**
* A JComponent that handle a splash screen.
* @author Gabriel Dromard
* @version 1.1
*/
public class JSplashScreen extends JWindow implements Runnable {
	private static final long serialVersionUID = 7313196911936234411L;
	private static JSplashScreen splashScreen = new JSplashScreen();

	public static JSplashScreen getSplashScreen() {
		return splashScreen;
	}
	
	/**
	 * Build window with an image.
	 * @param image Bcakground image.
	 */
	private JSplashScreen() {
		super();
		setLayout(new BorderLayout());
	}

	public void setSplashScreenImage(Image image) {
		setSize(image.getWidth(null), image.getHeight(null));
		add(new JImage(image), BorderLayout.CENTER);
	}
	
	/**
	 * Show the splash screen.
	 */	
	public void showSplashScreen() {
		new Thread(this).start();
	}
	
	/**
	 * Hide the splash screen.
	 */	
	public void hideSplashScreen() {
		setVisible(false);
		dispose();
	}
	
	public void run() { 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width/2 - this.getSize().width/2, screenSize.height/2 - this.getSize().height/2);
		this.setVisible(true); 
		this.toFront();
	}
	public void destroy() { this.setVisible(false); }
	public void resume() { this.setVisible(true); }
	public void stop() { this.setVisible(false); }
}
