package net.dromard.common.widget.infinityprogress;

import javax.swing.JComponent;

import net.dromard.common.swing.SwingComponent;

public class InfiniteProgress implements SwingComponent<JComponent> {
    private final InfiniteProgressComponent component = new InfiniteProgressComponent();

    public void setWidth(final int width) {
        component.setWidth(width);
    }

    public void start() {
        component.start();
    }

    public void stop() {
        component.stop();
    }

    @Override
    public JComponent getComponent() {
        return component;
    }
}