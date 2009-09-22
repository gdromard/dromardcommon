package net.dromard.common.swing.widget;

/**
 * A pref pane item.
 * @author Gabriel Dromard
 * 9 sept. 2009
 */
public class PrefPaneItem {
    /** The name. */
    private final String name;

    public PrefPaneItem(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
