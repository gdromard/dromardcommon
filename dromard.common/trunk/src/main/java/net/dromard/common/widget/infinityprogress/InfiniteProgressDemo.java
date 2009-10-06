package net.dromard.common.widget.infinityprogress;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.dromard.common.swing.SwingHelper;

public class InfiniteProgressDemo {

    public static void main(final String[] args) {
        final JFrame demo = new JFrame("Infinite Progress Panel Demo");
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.getContentPane().setLayout(new GridBagLayout());
        demo.setSize(new Dimension(250, 300));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = c.PAGE_START;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipadx = 0;
        c.ipady = 0;
        c.fill = c.NONE;

        JButton btn = new JButton("Start Infinite progress bar");
        demo.getContentPane().add(btn, c);
        final InfiniteProgress progress = new InfiniteProgress();
        progress.setWidth(100);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridy = 1;
        c.anchor = c.CENTER;
        c.fill = c.NONE;

        demo.getContentPane().add(progress.getComponent(), c);

        c.weightx = 0;
        c.weighty = 0;
        c.gridy = 2;
        c.anchor = c.LAST_LINE_START;
        c.fill = c.HORIZONTAL;
        demo.getContentPane().add(new JLabel("Test"), c);

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                System.out.println("[DEBUG] starting progression");
                progress.start();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("[DEBUG] stoping progression");
                        progress.stop();
                        demo.repaint();
                    }
                }.start();
            }
        });
        SwingHelper.centerInScreen(demo);
        demo.setVisible(true);
    }
}