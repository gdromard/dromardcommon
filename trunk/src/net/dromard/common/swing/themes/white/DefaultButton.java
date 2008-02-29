/*
 * Created on 15 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.swing.themes.white;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * @author Gabriel DROMARD
 * 
 * TODO Change this comment
 */
public class DefaultButton extends JButton {
    public DefaultButton() {
        super();
        
        setRolloverEnabled(true);
        setSize(DefaultCommon.DIMENSION_BUTTONS);
        setPreferredSize(DefaultCommon.DIMENSION_BUTTONS);
        
        normal();
        
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent arg0) {hover(); }
            public void mousePressed(MouseEvent arg0) { hover(); }
            public void mouseEntered(MouseEvent arg0) { hover(); }
            public void mouseReleased(MouseEvent arg0) { normal(); }
            public void mouseExited(MouseEvent arg0) { normal(); }
        });
        this.setFocusPainted(false);
    }
    
    protected void hover() {
        if(isEnabled()) {
	        setBorder(DefaultCommon.BORDER_ORANGE);
	        setBackground(DefaultCommon.BLUE);
	        setForeground(DefaultCommon.WHITE);
        }
    }
    
    protected void normal() {
        setBorder(DefaultCommon.BORDER_DARK_BLUE);
        setBackground(DefaultCommon.LIGHT_BLUE);
        setForeground(DefaultCommon.DARK_BLUE);
    }
}
