package net.dromard.common.swing.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.dromard.common.swing.JForm;
import net.dromard.common.swing.SwingComponent;
import net.dromard.common.swing.SwingHelper;
import net.dromard.common.swing.ui.GradientBackgroundPanelUI;

/**
 * A widget that handler several categorized JList.
 * @author Gabriel Dromard
 * 3 sept. 2009
 */
public class JPrefPane implements SwingComponent<JComponent> {
    /** The CATEGORY_FONT. */
    private static final Font CATEGORY_FONT = UIManager.getFont("Label.font").deriveFont(Font.BOLD, 12.0f);
    /** The ITEM_FONT. */
    private static final Font ITEM_FONT = UIManager.getFont("Label.font").deriveFont(12.0f).deriveFont(Font.BOLD);

    /** The ITEM_ACTIVE_BACKGROUND_TOP_COLOR. */
    private static final Color ITEM_ACTIVE_BACKGROUND_TOP_COLOR = new Color(0x5d94d6);
    /** The ITEM_ACTIVE_BACKGROUND_BOTTOM_COLOR. */
    private static final Color ITEM_ACTIVE_BACKGROUND_BOTTOM_COLOR = new Color(0x1956ad);

    /** The ACTIVE_BACKGROUND_COLOR. */
    private static final Color ACTIVE_BACKGROUND_COLOR = new Color(0xd6dde5);
    /** The INACTIVE_BACKGROUND_COLOR. */
    //private static final Color INACTIVE_BACKGROUND_COLOR = new Color(0xe8e8e8);
    /** The CATEGORY_FONT_COLOR. */
    private static final Color CATEGORY_FONT_COLOR = new Color(0x606e80);
    /** The ITEM_FONT_COLOR. */
    private static final Color ITEM_FONT_COLOR = Color.BLACK;
    /** The ITEM_SELECTED_FONT_COLOR. */
    private static final Color ITEM_SELECTED_FONT_COLOR = Color.WHITE;

    /** The main. */
    private final JForm main;
    /** The categoryPanelByCategoryName. */
    private final HashMap<PrefPaneCategory, JPanel> categoryPanelByCategoryName = new HashMap<PrefPaneCategory, JPanel>();
    /** The itemsPanelByCategoryName. */
    private final HashMap<PrefPaneCategory, JList> itemsPanelByCategoryName = new HashMap<PrefPaneCategory, JList>();
    /** The listeners. */
    private final ArrayList<ItemSelectedListener> listeners = new ArrayList<ItemSelectedListener>();

    public JPrefPane() {
        main = new JForm();
    }

    /**
     * @param listener The ItemSelectedListener
     */
    public void addItemSelectedListener(final ItemSelectedListener listener) {
        listeners.add(listener);
    }

