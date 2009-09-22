/**
 * 	File : POISample.java Apr 4, 2008
 */
package net.dromard.common.poi;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.EscherGraphics;
import org.apache.poi.hssf.usermodel.EscherGraphics2d;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFChildAnchor;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFShapeGroup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class POISample {

    public static final String FILE_SAMPLE = "c:/temp/workbook.xls";

    /**
     * 
     * @throws IOException
     */
    public static void createNewWorkbook() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        FileOutputStream fileOut = new FileOutputStream(FILE_SAMPLE);
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * 
     * @throws IOException
     */
    public static void createNewSheet() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet("new sheet");
        HSSFSheet sheet2 = wb.createSheet("second sheet");
        FileOutputStream fileOut = new FileOutputStream(FILE_SAMPLE);
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     *
     * @throws IOException
     */
    public static void createDateCells() throws IOException {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = sheet.createRow((short) 0);

        // Create a cell and put a date value in it.  The first cell is not styled
        // as a date.
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(new Date());

        // we style the second cell as a date (and time).  It is important to
        // create a new cell style from the workbook otherwise you can end up
        // modifying the built in style and effecting not only this cell but other cells.
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        cell = row.createCell((short) 1);
        cell.setCellValue(new Date());
        cell.setCellStyle(cellStyle);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(FILE_SAMPLE);
        wb.write(fileOut);
        fileOut.close();
    }

    public static void workWithDifferentTypesOfCells()  throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");
        HSSFRow row = sheet.createRow((short) 2);
        row.createCell((short) 0).setCellValue(1.1);
        row.createCell((short) 1).setCellValue(new Date());
        row.createCell((short) 2).setCellValue("a string");
        row.createCell((short) 3).setCellValue(true);
        row.createCell((short) 4).setCellType(HSSFCell.CELL_TYPE_ERROR);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void createCells() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = sheet.createRow((short) 0);
        // Create a cell and put a value in it.
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(1);

        // Or do it on one line.
        row.createCell((short) 1).setCellValue(1.2);
        row.createCell((short) 2).setCellValue("This is a string");
        row.createCell((short) 3).setCellValue(true);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(FILE_SAMPLE);
        wb.write(fileOut);
        fileOut.close();
    }

    public static void demonstratesVariousAlignmentOptions() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");
        HSSFRow row = sheet.createRow((short) 2);
        createCell(wb, row, (short) 0, HSSFCellStyle.ALIGN_CENTER);
        createCell(wb, row, (short) 1, HSSFCellStyle.ALIGN_CENTER_SELECTION);
        createCell(wb, row, (short) 2, HSSFCellStyle.ALIGN_FILL);
        createCell(wb, row, (short) 3, HSSFCellStyle.ALIGN_GENERAL);
        createCell(wb, row, (short) 4, HSSFCellStyle.ALIGN_JUSTIFY);
        createCell(wb, row, (short) 5, HSSFCellStyle.ALIGN_LEFT);
        createCell(wb, row, (short) 6, HSSFCellStyle.ALIGN_RIGHT);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * Creates a cell and aligns it a certain way.
     *
     * @param wb        the workbook
     * @param row       the row to create the cell in
     * @param column    the column number to create the cell in
     * @param align     the alignment for the cell.
     */
    private static void createCell(final HSSFWorkbook wb, final HSSFRow row, final short column, final short align)
    {
        HSSFCell cell = row.createCell(column);
        cell.setCellValue("Align It");
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(align);
        cell.setCellStyle(cellStyle);
    }

    public static void workWithBorders() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = sheet.createRow((short) 1);

        // Create a cell and put a value in it.
        HSSFCell cell = row.createCell((short) 1);
        cell.setCellValue(4);

        // Style the cell with borders all around.
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.GREEN.index);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLUE.index);
        style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        cell.setCellStyle(style);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void iterateOverRowsAndCells() {
        /**Iterate over rows and cells

        Sometimes, you'd like to just iterate over all the rows in a sheet, or all the cells in a row. This is possible with a simple for loop.

        Luckily, this is very easy. HSSFRow defines a CellIterator inner class to handle iterating over the cells (get one with a call to row.cellIterator()), and HSSFSheet provides a rowIterator() method to give an iterator over all the rows.

        (Unfortunately, due to the broken and backwards-incompatible way that Java 5 foreach loops were implemented, it isn't possible to use them on a codebase that supports Java 1.4, as POI does)
        */
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.getSheetAt(0);
        for (Iterator rit = sheet.rowIterator(); rit.hasNext();) {
            HSSFRow row = (HSSFRow) rit.next();
            for (Iterator cit = row.cellIterator(); cit.hasNext();) {
                HSSFCell cell = (HSSFCell) cit.next();
                // Do something here
            }
        }
    }
