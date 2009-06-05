package net.dromard.common.swing.themes.wave;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.plaf.ComponentUI;

import sun.swing.SwingUtilities2;


public class WaveButton extends JButton {
	private static final long serialVersionUID = -1624555226686846386L;

	public WaveButton(String text) {
        super(text);
        getUI().uninstallUI(this);
        ComponentUI ui = WaveButtonUI.createUI(this);
        setUI(ui);
        setBackground(new Color(183, 234, 98));
        putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, new Boolean(true));
    }

	public static void main(String[] args) {
		JFrame f = new JFrame("WaveButton UI Tester");
		f.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		WaveButton test1 = new WaveButton("Test 1");
		WaveButton test2 = new WaveButton("Test 2");
		test2.setEnabled(false);
		f.getContentPane().add(test1);
		f.getContentPane().add(test2);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
