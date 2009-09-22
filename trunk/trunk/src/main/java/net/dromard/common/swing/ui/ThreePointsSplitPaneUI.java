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

/**
 * A ThreePoints SplitPaneUI.
 * @author Gabriel Dromard
 * 16 sept. 2009
 */
public class ThreePointsSplitPaneUI extends BasicSplitPaneUI {

    public static ComponentUI createUI(final JComponent x) {
        return new ThreePointsSplitPaneUI();
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new ThreePointsSplitPaneDivider(this);
    }

    /**
     * The divider implementation.
     * @author Gabriel Dromard
     * 16 sept. 2009
     */
    private static class ThreePointsSplitPaneDivider extends BasicSplitPaneDivider {
        /** The serialVersionUID. */
        private static final long serialVersionUID = 7170734984569161813L;
        float strokeThickness = 2f;
        float miterLimit = 2f;
        float[] dashPattern = {2f};
        float dashPhase = 2f;

        public ThreePointsSplitPaneDivider(final BasicSplitPaneUI ui) {
            super(ui);
            ui.getSplitPane().setContinuousLayout(true);
        }

        protected class DragController extends BasicSplitPaneDivider.DragController {
            public DragController(final MouseEvent event) {
                super(event);
            }
        }

        @Override
        public void paint(final Graphics g) {
            Graphics2D gg = (Graphics2D) g;
            gg.setColor(Color.darkGray);
            gg.setStroke(new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase));

            if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
                g.drawLine(getWidth() / 2 - 7, getHeight() / 2, getWidth() / 2 + 7, getHeight() / 2);
            } else {
                g.drawLine(getWidth() / 2, getHeight() / 2 - 7, getWidth() / 2, getHeight() / 2 + 7);
            }
        }
    }
}