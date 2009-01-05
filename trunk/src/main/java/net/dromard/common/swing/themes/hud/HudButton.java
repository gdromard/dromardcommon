package net.dromard.common.swing.themes.hud;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;

import sun.swing.SwingUtilities2;
import sun.swing.SwingUtilities2.AATextInfo;

public class HudButton extends JButton {
	private static final long serialVersionUID = -1624555226686846386L;

	public HudButton(String text) {
        super(text);
        getUI().uninstallUI(this);
        ComponentUI ui = HudButtonUI.createUI(this);
        setUI(ui);
        putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, AATextInfo.getAATextInfo(true));
        putClientProperty("JButton.buttonType","roundRect");
    }

	public static void main(String[] args) {
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
