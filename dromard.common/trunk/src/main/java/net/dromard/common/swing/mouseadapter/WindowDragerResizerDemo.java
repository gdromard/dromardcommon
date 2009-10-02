package net.dromard.common.swing.mouseadapter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;

/**
 * The MoveAndResizeMouseListener demo.
 * @author Gabriel Dromard
 * 2 oct. 2009
 */
public class WindowDragerResizerDemo {

    public static void main(final String[] args) {
        final JWindow frame = new JWindow();
        frame.setAlwaysOnTop(true);
        JPanel main = (JPanel) frame.getContentPane();
        main.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel clock = new JLabel("<html><center>Movable & Resizable window !!<br>(Double click to close me)</center></html>");
        main.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        main.add(clock, BorderLayout.CENTER);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (!e.isConsumed() && e.getClickCount() >= 2) {
                    e.consume();
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Do you want to close clock ?", "Close clock ?", JOptionPane.YES_NO_OPTION)) {
                        frame.dispose();
                    }
                }
            }
        };
        frame.addMouseListener(mouseAdapter);
        new WindowResizer(frame);
        new WindowDragger(frame, frame);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
