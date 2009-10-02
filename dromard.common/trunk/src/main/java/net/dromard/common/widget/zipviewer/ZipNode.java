package net.dromard.common.widget.zipviewer;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A zipnode class.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class ZipNode extends DefaultMutableTreeNode {
    /** The serialVersionUID. */
    private static final long serialVersionUID = 2250802888767301081L;
    /** The path. */
    private final String path;
    /** The name. */
    private final String name;

    public ZipNode(final String path) {
        this.path = path.replace('\\', '/');
        int token = this.path.lastIndexOf('/');
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