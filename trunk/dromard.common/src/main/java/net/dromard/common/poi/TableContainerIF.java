/**
 * 	File : TableContainerIF.java Apr 4, 2008
 */

package net.dromard.common.poi;

/**
 * Table container interface.
 * <br>
 * @author Gabriel Dromard
 */
public interface TableContainerIF {
    /**
     * @return
     */
    int getRowCount();

    /**
     * @return
     */
    int getColumnCount();

    /**
     * @param row
     * @param col
     * @return
     */
    Object getCellValue(int row, int col);
}


