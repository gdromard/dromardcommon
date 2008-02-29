/*
 * Created on 14 avr. 2006
 * By Gabriel DROMARD
 */
package net.dromard.common.swing.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class MySplitPaneUI extends BasicSplitPaneUI {
    
    public static ComponentUI createUI(JComponent x) {
        return new MySplitPaneUI();
    }
    
    public BasicSplitPaneDivider createDefaultDivider() {
        return new MySplitPaneDivider(this);
    }
}

class MySplitPaneDivider extends BasicSplitPaneDivider {
    float strokeThickness = 2f;
    float miterLimit = 2f;
    float[] dashPattern = {2f};
    float dashPhase = 2f;
    
    
    public MySplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
        ui.getSplitPane().setContinuousLayout(true);
    }
    
    protected class DragController extends BasicSplitPaneDivider.DragController {
        public DragController(MouseEvent event) {
            super(event);
        }
    }

    public void paint(Graphics g) {
        Graphics2D gg = (Graphics2D)g;
        gg.setColor(Color.darkGray);
        gg.setStroke(new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase));        
        
        if(this.splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
            g.drawLine(getWidth()/2-7, getHeight()/2, getWidth()/2+7, getHeight()/2);
        } else {
            g.drawLine(getWidth()/2, getHeight()/2-7, getWidth()/2, getHeight()/2+7);
        }
    }
}