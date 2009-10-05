package net.dromard.common.widget.glasspane;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.dromard.common.swing.SwingHelper;
import net.dromard.common.widget.infinityprogress.InfiniteProgress;

public class GlassPaneDemo {

    public static void main(final String[] args) {
        final JFrame demo = new JFrame("Infinite Progress Panel Demo");
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.getContentPane().setLayout(new FlowLayout());
        demo.setSize(new Dimension(300, 300));
        JButton btn = new JButton("Start Infinite progress bar");
        demo.getContentPane().add(btn);

        final GlassPane glassPane = new GlassPane();

        glassPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        final InfiniteProgress progress = new InfiniteProgress();
        progress.setWidth(100);
        glassPane.add(progress.getComponent(), c);
        c.gridy = 1;
        final JLabel progressLabel = new JLabel("Progress Demo");
        glassPane.add(progressLabel, c);
        glassPane.installGlassPane(demo);

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                System.out.println("[DEBUG] starting progression");
                progress.start();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            glassPane.setVisible(true);
                            progressLabel.setText("Progress Demo 1/5");
                            Thread.sleep(1000);
                            progressLabel.setText("Progress Demo 2/5");
                            Thread.sleep(1000);
                            progressLabel.setText("Progress Demo 3/5");
                            Thread.sleep(1000);
                            progressLabel.setText("Progress Demo 4/5");
                            Thread.sleep(1000);
                            progressLabel.setText("Progress Demo 5/5");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("[DEBUG] stoping progression");
                        progress.stop();
                        glassPane.setVisible(false);
                        demo.repaint();
                    }
                }.start();
            }
        });
        SwingHelper.centerInScreen(demo);
        demo.setVisible(true);
    }
}
