package net.dromard.common.swing;

import javax.swing.JComponent;

public interface SwingComponent<T extends JComponent> {
    T getComponent();
}