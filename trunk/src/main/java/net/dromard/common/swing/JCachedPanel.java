package net.dromard.common.swing;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * This class is an extention of JPanel that handle cached graphics.
 * It can be used to optimise swing display.
 *
 * <br/>
 * <pre>
 *    +---------+
 *    | History |
 *    +---------+
 *
 *  [11/05/07] by Gabriel Dromard
 *     - Creation.
 * </pre>
 * <br/>
 * @author Gabriel Dromard (gabriel.dromard@libertysurf.fr)
 */
@SuppressWarnings("serial")
public class JCachedPanel extends JPanel {
    private BufferedImage buffer = null;

    /**
     * Construct the cached panel.
     * @param layout The layout to used.
     */
    public JCachedPanel(LayoutManager layout) {
        super(layout);
    }

    /**
     * Override the JPanel.paintChildren() implementation.
     * It paint the childrens only when the cache has to be updated, else the cahe is used instead of asking childrens to paint them self.
     */
    @Override
    protected void paintChildren(Graphics g) {
        if (isCacheDirty()) {
        	System.err.println("[DEBUG] painting childrens ... Refreching cache");
            createCache();
	
	        // paints children in the cache
	        Graphics2D g2 = buffer.createGraphics();
	        copySettings(g, g2);
	        clearCache(g2);
	        super.paintChildren(g2);
	        g2.dispose();
        }

        // draws cache on screen
        g.drawImage(buffer, 0, 0, null);
    }

    /**
     * Copy graphics settings.
     * @param src The source graphics instance from which to copy the settings.
     * @param dst The destination graphics into which to paste the settings.
     */
    private static void copySettings(Graphics src, Graphics dst) {
        dst.setClip(src.getClip());
    }

    /**
     * Clear graphics cache.
     * @param g2 The graphics to be cleared.
     */
    private void clearCache(Graphics2D g2) {
        Composite composite = g2.getComposite();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        g2.setComposite(composite);
    }

    /**
     * Create the cache.
     */
    private void createCache() {
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Says if the cache is dirty.
     * @return true if the cache has to be refreshed.
     */
    private boolean isCacheDirty() {
        return buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight();
    }
}
