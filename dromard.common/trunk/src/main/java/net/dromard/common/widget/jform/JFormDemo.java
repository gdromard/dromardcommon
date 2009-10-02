package net.dromard.common.widget.jform;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import net.dromard.common.swing.SwingHelper;

/**
 * This is a demo of JForm widget.
 * @author Gabriel Dromard
 */
public class JFormDemo {

    public static void main(final String[] args) {
        JLabel middle = new JLabel("First");
        final JForm jform = new JForm(null, middle, null);
        final JButton second = new JButton("second");
        second.setAction(new AbstractAction() {
            /** The serialVersionUID. */
            private static final long serialVersionUID = 4307627539340385011L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                jform.removeLine(second);
            }

            @Override
            public Object getValue(final String key) {
                if (key == "Name") {
                    return "second";
                }
                return super.getValue(key);
            }
        });
        final JButton third = new JButton("third");
        third.setAction(new AbstractAction() {
            /** The serialVersionUID. */
            private static final long serialVersionUID = -5289238573911550836L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                jform.removeLine(third);
            }

            @Override
            public Object getValue(final String key) {
                if (key == "Name") {
                    return "third";
                }
                return super.getValue(key);
            }
        });
        final JButton newOne = new JButton("newOne");
        newOne.setAction(new AbstractAction() {
            /** The serialVersionUID. */
            private static final long serialVersionUID = 8784234375900624541L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JButton lastOne = new JButton("lastOne");
                lastOne.setAction(new AbstractAction() {
                    /** The serialVersionUID. */
                    private static final long serialVersionUID = -8848396514818852634L;

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        jform.removeLine(lastOne);
                    }
                });
                jform.addLine(null, lastOne, null);
                SwingUtilities.updateComponentTreeUI(jform);
            }
        });

        jform.addLine(null, second, null);
        jform.addLine(null, third, null);
        jform.addLine(null, new JLabel("test"), null, true);
        jform.addLine(null, newOne, null);

        SwingHelper.openInFrame(jform, "test");
    }
}
