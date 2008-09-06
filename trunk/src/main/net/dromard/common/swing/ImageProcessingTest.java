package net.dromard.common.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.metal.MetalIconFactory;

/**
 * @author E 2M A
 * 
 */
public class ImageProcessingTest {
	public static void main(String[] args) {
		JFrame frame = new ImageProcessingFrame();
		frame.show();
	}
}

class ImageProcessingFrame extends JFrame implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private ImageProcessingPanel panel;

	private JMenu menuFichier;
	private JMenuItem menuFichierOuvrir;
	private JMenuItem menuFichierQuitter;

	private JMenuBar menuBar;
	private JMenu menuEdition;
	private JMenuItem menuEditionFlou;
	private JMenuItem menuEdidtionNet;
	private JMenuItem menuEditionClair;
	private JMenuItem menuEditionBordure;
	private JMenuItem menuEditionInverser;
	private JMenuItem menuEditionRotation;

	private String nomfichier;

	static final String titreAppli = "Image Processing :: ";

	public ImageProcessingFrame() {
		super(titreAppli + "Version Test");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = getContentPane();

		panel = new ImageProcessingPanel();
		contentPane.add(panel, "Center");

		menuFichier = new JMenu("Fichier");
		menuEdition = new JMenu("Edition");

		menuFichierOuvrir = new JMenuItem("Ouvrir", 'O');
		menuFichierOuvrir.setMnemonic(KeyEvent.VK_O);
		menuFichierOuvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		menuFichierOuvrir.setToolTipText("Ouvrir un fichier de type Jpeg.");
		menuFichierOuvrir.addActionListener(this);
		menuFichier.add(menuFichierOuvrir);

		menuFichierQuitter = new JMenuItem("Quitter", 'Q');
		menuFichierQuitter.setMnemonic(KeyEvent.VK_Q);
		menuFichierQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		menuFichierQuitter.setToolTipText("Quitter l'application.");
		menuFichierQuitter.addActionListener(this);
		menuFichier.add(menuFichierQuitter);

		menuEditionFlou = new JMenuItem("Effet Flou", 'L');
		menuEditionFlou.setMnemonic(KeyEvent.VK_L);
		menuEditionFlou.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));
		menuEditionFlou.setToolTipText("Appliquer un effet de flou à l'image.");
		menuEditionFlou.addActionListener(this);
		menuEdition.add(menuEditionFlou);

		menuEdidtionNet = new JMenuItem("Effet Net", 'N');
		menuEdidtionNet.setMnemonic(KeyEvent.VK_N);
		menuEdidtionNet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		menuEdidtionNet.setToolTipText("Augmenter la netteté de l'image.");
		menuEdidtionNet.addActionListener(this);
		menuEdition.add(menuEdidtionNet);

		menuEditionClair = new JMenuItem("Eclaircir Image", 'E');
		menuEditionClair.setMnemonic(KeyEvent.VK_E);
		menuEditionClair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.CTRL_MASK));
		menuEditionClair.setToolTipText("Eclaicir l'image.");
		menuEditionClair.addActionListener(this);
		menuEdition.add(menuEditionClair);

		menuEditionBordure = new JMenuItem("Effet Détection bordure", 'D');
		menuEditionBordure.setMnemonic(KeyEvent.VK_D);
		menuEditionBordure.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				ActionEvent.CTRL_MASK));
		menuEditionBordure
				.setToolTipText("Effet de détection des bordures de l'image.");
		menuEditionBordure.addActionListener(this);
		menuEdition.add(menuEditionBordure);

		menuEditionInverser = new JMenuItem("Effet Inverser couleurs", 'I');
		menuEditionInverser.setMnemonic(KeyEvent.VK_I);
		menuEditionInverser.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		menuEditionInverser.setToolTipText("Inverser les couleurs de l'image.");
		menuEditionInverser.addActionListener(this);
		menuEdition.add(menuEditionInverser);

		menuEditionRotation = new JMenuItem("Rotation de l'image", 'R');
		menuEditionRotation.setMnemonic(KeyEvent.VK_R);
		menuEditionRotation.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		menuEditionRotation.setToolTipText("Rotation de l'image de 5°.");
		menuEditionRotation.addActionListener(this);
		menuEdition.add(menuEditionRotation);

		menuBar = new JMenuBar();
		menuBar.add(menuFichier);
		menuBar.add(menuEdition);
		setJMenuBar(menuBar);
	}

	private void browse() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));

		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				String name = f.getName().toLowerCase();
				return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".png") || f.isDirectory();
			}

			public String getDescription() {
				return "Image files";
			}
		});
		chooser.setFileView(new ThumbNailFileView(this));
		int resultat = chooser.showOpenDialog(this);
		if (resultat == JFileChooser.APPROVE_OPTION) {
			nomfichier = chooser.getSelectedFile().getAbsolutePath();
			panel.loadImage(nomfichier);
			panel.tailleReelle();
			setTitle(titreAppli + nomfichier);
			setSize(panel.getwidthImage(), panel.getHeightImage() + menuBar.getHeight());
			panel.repaint();
			setResizable(false);
		}
		if (resultat == JFileChooser.CANCEL_OPTION) {
			String message = "Vous avez annulé l'ouverture d'un fichier de type Jpeg.";
			String Titre = "Image Processing :: Erreur";
			JOptionPane.showMessageDialog(new JFrame(), message, Titre, JOptionPane.WARNING_MESSAGE);
		}
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == menuFichierOuvrir) {
			browse();
		} else if (source == menuFichierQuitter) {
			System.exit(0);
		} else if (source == menuEditionFlou) {
			if (panel.imageLoaded() == true)
				panel.blur();
			else
				browse();
		} else if (source == menuEdidtionNet) {
			if (panel.imageLoaded() == true)
				panel.sharpen();
			else
				browse();
		} else if (source == menuEditionClair) {
			if (panel.imageLoaded() == true)
				panel.brighten();
			else
				browse();
		} else if (source == menuEditionBordure) {
			if (panel.imageLoaded() == true)
				panel.edgeDetect();
			else
				browse();
		} else if (source == menuEditionInverser) {
			if (panel.imageLoaded() == true)
				panel.negative();
			else
				browse();
		} else if (source == menuEditionRotation) {
			if (panel.imageLoaded() == true)
				panel.rotate();
			else
				browse();
		}
	}
}