/*
    public static void doTextExtration() throws IOException {
        //Text Extraction

        //For most text extraction requirements, the standard ExcelExtractor class should provide all you need.

        InputStream inp = new FileInputStream("workbook.xls");
        HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
        ExcelExtractor extractor = new ExcelExtractor(wb);

        extractor.setFormulasNotResults(true);
        extractor.setIncludeSheetNames(false);
        String text = extractor.getText();
    }
/**/
    public static void handleFillsAndColors() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = sheet.createRow((short) 1);

        // Aqua background
        HSSFCellStyle style = wb.createCellStyle();
        style.setFillBackgroundColor(HSSFColor.AQUA.index);
        style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
        HSSFCell cell = row.createCell((short) 1);
        cell.setCellValue("X");
        cell.setCellStyle(style);

        // Orange "foreground", foreground being the fill foreground not the font color.
        style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.ORANGE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell = row.createCell((short) 2);
        cell.setCellValue("X");
        cell.setCellStyle(style);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void mergeCells() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFRow row = sheet.createRow((short) 1);
        HSSFCell cell = row.createCell((short) 1);
        cell.setCellValue("This is a test of merging");

        sheet.addMergedRegion(new Region(1, (short) 1, 1, (short) 2));

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void workingWithFonts() throws IOException {
        //Working with fonts

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = sheet.createRow((short) 1);

        // Create a new font and alter it.
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 24);
        font.setFontName("Courier New");
        font.setItalic(true);
        font.setStrikeout(true);

        // Fonts are set into a style so create a new one to use.
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);

        // Create a cell and put a value in it.
        HSSFCell cell = row.createCell((short) 1);
        cell.setCellValue("This is a test of fonts");
        cell.setCellStyle(style);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();

        /*
        Note, the maximum number of unique fonts in a workbook is limited to 32767 ( the maximum positive short). You should re-use fonts in your apllications instead of creating a font for each cell. Examples:

        Wrong:

                for (int i = 0; i < 10000; i++) {
                    HSSFRow row = sheet.createRow(i);
                    HSSFCell cell = row.createCell((short) 0);

                    HSSFCellStyle style = workbook.createCellStyle();
                    HSSFFont font = workbook.createFont();
                    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }

        Correct:

                HSSFCellStyle style = workbook.createCellStyle();
                HSSFFont font = workbook.createFont();
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                style.setFont(font);
                for (int i = 0; i < 10000; i++) {
                    HSSFRow row = sheet.createRow(i);
                    HSSFCell cell = row.createCell((short) 0);
                    cell.setCellStyle(style);
                }
         */
    }

    public static void customColors() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("Default Palette");

        //apply some colors from the standard palette,
        // as in the previous examples.
        //we'll use red text on a lime background

        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.LIME.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.RED.index);
        style.setFont(font);

        cell.setCellStyle(style);

        //save with the default palette
        FileOutputStream out = new FileOutputStream("default_palette.xls");
        wb.write(out);
        out.close();

        //now, let's replace RED and LIME in the palette
        // with a more attractive combination
        // (lovingly borrowed from freebsd.org)

        cell.setCellValue("Modified Palette");

        //creating a custom palette for the workbook
        HSSFPalette palette = wb.getCustomPalette();

        //replacing the standard red with freebsd.org red
        palette.setColorAtIndex(HSSFColor.RED.index,
                (byte) 153,  //RGB red (0-255)
                (byte) 0,    //RGB green
                (byte) 0     //RGB blue
        );
        //replacing lime with freebsd.org gold
        palette.setColorAtIndex(HSSFColor.LIME.index, (byte) 255, (byte) 204, (byte) 102);

        //save with the modified palette
        // note that wherever we have previously used RED or LIME, the
        // new colors magically appear
        out = new FileOutputStream("modified_palette.xls");
        wb.write(out);
        out.close();
    }

    public static void readAndWriteWorkBooks() throws IOException {
        //Reading and Rewriting Workbooks

        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("workbook.xls"));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(2);
        HSSFCell cell = row.getCell((short) 3);
        if (cell == null) {
            cell = row.createCell((short) 3);
        }
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("a test");

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void useNewLineInCells() throws IOException {
        //Using newlines in cells

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet s = wb.createSheet();
        HSSFRow r = null;
        HSSFCell c = null;
        HSSFCellStyle cs = wb.createCellStyle();
        HSSFFont f = wb.createFont();
        HSSFFont f2 = wb.createFont();

        cs = wb.createCellStyle();

        cs.setFont(f2);
        //Word Wrap MUST be turned on
        cs.setWrapText(true);

        r = s.createRow((short) 2);
        r.setHeight((short) 0x349);
        c = r.createCell((short) 2);
        c.setCellType(HSSFCell.CELL_TYPE_STRING);
        c.setCellValue("Use \n with word wrap on to create a new line");
        c.setCellStyle(cs);
        s.setColumnWidth((short) 2, (short) ((50 * 8) / ((double) 1 / 20)));

        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void handleDataFormats() throws Exception {
        //Data Formats

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("format sheet");
        HSSFCellStyle style;
        HSSFDataFormat format = wb.createDataFormat();
        HSSFRow row;
        HSSFCell cell;
        short rowNum = 0;
        short colNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(colNum);
        cell.setCellValue(11111.25);
        style = wb.createCellStyle();
        style.setDataFormat(format.getFormat("0.0"));
        cell.setCellStyle(style);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(colNum);
        cell.setCellValue(11111.25);
        style = wb.createCellStyle();
        style.setDataFormat(format.getFormat("#,##0.0000"));
        cell.setCellStyle(style);

        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void fitSheetToOnePage() throws IOException {
        //Fit Sheet to One Page

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("format sheet");
        HSSFPrintSetup ps = sheet.getPrintSetup();

        sheet.setAutobreaks(true);

        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);


        // Create various cells and rows for spreadsheet.

        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void setPrintArea() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Sheet1");
        wb.setPrintArea(0, "$A$1:$C$2");
        //sets the print area for the first sheet
        //Alternatively:
        //wb.setPrintArea(0, 0, 1, 0, 0) is equivalent to using the name reference (See the JavaDocs for more details)

        // Create various cells and rows for spreadsheet.

        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void setPageNumberOnFooter() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("format sheet");
        HSSFFooter footer = sheet.getFooter();

        footer.setRight("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());

        // Create various cells and rows for spreadsheet.

        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();

    }

    /*
    public static void useConvenienceFunctions() throws IOException {
        //Using the Convenience Functions

        //The convenience functions live in contrib and provide utility features such as setting borders around merged regions and changing style attributes without explicitly creating new styles.

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet1 = wb.createSheet( "new sheet" );

            // Create a merged region
            HSSFRow row = sheet1.createRow( (short) 1 );
            HSSFRow row2 = sheet1.createRow( (short) 2 );
            HSSFCell cell = row.createCell( (short) 1 );
            cell.setCellValue( "This is a test of merging" );
            Region region = new Region( 1, (short) 1, 4, (short) 4 );
            sheet1.addMergedRegion( region );

            // Set the border and border colors.
            final short borderMediumDashed = HSSFCellStyle.BORDER_MEDIUM_DASHED;
            HSSFRegionUtil.setBorderBottom( borderMediumDashed, region, sheet1, wb );
            HSSFRegionUtil.setBorderTop( borderMediumDashed,
                region, sheet1, wb );
            HSSFRegionUtil.setBorderLeft( borderMediumDashed,
                region, sheet1, wb );
            HSSFRegionUtil.setBorderRight( borderMediumDashed,
                region, sheet1, wb );
            HSSFRegionUtil.setBottomBorderColor(HSSFColor.AQUA.index, region, sheet1, wb);
            HSSFRegionUtil.setTopBorderColor(HSSFColor.AQUA.index, region, sheet1, wb);
            HSSFRegionUtil.setLeftBorderColor(HSSFColor.AQUA.index, region, sheet1, wb);
            HSSFRegionUtil.setRightBorderColor(HSSFColor.AQUA.index, region, sheet1, wb);

            // Shows some usages of HSSFCellUtil
            HSSFCellStyle style = wb.createCellStyle();
            style.setIndention((short)4);
            HSSFCellUtil.createCell(row, 8, "This is the value of the cell", style);
            HSSFCell cell2 = HSSFCellUtil.createCell( row2, 8, "This is the value of the cell");
            HSSFCellUtil.setAlignment(cell2, wb, HSSFCellStyle.ALIGN_CENTER);

            // Write out the workbook
            FileOutputStream fileOut = new FileOutputStream( "workbook.xls" );
            wb.write( fileOut );
            fileOut.close();
    }*/

    public static void shiftRowsUpOrDown() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("row sheet");

        // Create various cells and rows for spreadsheet.

        // Shift rows 6 - 11 on the spreadsheet to the top (rows 0 - 5)
        sheet.shiftRows(5, 10, -5);

        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();

    }

    public static void setAsheetAsSelected() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("row sheet");
        sheet.setSelected(true);

        // Create various cells and rows for spreadsheet.

        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();

    }

    public static void setZoomMagnification() throws IOException {
        //The zoom is expressed as a fraction. For example to express a zoom of 75% use 3 for the numerator and 4 for the denominator.

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet("new sheet");
        sheet1.setZoom(3, 4);   // 75 percent magnification
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    public static void splitAndFreezePane() throws IOException {
        /*Splits and freeze panes

        There are two types of panes you can create; freeze panes and split panes.

        A freeze pane is split by columns and rows. You create a freeze pane using the following mechanism:

        sheet1.createFreezePane( 3, 2, 3, 2 );

        The first two parameters are the columns and rows you wish to split by. The second two parameters indicate the cells that are visible in the bottom right quadrant.

        Split pains appear differently. The split area is divided into four separate work area's. The split occurs at the pixel level and the user is able to adjust the split by dragging it to a new position.

        Split panes are created with the following call:

        sheet2.createSplitPane( 2000, 2000, 0, 0, HSSFSheet.PANE_LOWER_LEFT );

        The first parameter is the x position of the split. This is in 1/20th of a point. A point in this case seems to equate to a pixel. The second parameter is the y position of the split. Again in 1/20th of a point.

        The last parameter indicates which pane currently has the focus. This will be one of HSSFSheet.PANE_LOWER_LEFT, PANE_LOWER_RIGHT, PANE_UPPER_RIGHT or PANE_UPPER_LEFT.
         */
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet1 = wb.createSheet("new sheet");
            HSSFSheet sheet2 = wb.createSheet("second sheet");
            HSSFSheet sheet3 = wb.createSheet("third sheet");
            HSSFSheet sheet4 = wb.createSheet("fourth sheet");

            // Freeze just one row
            sheet1.createFreezePane(0, 1, 0, 1);
            // Freeze just one column
            sheet2.createFreezePane(1, 0, 1, 0);
            // Freeze the columns and rows (forget about scrolling position of the lower right quadrant).
            sheet3.createFreezePane(2, 2);
            // Create a split with the lower left side being the active quadrant
            sheet4.createSplitPane(2000, 2000, 0, 0, HSSFSheet.PANE_LOWER_LEFT);

            FileOutputStream fileOut = new FileOutputStream("workbook.xls");
            wb.write(fileOut);
            fileOut.close();
    }

    public static void repeatRowsAndColumns() throws IOException {
        /*Repeating rows and columns

        It's possible to set up repeating rows and columns in your printouts by using the setRepeatingRowsAndColumns() function in the HSSFWorkbook class.

        This function Contains 5 parameters. The first parameter is the index to the sheet (0 = first sheet). The second and third parameters specify the range for the columns to repreat. To stop the columns from repeating pass in -1 as the start and end column. The fourth and fifth parameters specify the range for the rows to repeat. To stop the columns from repeating pass in -1 as the start and end rows.
*/
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet1 = wb.createSheet("new sheet");
            HSSFSheet sheet2 = wb.createSheet("second sheet");

            // Set the columns to repeat from column 0 to 2 on the first sheet
            wb.setRepeatingRowsAndColumns(0, 0, 2, -1, -1);
            // Set the the repeating rows and columns on the second sheet.
            wb.setRepeatingRowsAndColumns(1, 4, 5, 1, 2);

            FileOutputStream fileOut = new FileOutputStream("workbook.xls");
            wb.write(fileOut);
            fileOut.close();
    }

    public static void setHeadersAndFooters() throws IOException {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("new sheet");

            HSSFHeader header = sheet.getHeader();
            header.setCenter("Center Header");
            header.setLeft("Left Header");
            header.setRight(HSSFHeader.font("Stencil-Normal", "Italic")
                    + HSSFHeader.fontSize((short) 16) + "Right w/ Stencil-Normal Italic font and size 16");

            FileOutputStream fileOut = new FileOutputStream("workbook.xls");
            wb.write(fileOut);
            fileOut.close();
    }

    public static void handleDrawingShape() {
       /* Drawing Shapes

        POI supports drawing shapes using the Microsoft Office drawing tools. Shapes on a sheet are organized in a hiearchy of groups and and shapes. The top-most shape is the patriarch. This is not visisble on the sheet at all. To start drawing you need to call createPatriarch on the HSSFSheet class. This has the effect erasing any other shape information stored in that sheet. By default POI will leave shape records alone in the sheet unless you make a call to this method.

        To create a shape you have to go through the following steps:

           1. Create the patriarch.
           2. Create an anchor to position the shape on the sheet.
           3. Ask the patriarch to create the shape.
           4. Set the shape type (line, oval, rectangle etc...)
           5. Set any other style details converning the shape. (eg: line thickness, etc...)
        */

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor a = new HSSFClientAnchor(0, 0, 1023, 255, (short) 1, 0, (short) 1, 0);
        HSSFSimpleShape shape1 = patriarch.createSimpleShape(a);
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);

        //Text boxes are created using a different call:

        HSSFTextbox textbox1 = patriarch.createTextbox(new HSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 2, 2));
        textbox1.setString(new HSSFRichTextString("This is a test"));


       // It's possible to use different fonts to style parts of the text in the textbox. Here's how:

        HSSFFont font = wb.createFont();
        font.setItalic(true);
        font.setUnderline(HSSFFont.U_DOUBLE);
        HSSFRichTextString string = new HSSFRichTextString("Woo!!!");
        string.applyFont(2, 5, font);
        textbox1.setString(string);

       /* Just as can be done manually using Excel, it is possible to group shapes together. This is done by calling createGroup() and then creating the shapes using those groups.

        It's also possible to create groups within groups.
        Warning
        Any group you create should contain at least two other shapes or subgroups.

        Here's how to create a shape group:
        */

        // Create a shape group.
        HSSFShapeGroup group = patriarch.createGroup(new HSSFClientAnchor(0, 0, 900, 200, (short) 2, 2, (short) 2, 2));

        // Create a couple of lines in the group.
        /*HSSFSimpleShape*/ shape1 = group.createShape(new HSSFChildAnchor(3, 3, 500, 500));
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        ((HSSFChildAnchor) shape1.getAnchor()).setAnchor((short) 3, 3, 500, 500);
        HSSFSimpleShape shape2 = group.createShape(new HSSFChildAnchor((short) 1, 200, 400, 600));
        shape2.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);

        /*If you're being observant you'll noticed that the shapes that are added to the group use a new type of anchor: the HSSFChildAnchor. What happens is that the created group has it's own coordinate space for shapes that are placed into it. POI defaults this to (0,0,1023,255) but you are able to change it as desired. Here's how:

            myGroup.setCoordinates(10,10,20,20); // top-left, bottom-right


        If you create a group within a group it's also going to have it's own coordinate space.
        */
    }

    public static void handleStylingShape() {
        /*Styling Shapes

        By default shapes can look a little plain. It's possible to apply different styles to the shapes however. The sorts of things that can currently be done are:

            * Change the fill color.
            * Make a shape with no fill color.
            * Change the thickness of the lines.
            * Change the style of the lines. Eg: dashed, dotted.
            * Change the line color.

        Here's an examples of how this is done:
        */

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor a = new HSSFClientAnchor(0, 0, 1023, 255, (short) 1, 0, (short) 1, 0);

        HSSFSimpleShape s = patriarch.createSimpleShape(a);
        s.setShapeType(HSSFSimpleShape.OBJECT_TYPE_OVAL);
        s.setLineStyleColor(10, 10, 10);
        s.setFillColor(90, 10, 200);
        s.setLineWidth(HSSFShape.LINEWIDTH_ONE_PT * 3);
        s.setLineStyle(HSSFShape.LINESTYLE_DOTSYS);

    }

    public static void workWitghShapesAndGraphic2D() {
        /*Shapes and Graphics2d

        While the native POI shape drawing commands are the recommended way to draw shapes in a shape it's sometimes desirable to use a standard API for compatibility with external libraries. With this in mind we created some wrappers for Graphics and Graphics2d.
        Warning
        It's important to not however before continuing that Graphics2d is a poor match to the capabilities of the Microsoft Office drawing commands. The older Graphics class offers a closer match but is still a square peg in a round hole.

        All Graphics commands are issued into an HSSFShapeGroup. Here's how it's done:
        */
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        HSSFClientAnchor a =  new HSSFClientAnchor(0, 0, 1023, 255, (short) 1, 0, (short) 1, 0);
        HSSFShapeGroup    group = patriarch.createGroup(a);
            group.setCoordinates(0, 0, 80 * 4 , 12 * 23);
            float verticalPointsPerPixel = a.getAnchorHeightInPoints(sheet) / (float) Math.abs(group.getY2() - group.getY1());
            EscherGraphics g = new EscherGraphics(group, wb, Color.black, verticalPointsPerPixel);
            EscherGraphics2d g2d = new EscherGraphics2d(g);
      //TODO      drawChemicalStructure( g2d );


        /*The first thing we do is create the group and set it's coordinates to match what we plan to draw. Next we calculate a reasonable fontSizeMultipler then create the EscherGraphics object. Since what we really want is a Graphics2d object we create an EscherGraphics2d object and pass in the graphics object we created. Finally we call a routine that draws into the EscherGraphics2d object.

        The vertical points per pixel deserves some more explanation. One of the difficulties in converting Graphics calls into escher drawing calls is that Excel does not have the concept of absolute pixel positions. It measures it's cell widths in 'characters' and the cell heights in points. Unfortunately it's not defined exactly what type of character it's measuring. Presumably this is due to the fact that the Excel will be using different fonts on different platforms or even within the same platform.

        Because of this constraint we've had to implement the concept of a verticalPointsPerPixel. This the amount the font should be scaled by when you issue commands such as drawString(). To calculate this value use the follow formula:
        multipler = groupHeightInPoints / heightOfGroup

        The height of the group is calculated fairly simply by calculating the difference between the y coordinates of the bounding box of the shape. The height of the group can be calculated by using a convenience called HSSFClientAnchor.getAnchorHeightInPoints().
        Many of the functions supported by the graphics classes are not complete. Here's some of the functions that are known to work.

            * fillRect()
            * fillOval()
            * drawString()
            * drawOval()
            * drawLine()
            * clearRect()

        Functions that are not supported will return and log a message using the POI logging infrastructure (disabled by default).
        */
    }

    public static void handleOutlining() throws IOException {

        //Outlines are great for grouping sections of information together and can be added easily to columns and rows using the POI API. Here's how:

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet1 = wb.createSheet("new sheet");

            sheet1.groupRow(5, 14);
            sheet1.groupRow(7, 14);
            sheet1.groupRow(16, 19);

            sheet1.groupColumn((short) 4, (short) 7);
            sheet1.groupColumn((short) 9, (short) 12);
            sheet1.groupColumn((short) 10, (short) 11);

            FileOutputStream fileOut = new FileOutputStream(FILE_SAMPLE);
            wb.write(fileOut);
            fileOut.close();


       /* To collapse (or expand) an outline use the following calls:

            sheet1.setRowGroupCollapsed( 7, true );
            sheet1.setColumnGroupCollapsed( (short)4, true );



        */
    }

    public static void hanldeImage() {
       /* Images are part of the drawing support. To add an image just call createPicture() on the drawing patriarch. At the time of writing the following types are supported:

            * PNG
            * JPG
            * DIB

        It should be noted that any existing drawings may be erased once you add a image to a sheet.

            // Create the drawing patriarch.  This is the top level container for
            // all shapes. This will clear out any existing shapes for that sheet.
             */

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet5 = wb.createSheet("new sheet");
            HSSFPatriarch patriarch = sheet5.createDrawingPatriarch();

            HSSFClientAnchor anchor;
            anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 2, 2, (short) 4, 7);
            anchor.setAnchorType(2);
            /*patriarch.createPicture(anchor, loadPicture( "src/resources/logos/logoKarmokar4.png", wb ));


       // Creating an image and setting its anchor to the actual width and height:

           HSSFPatriarch patriarch = sheet5.createDrawingPatriarch();

            HSSFPicture picture = patriarch.createPicture(new HSSFClientAnchor(), loadPicture( "src/resources/logos/logoKarmokar4.png", wb ));
            picture.resize();

        or

            HSSFPatriarch patriarch = sheet5.createDrawingPatriarch();

            HSSFPicture picture = patriarch.createPicture(new HSSFClientAnchor(), loadPicture( "src/resources/logos/logoKarmokar4.png", wb ));
            HSSFClientAnchor preferredSize = picture.getPreferredSize();
            picture.setAnchor(preferredSize);
            */


        //Warning
        //HSSFPicture.resize() works only for JPEG and PNG. Other formats are not yet supported.

        /*Reading images from a workbook:

            HSSFWorkbook wb;

            List lst = wb.getAllPictures();
            for (Iterator it = lst.iterator(); it.hasNext(); ) {
                HSSFPictureData pict = (HSSFPictureData)it.next();
                String ext = pict.suggestFileExtension();
                byte[] data = pict.getData();
                if (ext.equals("jpeg")){
                  FileOutputStream out = new FileOutputStream("pict.jpg");
                  out.write(data);
                  out.close();
                }
            }
            */
    }

    public static void handleNamedRangesAndNamedCells() {
        /*
        Named Ranges and Named Cells

Named Range is a way to refer to a group of cells by a name. Named Cell is a degenerate case of Named Range in that the 'group of cells' contains exactly one cell. You can create as well as refer to cells in a workbook by their named range. When working with Named Ranges, the classes: org.apache.poi.hssf.util.CellReference and & org.apache.poi.hssf.util.AreaReference are used.

Creating Named Range / Named Cell

    // setup code
    String sname = "TestSheet", cname = "TestName", cvalue = "TestVal";
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet(sname);
    sheet.createRow(0).createCell((short) 0).setCellValue(cvalue);

    // 1. create named range for a single cell using areareference
    HSSFName namedCell = wb.createName();
    namedCell.setNameName(cname);
    String reference = sname+"!A1:A1"; // area reference
    namedCell.setReference(reference);

    // 2. create named range for a single cell using cellreference
    HSSFName namedCell = wb.createName();
    namedCell.setNameName(cname);
    String reference = sname+"!A1"; // cell reference
    namedCell.setReference(reference);

    // 3. create named range for an area using AreaReference
    HSSFName namedCell = wb.createName();
    namedCell.setNameName(cname);
    String reference = sname+"!A1:C5"; // area reference
    namedCell.setReference(reference);



Reading from Named Range / Named Cell

    // setup code
    String cname = "TestName";
    HSSFWorkbook wb = getMyWorkbook(); // retrieve workbook

    // retrieve the named range
    int namedCellIdx = wb.getNameIndex(cellName);
    HSSFName aNamedCell = wb.getNameAt(namedCellIdx);

    // retrieve the cell at the named range and test its contents
    AreaReference aref = new AreaReference(aNamedCell.getReference());
    CellReference[] crefs = aref.getAllReferencedCells();
    for (int i=0; i<crefs.length; i++) {
        HSSFSheet s = wb.getSheet(crefs[i].getSheetName());
        HSSFRow r = sheet.getRow(crefs[i].getRow());
        HSSFCell c = r.getCell(crefs[i].getCol());
        // extract the cell contents based on cell type etc.
    }


Reading from non-contiguous Named Ranges

    // Setup code
    String cname = "TestName";
    HSSFWorkbook wb = getMyWorkbook(); // retrieve workbook

    // Retrieve the named range
    // Will be something like "$C$10,$D$12:$D$14";
    int namedCellIdx = wb.getNameIndex(cellName);
    HSSFName aNamedCell = wb.getNameAt(namedCellIdx);

    // Retrieve the cell at the named range and test its contents
    // Will get back one AreaReference for C10, and
    //  another for D12 to D14
    AreaReference[] arefs = AreaReference.generateContiguous(aNamedCell.getReference());
    for (int i=0; i<arefs.length; i++) {
        // Only get the corners of the Area
        // (use arefs[i].getAllReferencedCells() to get all cells)
        CellReference[] crefs = arefs[i].getCells();
        for (int j=0; j<crefs.length; j++) {
            // Check it turns into real stuff
            HSSFSheet s = wb.getSheet(crefs[j].getSheetName());
            HSSFRow r = s.getRow(crefs[j].getRow());
            HSSFCell c = r.getCell(crefs[j].getCol());
            // Do something with this corner cell
        }
    }

         */
    }

    public static void handleCellComments() {
        /*
         * In Excel a comment is a kind of a text shape, so inserting a comment is very similar to placing a text box in a worksheet:

    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet("Cell comments in POI HSSF");

    // Create the drawing patriarch. This is the top level container for all shapes including cell comments.
    HSSFPatriarch patr = sheet.createDrawingPatriarch();

    //create a cell in row 3
    HSSFCell cell1 = sheet.createRow(3).createCell((short)1);
    cell1.setCellValue(new HSSFRichTextString("Hello, World"));

    //anchor defines size and position of the comment in worksheet
    HSSFComment comment1 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 6, 5));

     // set text in the comment
    comment1.setString(new HSSFRichTextString("We can set comments in POI"));

    //set comment author.
    //you can see it in the status bar when moving mouse over the commented cell
    comment1.setAuthor("Apache Software Foundation");

    // The first way to assign comment to a cell is via HSSFCell.setCellComment method
    cell1.setCellComment(comment1);

    //create another cell in row 6
    HSSFCell cell2 = sheet.createRow(6).createCell((short)1);
    cell2.setCellValue(36.6);


    HSSFComment comment2 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 8, (short) 6, 11));
    //modify background color of the comment
    comment2.setFillColor(204, 236, 255);

    HSSFRichTextString string = new HSSFRichTextString("Normal body temperature");

    //apply custom font to the text in the comment
    HSSFFont font = wb.createFont();
    font.setFontName("Arial");
    font.setFontHeightInPoints((short)10);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.RED.index);
    string.applyFont(font);

    comment2.setString(string);
    //by default comments are hidden. This one is always visible.
    comment2.setVisible(true);

    comment2.setAuthor("Bill Gates");


//     The second way to assign comment to a cell is to implicitly specify its row and column.
//     Note, it is possible to set row and column of a non-existing cell.
//     It works, the commnet is visible.

    comment2.setRow(6);
    comment2.setColumn((short)1);

    FileOutputStream out = new FileOutputStream("poi_comment.xls");
    wb.write(out);
    out.close();


Reading cell comments

    HSSFCell cell = sheet.get(3).getColumn((short)1);
    HSSFComment comment = cell.getCellComment();
    if (comment != null) {
      HSSFRichTextString str = comment.getString();
      String author = comment.getAuthor();
    }
    //  alternatively you can retrieve cell comments by (row, column)
    comment = sheet.getCellComment(3, 1);
    */
    }

    public static void ajustColumnWidth() {
        //Adjust column width to fit the contents
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.getSheetAt(0);
        sheet.autoSizeColumn((short) 0); //adjust width of the first column
        sheet.autoSizeColumn((short) 1); //adjust width of the second column

    /*Warning
    To calculate column width HSSFSheet.autoSizeColumn uses Java2D classes that throw exception if graphical environment is not available. In case if graphical environment is not available, you must tell Java that you are running in headless mode and set the following system property: java.awt.headless=true (either via -Djava.awt.headless=true startup parameter or via System.setProperty("java.awt.headless", "true")).
    */

    }

    /*
    public static void readHyperlink() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFCell cell = sheet.getRow(0).getCell((short)0);
        HSSFHyperlink link = cell.getHyperlink();
        if(link != null){
            System.out.println(link.getAddress());
        }

    }

    public static void createHyperLinks() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();

        //cell style for hyperlinks
        //by default hypelrinks are blue and underlined
        HSSFCellStyle hlink_style = wb.createCellStyle();
        HSSFFont hlink_font = wb.createFont();
        hlink_font.setUnderline(HSSFFont.U_SINGLE);
        hlink_font.setColor(HSSFColor.BLUE.index);
        hlink_style.setFont(hlink_font);

        HSSFCell cell;
        HSSFSheet sheet = wb.createSheet("Hyperlinks");

        //URL
        cell = sheet.createRow(0).createCell((short)0);
        cell.setCellValue("URL Link");
        HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
        link.setAddress("http://poi.apache.org/");
        cell.setHyperlink(link);
        cell.setCellStyle(hlink_style);

        //link to a file in the current directory
        cell = sheet.createRow(1).createCell((short)0);
        cell.setCellValue("File Link");
        link = new HSSFHyperlink(HSSFHyperlink.LINK_FILE);
        link.setAddress("link1.xls");
        cell.setHyperlink(link);
        cell.setCellStyle(hlink_style);

        //e-mail link
        cell = sheet.createRow(2).createCell((short)0);
        cell.setCellValue("Email Link");
        link = new HSSFHyperlink(HSSFHyperlink.LINK_EMAIL);
        //note, if subject contains white spaces, make sure they are url-encoded
        link.setAddress("mailto:poi@apache.org?subject=Hyperlinks");
        cell.setHyperlink(link);
        cell.setCellStyle(hlink_style);

        //link to a place in this workbook

        //create a target sheet and cell
        HSSFSheet sheet2 = wb.createSheet("Target Sheet");
        sheet2.createRow(0).createCell((short)0).setCellValue("Target Cell");

        cell = sheet.createRow(3).createCell((short)0);
        cell.setCellValue("Worksheet Link");
        link = new HSSFHyperlink(HSSFHyperlink.LINK_DOCUMENT);
        link.setAddress("'Target Sheet'!A1");
        cell.setHyperlink(link);
        cell.setCellStyle(hlink_style);

        FileOutputStream out = new FileOutputStream("hssf-links.xls");
        wb.write(out);
        out.close();
    }*/

    public static final void main(final String[] args) throws IOException {

        createCells();
    }
}


