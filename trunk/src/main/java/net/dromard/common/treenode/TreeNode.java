package net.dromard.common.treenode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.dromard.common.visitable.Visitable;
import net.dromard.common.visitable.Visitor;


/**
 * TreeNode implementation.
 * <br>
 * @author Gabriel Dromard
 */
public class TreeNode implements Visitable {
    /** Parent TreeNode. */
    private TreeNode parent = null;
    /** Node childs. */
    private List<TreeNode> childs = null;
    /** Node attached object. */
    private Object data = null;

    /**
     * Default constructor.
     *
     */
    public TreeNode() {
        /* Empty constructor */
    }

    /**
     * Construct the node with a given data object.
     * @param data The node attached object.
     */
    public TreeNode(final Object data) {
        this();
        this.data = data;
    }

    /**
     * Return node rank.
     * @return the rank
     */
    public final int getRank() {
        // Initialize rank;
        int rank = 0;
        // Retreive parent object
        TreeNode father = this;
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
    public List<TreeNode> getChild() {
        return childs;
    }

    /**
     * @param childs the childs to set
     */
    public final void setChilds(final List<TreeNode> childs) {
        this.childs = childs;
    }

    /**
     * WARNING this recurse into sub node so as to retrieve all the child counts !
     * @return How many nodes (including myself) in the tree ?
     */
    public final int size() {
        int size = 1; // me !

        // plus my children.
        if (this.childs != null && this.childs.size() > 0) {
            Iterator<? extends TreeNode> children = this.childs.iterator();
            while (children.hasNext()) {
                TreeNode child = children.next();
                size += child.size();
            }
        }

        return size;
    }

    /**
     * @return the data
     */
    public final Object getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public final void setData(final Object data) {
        this.data = data;
    }

    /**
     * @return the parent
     */
    public final TreeNode getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    private void setParent(final TreeNode parent) {
        this.parent = parent;
    }

    /**
     * Add a child to node.
     * @param child The node child element to be added.
     */
    public final void addChild(final TreeNode child) {
        child.setParent(this);
        if (childs == null) childs = new ArrayList<TreeNode>();
        childs.add(child);
    }

    /**
     * Accept method implementation of Visitor pattern.
     * @param visitor the visitor.
     * @throws Exception Any exception can occurred during visit.
     */
    public final void accept(final Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
