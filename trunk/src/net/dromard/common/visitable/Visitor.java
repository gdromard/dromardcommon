package net.dromard.common.visitable;
/**
 * Visitor pattern interface.
 * <br>
 * @author          Pingus
 */
public interface Visitor {
    /**
     * Vist function.
     * @param node The element object of the tree.
     * @throws Exception Any exception can occured during visit.
     */
    void visit(Object node) throws Exception;
}

