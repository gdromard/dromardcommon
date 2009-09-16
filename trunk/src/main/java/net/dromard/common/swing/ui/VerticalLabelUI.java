package net.dromard.common.swing.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * <code>
 * JLabel myLabel = new JLabel("some text");
 * myLabel.setUI(new VerticalLabelUI(VerticalLabelUI.Direction.CLOCKWISE));
 * someContainer.add(myLabel);
 * </code>.
 * <p/>
 */
public class VerticalLabelUI extends BasicLabelUI {

    /**
     * Direction in which the label's text will be rotated.
     * <ul>
     * <li>CLOCKWISE</li>
     * <li>COUNTERCLOCKWISE</li>
     * </ul>
     */
    public enum Direction {
        /** The CLOCKWISE. */
        CLOCKWISE,
        /** The COUNTERCLOCKWISE. */
        COUNTERCLOCKWISE
    }

    /**
     *
     * The default <code>BasicLabelUI</code> instance. This field might
     * not be used. To change the default instance use a subclass which
     * overrides the <code>createUI</code> method, and place that class
     * name in defaults table under the key "LabelUI".
     */
    static {
        BasicLabelUI.labelUI = new VerticalLabelUI(Direction.COUNTERCLOCKWISE);
    }

    /* 90° */
    /** The NINETY_DEGREES. */
    private final double ninetyDegrees = Math.PI / 2;

    /* see BasicLabelUI for description of these fields. I kept the same names.*/
    /** The paintIconR. */
    private static Rectangle paintIconR = new Rectangle();
    /** The paintTextR. */
    private static Rectangle paintTextR = new Rectangle();
    /** The paintViewR. */
    private static Rectangle paintViewR = new Rectangle();
    /** The paintViewInsets. */
    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

    /**
     * Direction how text will be rotated (clcokwise or counterclockwise ?
     */
    private final Direction direction;

    /**
     * Create a (look and) feel object to render labels containing vertical
     * text.
     *
     * @param direction
     */
    public VerticalLabelUI(final Direction direction) {
        super();
        this.direction = direction;
    }

    /*
     * the dimension returned are inversed !!!
     * (that's what we expected, so if you want to change this, do it carefully)
     */
    @Override
    public Dimension getPreferredSize(final JComponent c) {
        Dimension dim = super.getPreferredSize(c);
        /* prefered width is computed by the parent UI and we just return it
        parent width is our height and we compute our width using label's
        fontmetrics.
        remember the label is rotated this is why it's turned around.
         */

        FontMetrics fm = c.getFontMetrics(c.getFont());
        int height = fm.getHeight() /*+ fm.getAscent() + fm.getDescent()*/;

        /*todo {check this dim.height: wrong value returned by super.getPreferedSize(c)
                in all likelihood it's depending on the (default) Locale.
         */
        return new Dimension(height, dim.width);
    }

    /**
     * Javadoc copied from BasicLabelUI.paint
     * Paint the label text in the foreground color, if the label
     * is opaque then paint the entire background with the background
     * color.  The Label text is drawn by paintEnabledText() or
     * paintDisabledText().  The locations of the label parts are computed
     * by layoutCL.
     *
     * @see #paintEnabledText
     * @see #paintDisabledText
     * @see #layoutCL
     */
    @Override
    public void paint(final Graphics g, final JComponent c) {

        /* inspired by BasicLabelUI#paint*/

        JLabel label = (JLabel) c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        if ((icon == null) && (text == null)) {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        VerticalLabelUI.paintViewInsets = c.getInsets(VerticalLabelUI.paintViewInsets);

        VerticalLabelUI.paintViewR.y = VerticalLabelUI.paintViewInsets.top;
        VerticalLabelUI.paintViewR.x = VerticalLabelUI.paintViewInsets.left;

        /*
          as the label is rotated, height and width are inversed.
         */
        VerticalLabelUI.paintViewR.height = c.getWidth() - (VerticalLabelUI.paintViewInsets.left + VerticalLabelUI.paintViewInsets.right);
        VerticalLabelUI.paintViewR.width = c.getHeight() - (VerticalLabelUI.paintViewInsets.top + VerticalLabelUI.paintViewInsets.bottom);

        VerticalLabelUI.paintTextR.x = 0;
        VerticalLabelUI.paintTextR.y = 0;
        VerticalLabelUI.paintTextR.width = 0;
        VerticalLabelUI.paintTextR.height = 0;

        VerticalLabelUI.paintIconR.x = 0;
        VerticalLabelUI.paintIconR.y = 0;
        VerticalLabelUI.paintIconR.width = 0;
        VerticalLabelUI.paintIconR.height = 0;

        String clippedText = layoutCL(label, fm, text, icon, VerticalLabelUI.paintViewR, VerticalLabelUI.paintIconR, VerticalLabelUI.paintTextR);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform tr = g2d.getTransform();
        if (direction == Direction.CLOCKWISE) {
            g2d.rotate(ninetyDegrees);
            g2d.translate(0, -c.getWidth());
        } else {
            g2d.rotate(-ninetyDegrees);
            g2d.translate(-c.getHeight(), 0);
        }

        if (icon != null) {
            icon.paintIcon(c, g, VerticalLabelUI.paintIconR.x, VerticalLabelUI.paintIconR.y);
        }

        if (text != null) {
            int textX = VerticalLabelUI.paintTextR.x;
            int textY = VerticalLabelUI.paintTextR.y + fm.getAscent();

            if (label.isEnabled()) {
                paintEnabledText(label, g, clippedText, textX, textY);
            } else {
                paintDisabledText(label, g, clippedText, textX, textY);
            }
        }

        g2d.setTransform(tr);
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame("Vertical Label Test");
                f.setLayout(new GridBagLayout());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel myVerticalLabel = null;
                //                try {
                //Icon icon = new ImageIcon(ImageIO.read(new URL("http://java.developpez.com/img/small_faq_java.gif")));
                myVerticalLabel = new JLabel("This is a litle test of text label painted in vertical", null, SwingConstants.CENTER);
                myVerticalLabel.setBackground(Color.WHITE);
                myVerticalLabel.setFont(new Font("Lucida Sans", Font.PLAIN, 15));
                myVerticalLabel.setUI(new VerticalLabelUI(VerticalLabelUI.Direction.CLOCKWISE));
                f.add(myVerticalLabel);

                myVerticalLabel = new JLabel("This is a litle test of text label painted in vertical", null, SwingConstants.LEFT);
                myVerticalLabel.setBackground(Color.WHITE);
                myVerticalLabel.setFont(new Font("Lucida Sans", Font.PLAIN, 15));
                myVerticalLabel.setUI(new VerticalLabelUI(VerticalLabelUI.Direction.COUNTERCLOCKWISE));
                f.add(myVerticalLabel);
                //                } catch (IOException e) {
                //                    e.printStackTrace();
                //                }

                f.setSize(400, 300);
                f.setVisible(true);
            }
        });
    }
}
