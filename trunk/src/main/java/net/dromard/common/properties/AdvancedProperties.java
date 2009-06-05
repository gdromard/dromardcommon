package net.dromard.common.properties;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;

public class AdvancedProperties extends Properties {

    /** The serialVersionUID. */
    private static final long serialVersionUID = 6360848705053941067L;

    /**
     * Default constructor.
     */
    public AdvancedProperties() {
        super();
    }

    /**
     * Retrieve a property value.
     * @param name The property name
     * @param defaultValue The default value to be used if no values has been found.
     * @return The property value
     */
    @Override
    public String getProperty(final String key, final String defaultValue) {
        String value = null;
        try {
            value = getProperty(key);
            if (value == null) {
                value = defaultValue;
            }
        } catch (MissingResourceException e) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Retrieve a property integer value.
     * @param name The property name
     * @param defaultValue The default value to be used if no values has been found.
     * @return The property value
     */
    public int getProperty(final String key, final int defaultValue) {
        return Integer.parseInt(getProperty(key, "" + defaultValue));
    }

    /**
     * Retrieve and transform the property value into a Color.
     * @param name The property name
     * @return The corresponding color
     */
    public Color getPropertyAsColor(final String name) {
        String color = getProperty(name);
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
    public Dimension getPropertyAsDimension(final String name) {
        String dimension = getProperty(name);
        String[] dim = dimension.split(",");
        if (dim.length == 2) {
            try {
                return new Dimension(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));
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
    public Font getPropertyAsFont(final String name) {
        String font = getProperty(name);
        if (font != null)
            return Font.decode(font);
        return null;
    }

    /**
     * Retrieve a property using a formatter.
     * @see java.util.Properties#getProperty(java.lang.String)
     * @see java.text.MessageFormat#format(java.lang.String, java.lang.Object[])
     * @param key       The property key.
     * @param arguments The MessageFormat arguments.
     * @return The property value formated.
     */
    public String getFormattedProperty(final String key, final Object[] arguments) {
        return MessageFormat.format(getProperty(key), arguments);
    }
}
