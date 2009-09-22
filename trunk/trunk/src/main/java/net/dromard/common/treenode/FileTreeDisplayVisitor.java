package net.dromard.common.treenode;
import java.io.File;
import java.util.Iterator;

import net.dromard.common.visitable.Visitor;

/**
 * Visitor implementation for FileTreeDisplay.
 * <br>
 * @author Gabriel Dromard
 */
public class FileTreeDisplayVisitor implements Visitor {

    /**
     * Vist implementation.
     * @param node The element object of the tree.
     * @throws Exception Any exception can occured during visit.
     */
    public final void visit(final Object node) throws Exception {
        if (!(node instanceof FileTreeNode)) {
            throw new ClassCastException("FileTreeNode expected");
        }

        for (int i = 0; i < ((FileTreeNode) node).getRank(); i++) {
            System.out.print("  ");
        }
        System.out.println(((FileTreeNode) node).getFile().getName());

        for (Iterator i = ((FileTreeNode) node).getChild().iterator(); i.hasNext();) {
        	FileTreeNode childNode = (FileTreeNode) (i.next());
            childNode.accept(this);
        }
    }
    

	public static void main(String[] args) throws Exception {
		File file = new File(".");
		if (args.length == 1) {
			new File(args[0]);
		}
		new FileTreeNode(file).accept(new FileTreeDisplayVisitor());
	}
}
