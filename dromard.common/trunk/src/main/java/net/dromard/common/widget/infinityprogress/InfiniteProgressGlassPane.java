package net.dromard.common.widget.infinityprogress;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import net.dromard.common.widget.glasspane.GlassPane;

public class InfiniteProgressGlassPane {
    protected final GlassPane glassPane = new GlassPane();
    protected final InfiniteProgress progress = new InfiniteProgress();
    protected final JLabel progressLabel = new JLabel();

    public InfiniteProgressGlassPane(final JFrame frame) {
        glassPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        progress.setWidth(100);
        glassPane.add(progress.getComponent(), c);
        c.gridy = 1;
        glassPane.add(progressLabel, c);
        glassPane.installGlassPane(frame);
    }

    public void start() {
        glassPane.setVisible(true);
        progress.start();
    }

    public void stop() {
        progress.stop();
        glassPane.setVisible(false);
    }

    public void setText(final String text) {
        progressLabel.setText(text);
    }
}