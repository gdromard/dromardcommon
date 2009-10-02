package net.dromard.common.widget.digitalclock;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;

import net.dromard.common.swing.mouseadapter.MoveAndResizeMouseListener;

/**
 * The DigitalClock demo.
 * @author Gabriel Dromard
 * 2 oct. 2009
 */
public class DigitalClockDemo {

    public static void main(final String[] args) {
        final JWindow clockFrame = new JWindow();
        //((JPanel) clockFrame.getContentPane()).setUI(new TransparentUI());
        clockFrame.setAlwaysOnTop(true);
        Container main = clockFrame.getContentPane();
        main.setLayout(new BorderLayout());
        final DigitalClock clock = new DigitalClock();
        clock.getComponent().setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        main.add(clock.getComponent(), BorderLayout.CENTER);
        clockFrame.setSize(150, 60);
        clockFrame.setLocationRelativeTo(null);
        clockFrame.setVisible(true);
        MouseAdapter mouseAdapter = new MoveAndResizeMouseListener(clockFrame) {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (!e.isConsumed() && e.getClickCount() >= 2) {
                    e.consume();
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Do you want to close clock ?", "Close clock ?", JOptionPane.YES_NO_OPTION)) {
                        clock.stop();
                        clockFrame.dispose();
                    }
                }
            }
        };
        clockFrame.addMouseListener(mouseAdapter);
        clockFrame.addMouseMotionListener(mouseAdapter);
    }
}
