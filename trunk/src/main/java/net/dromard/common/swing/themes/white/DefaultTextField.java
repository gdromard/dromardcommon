/*
 * Created on 15 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.swing.themes.white;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * @author Gabriel DROMARD
 *
 * TODO Change this comment
 */
public class DefaultTextField extends JTextField {
    public DefaultTextField() {
        super();
        setBackground(DefaultCommon.BLUE);
        setBorder(DefaultCommon.BORDER_GRAY);
        setPreferredSize(DefaultCommon.DIMENSION_FIELDS);
        setSize(DefaultCommon.DIMENSION_FIELDS);
        setSelectionColor(DefaultCommon.DARK_BLUE);
        setForeground(DefaultCommon.DARK_BLUE);
        setSelectedTextColor(DefaultCommon.BACKGROUND);

        this.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent arg0) { setBorder(DefaultCommon.BORDER_FOCUSED); }
            public void focusLost(FocusEvent arg0) { setBorder(DefaultCommon.BORDER_GRAY); }
        });
    }
}
