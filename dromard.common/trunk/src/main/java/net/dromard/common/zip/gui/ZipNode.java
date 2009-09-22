/**
 * 
 */
package net.dromard.common.zip.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class ZipNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 2250802888767301081L;
	private String path;
	private String name;

	public ZipNode(final String path) {
		this.path = path;
		int token = path.lastIndexOf("/");
		name = path.substring(token + 1);
	}
	@Override
	public String toString() {
		return name;
	}
	public String getEntryPath() {
		return path;
	}
}