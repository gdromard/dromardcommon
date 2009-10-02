package net.dromard.common.swing.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class TransparentUIDemo {
    public static void main(final String[] args) {
        JFrame frame = new JFrame("Transparent Window");
        frame.getContentPane().setLayout(new BorderLayout());
        ((JPanel) frame.getContentPane()).setUI(new TransparentUI());

        JButton button = new JButton("This is a button");
        frame.getContentPane().add("North", button);
        JLabel label = new JLabel("This is a label");
        frame.getContentPane().add("South", label);
        frame.pack();
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}