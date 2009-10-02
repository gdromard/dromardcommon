package net.dromard.common.widget.jform;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This is a swing class that handle Forms by managing lines.
 * In one line you can put a label (left), a component (auto resize - middle) and an other component (rigth)
 * @author Gabriel Dromard
 */
public class JForm extends JPanel {
    /** The serialVersionUID. */
    public static final long serialVersionUID = 364635478;
    /** The leftComponents. */
    private final ArrayList<JComponent> leftComponents = new ArrayList<JComponent>();
    /** The rightComponents. */
    private final ArrayList<JComponent> rightComponents = new ArrayList<JComponent>();
    /** The lastLine. */
    private JPanel lastLine = null;
    /** The hgap and vgap of the form. */
    private final int hgap, vgap;

    /** The right components width. */
    private int rightWidth;
    /** The left components width. */
    private int leftWidth;
    /** The insertion mode. */
    private int insertionMode = JForm.INSERTION_MODE_FROM_TOP_TO_BOTTOM;

    /** The insertion mode from top to bottom. */
    public static final int INSERTION_MODE_FROM_TOP_TO_BOTTOM = 0;
    /** The insertion mode from bottom to top. */
    public static final int INSERTION_MODE_FROM_BOTTOM_TO_TOP = 1;

    /**
     * Initialization of the component.
     */
    public JForm() {
        this(0, 0);
    }

    /**
     * Initialization of the component.
     * @param hgap the horizontal gap of the layout.
     * @param vgap the vertical gap of the layout.
     */
    public JForm(final int hgap, final int vgap) {
        super();
        this.hgap = hgap;
        this.vgap = vgap;
        setLayout(new BorderLayout(hgap, vgap));
        lastLine = this;
    }

    /**
     * Initialization of the component.
     * @param hgap the horizontal gap of the layout.
     * @param vgap the vertical gap of the layout.
     */
    public JForm(final int hgap, final int vgap, final int insertionMode) {
        this(hgap, vgap);
        this.insertionMode = insertionMode;
    }

    /**
     * Create a form by adding a line.
     * @param left  The label (WEST)
     * @param middle A component (middle)
     * @param right  A component (EAST)
     */
    public JForm(final JComponent left, final JComponent middle, final JComponent right) {
        this();
        addLine(left, middle, right);
    }

    /**
     * Create a form by adding a line.
     * @param left  The label (WEST)
     * @param middle A component (middle)
     * @param right  A component (EAST)
     * @param insertionMode the insertion mode
     */
    public JForm(final JComponent left, final JComponent middle, final JComponent right, final int insertionMode) {
        this();
        addLine(left, middle, right);
        this.insertionMode = insertionMode;
    }

