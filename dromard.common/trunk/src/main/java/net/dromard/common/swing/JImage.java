/*
 * Created on 3 mai 07
 * By Gabriel DROMARD
 */
package net.dromard.common.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class JImage extends JPanel implements ImageObserver {
	private static final long serialVersionUID = 6120175092382504085L;
	private Image image;

    public JImage(Image image) {
        super(null);
        this.image = image;
    }

    public JImage(Image image, int hints) {
        super(null);
        this.image = image;
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        if (infoflags == ALLBITS) {
            repaint();
        }
        return true;
    }

    @Override
    public void paintComponent(Graphics g) {
		if(image != null) {
            // Paint parent
            Insets insets = getInsets();

            // Initialize painted image
            int w = getWidth() - insets.left - insets.right;
            int h = getHeight() - insets.top - insets.bottom;
            
            //if (image.getWidth(this) > w || image.getHeight(this) > h) 
            /**
            image = ImageHelper.scaleImage(image, w, h);
			g.drawImage(image, insets.left + (w - image.getWidth(this)) / 2, insets.top + (h - image.getHeight(this)) / 2, this);
			/**/
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            int orgW = image.getWidth(null);
            int orgH = image.getHeight(null);

            int ww = w;
            int hh = ww * orgH / orgW;
            if (hh > h) {
                hh = h;
                ww = hh * orgW / orgH;
            }
            g.drawImage(image, insets.left + (w - ww) / 2, insets.top + (h - hh) / 2, ww, hh, this);
            /**/
		}
    }

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the image original dimension.
	 */
	public Dimension getImageSize() {
		if (image != null) {
			return new Dimension(image.getWidth(this), image.getHeight(this));
		}
		return null;
	}
}