class ImageProcessingPanel extends JPanel {

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets insets = getInsets();
		if (image != null) g.drawImage(image, insets.left, insets.top, this);
	}

	public void update(Graphics g) {
		paintComponent(g);
	}

	public int getHeightImage() {
		int hauteur = image.getHeight(this);
		if (hauteur == -1) return hauteur;

		addNotify();
		return hauteur;
	}

	public int getwidthImage() {
		int largeur = image.getWidth(this);
		if (largeur == -1) return largeur;

		addNotify();
		return largeur;
	}

	public void tailleReelle() {
		int largeurReelle = image.getHeight(this);
		int hauteurReelle = image.getWidth(this);

		if (largeurReelle == -1 || hauteurReelle == -1) return;

		addNotify();
		System.out.println("Largeur image: " + largeurReelle);
		System.out.println("Hauteur image: " + hauteurReelle);
	}

	public void loadImage(String name) {
		Image loadedImage = Toolkit.getDefaultToolkit().getImage(name);

		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(loadedImage, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this, "Erreur:\n" + e.getMessage(), "Image Processing :: Erreur", JOptionPane.ERROR_MESSAGE);
		}
		image = new BufferedImage(loadedImage.getWidth(null), loadedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(loadedImage, 0, 0, null);
		repaint();
	}

	public boolean imageLoaded() {
		return (image != null);
	}

	private void filter(BufferedImageOp op) {
		BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		op.filter(image, filteredImage);
		image = filteredImage;
		repaint();
	}

	private void convolve(float[] elements) {
		Kernel kernel = new Kernel(3, 3, elements);
		ConvolveOp op = new ConvolveOp(kernel);
		filter(op);
	}

	public void blur() {
		float weight = 1.0f / 9.0f;
		float[] elements = new float[9];
		for (int i = 0; i < 9; i++) elements[i] = weight;
		convolve(elements);
	}

	public void sharpen() {
		float[] elements = { 0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f };
		convolve(elements);
	}

	void edgeDetect() {
		float[] elements = { 0.0f, -1.0f, 0.0f, -1.0f, 4.0f, -1.0f, 0.0f, -1.0f, 0.0f };
		convolve(elements);
	}

	public void brighten() {
		float a = 1.5f;
		float b = -20.0f;
		RescaleOp op = new RescaleOp(a, b, null);
		filter(op);
	}

	void negative() {
		byte negative[] = new byte[256];
		for (int i = 0; i < 256; i++)
			negative[i] = (byte) (255 - i);
		ByteLookupTable table = new ByteLookupTable(0, negative);
		LookupOp op = new LookupOp(table, null);
		filter(op);
	}

	void rotate() {
		AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(5), image.getWidth() / 2, image.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		filter(op);
	}

	private BufferedImage image;
}

// ThumbNailFileView.java
// A simple implementation of the FileView class that provides a 16x16 image of
// each GIF or JPG file for its icon. This could be SLOW for large images, as we
// simply load the real image and then scale it.
//
class ThumbNailFileView extends FileView {
	private Icon fileIcon = MetalIconFactory.getTreeLeafIcon();
	private Icon folderIcon = MetalIconFactory.getTreeFolderIcon();
	private Component observer;

	public ThumbNailFileView(Component c) {
		// We need a component around to create our icon's image
		observer = c;
	}

	public String getDescription(File f) {
		// We won't store individual descriptions, so just return the
		// type description.
		return getTypeDescription(f);
	}

	public Icon getIcon(File f) {
		// Is it a folder?
		if (f.isDirectory()) {
			return folderIcon;
		}

		// Ok, it's a file, so return a custom icon if it's an image file
		String name = f.getName().toLowerCase();
		if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".png")) {
			return new Icon16(f.getAbsolutePath());
		}

		// Return the generic file icon if it's not
		return fileIcon;
	}

	public String getName(File f) {
		String name = f.getName();
		return name.equals("") ? f.getPath() : name;
	}

	public String getTypeDescription(File f) {
		String name = f.getName().toLowerCase();
		if (f.isDirectory()) {
			return "Répertoire";
		}
		if (name.endsWith(".jpg")) {
			return "Image de type Jpeg";
		}

		return "Fichier Générique";
	}

	public Boolean isTraversable(File f) {
		// We'll mark all directories as traversable
		return f.isDirectory() ? Boolean.TRUE : Boolean.FALSE;
	}

	public class Icon16 extends ImageIcon {
		private static final int iconLength = 16;
		private static final long serialVersionUID = 1L;

		public Icon16(String f) {
			super(f);
			Image i = observer.createImage(iconLength, iconLength);
			i.getGraphics().drawImage(getImage(), 0, 0, iconLength, iconLength, observer);
			setImage(i);
		}

		public int getIconHeight() {
			return iconLength;
		}

		public int getIconWidth() {
			return iconLength;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.drawImage(getImage(), x, y, c);
		}
	}
}