/**
 * File : POIHelper.java
 * 29 avr. 08
 */
package net.dromard.common.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import net.dromard.common.util.StringHelper;

/**
 * POI Excel helper class.
 * <br>
 * @author Gabriel Dromard
 */
public final class POIHelper {
    /**
     * Private constructor to prevent util class instanciation.
     */
    private POIHelper() {
    }
    /**
     * Retrieve or create a row.
     * @param sheet    The excel sheet
     * @param rowIndex The row index
     * @return The row
     */
    public static HSSFRow getRow(final HSSFSheet sheet, final int rowIndex) {
        HSSFRow row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

    /**
     * Retrieve or create a cell.
     * @param row       The excel row
     * @param cellIndex The cell index
     * @return The cell
     */
    public static HSSFCell getCell(final HSSFRow row, final int cellIndex) {
        HSSFCell cell = row.getCell((short) cellIndex);
        if (cell == null) {
            cell = row.createCell((short) cellIndex);
        }
        return cell;
    }

    /**
     * Retrieve a cell.
     * Note that if the row is not initialized in the sheet a new one will be created.
     * If the cell is not yet initialized in the row a new one will be created.
     * @param sheet    The excel sheet
     * @param rowIndex The row index
     * @param cellIndex The cell index
     * @return The cell
     */
    public static HSSFCell getCell(final HSSFSheet sheet, final int rowIndex, final int cellIndex) {
        HSSFRow row = getRow(sheet, rowIndex);
        HSSFCell cell = getCell(row, cellIndex);
        return cell;
    }

    /**
     * Set a value to a cell.
     * Note that if the row is not initialized in the sheet a new one will be created.
     * If the cell is not yet initialized in the row a new one will be created.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param value     The value to be set into the cell
     * @return The modified cell.
     */
    public static HSSFCell setCellValue(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final String value) {
        HSSFCell cell = getCell(sheet, rowIndex, cellIndex);
        if (HSSFCell.CELL_TYPE_STRING != cell.getCellType()) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        }
        cell.setCellValue(new HSSFRichTextString(value));
        return cell;
    }

