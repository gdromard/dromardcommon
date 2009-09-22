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
	 * Create a form by adding a line
	 * @param left  The label (WEST)
	 * @param middle A component (middle)
	 * @param right  A component (EAST)
	 */
	public JForm(JComponent left, JComponent middle, JComponent right) {
		this();
		addLine(left, middle, right);
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
	public JLine addLine(JComponent left, JComponent middle, JComponent right) {
		return addLine(left, middle, right, false);
	}

	/**
	 * Add a line to the form
	 * @param left  The label (WEST)
	 * @param middle A component (middle)
	 * @param right  A component (EAST)
	 */
	public JLine addLine(JComponent left, JComponent middle, JComponent right, boolean center) {
		JLine line = new JLine(left, middle, right);
		if (!center) {
			lastLine.add(line, BorderLayout.NORTH);
			JPanel nextLine = buildPanel();
			lastLine.add(nextLine, BorderLayout.CENTER);
			lastLine = nextLine;
		} else {
			lastLine.add(line, BorderLayout.CENTER);
			JPanel nextLine = buildPanel();
			lastLine.add(nextLine, BorderLayout.SOUTH);
			lastLine = nextLine;
		}
		return line;
	}

	public class JLine extends JPanel {
		private JComponent left;
		private JComponent middle;
		private JComponent right;
		public JLine() {
			super(new BorderLayout(hgap, vgap));
			setOpaque(this.isOpaque());
			setBackground(this.getBackground());
		}
		public JLine(final JComponent left, final JComponent middle, final JComponent right) {
			this();
			addLeftComponent(left);
			addRightComponent(right);
			addMiddleComponent(middle);
		}
		private void addLeftComponent(final JComponent left) {
	        if(left != null) {
	        	if (this.left != null) {
	        		rightWidth = replaceComponent(leftComponents, this.left, left, leftWidth);
	        	} else {
	        		leftWidth = addComponent(leftComponents, left, leftWidth);
	        	}
	            add(left, BorderLayout.WEST);
				this.left = left;
	        }
		}
		private void addRightComponent(final JComponent right) {
	        if(right != null) {
	        	if (this.right != null) {
	        		rightWidth = replaceComponent(rightComponents, this.right, right, rightWidth);
	        	} else {
	        		rightWidth = addComponent(rightComponents, right, rightWidth);
	        	}
	            add(right, BorderLayout.EAST);
				this.right = right;
	        }
		}
		private void addMiddleComponent(final JComponent middle) {
	        if(middle != null) {
	        	if (this.middle != null) remove(this.middle);
	        	add(middle, BorderLayout.CENTER);
				this.middle = middle;
	        }
		}
	}

    /**
     * This methods is used to set width of left or rigth component. All the component on left must have the same width !
     * And it is the same for rigth ones.
     * 
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
    	for (JComponent cmp : components) {
			cmp.setPreferredSize(new Dimension(size, (int)cmp.getPreferredSize().getHeight()));
        }
    }

    public void refreshWidth() {
    	double leftWidth = 0;
    	for (JComponent cmp : leftComponents) {
			cmp.setPreferredSize(null);
			leftWidth = Math.max(cmp.getPreferredSize().getWidth(), leftWidth);
        }
    	double rightWidth = 0;
    	for (JComponent cmp : rightComponents) {
			cmp.setPreferredSize(null);
			rightWidth = Math.max(cmp.getPreferredSize().getWidth(), rightWidth);
        }
    	this.leftWidth = (int) leftWidth;
    	adaptWidth(leftComponents, this.leftWidth);
    	this.rightWidth = (int) rightWidth;
    	adaptWidth(rightComponents, this.rightWidth);
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
