package net.dromard.common.swing.themes.wave;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.plaf.ComponentUI;

import sun.swing.SwingUtilities2;

public class WaveButton extends JButton {
    public WaveButton(String text) {
        super(text);
        getUI().uninstallUI(this);
        ComponentUI ui = WaveButtonUI.createUI(this);
        setUI(ui);
        setBackground(new Color(183, 234, 98));
        putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, new Boolean(true));
    }
}
