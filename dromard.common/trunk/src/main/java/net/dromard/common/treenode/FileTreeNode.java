package net.dromard.common.treenode;

import java.io.File;
import java.util.List;

import net.dromard.common.visitable.Visitable;

/**
 * File tree node object.
 * @author Gabriel Dromard
 */
public class FileTreeNode extends TreeNode implements Visitable {
    /**
     * Construct the node with a given file object.
     * @param file The node attached file.
     */
    public FileTreeNode(final File file) {
        super(file);
    }

    /**
     * Retreive the childs.
     * @return the childs
     */
    public final List<TreeNode> getChild() {
    	if (super.getChild() == null) {
	    	if (getData() != null) {
	    		File[] tmp = ((File) getData()).listFiles();
	    		if (tmp != null && tmp.length > 0) {
					for (int i = 0; i < tmp.length; ++i) {
						addChild(new FileTreeNode(tmp[i]));
					}
	    		}
	    	}
    	}
        return super.getChild();
    }
	
    /**
     * @return the data
     */
	public final File getFile() {
		return (File) getData();
	}
	
	/**
	 * @param data the data to set
	 */
	public final void setFile(final File file) {
        setData(file);
    }
}