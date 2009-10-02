package net.dromard.common.widget.prefpane;

import java.awt.Color;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import net.dromard.common.swing.SwingHelper;
import net.dromard.common.widget.prefpane.JPrefPane.ItemSelectedListener;

public class JPrefPaneDemo {

    public static void main(final String[] args) {
        JSplitPane sp = new JSplitPane();
        sp.setBackground(Color.GRAY.darker());
        sp.setDividerSize(1);
        sp.setDividerLocation(200);
        JPrefPane jPrefPane = new JPrefPane();
        PrefPaneCategory category1 = new PrefPaneCategory("cat1");
        jPrefPane.addCategory(category1);
        jPrefPane.addItem(category1, new PrefPaneItem("item1.1"));
        jPrefPane.addItem(category1, new PrefPaneItem("item1.2"));
        jPrefPane.addItem(category1, new PrefPaneItem("item1.3"));
        PrefPaneCategory category2 = new PrefPaneCategory("cat2");
        jPrefPane.addCategory(category2);
        jPrefPane.addItem(category2, new PrefPaneItem("item2.1"));
        jPrefPane.addItem(category2, new PrefPaneItem("item2.2"));
        jPrefPane.addItem(category2, new PrefPaneItem("item2.3"));

        sp.setLeftComponent(jPrefPane.getComponent());
        final JTextArea textArea = new JTextArea();
        sp.setRightComponent(textArea);

        SwingHelper.openInFrame(sp, "PrefPane demo");

        jPrefPane.addItemSelectedListener(new ItemSelectedListener() {
            @Override
            public void itemSelected(final PrefPaneItem item) {
                textArea.setText(textArea.getText() + item.getName() + " Selected\n");
            }
        });
    }
}
