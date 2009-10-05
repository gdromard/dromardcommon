package net.dromard.common.widget.infinityprogress;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.dromard.common.swing.SwingHelper;

public class InfiniteProgressDemo {

    public static void main(final String[] args) {
        final JFrame demo = new JFrame("Infinite Progress Panel Demo");
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.getContentPane().setLayout(new FlowLayout());
        demo.setSize(new Dimension(250, 300));
        JButton btn = new JButton("Start Infinite progress bar");
        demo.getContentPane().add(btn);
        final InfiniteProgress progress = new InfiniteProgress();
        progress.setWidth(100);
        demo.getContentPane().add(progress.getComponent());
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