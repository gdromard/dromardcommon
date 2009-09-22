/*
 * Created on 14 mai 07
 * By Gabriel DROMARD
 */
package net.dromard.common.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.dromard.common.io.FileExtensionFilter;

public class JImages extends JPanel implements Runnable {
    private static final long serialVersionUID = 7573856786476856873L;
    private final HashMap<File, JPanel> images = new HashMap<File, JPanel>();

    public JImages(final File folder) {
        super(new CellFlowLayout(10, 10));
        FileExtensionFilter filter = new FileExtensionFilter();
        filter.addExtension("gif");
        filter.addExtension("jpg");
        filter.addExtension("png");
        filter.addExtension("jpeg");
        File[] files = folder.listFiles(filter);
        // Prepare thumbnails
        for (int i = 0; i < files.length; ++i) {
            JPanel thumbnail = createThumbnail(files[i]);
            add(thumbnail);
            images.put(files[i], thumbnail);
        }
        // Load images in background
        new Thread(this).start();
    }

    /**
     * Create the graphical thumbnail panel.
     * @param file The thumbnail file.
     * @return The graphical panel representing the image.
     */
    private JPanel createThumbnail(final File file) {
        JShadowPanel thumbnail = new JShadowPanel(new BorderLayout());
        thumbnail.setLineBorderColor(Color.GRAY);
        /*
        JPanel thumbnail = new JPanel(new BorderLayout());
        thumbnail.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        /**/
        JLabel fileName = new JLabel(file.getName(), SwingConstants.CENTER);
        //thumbnail.setBorder(new ShadowBorder(Color.BLACK, Color.GRAY, 5, ShadowBorder.BOTTOM_RIGHT));
        thumbnail.setSize(new Dimension(160, 140));
        thumbnail.add(fileName, BorderLayout.SOUTH);
        System.out.println("[DEBUG] Adding image: " + file.getName());
        return thumbnail;
    }

    /**
     * Background images loading. 
     */
    public void run() {
        Iterator<File> it = images.keySet().iterator();
        while (it.hasNext()) {
            Date d = new Date();
            File file = it.next();
            JPanel thumbnail = images.get(file);
            JImage image = new JImage(new ImageIcon(file.getAbsolutePath()).getImage(), Image.SCALE_AREA_AVERAGING);
            //JImage image = new JImage(ImageHelper.toGray((BufferedImage) ImageIO.read(file)), Image.SCALE_AREA_AVERAGING);

            long fileLength = file.length() / 100;
            DateFormat df = DateFormat.getDateInstance();
            String date = df.format(new Date(file.lastModified()));
            image.setToolTipText("<html><center>" + file.getName() + "<br>" + fileLength + " Ko<br>" + date + "</center></html>");

            image.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            //image.setBorder(new ShadowBorder(Color.GRAY));
            thumbnail.add(image, BorderLayout.CENTER);
            thumbnail.revalidate();
            System.out.println("[DEBUG] Loading image: " + file.getName() + " - " + ((new Date()).getTime() - d.getTime()) + "ms");
        }
    }

    public static void main(final String[] args) {
        //SwingHelper.openInFrame(new JImages(new File("C:/Documents and Settings/me/Desktop/Icons")));
        //SwingHelper.openInFrame(new JImages(new File("/Users/me/Pictures/Wallpapers")));
        //SwingHelper.openInFrame(new JImages(new File("C:/Home/me/My Pictures")));
    }
}
