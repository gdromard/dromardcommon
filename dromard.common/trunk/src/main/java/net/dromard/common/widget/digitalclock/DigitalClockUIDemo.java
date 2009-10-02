package net.dromard.common.widget.digitalclock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * The DigitalClock UI demo.
 * @author Gabriel Dromard
 * 2 oct. 2009
 */
class DigitalClockUIDemo {
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame("Digital Label Test");
                f.setLayout(new BorderLayout());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel myVerticalLabel = null;
                myVerticalLabel = new JLabel("12:56", null, SwingConstants.CENTER);
                myVerticalLabel.setBackground(Color.WHITE);
                myVerticalLabel.setUI(new DigitalClockUI());
                f.add(myVerticalLabel, BorderLayout.CENTER);

                f.setSize(400, 300);
                f.setVisible(true);
            }
        });
    }
}
