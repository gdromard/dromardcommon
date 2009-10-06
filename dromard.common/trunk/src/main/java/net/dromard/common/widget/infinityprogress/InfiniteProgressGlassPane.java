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
    protected final JLabel infoLabel = new JLabel();

    public InfiniteProgressGlassPane(final JFrame frame) {
        glassPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        progress.setWidth(100);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTH;
        glassPane.add(progress.getComponent(), c);

        progressLabel.setHorizontalAlignment(JLabel.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridy = 1;
        glassPane.add(progressLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.weightx = 0;
        c.weighty = 0;
        c.gridy = 2;
        glassPane.add(infoLabel, c);

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

    public void setInfo(final String text) {
        infoLabel.setText(text);
    }
}
