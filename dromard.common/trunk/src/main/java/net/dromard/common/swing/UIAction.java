package net.dromard.common.swing;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 * Generic UI action.
 * @author Gabriel Dromard
 */
public abstract class UIAction extends AbstractAction {
    /**
     * Generated UUID.
     */
    private static final long serialVersionUID = -2009827897758243978L;

    /**
     * Main constructor.
     * @param textValue Component text value.
     * @param sIcon Component's icon ( can be null ).
     * @param actionToDo Action treatment.
     * @param runActionType Specify how action is started ( into a thread or directly ) {@link RunActionType}
     */
    public UIAction(final String textValue) {
        super(textValue);
    }

    /**
     * Main constructor.
     * @param textValue Component text value.
     * @param tooltipValueName Tooltip message ( can be "" ).
     * @param sIcon Component's icon ( can be null ).
     */
    public UIAction(final String textValue, final String tooltipValueName, final Icon sIcon) {
        super(textValue, sIcon);
        setToolTip(tooltipValueName);
    }

    /**
     * Main constructor with accelerator to access to this action.
     * @param textValue Component text value.
     * @param tooltipValueName Tooltip message ( can be null ).
     * @param sIcon Component's icon ( can be null ).
     * @param keyStroke fast keyboard access ( a keyboard key associated with mask  : ex  <code>KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK)</code>
     */
    public UIAction(final String textValue, final String tooltipValueName, final Icon sIcon, final KeyStroke keyStroke) {
        this(textValue, tooltipValueName, sIcon);
        setAccelerator(keyStroke);
    }

    /**
     * @param txt text to be displayed into component.
     */
    public final void setComponentText(final String txt) {
        putValue(Action.NAME, txt);
    }

    /**
     * @param tooltipValue Tooltip message.
     */
    public final void setToolTip(final String tooltipValue) {
        putValue(Action.SHORT_DESCRIPTION, tooltipValue);
    }

    /**
     * @param accelerator to access to this action.
     */
    public final void setAccelerator(final KeyStroke keyStroke) {
        putValue(Action.ACCELERATOR_KEY, keyStroke);
    }

    /**
     * @param icon icon that will be displayed on component.
     */
    public final void setIcon(final ImageIcon icon) {
        putValue(Action.SMALL_ICON, icon);
    }
}