    /**
     * @param category The category.
     */
    public void addCategory(final PrefPaneCategory category) {
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setOpaque(false);
        JList items = new JList();
        items.setOpaque(false);
        items.setCellRenderer(new PrefPaneListCellRenderer());
        items.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(final FocusEvent e) {
            }

            @Override
            public void focusGained(final FocusEvent e) {
                for (JList list : itemsPanelByCategoryName.values()) {
                    if (list != (JList) e.getSource()) {
                        list.clearSelection();
                    }
                }
            }
        });
        items.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && ((JList) e.getSource()).getSelectedValue() != null) {
                    fireItemSelected((PrefPaneItem) ((JList) e.getSource()).getSelectedValue());
                }
            }
        });
        items.setModel(new SimpleListModel());
        JLabel categoryLbl = new JCategoryLabel(category.toString());
        categoryLbl.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        categoryLbl.setFont(JPrefPane.CATEGORY_FONT);
        categoryLbl.setForeground(JPrefPane.CATEGORY_FONT_COLOR);
        categoryLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JList jList = itemsPanelByCategoryName.get(category);
                    jList.setVisible(!jList.isVisible());
                }
            }
        });
        categoryPanel.add(categoryLbl, BorderLayout.NORTH);
        categoryPanel.add(items, BorderLayout.CENTER);
        //category.add(SwingHelper.createEmptyComponent(30, 1), BorderLayout.WEST);
        categoryPanelByCategoryName.put(category, categoryPanel);
        itemsPanelByCategoryName.put(category, items);
        main.addLine(null, categoryPanel, null);
    }

    private void fireItemSelected(final PrefPaneItem item) {
        for (ItemSelectedListener listener : listeners) {
            listener.itemSelected(item);
        }
    }

    public void addItem(final PrefPaneCategory categoryName, final PrefPaneItem itemName) {
        ((SimpleListModel) itemsPanelByCategoryName.get(categoryName).getModel()).add(itemName);
    }

    /**
     * @param category The Category
     * @param item
     */
    public void selectItem(final PrefPaneCategory category, final PrefPaneItem item) {
        itemsPanelByCategoryName.get(category).setSelectedIndex(((SimpleListModel) itemsPanelByCategoryName.get(category).getModel()).getElementIndex(item));
    }

    /**
     * @return The PrefPane component.
     */
    public JComponent getComponent() {
        return main;
    }

    /**
     * The definition of ItemSelectedListener.
     * @author Gabriel Dromard
     * 3 sept. 2009
     */
    public interface ItemSelectedListener {
        /** @param item The selected item. */
        void itemSelected(PrefPaneItem item);
    }

    private static JComponent createEmptyComponent(final int width, final int height) {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        Dimension size = new Dimension(width, height);
        emptyPanel.setSize(size);
        emptyPanel.setPreferredSize(size);
        emptyPanel.setMinimumSize(size);
        return emptyPanel;
    }

    /**
     * Renderer of list items.
     * @author Gabriel Dromard
     * 3 sept. 2009
     */
    class PrefPaneListCellRenderer implements ListCellRenderer {
        /** The cellContainer. */
        private final JForm cellContainer = new JForm();
        /** The cellLabel. */
        private final JLabel cellLabel = new JLabel();

        /**
         * Constructor.
         */
        public PrefPaneListCellRenderer() {
            cellContainer.setUI(new GradientBackgroundPanelUI(JPrefPane.ITEM_ACTIVE_BACKGROUND_TOP_COLOR, JPrefPane.ITEM_ACTIVE_BACKGROUND_BOTTOM_COLOR));
            cellContainer.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            cellContainer.setOpaque(true);
            cellContainer.setBackground(JPrefPane.ITEM_ACTIVE_BACKGROUND_TOP_COLOR);
            cellContainer.addLine(JPrefPane.createEmptyComponent(30, 1), cellLabel, null);
            cellLabel.setFont(JPrefPane.ITEM_FONT);
            cellLabel.setOpaque(false);
        }

        /* (non-Javadoc)
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
            cellLabel.setText(value.toString());

            if (isSelected) {
                cellContainer.setOpaque(true);
                cellLabel.setForeground(JPrefPane.ITEM_SELECTED_FONT_COLOR);
            } else {
                cellContainer.setOpaque(false);
                cellLabel.setForeground(JPrefPane.ITEM_FONT_COLOR);
            }
            return cellContainer;
        }
    }

    /**
     * The JList model implementation.
     * @author Gabriel Dromard
     * 3 sept. 2009
     */
    class SimpleListModel extends AbstractListModel {
        /** The serialVersionUID. */
        private static final long serialVersionUID = 5813272157637933072L;
        /** The list items. */
        private final Vector<Object> elements = new Vector<Object>();

        /* (non-Javadoc)
         * @see javax.swing.ListModel#getSize()
         */
        @Override
        public int getSize() {
            return elements.size();
        }

        /**
         * @param item The item on which to retrieve index.
         * @return The index of the given item.
         */
        public int getElementIndex(final Object item) {
            for (int i = 0; i < elements.size(); ++i) {
                if (elements.get(i) == item) {
                    return i;
                }
            }
            return -1;
        }

        /* (non-Javadoc)
         * @see javax.swing.ListModel#getElementAt(int)
         */
        @Override
        public Object getElementAt(final int index) {
            return elements.get(index);
        }

        /**
         * @param item The item to be added.
         */
        public void add(final Object item) {
            elements.add(item);
        }
    }

    /**
     * The JLabel of category item.
     * It add the arrow painting and handling.
     * @author Gabriel Dromard
     * 3 sept. 2009
     */
    class JCategoryLabel extends JLabel {
        /** The serialVersionUID. */
        private static final long serialVersionUID = -1346303524486902569L;
        /** Save the state of the category. */
        private boolean opened = true;

        public JCategoryLabel(final String text) {
            super(text);
            setOpaque(false);
            setForeground(Color.GRAY);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        flip();
                    }
                }
            });
        }

        /** Flip category state. */
        private void flip() {
            opened = !opened;
            repaint();
        }

        /* (non-Javadoc)
         * @see javax.swing.JComponent#paint(java.awt.Graphics)
         */
        @Override
        public void paint(final Graphics g) {
            Rectangle bounds = g.getClip().getBounds();
            super.paint(g.create(bounds.x + 20, bounds.y, bounds.width - 10, bounds.height));
            int arrowWidth = 10;
            g.setColor(Color.GRAY);
            if (opened) {
                g.fillPolygon(//
                        new int[]{bounds.x + 1, bounds.x + 1 + arrowWidth / 2, bounds.x + 1 + arrowWidth}, //
                        new int[]{bounds.y + 5, bounds.y + 5 + arrowWidth, bounds.y + 5}, 3);
            } else {
                g.fillPolygon(//
                        new int[]{bounds.x + 1, bounds.x + 1 + arrowWidth, bounds.x + 1}, //
                        new int[]{bounds.y + 5, bounds.y + 5 + arrowWidth / 2, bounds.y + 5 + arrowWidth}, 3);
            }
        }
    }

    public static void main(final String[] args) {
        JSplitPane sp = new JSplitPane();
        sp.setBackground(Color.GRAY.darker());
        sp.setDividerSize(1);
        sp.setDividerLocation(200);
        JPrefPane jPrefPane = new JPrefPane();
        PrefPaneCategory category1 = new PrefPaneCategory("cat1");
        jPrefPane.addCategory(category1);
        jPrefPane.addItem(category1, new PrefPaneItem("item1"));
        jPrefPane.addItem(category1, new PrefPaneItem("item2"));
        jPrefPane.addItem(category1, new PrefPaneItem("item3"));
        PrefPaneCategory category2 = new PrefPaneCategory("cat2");
        jPrefPane.addCategory(category2);
        jPrefPane.addItem(category2, new PrefPaneItem("item1"));
        jPrefPane.addItem(category2, new PrefPaneItem("item2"));
        jPrefPane.addItem(category2, new PrefPaneItem("item3"));

        sp.setLeftComponent(jPrefPane.getComponent());
        jPrefPane.getComponent().setBackground(JPrefPane.ACTIVE_BACKGROUND_COLOR);
        sp.setRightComponent(new JTextArea());

        SwingHelper.openInFrame(sp, "PrefPane demo");
    }
}
