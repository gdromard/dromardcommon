package net.dromard.common.swing;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This is a swing class that handle Forms by managing lines.
 * In one line you can put a label (left), a component (auto resize - middle) and an other component (rigth)
 * @author Gabriel Dromard
 */
public class JForm extends JPanel {
	public static final long serialVersionUID = 364635478;
    private ArrayList<JComponent> leftComponents = new ArrayList<JComponent>();
    private ArrayList<JComponent> rightComponents = new ArrayList<JComponent>();
	private JPanel lastLine = null;
	private int hgap, vgap;

	private int rightWidth;
	private int leftWidth;

	/**
	 * Initialization of the component
	 */
	public JForm() {
		this(0, 0);
	}

	/**
	 * Initialization of the component
	 * @param hgap the horizontal gap of the layout.
	 * @param vgap the vertical gap of the layout.
	 */
	public JForm(int hgap, int vgap) {
		super();
		this.hgap = hgap;
		this.vgap = vgap;
		this.setLayout(new BorderLayout(hgap, vgap));
		lastLine = this;
	}

	/**
	 * Centralize creation of panels
	 * @return A new instance of panel (with border layout and opaque false)
	 */
	public JPanel buildPanel() {
		JPanel panel = new JPanel(new BorderLayout(hgap, vgap));
		panel.setOpaque(this.isOpaque());
		panel.setBackground(this.getBackground());
		return panel;
	}

	/**
	 * Add a line to the form
	 * @param left  The label (WEST)
	 * @param middle A component (middle)
	 * @param right  A component (EAST)
	 */
	public void addLine(JComponent left, JComponent middle, JComponent right) {
		JPanel line = buildPanel();
        if(left != null) {
            leftWidth = addComponent(leftComponents, left, leftWidth);
            line.add(left, BorderLayout.WEST);
        }
        if(right != null) {
            rightWidth = addComponent(rightComponents, right, rightWidth);
            line.add(right, BorderLayout.EAST);
        }
        if(middle != null) {
            line.add(middle, BorderLayout.CENTER);
        }
		getLastLine().add(line, BorderLayout.NORTH);
		JPanel nextLine = buildPanel();
		getLastLine().add(nextLine, BorderLayout.CENTER);
		lastLine = nextLine;
	}


    /**
     * This methods is used to set width of left or right component. All the component on left must have the same width !
     * And it is the same for right ones.
     * 
     * @param components The ArrayList of components.
     * @param component The component to add into the ArrayList.
     * @param maxSize The current max size of the components.
     * @return The new max size of the components
     */
	private int addComponent(ArrayList<JComponent> components, JComponent component, int maxSize) {
        int size = (int)component.getPreferredSize().getWidth();

        if (size > maxSize) {
        	maxSize = size;
        	adaptWidth(components, maxSize);
        } else {
        	component.setPreferredSize(new Dimension(maxSize, (int)component.getPreferredSize().getHeight()));
        }

		components.add(component);

		return maxSize;
	}

    /**
     * This methods is used to adapt the prefered size of components of an array.
     * 
     * @param components The components.
     * @param size The new prefered size.
     */
    private void adaptWidth(ArrayList<JComponent> components, int size) {
    	JComponent cmp;
        // Set preferred size for left components
        for (int i=0; i<components.size(); i++) {
            cmp = components.get(i);
            cmp.setPreferredSize(new Dimension(size, (int)cmp.getPreferredSize().getHeight()));
        }
    }

	public JPanel getLastLine() {
		return lastLine;
	}

	/**
     * This is a way of testing this class.
     * 
     * @param args Not used.
     */
    public static void main(String[] args) {
        JForm form = new JForm(10, 10);
        form.setBackground(Color.WHITE);
        form.setOpaque(true);

        for (int i = 0; i < 10; ++i) {
            if (i == 1) {
                form.addLine(new JLabel("JLabel n°" + i), new JTextField(), null);
            } else if (i == 2) {
                form.addLine(null, new JTextField(), new JButton("JButton n°" + i));
            } else if (i == 3) {
                form.addLine(null, new JTextField(), null);
            } else if (i == 4) {
                form.addLine(new JLabel("JLabel n°" + i), null, null);
            } else if (i == 5) {
                form.addLine(null, null, new JButton("JButton n°" + i));
            } else {
                form.addLine(new JLabel("JLabel n°" + i), new JTextField(), new JButton("JButton n°" + i));
            }
        }
        SwingHelper.openInFrame(form);
    }
}
