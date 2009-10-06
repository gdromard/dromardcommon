package net.dromard.common.widget.infinityprogress;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.dromard.common.swing.SwingHelper;

public class InfiniteProgressGlassPaneDemo {

    public static void main(final String[] args) {
        final JFrame demo = new JFrame("Infinite Progress Panel Demo");
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.getContentPane().setLayout(new FlowLayout());
        demo.setSize(new Dimension(300, 300));
        JButton btn = new JButton("Start Infinite progress bar");
        demo.getContentPane().add(btn);

        final InfiniteProgressGlassPane glassPane = new InfiniteProgressGlassPane(demo);

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                System.out.println("[DEBUG] starting progression");
                glassPane.start();
                glassPane.setInfo("Progress demo info");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            glassPane.setText("Progress text 1/5");
                            Thread.sleep(1000);
                            glassPane.setText("Progress text 2/5");
                            Thread.sleep(1000);
                            glassPane.setText("Progress text 3/5");
                            Thread.sleep(1000);
                            glassPane.setText("Progress text 4/5");
                            Thread.sleep(1000);
                            glassPane.setText("Progress text 5/5");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("[DEBUG] stoping progression");
                        glassPane.stop();
                        demo.repaint();
                    }
                }.start();
            }
        });
        SwingHelper.centerInScreen(demo);
        demo.setVisible(true);
    }
}