    /**
     * Centralize creation of panels.
     * @return A new instance of panel (with border layout and opaque false)
     */
    private JPanel buildPanel() {
        JPanel panel = new JPanel(new BorderLayout(hgap, vgap));
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Add a line to the form.
     * @param left  The label (WEST)
     * @param middle A component (middle)
     * @param right  A component (EAST)
     */
    public final JLine addLine(final JComponent left, final JComponent middle, final JComponent right) {
        return addLine(left, middle, right, false);
    }

    /**
     * Add a line to the form.
     * @param left  The label (WEST)
     * @param middle A component (middle)
     * @param right  A component (EAST)
     */
    public final JLine addLine(final JComponent left, final JComponent middle, final JComponent right, final boolean center) {
        JLine line = new JLine(left, middle, right);
        if (insertionMode == JForm.INSERTION_MODE_FROM_TOP_TO_BOTTOM) {
            if (center) {
                lastLine.add(line, BorderLayout.CENTER);
                JPanel nextLine = buildPanel();
                lastLine.add(nextLine, BorderLayout.SOUTH);
                lastLine = nextLine;
            } else {
                lastLine.add(line, BorderLayout.NORTH);
                JPanel nextLine = buildPanel();
                lastLine.add(nextLine, BorderLayout.CENTER);
                lastLine = nextLine;
            }
        } else {
            if (center) {
                lastLine.add(line, BorderLayout.SOUTH);
                JPanel nextLine = buildPanel();
                lastLine.add(nextLine, BorderLayout.CENTER);
                lastLine = nextLine;
            } else {
                lastLine.add(line, BorderLayout.CENTER);
                JPanel nextLine = buildPanel();
                lastLine.add(nextLine, BorderLayout.NORTH);
                lastLine = nextLine;
            }
        }
        return line;
    }

    /**
     * Add a line to the form.
     * @param left  The label (WEST)
     * @param middle A component (middle)
     * @param right  A component (EAST)
     */
    public final JLine removeLine(final JComponent component) {
        Container parent = component.getParent();
        if (parent != null && parent instanceof JLine) {
            JLine toRemove = (JLine) parent;
            Container container = toRemove.getParent();
            if (container != null) {
                container.remove(toRemove);
                SwingUtilities.updateComponentTreeUI(container);
            }
        }
        return null;
    }

    /**
     * The internal JLine component.
     * @author Gabriel DROMARD
     * 30 juil. 2009
     */
    public class JLine extends JPanel {
        /** The serialVersionUID. */
        private static final long serialVersionUID = 739555442397839608L;
        /** The left. */
        private JComponent left;
        /** The middle. */
        private JComponent middle;
        /** The right. */
        private JComponent right;

        public JLine() {
            super(new BorderLayout(hgap, vgap));
            setOpaque(false);
        }

        public JLine(final JComponent left, final JComponent middle, final JComponent right) {
            this();
            addLeftComponent(left);
            addRightComponent(right);
            addMiddleComponent(middle);
        }

        private void addLeftComponent(final JComponent pLeft) {
            if (pLeft != null) {
                if (left != null) {
                    rightWidth = replaceComponent(leftComponents, left, pLeft, leftWidth);
                } else {
                    leftWidth = addComponent(leftComponents, pLeft, leftWidth);
                }
                add(pLeft, BorderLayout.WEST);
                left = pLeft;
            }
        }

        private void addRightComponent(final JComponent pRight) {
            if (pRight != null) {
                if (right != null) {
                    rightWidth = replaceComponent(rightComponents, right, pRight, rightWidth);
                } else {
                    rightWidth = addComponent(rightComponents, pRight, rightWidth);
                }
                add(pRight, BorderLayout.EAST);
                right = pRight;
            }
        }

        private void addMiddleComponent(final JComponent pMiddle) {
            if (pMiddle != null) {
                if (middle != null) {
                    remove(middle);
                }
                add(pMiddle, BorderLayout.CENTER);
                middle = pMiddle;
            }
        }

        public JComponent getMiddleComponent() {
            return middle;
        }

        public JComponent getLeftComponent() {
            return left;
        }

        public JComponent getRightComponent() {
            return right;
        }

        @Override
        public void setVisible(final boolean visible) {
            super.setVisible(visible);
            refreshWidth();
        }
    }

    /**
     * This methods is used to set width of left or rigth component. All the component on left must have the same width !
     * And it is the same for rigth ones.
     * @param components The ArrayList of components.
     * @param component The component to add into the ArrayList.
     * @param maxSize The current max size of the components.
     * @return The new max size of the components
     */
    private int replaceComponent(final ArrayList<JComponent> components, final JComponent oldComponent, final JComponent newComponent, final int maxSize) {
        components.remove(oldComponent);
        return addComponent(components, newComponent, maxSize);
    }

    /**
     * This methods is used to set width of left or rigth component. All the component on left must have the same width !
     * And it is the same for rigth ones.
     * @param components The ArrayList of components.
     * @param component The component to add into the ArrayList.
     * @param maxSize The current max size of the components.
     * @return The new max size of the components
     */
    private int addComponent(final ArrayList<JComponent> components, final JComponent component, final int maxSize) {
        int localmaxSize = maxSize;
        int size = (int) component.getPreferredSize().getWidth();

        if (size > localmaxSize) {
            localmaxSize = size;
            adaptWidth(components, localmaxSize);
        } else {
            component.setPreferredSize(new Dimension(localmaxSize, (int) component.getPreferredSize().getHeight()));
        }
        components.add(component);

        return localmaxSize;
    }

    /**
     * This methods is used to adapt the prefered size of components of an array.
     * @param components The components.
     * @param size The new prefered size.
     */
    private void adaptWidth(final ArrayList<JComponent> components, final int size) {
        for (JComponent cmp : components) {
            cmp.setPreferredSize(new Dimension(size, (int) cmp.getPreferredSize().getHeight()));
        }
    }

    public void refreshWidth() {
        double lLeftWidth = 0;
        for (JComponent cmp : leftComponents) {
            cmp.setPreferredSize(null);
            lLeftWidth = Math.max(cmp.getPreferredSize().getWidth(), lLeftWidth);
        }
        double lRightWidth = 0;
        for (JComponent cmp : rightComponents) {
            cmp.setPreferredSize(null);
            lRightWidth = Math.max(cmp.getPreferredSize().getWidth(), lRightWidth);
        }
        leftWidth = (int) lLeftWidth;
        adaptWidth(leftComponents, leftWidth);
        rightWidth = (int) lRightWidth;
        adaptWidth(rightComponents, rightWidth);
    }
}
