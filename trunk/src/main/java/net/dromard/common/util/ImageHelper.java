/*
 * Created on 15 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.net.URL;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;

/**
 * @author Gabriel DROMARD
 *
 * TODO Change this comment
 */
public class ImageHelper {
    /**
     * Transform an icon as disabled.
     * @param image The icon to transform
     * @return The gray icon.
     */
    public static final Image toGray(Image image) {
    	if (image == null) return image;
        Image grayImage = GrayFilter.createDisabledImage(image);
        return grayImage;
    }
    
    /**
     * Transform an icon as disabled.
     * @param image The icon to transform
     * @return The gray icon.
     */
    public static final Image toGray(BufferedImage image) {
    	if (image == null) return null;
    	byte[] comp = {0 , -1};
        IndexColorModel cm = new IndexColorModel(2,2,comp,comp,comp);
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, cm);
        Graphics2D g = grayImage.createGraphics();
        g.drawRenderedImage(image, null);
        g.dispose();    	
    	
        //Image grayImage = GrayFilter.createDisabledImage(image);
        return grayImage;
    }
    
    /**
     * This load an Image and will load each pixels in background.
     * <code>if(image.getWidth(null) >= 0) // Then the image has been fully loaded !</code>
     * @param image The image URL to load
     * @return Return an Image object.
     */
	public static final Image loadImageInBackground(URL image) {
	    return Toolkit.getDefaultToolkit().getImage(image);
	}
	
	/**
	 * This method ensures that all pixels have been loaded before returning.
	 * @param image The image URL to load 
	 * @return Return an Image object.
	 */
	public static final Image loadImage(URL image) {
	    return new ImageIcon(image).getImage();	
	}

    /**
     * Resize the image using the given size but keeping proportions.
     * @param image The icon image instance
     * @param ww    The image width
     * @param hh    The image height
     * @return The resized image.
     */
    public static Image scaleImage(Image image, int ww, int hh) {
        // determiner la taille de la vignette.
        int orgW = image.getWidth(null);
        int orgH = image.getHeight(null);
        int w;
        int h;
        int hmargin = 0;
        int vmargin = 0;

        w = ww - hmargin;
        h = w * orgH / orgW;
        if (h > hh - vmargin) {
            h = hh - vmargin;
            w = h * orgW / orgH;
        }
        // No need to scale if size is 0
        if (w == 0 || h == 0) return image;
        // No need to scale if image is already at the wanted size
        if (orgW == w && orgH == h) return image;

        // Draw original image to thumbnail image object and scale it to the new size on-the-fly
        BufferedImage thumbImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(image, 0, 0, w, h, Color.WHITE, null);
        return thumbImage;
    }
}
