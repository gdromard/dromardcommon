package net.dromard.common.widget.digitalclock;

import java.awt.Font;
import java.io.InputStream;

/**
 * The font helper that retreive the needed digital font.
 * @author Gabriel Dromard
 * 2 oct. 2009
 */
class FontHelper {
    public static Font getFont(final String name) {
        Font font = null;
        String fName = name;
        try {
            InputStream is = FontHelper.class.getResourceAsStream(fName);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(fName + " not loaded.  Using serif font.");
            font = new Font("serif", Font.PLAIN, 24);
        }
        return font;
    }
}
