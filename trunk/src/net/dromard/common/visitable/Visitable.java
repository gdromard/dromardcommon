package net.dromard.common.visitable;

/**
 * Visitable pattern interface.
 * <br>
 * @author          Pingus
 */
public interface Visitable {
    /**
     * Vist function.
     * @param visitor The visitor.
     * @throws Exception Any exception can occured during visit.
     */
	void accept(Visitor visitor) throws Exception;
}
