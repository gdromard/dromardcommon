package net.dromard.common.swing;

// AWT
import java.awt.*;
import java.awt.event.*;
// Swing
import javax.swing.*;

/**
 * This a little implementation of a JPopupMenu
 * @see #javax.swing.JPopupMenu
 * @author Gabriel Dromard
 * @version 1.0
 */
public class MyJPopupMenu extends JPopupMenu {

	public MyJPopupMenu() { super(); }
		
	/**
	 * @param invoker The parent frame
	 * @param menu The popup menu
	 */
	public MyJPopupMenu(Component invoker, JComponent[] menu) {
		super();
		for(int i=0; i<menu.length; ++i) add(menu[i]);
		setInvoker(invoker);
	}
	
	/**
	 * @param menu The popup menu
	 */
	public MyJPopupMenu(JComponent[] menu) {
		super();
		for(int i=0; i<menu.length; ++i) add(menu[i]);
	}
	
	/**
	 * @param invoker
	 */
	public void setInvoker(Component invoker) {
		super.setInvoker(invoker);
		invoker.addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {/**/}
			public void mouseEntered(MouseEvent e) {/**/}
			public void mouseReleased(MouseEvent e) {/**/}
			public void mousePressed(MouseEvent e) {/**/}
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					int x=new Double(e.getX()+getInvoker().getLocationOnScreen().getX()).intValue();
					int y=new Double(e.getY()+getInvoker().getLocationOnScreen().getY()).intValue();
					setLocation(x, y);
					setVisible(true);
				} else {
					setVisible(false);
				}
			}
		});
	}
	
	public void setWaittingCursor() {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	public void setNormalCursor() {
		this.setCursor(Cursor.getDefaultCursor());
	}
}