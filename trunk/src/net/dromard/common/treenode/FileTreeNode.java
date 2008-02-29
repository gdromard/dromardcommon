package net.dromard.common.treenode;

import java.io.File;
import java.util.ArrayList;

import net.dromard.common.visitable.Visitor;

/**
 * File tree node object.
 * @author Pingus
 */
public class FileTreeNode {
	/** Parent TreeNode. */
	private FileTreeNode parent = null;
	/** Node childs. */
	private ArrayList<FileTreeNode> childs = null;
	/** Node attached object. */
	private File file = null;

    /**
     * Default constructor.
     *
     */
    public FileTreeNode() {
        /* Empty constructor */
    }

    /**
     * Construct the node with a given file object.
     * @param file The node attached file.
     */
    public FileTreeNode(final File file) {
        this();
        this.file = file;
    }

    /**
     * Return node rank.
     * @return the rank
     */
    public final int getRank() {
        // Initialize rank;
        int rank = 0;
        // Retreive parent object
        FileTreeNode father = this;
        // Iter until root node
        while ((father = father.getParent()) != null) {
            ++rank;
        }
        return rank;
    }

    /**
     * Is this node a leaf ?
     * @return If this node is a leaf (if it does not contain any child)
     */
    public final boolean isLeaf() {
        return (childs == null || childs.size() == 0);
    }

    /**
     * Retreive the childs.
     * @return the childs
     */
    public final ArrayList getChilds() {
    	if (childs == null) {
    		childs = new ArrayList<FileTreeNode>();
	    	if (file != null) {
	    		File[] tmp = file.listFiles();
	    		if (tmp != null && tmp.length > 0) {
					for (int i = 0; i < tmp.length; ++i) {
						addChild(new FileTreeNode(tmp[i]));
					}
	    		}
	    	}
    	}
        return childs;
    }
	
    /**
     * @return the data
     */
	public final File getFile() {
		return file;
	}
	
	/**
	 * @param data the data to set
	 */
	public final void setFile(final File file) {
        this.file = file;
    }

    /**
     * @return the parent
     */
    public final FileTreeNode getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    private void setParent(final FileTreeNode parent) {
        this.parent = parent;
    }

    /**
     * Add a child to node.
     * @param child The node child element to be added.
     */
    public final void addChild(final FileTreeNode child) {
        child.setParent(this);
        this.childs.add(child);
    }

    /**
     * Accept method implementation of Visitor pattern.
     * @param visitor the visitor.
     * @throws Exception Any exception can occured during visit.
     */
    public final void accept(final Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}