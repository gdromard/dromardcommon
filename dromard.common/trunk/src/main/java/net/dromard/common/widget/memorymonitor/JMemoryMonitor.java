package net.dromard.common.widget.memorymonitor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.dromard.common.swing.SwingComponent;

/**
 * Tracks Memory allocated & used, displayed in graph form.
 */
public class JMemoryMonitor implements SwingComponent {

    /** The serialVersionUID. */
    private static final long serialVersionUID = -347102535481575324L;
    /** The dateStampCB. */
    private static JCheckBox dateStampCB = new JCheckBox("Output Date Stamp");
    /** The jMemoryMonitor. */
    private final JPanel jMemoryMonitor;
    /** The surf. */
    private final Surface surf;
    /** The controls. */
    private final JPanel controls;
    /** The doControls. */
    private boolean doControls;
    /** The tf. */
    private final JTextField tf;

    public JMemoryMonitor() {
        jMemoryMonitor = new JPanel();
        jMemoryMonitor.setLayout(new BorderLayout());
        jMemoryMonitor.setBorder(new TitledBorder(new EtchedBorder(), "Memory Monitor"));
        surf = new Surface();
        jMemoryMonitor.add(surf);
        controls = new JPanel();
        //controls.setPreferredSize(new Dimension(135, 80));
        Font font = new Font("serif", Font.PLAIN, 10);
        JLabel label = new JLabel("Sample Rate");
        label.setFont(font);
        label.setForeground(Color.black);
        controls.add(label);
        tf = new JTextField("1000");
        tf.setPreferredSize(new Dimension(45, 20));
        controls.add(tf);
        label = new JLabel("ms");
        controls.add(label);
        label.setFont(font);
        label.setForeground(Color.black);
        controls.add(JMemoryMonitor.dateStampCB);
        JButton gc = new JButton(new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                System.gc();
            }
        });
        gc.setFont(font);
        gc.setText("Force GC");
        controls.add(gc);

        JMemoryMonitor.dateStampCB.setFont(font);
        jMemoryMonitor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                jMemoryMonitor.removeAll();
                doControls = !doControls;
                if (doControls) {
                    surf.stop();
                    jMemoryMonitor.add(controls);
                } else {
                    try {
                        surf.sleepAmount = Long.parseLong(tf.getText().trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    surf.start();
                    jMemoryMonitor.add(surf);
                }
                jMemoryMonitor.validate();
                jMemoryMonitor.repaint();
            }
        });
    }

    public void start() {
        surf.start();
    }

    public void stop() {
        surf.stop();
    }

    /**
     * TODO Comment here.
     * @author Gabriel Dromard
     * 30 juil. 2009
     */
    private class Surface extends JPanel implements Runnable {

        /** The serialVersionUID. */
        private static final long serialVersionUID = -4298072680026629003L;
        /** The thread. */
        private Thread thread;
        /** The sleepAmount. */
        private long sleepAmount = 1000;
        /** The w. */
        private int w, h;
        /** The bimg. */
        private BufferedImage bimg;
        /** The big. */
        private Graphics2D big;
        /** The font. */
        private final Font font = new Font("Times New Roman", Font.PLAIN, 11);

        /** The columnInc. */
        private int columnInc;
        /** The pts. */
        private int[] pts;
        /** The ptNum. */
        private int ptNum;
        /** The ascent. */
        private int ascent, descent;
        // private float freeMemory, totalMemory;
        // private Rectangle graphOutlineRect = new Rectangle();
        /** The mfRect. */
        private final Rectangle2D mfRect = new Rectangle2D.Float();
        /** The muRect. */
        private final Rectangle2D muRect = new Rectangle2D.Float();
        /** The graphLine. */
        private final Line2D graphLine = new Line2D.Float();
        /** The graphColor. */
        private final Color graphColor = new Color(46, 139, 87);
        /** The mfColor. */
        private final Color mfColor = new Color(0, 100, 0);
        /** The usedStr. */
        private String usedStr;

        public Surface() {
            setBackground(Color.black);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (thread == null) {
                        start();
                    } else {
                        stop();
                }
                }
            });
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(135, 80);
        }

        @Override
        public void paint(final Graphics g) {

            if (big == null) {
                return;
            }

            big.setBackground(getBackground());
            big.clearRect(0, 0, w, h);

            //float freeMemory = Runtime.getRuntime().freeMemory();
            float allocatedMemory = Runtime.getRuntime().totalMemory();
            float maxMemory = Runtime.getRuntime().maxMemory();
            float usedMemory = allocatedMemory - Runtime.getRuntime().freeMemory();
            float freeMemory = maxMemory - usedMemory;

            // .. Draw allocated and used strings ..
            big.setColor(Color.green);
            big.drawString(String.valueOf((int) maxMemory / 1024) + "Ko", 4.0f, ascent + 0.5f);
            usedStr = (((int) usedMemory) / 1024) + "Ko used";
            big.drawString(usedStr, 4, h - descent);

            // Calculate remaining size
            float ssH = ascent + descent;
            float remainingHeight = (h - (ssH * 2) - 0.5f);
            int nbBlocks = 20;
            float blockHeight = remainingHeight / nbBlocks;
            float blockWidth = 20.0f;
            // float remainingWidth = (w - blockWidth - 10);

            // .. Memory Free ..
            big.setColor(mfColor);
            int freeMem = (int) (((freeMemory - allocatedMemory) / maxMemory) * nbBlocks * 2);
            int i = 0;
            for (; i < freeMem; i++) {
                mfRect.setRect(5, ssH + i * blockHeight / 2, blockWidth, blockHeight / 2 - 1);
                big.fill(mfRect);
            }

            // .. Memory Allocated ..
            big.setColor(mfColor.brighter());
            int memUsage = (int) ((freeMemory / maxMemory) * nbBlocks * 2);
            for (; i < memUsage; i++) {
                mfRect.setRect(5, ssH + i * blockHeight / 2, blockWidth, blockHeight / 2 - 1);
                big.fill(mfRect);
            }

            // .. Memory Used ..
            big.setColor(Color.green);
            for (; i < nbBlocks * 2; i++) {
                muRect.setRect(5, ssH + i * blockHeight / 2, blockWidth, blockHeight / 2 - 1);
                big.fill(muRect);
            }

            // .. Draw History Lines ..
            big.setColor(graphColor);
            int graphX = 30;
            int graphY = (int) ssH;
            int graphW = w - graphX - 5;
            int graphH = (int) remainingHeight;
            for (i = 0; i < nbBlocks; i++) {
                muRect.setRect(graphX, graphY + i * blockHeight - 0.5f, graphW, blockHeight);
                big.draw(muRect);
            }

            // .. Draw History columns ..
            int graphColumn = graphW / 15;

            if (columnInc == 0) {
                columnInc = graphColumn;
            }
            for (int j = graphX + columnInc; j < graphW + graphX; j += graphColumn) {
                graphLine.setLine(j, graphY, j, graphY + graphH);
                big.draw(graphLine);
            }

            --columnInc;

            // .. Compute history memory usage graph ..
            if (pts == null) {
                pts = new int[graphW];
                ptNum = 0;
            } else if (pts.length != graphW) {
                int[] tmp = null;
                if (ptNum < graphW) {
                    tmp = new int[ptNum];
                    System.arraycopy(pts, 0, tmp, 0, tmp.length);
                } else {
                    tmp = new int[graphW];
                    System.arraycopy(pts, pts.length - tmp.length, tmp, 0, tmp.length);
                    ptNum = tmp.length - 2;
                }
                pts = new int[graphW];
                System.arraycopy(tmp, 0, pts, 0, tmp.length);
            } else {
                // .. Draw history memory usage graph ..
                big.setColor(Color.yellow);
                pts[ptNum] = (int) (graphY + graphH * (freeMemory / maxMemory));
                for (int j = graphX + graphW - ptNum, k = 0; k < ptNum; k++, j++) {
                    if (k != 0) {
                        if (pts[k] != pts[k - 1]) {
                            big.drawLine(j - 1, pts[k - 1], j, pts[k]);
                        } else {
                            big.fillRect(j, pts[k], 1, 1);
                        }
                    }
                }
                if (ptNum + 2 == pts.length) {
                    // throw out oldest point
                    for (int j = 1; j < ptNum; j++) {
                        pts[j - 1] = pts[j];
                    }
                    --ptNum;
                } else {
                    ptNum++;
                }
            }

            g.drawImage(bimg, 0, 0, this);
        }

        public void start() {
            thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setName("MemoryMonitor");
            thread.start();
        }

        public synchronized void stop() {
            thread = null;
            notify();
        }

        public void run() {

            Thread me = Thread.currentThread();

            while (thread == me && !isShowing() || getSize().width == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return;
                }
            }

            while (thread == me && isShowing()) {
                Dimension d = getSize();
                if (d.width != w || d.height != h) {
                    w = d.width;
                    h = d.height;
                    bimg = (BufferedImage) createImage(w, h);
                    big = bimg.createGraphics();
                    big.setFont(font);
                    FontMetrics fm = big.getFontMetrics(font);
                    ascent = fm.getAscent();
                    descent = fm.getDescent();
                }
                repaint();
                try {
                    Thread.sleep(sleepAmount);
                } catch (InterruptedException e) {
                    break;
                }
                if (JMemoryMonitor.dateStampCB.isSelected()) {
                    System.out.println(new Date().toString() + " " + usedStr + " / " + (Runtime.getRuntime().totalMemory() / 1024) + " Free mem: " + (Runtime.getRuntime().maxMemory() / 1024));
                }
            }
            thread = null;
        }
    }

    @Override
    public JComponent getComponent() {
        return jMemoryMonitor;
    }

    public static void main(final String[] s) {
        final JMemoryMonitor demo = new JMemoryMonitor();
        WindowListener l = new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowDeiconified(final WindowEvent e) {
                demo.surf.start();
            }

            @Override
            public void windowIconified(final WindowEvent e) {
                demo.surf.stop();
            }
        };
        JFrame f = new JFrame("Memory Monitor");
        f.addWindowListener(l);
        f.getContentPane().add("Center", demo.getComponent());
        f.pack();
        f.setSize(new Dimension(200, 200));
        f.setVisible(true);
        demo.surf.start();
    }
}