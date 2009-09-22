/**
 * 
 */
package net.dromard.common.zip.gui;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ZipEntryTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = -1750530388684495121L;

	public ZipEntryTreeModel(ZipFile zipFile) throws IOException {
		super(null);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new File(zipFile.getName()).getName());
		load(root, zipFile.entries());
		zipFile.close();
	}
	
	private void load(DefaultMutableTreeNode root, Enumeration<? extends ZipEntry> entries) {
		final Map<String, ZipNode> nodesByPath = new HashMap<String, ZipNode>();
		while (entries.hasMoreElements()) {
			load(nodesByPath, root, entries.nextElement().getName());
		}
		super.setRoot(root);
	}

	private ZipNode load(final Map<String, ZipNode> nodesByPath, final DefaultMutableTreeNode root, String path) {
		ZipNode zipNode = new ZipNode(path);
					
		int token = path.lastIndexOf("/");
		if (token > -1) {
			ZipNode parent = nodesByPath.get(path.substring(0, token));
			if (parent == null) {
				parent = load(nodesByPath, root, path.substring(0, token));
			}
			parent.add(zipNode);
		} else {
			root.add(zipNode);
		}
		nodesByPath.put(path, zipNode);
		return zipNode;
	}
}