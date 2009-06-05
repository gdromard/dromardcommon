package net.dromard.common.swing.themes.hud;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import sun.swing.SwingUtilities2;

public class HudButton extends JButton {
    private static final long serialVersionUID = -1624555226686846386L;

    public HudButton(final String text) {
        super(text);
        getUI().uninstallUI(this);
        ComponentUI ui = BasicButtonUI.createUI(this);
        setUI(ui);
        putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, new Boolean(true));
        putClientProperty("JButton.buttonType", "roundRect");
    }

    public static void main(final String[] args) {
        HudWindow window = new HudWindow("HudButton UI Tester");
        JFrame f = window.getJFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        HudButton test1 = new HudButton("Test 1");
        HudButton test2 = new HudButton("Test 2");
        test2.setEnabled(false);
        p.add(test1);
        p.add(test2);
        window.setContentPane(p);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
