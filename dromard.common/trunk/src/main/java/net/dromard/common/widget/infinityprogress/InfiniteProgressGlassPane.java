package net.dromard.common.widget.infinityprogress;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.dromard.common.swing.SwingPropertiesHelper;
import net.dromard.common.widget.glasspane.GlassPane;

public class InfiniteProgressGlassPane {
    protected final GlassPane glassPane = new GlassPane();
    protected final InfiniteProgress progress = new InfiniteProgress();
    protected final JLabel progressLabel = new JLabel();
    protected final JLabel infoLabel = new JLabel();

    public InfiniteProgressGlassPane(final JFrame frame) {
        progressLabel.setFont(SwingPropertiesHelper.asFont("Century Gothic-BOLD-15"));
        infoLabel.setFont(SwingPropertiesHelper.asFont("Century Gothic-BOLD-12"));
        glassPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        progress.setWidth(100);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.SOUTH;
        glassPane.add(progress.getComponent(), c);

        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
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
        if (!progress.isRunning()) {
            glassPane.setVisible(true);
            progress.start();
        }
    }

    public void stop() {
        if (progress.isRunning()) {
            progress.stop();
            glassPane.setVisible(false);
        }
    }

    public void setPourcentage(final int pourcentage) {
        setText(progressLabel.getText(), pourcentage);
    }

    public void setText(final String text, final int pourcentage) {
        if (text == null) {
            progressLabel.setText(pourcentage + "%");
        } else {
            progressLabel.setText(text + " " + pourcentage + "%");
        }
    }

    public void setText(final String text) {
        if (text == null) {
            progressLabel.setText("");
        } else {
            progressLabel.setText(text);
        }
    }

    public void setInfo(final String text) {
        if (text == null) {
            infoLabel.setText("");
        } else {
            infoLabel.setText(text);
        }
    }
}
