package net.dromard.common.util;

import java.awt.Color;

/**
 * An util class for color management.
 * @author Gabriel Dromard
 * 22 sept. 2009
 */
public final class ColorHelper {
    private ColorHelper() {
    }

    /**
     * This methods encode a color into HTML style : 000000 (black) FFFFFF (white) ...
     * @param color The color to be encoded.
     * @return a String that represents an opaque color as a 24-bit integer.
     */
    public static String encode(final Color color) {
        return Integer.toHexString((color.getRGB() & 0xffffff) | 0x1000000).substring(1);
    }

    /**
     * Converts a String to an integer and returns the specified opaque Color.
     * This method handles string formats that are used to represent octal and hexidecimal numbers.
     * @see java.lang.Integer.decode
     * @param color a String that represents an opaque color as a 24-bit integer.
     * @return the new Color object.
     */
    public static Color decode(final String color) {
        return Color.decode(color);
    }
}
