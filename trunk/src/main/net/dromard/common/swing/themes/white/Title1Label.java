/*
 * Created on 15 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.swing.themes.white;

import javax.swing.SwingConstants;


/**
 * @author Gabriel DROMARD
 * Title 1 label definition 
 */
public class Title1Label extends DefaultLabel {
    public Title1Label() {
        super();
        setPreferredSize(DefaultCommon.TITLE1_DIMENSION);
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(DefaultCommon.TITLE1_FONT);
        setForeground(DefaultCommon.ORANGE);
        setVerticalAlignment(SwingConstants.TOP);
    }
}
