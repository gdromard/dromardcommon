package net.dromard.common.treenode;

import net.dromard.common.visitable.Visitor;

/**
 * Default treenode visitor implementation.
 * It visit the tree but does nothing
 * <br>
 * @author Gabriel Dromard
 */
public abstract class DefaultTreeNodeVisitor implements Visitor {

    /**
     * Call this in your visit implementation so as to accept all childs.
     * @param node The element object of the tree.
     * @throws Exception Any exception can occured during visit.
     */
    protected final void acceptChilds(final Object node) throws Exception {
        if (!(node instanceof TreeNode)) {
            throw new ClassCastException("TreeNode expected");
        }

        if (((TreeNode) node).getChild() != null) {
            for (TreeNode treeNode : ((TreeNode) node).getChild()) {
                treeNode.accept(this);
            }
        }
    }
}
