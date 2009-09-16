package net.dromard.common.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;

/**
 * A helper class that is able to transform string into Color, Dimension.
 * @author Gabriel Dromard
 * 16 sept. 2009
 */
public class SwingPropertiesHelper {

    /**
     * Retrieve and transform the property value into a Color.
     * @param name The property name
     * @return The corresponding color
     */
    public static Color asColor(final String color) {
        if (color != null) {
            String[] rgb = color.split(",");
            if (rgb.length == 3) {
                try {
                    return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Retrieve and transform the property value into a Dimension.
     * The comma is used to distinguish x from y.
     * @param name The property name
     * @return The dimension if the value is valid
     * @throws IOException If an error occurred while accessing the properties file.
     */
    public static Dimension asDimension(final String dimension) {
        String[] dimensions = dimension.split(",");
        if (dimensions.length == 2) {
            try {
                return new Dimension(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Retrieve and transform the property value into a Font.
     * @param name The property name
     * @return A valid font (or null if invalid)
     * @throws IOException If an error occurred while accessing the properties file.
     */
    public static Font asFont(final String font) {
        if (font != null) {
            return Font.decode(font);
        }
        return null;
    }

}