    /**
     * Set a value to a cell.
     * Note that if the row is not initialized in the sheet a new one will be created.
     * If the cell is not yet initialized in the row a new one will be created.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param value     The value to be set into the cell
     * @param style     The style of the cell to be applied
     * @return The modified cell.
     */
    public static HSSFCell setCellValue(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final String value, final HSSFCellStyle style) {
        HSSFCell cell = setCellValue(sheet, rowIndex, cellIndex, value);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * Set a value to a cell only if the cell is empty.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param value     The value to be set into the cell
     * @return The modified cell.
     */
    public static HSSFCell setCellValueIfEmpty(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final String value) {
        HSSFRow row = getRow(sheet, rowIndex);
        HSSFCell cell = row.getCell((short) cellIndex);
        if (cell == null) {
            cell = row.createCell((short) cellIndex);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(value));
        } else if (StringHelper.isEmpty(cell.toString())) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            HSSFCellStyle cellStyle = cell.getCellStyle();
            cell.setCellValue(new HSSFRichTextString(value));
            cell.setCellStyle(cellStyle);
        }
        return cell;
    }

    /**
     * Set a value to a cell only if the cell is empty.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param value     The value to be set into the cell
     * @param style     The style of the cell to be applied
     * @return The modified cell.
     */
    public static HSSFCell setCellValueIfEmpty(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final String value, final HSSFCellStyle style) {
        HSSFCell cell = setCellValueIfEmpty(sheet, rowIndex, cellIndex, value);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * Set a value to a cell.
     * Note that if the row is not initialized in the sheet a new one will be created.
     * If the cell is not yet initialized in the row a new one will be created.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param value     The value to be set into the cell
     * @return The modified cell.
     */
    public static HSSFCell setCellValue(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final boolean value) {
        HSSFCell cell = getCell(sheet, rowIndex, cellIndex);
        cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * Set a value to a cell.
     * Note that if the row is not initialized in the sheet a new one will be created.
     * If the cell is not yet initialized in the row a new one will be created.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param value     The value to be set into the cell
     * @return The modified cell.
     */
    public static HSSFCell setCellValue(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final double value) {
        HSSFCell cell = getCell(sheet, rowIndex, cellIndex);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * Set a value to a cell.
     * Note that if the row is not initialized in the sheet a new one will be created.
     * If the cell is not yet initialized in the row a new one will be created.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param value     The value to be set into the cell
     * @param style     The style of the cell to be applied
     * @return The modified cell.
     */
    public static HSSFCell setCellValue(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final double value, final HSSFCellStyle style) {
        HSSFCell cell = setCellValue(sheet, rowIndex, cellIndex, value);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * Set a value to a cell only if the cell is empty.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param value     The value to be set into the cell
     * @return The modified cell.
     */
    public static HSSFCell setCellValueIfEmpty(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final double value) {
        HSSFRow row = getRow(sheet, rowIndex);
        HSSFCell cell = row.getCell((short) cellIndex);
        if (cell == null) {
            cell = row.createCell((short) cellIndex);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(value);
        } else if (StringHelper.isEmpty(cell.toString())) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(value);
        }
        return cell;
    }

    /**
     * Set a formula to a cell.
     * Note that if the row is not initialized in the sheet a new one will be created.
     * If the cell is not yet initialized in the row a new one will be created.
     * @param sheet     The excel sheet
     * @param rowIndex  The row index
     * @param cellIndex The cell index
     * @param formula   The formula to be set into the cell
     * @return The modified cell.
     */
    public static HSSFCell setCellFormula(final HSSFSheet sheet, final int rowIndex, final int cellIndex, final String formula) {
        HSSFCell cell = getCell(sheet, rowIndex, cellIndex);
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formula);
        return cell;
    }

    /**
     * Set a black border to given cell style.
     * @param style The style to be updated.
     * @return The updated style.
     */
    public static HSSFCellStyle setBlackBorderStyle(final HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        return style;
    }

    /**
     * Set a text centered to given cell style.
     * @param style          The style to be updated.
     * @return The updated style.
     */
    public static HSSFCellStyle setTextCenter(final HSSFCellStyle style) {
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        return style;
    }

    /**
     * Set a black border to given cell style.
     * @param style          The style to be updated.
     * @param parentWorkbook The parent workbook of the source style (needed to retrieve the font)
     * @return The updated style.
     */
    public static HSSFCellStyle setBold(final HSSFCellStyle style, final HSSFWorkbook parentWorkbook) {
        HSSFFont font = parentWorkbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    /**
     * Set a black border to given cell style.
     * @param sourceStyle      The style to be applied to destination style.
     * @param destinationStyle The style that will inherit the properties of the source style.
     * @param parentWorkbook   The parent workbook of the source style (needed to retrieve the font)
     * @return The updated style.
     */
    public static HSSFCellStyle copyStyle(final HSSFCellStyle sourceStyle, final HSSFCellStyle destinationStyle, final HSSFWorkbook parentWorkbook) {
        destinationStyle.setBorderBottom(sourceStyle.getBorderBottom());
        destinationStyle.setBorderTop(sourceStyle.getBorderTop());
        destinationStyle.setBorderLeft(sourceStyle.getBorderLeft());
        destinationStyle.setBorderRight(sourceStyle.getBorderRight());
        destinationStyle.setBottomBorderColor(sourceStyle.getBottomBorderColor());
        destinationStyle.setTopBorderColor(sourceStyle.getTopBorderColor());
        destinationStyle.setLeftBorderColor(sourceStyle.getLeftBorderColor());
        destinationStyle.setRightBorderColor(sourceStyle.getRightBorderColor());
        destinationStyle.setAlignment(sourceStyle.getAlignment());
        destinationStyle.setDataFormat(sourceStyle.getDataFormat());
        destinationStyle.setFillBackgroundColor(sourceStyle.getFillBackgroundColor());
        destinationStyle.setFillForegroundColor(sourceStyle.getFillForegroundColor());
        destinationStyle.setFillPattern(sourceStyle.getFillPattern());
        destinationStyle.setFont(parentWorkbook.getFontAt(sourceStyle.getFontIndex()));
        destinationStyle.setHidden(sourceStyle.getHidden());
        destinationStyle.setIndention(sourceStyle.getIndention());
        destinationStyle.setLocked(sourceStyle.getLocked());
        destinationStyle.setRotation(sourceStyle.getRotation());
        destinationStyle.setVerticalAlignment(sourceStyle.getVerticalAlignment());
        destinationStyle.setWrapText(sourceStyle.getWrapText());
        return destinationStyle;
    }

    /**
     * Create a bordered black cell style.
     * This style will be based on the style of the cell A1 of first sheet.
     * @param workbook The workbook that will create the style.
     * @return The newly created style.
     */
    public static HSSFCellStyle createBorderedStyle(final HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        POIHelper.copyStyle(POIHelper.getCell(workbook.getSheetAt(0), 0, 0).getCellStyle(), style, workbook);
        POIHelper.setBlackBorderStyle(style);
        return style;
    }

    /**
     * Create a bordered bold cell style.
     * This style will be based on the style of the cell A1 of first sheet. {@link #createBorderedStyle(HSSFWorkbook)}
     * @param workbook The workbook that will create the style.
     * @return The newly created style.
     */
    public static HSSFCellStyle createBorderedBoldStyle(final HSSFWorkbook workbook) {
        HSSFCellStyle style = createBorderedStyle(workbook);
        POIHelper.setBold(style, workbook);
        return style;
    }

    /**
     * Retrieve a sheet from a given workbook.
     * If the sheet does not exists, a new one is created with the given name.
     * @param workbook  The workbook containing the sheet.
     * @param sheetName The sheet name.
     * @return The sheet.
     */
    public static HSSFSheet getSheet(final HSSFWorkbook workbook, final String sheetName) {
        HSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        return sheet;
    }

    /**
     * Set a comment on a cell.
     * @param sheet       The sheet containing the cell
     * @param rowIndex    The cell row index
     * @param colIndex    The cell column index
     * @param description The comment description
     */
    public static void setCellComment(final HSSFSheet sheet, final int rowIndex, final int colIndex, final String description) {
        HSSFComment comment = sheet.createDrawingPatriarch().createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) colIndex, rowIndex, (short) colIndex, rowIndex));
        comment.setString(new HSSFRichTextString(description));
    }
}


