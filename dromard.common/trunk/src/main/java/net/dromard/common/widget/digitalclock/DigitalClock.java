package net.dromard.common.widget.digitalclock;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import net.dromard.common.swing.SwingComponent;

/**
 * A digital clock widget.
 * @author Gabriel Dromard
 * 2 oct. 2009
 */
public class DigitalClock implements SwingComponent<JLabel> {
    private final JLabel clock = new JLabel();
    private boolean running = true;

    public DigitalClock() {
        clock.setUI(new DigitalClockUI());
        clock.setHorizontalAlignment(SwingConstants.CENTER);
        runThread();
    }

    public JLabel getComponent() {
        return clock;
    }

    private void runThread() {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (isRunning()) {
                        String time = DigitalClock.getTime();
                        clock.setText(time);
                        Thread.sleep(1000);
                        clock.setText(time.replace(':', ' '));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void stop() {
        running = false;
    }

    private static String getTime() {
        DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
        return formatter.format(new Date());
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }

    public static void main(final String[] args) {
        final JWindow clockFrame = new JWindow();
        //((JPanel) clockFrame.getContentPane()).setUI(new TransparentUI());
        clockFrame.setAlwaysOnTop(true);
        Container main = clockFrame.getContentPane();
        main.setLayout(new BorderLayout());
        DigitalClock clock = new DigitalClock();
        main.add(clock.getComponent(), BorderLayout.CENTER);
        clockFrame.setSize(150, 60);
        clockFrame.setLocationRelativeTo(null);
        clockFrame.setVisible(true);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point relative = null;

            @Override
            public void mousePressed(final MouseEvent e) {
                if (!e.isConsumed()) {
                    e.consume();
                    relative = e.getPoint();
                }
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                if (!e.isConsumed() && relative != null) {
                    e.consume();
                    clockFrame.setLocation(e.getLocationOnScreen().x - relative.x, e.getLocationOnScreen().y - relative.y);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (!e.isConsumed()) {
                    e.consume();
                    relative = null;
                }
            }
        };
        clockFrame.addMouseListener(mouseAdapter);
        clockFrame.addMouseMotionListener(mouseAdapter);
    }
}
