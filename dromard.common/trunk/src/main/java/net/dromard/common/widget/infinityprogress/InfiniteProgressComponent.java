package net.dromard.common.widget.infinityprogress;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

class InfiniteProgressComponent extends JComponent {
    /** The serialVersionUID. */
    private static final long serialVersionUID = -5609689377743753642L;
    private RenderingHints hints = null;
    private boolean running = false;
    private Area[] ticker = null;
    private int alphaLevel = 0;
    private final int rampDelay = 300;
    private final int barsCount = 14;
    private final float fps = 15.0f;
    protected double primitiveWidth = 0;
    private Thread animation;
    private Point2D.Double center = null;

    public InfiniteProgressComponent() {
        super();
        hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        setOpaque(false);
    }

    public void setWidth(final int width) {
        primitiveWidth = width / 16;
        setPreferredSize(new Dimension(width, width));
    }

    public void start() {
        setVisible(true);
        buildTicker();
        animation = new Thread(new Animator());
        animation.start();
    }

    public void stop() {
        running = false;
        setVisible(false);
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (running) {
            double maxY = 0.0;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(hints);

            for (int i = 0; i < ticker.length; i++) {
                int channel = 224 - 128 / (i + 1);
                g2.setColor(new Color(channel, channel, channel, alphaLevel));
                g2.fill(ticker[i]);

                Rectangle2D bounds = ticker[i].getBounds2D();
                if (bounds.getMaxY() > maxY) {
                    maxY = bounds.getMaxY();
                }
            }
        }
    }

    /**
     * @return The width of primitive
     */
    private double getPrimitiveWidth() {
        // If not defined -> Fill entire panel
        if (primitiveWidth <= 0) {
            primitiveWidth = Math.min((double) getWidth() / 16, getHeight()) / 16;
            if (primitiveWidth < 0) {
                primitiveWidth = 30 / 16;
            }
        }
        return primitiveWidth;
    }

    /**
     * @return The length of primitive
     */
    private double getPrimitiveLength() {
        return getPrimitiveWidth() * 2.7;
    }

    private void buildTicker() {
        if (getWidth() <= 1) {
            setSize(getParent().getSize());
        }
        Area[] ticker = new Area[barsCount];
        center = new Point2D.Double((double) getWidth() / 2, (double) getHeight() / 2);
        double fixedAngle = 2.0 * Math.PI / (barsCount);

        for (double i = 0.0; i < barsCount; i++) {
            Area primitive = buildPrimitive();

            AffineTransform toCenter = AffineTransform.getTranslateInstance(center.getX(), center.getY());
            AffineTransform toBorder = AffineTransform.getTranslateInstance(getPrimitiveLength(), -getPrimitiveWidth() / 2);
            AffineTransform toCircle = AffineTransform.getRotateInstance(-i * fixedAngle, center.getX(), center.getY());

            AffineTransform toWheel = new AffineTransform();
            toWheel.concatenate(toCenter);
            toWheel.concatenate(toBorder);

            primitive.transform(toWheel);
            primitive.transform(toCircle);

            ticker[(int) i] = primitive;
        }
        this.ticker = ticker;
    }

    private Area buildPrimitive() {
        Rectangle2D.Double body = new Rectangle2D.Double(getPrimitiveWidth() / 2, 0, getPrimitiveLength(), getPrimitiveWidth());
        Ellipse2D.Double head = new Ellipse2D.Double(0, 0, getPrimitiveWidth(), getPrimitiveWidth());
        Ellipse2D.Double tail = new Ellipse2D.Double(getPrimitiveLength(), 0, getPrimitiveWidth(), getPrimitiveWidth());

        Area tick = new Area(body);
        tick.add(new Area(head));
        tick.add(new Area(tail));

        return tick;
    }

    private class Animator implements Runnable {
        protected Animator() {
        }

        public void run() {
            double fixedIncrement = 2.0 * Math.PI / (barsCount);
            AffineTransform toCircle = AffineTransform.getRotateInstance(fixedIncrement, center.getX(), center.getY());
            long start = System.currentTimeMillis();
            alphaLevel = 0;

            running = true;
            boolean inRamp = true;

            while (running) {
                if (!inRamp) {
                    Point2D.Double newCenter = new Point2D.Double((double) getWidth() / 2, (double) getHeight() / 2);
                    if (center.x != newCenter.x || center.y != newCenter.y) {
                        buildTicker();
                        toCircle = AffineTransform.getRotateInstance(fixedIncrement, center.getX(), center.getY());
                    }
                    for (Area element : ticker) {
                        element.transform(toCircle);
                    }
                }

                repaint();

                if (alphaLevel < 255) {
                    alphaLevel = (int) (255 * (System.currentTimeMillis() - start) / rampDelay);
                    if (alphaLevel >= 255) {
                        alphaLevel = 255;
                        inRamp = false;
                    }
                }

                try {
                    Thread.sleep(inRamp ? 10 : (int) (1000 / fps));
                } catch (InterruptedException ie) {
                    break;
                }
                Thread.yield();
            }
        }
    }
}
