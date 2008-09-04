/**
 * 
 */
package net.dromard.common.zip.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

abstract class TreeSelectionHandler extends MouseAdapter implements KeyListener {
	
	/* ----------- MouseAdapter ----------- */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		if (e.getClickCount() > 1) {
			this.select(getSelectedNodes((JTree) e.getSource()));
		}
	}
	
	/* ----------- KeyListener ----------- */
	
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.select(getSelectedNodes((JTree) e.getSource()));
		}
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	public List<ZipNode> getSelectedNodes(JTree tree) {
		List<ZipNode> selectedZipEntries = new ArrayList<ZipNode>();
		// Returns the last path element of the selection.
		// This method is useful only when the selection model allows a single selection.
		TreePath[] selectionPaths = tree.getSelectionPaths();
		for (int i = 0; i < selectionPaths.length; i++) {
			ZipNode zipNode = (ZipNode) selectionPaths[i].getLastPathComponent();
			if (zipNode.isLeaf() || zipNode.toString().endsWith(".m2k")) {
				selectedZipEntries.add(zipNode);
			}
		}
		return selectedZipEntries;
	}
	
	/* ----------- Selection ----------- */
	
	public abstract void select(List<ZipNode> selectedZipEntries);
}