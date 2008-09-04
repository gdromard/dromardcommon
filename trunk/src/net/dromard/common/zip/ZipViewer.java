package net.dromard.common.zip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import net.dromard.common.swing.SwingHelper;
import net.dromard.common.zip.ZipEntryTreeModel.ZipNode;

public class ZipViewer extends JTree {
	
	public ZipViewer(String zipFilePath, TreeSelectionHandler treeHandler) throws IOException {
		setModel(new ZipEntryTreeModel(new ZipFile(zipFilePath)));
		addKeyListener(treeHandler);
		addMouseListener(treeHandler);
	}
	
	public static final void main(String[] args) throws IOException {
		String zipfile = "C:/Documents and Settings/45505230/Mes documents/NDTKit/Cartos/Zip CFF-CSC.zip";
		JTree tree = new ZipViewer(zipfile, new TreeSelectionHandler() {
			@Override
			public void select(List<ZipNode> selectedZipEntries) {
				System.out.println("---------------------");
				for (ZipNode entry : selectedZipEntries) {
					System.out.println(entry.getEntryPath());
				}
			}
		});
		SwingHelper.openInFrame(tree);
	}
}

class ZipEntryTreeModel extends DefaultTreeModel {
	private HashMap<String, ZipNode> nodesByPath = new HashMap<String, ZipNode>();
	
	public ZipEntryTreeModel(ZipFile zipFile) throws IOException {
		super(null);
		load(zipFile.entries());
		zipFile.close();
	}
	
	private void load(Enumeration<? extends ZipEntry> entries) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(null);
		while (entries.hasMoreElements()) {
			new ZipNode(root, entries.nextElement().getName());
		}
		super.setRoot(root);
	}
	class ZipNode extends DefaultMutableTreeNode {
		private String path;
		private String name;
		public ZipNode(final DefaultMutableTreeNode root, final String path) {
			this.path = path;
			int token = path.lastIndexOf("/");
			name = path.substring(token + 1);
			if (token > -1) {
				ZipNode parent = nodesByPath.get(path.substring(0, token));
				if (parent == null) {
					parent = new ZipNode(root, path.substring(0, token));
				}
				parent.add(this);
			} else {
				root.add(this);
			}
			nodesByPath.put(path, this);
		}
		@Override
		public String toString() {
			return name;
		}
		public String getEntryPath() {
			return path;
		}
	}
}
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
			if (zipNode.isLeaf()) {
				selectedZipEntries.add(zipNode);
			}
		}
		return selectedZipEntries;
	}
	
	/* ----------- Selection ----------- */

	public abstract void select(List<ZipNode> selectedZipEntries);
}