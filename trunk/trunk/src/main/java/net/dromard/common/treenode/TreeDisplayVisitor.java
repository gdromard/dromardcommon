package net.dromard.common.treenode;
import java.util.Iterator;

import net.dromard.common.visitable.Visitor;

/**
 * Visitor implementation for TreeDisplay.
 * <br>
 * @author Gabriel Dromard
 */
public class TreeDisplayVisitor extends DefaultTreeNodeVisitor implements Visitor {

    /**
     * Vist implementation.
     * @param node The element object of the tree.
     * @throws Exception Any exception can occured during visit.
     */
    public final void visit(final Object node) throws Exception {
        if (!(node instanceof TreeNode)) {
            throw new ClassCastException("TreeNode expected");
        }

        // Print indentation
        for (int i = 0; i < ((TreeNode) node).getRank(); i++) {
            System.out.print("   ");
        }
        // Print node data
        System.out.println(((TreeNode) node).getData().toString());

        // Continue visit
        acceptChilds(node);
    }

    /**
     * TreeDisplayVisitor test entry point.
     * @param args command lines aguments (not used).
     * @throws Exception Any exception can occured during visit.
     */
    public static void main(final String[] args) throws Exception {
        TreeNode root = new TreeNode("Level 1");

        TreeNode tmp = new TreeNode("Level 1.1");
        tmp.addChild(new TreeNode("Level 1.1.1"));
        tmp.addChild(new TreeNode("Level 1.1.2"));
        tmp.addChild(new TreeNode("Level 1.1.3"));
        root.addChild(tmp);

        tmp = new TreeNode("Level 1.2");
        tmp.addChild(new TreeNode("Level 1.2.1"));
        tmp.addChild(new TreeNode("Level 1.2.2"));
        tmp.addChild(new TreeNode("Level 1.2.3"));
        root.addChild(tmp);

        TreeDisplayVisitor visitor = new TreeDisplayVisitor();
        root.accept(visitor);
    }
}
