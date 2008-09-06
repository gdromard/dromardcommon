/*
 * Created on 17 oct. 06
 * By Gabriel DROMARD
 */
package net.dromard.common.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

/**
 * A class which implements a shadow border.
 * 
 * <p>Code Example: </p>
 * <pre>
 *        JPanel root = new JPanel();
 *        // Generate 4 example for each TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT and BOTTOM_RIGHT configurations
 *        for(int i=0; i<4; ++i) {
 *            JPanel test = new JPanel();
 *            test.setPreferredSize(new Dimension(100, 100));
 *            test.setBackground(Color.WHITE);
 *            test.setOpaque(false);
 *            test.setBorder(new ShadowBorder(Color.GRAY, Color.GRAY, 10, i));
 *            root.add(test);
 *        }
 *        //SwingHelper.openInFrame(root);
 *        JFrame frame = new JFrame();
 *        frame.getContentPane().setLayout(new BorderLayout());
 *        frame.getContentPane().add(root, BorderLayout.CENTER);
 *        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
 *        frame.pack();
 *        frame.setVisible(true);
 * </pre>
 */
@SuppressWarnings("serial")
public class ShadowBorder extends AbstractBorder {
    private int shadowWidth = 10;
    private int inset = 5;
    protected Color lineBorderColor = null;
    protected Color shadowColor = null;
    protected int type = 2;
    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_LEFT = 2;
    public static final int BOTTOM_RIGHT = 3;
    
    /**
     * Auto-generated main method to display this
     * JPanel inside a new JFrame.
     */
    public static void main(String[] args) {
        JPanel root = new JPanel();
        for(int i=0; i<4; ++i) {
            JPanel test = new JPanel();
            test.setPreferredSize(new Dimension(100, 100));
            test.setBackground(Color.WHITE);
            test.setOpaque(false);
            test.setBorder(new ShadowBorder(Color.GRAY, Color.GRAY, 10, i));
            root.add(test);
            test.add(new JLabel(""+i));
        }
        SwingHelper.openInFrame(root);
    }

    /**
     * Creates a shadow border with the specified shadow width and whose
     * colors will be derived from the background color of the
     * component passed into the paintBorder method.
     * @param shadowColor     The shadow color
     * @param lineBorderColor The border color
     * @param shadowWidth     The length of shadow
     * @param type            The shadow type (TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT)
     */
    public ShadowBorder(Color shadowColor, Color lineBorderColor, int shadowWidth, int type) {
        super();
        this.shadowWidth = shadowWidth;
        this.shadowColor = shadowColor;
        this.lineBorderColor = lineBorderColor;
        this.type = type;
    }

    /**
     * Creates a shadow border with the specified shadow width and whose
     * colors will be derived from the background color of the
     * component passed into the paintBorder method.
     * @param shadowColor The shadow color
     */
    public ShadowBorder(Color shadowColor) {
        super();
        this.shadowWidth = 5;
        this.shadowColor = shadowColor;
        this.lineBorderColor = null;
        this.type = BOTTOM_RIGHT;
        this.inset = 0;
    }
    
    

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    @Override
    public Insets getBorderInsets(Component c) {
    	int hPosition = shadowWidth;
    	int vPosition = shadowWidth;
    	
        if(type == TOP_LEFT) {
        	hPosition = 0;
        	vPosition = 0;
        } else if(type == TOP_RIGHT) {
        	hPosition = shadowWidth;
        	vPosition = 0;
        } else if(type == BOTTOM_LEFT) {
        	hPosition = 0;
        	vPosition = shadowWidth;
        } else if(type == BOTTOM_RIGHT) {
        	hPosition = shadowWidth;
        	vPosition = shadowWidth;
        }
        return new Insets((shadowWidth - hPosition)+inset, (shadowWidth - vPosition)+inset, hPosition+inset, vPosition+inset);
    }

	/** 
     * Reinitialize the insets parameter with this Border's current Insets. 
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
	@Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = shadowWidth+2;
        return insets;
    }

    /**
     * Returns the shadow color of the border
     * when rendered on the specified component.  If no shadow
     * color was specified at instantiation, the shadow color
     * is derived from the specified component's background color.
     * @param c the component for which the shadow may be derived
     */
    public Color getShadowColor(Component c) {
        Color shadow = getShadowColor();
        if(shadow == null) shadow = c.getBackground().darker().darker();
        return shadow;
    }

    /**
     * Returns the shadow color of the border.
     * Will return null if no shadow color was specified at instantiation.
     */
    public Color getShadowColor() {
        return shadowColor;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    @Override
    public boolean isBorderOpaque() {
        return true;
    }


    /**
     * Paints the border for the specified component with the specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    	int hPosition = shadowWidth;
    	int vPosition = shadowWidth;
    	
        if(type == TOP_LEFT) {
        	hPosition = 0;
        	vPosition = 0;
        } else if(type == TOP_RIGHT) {
        	hPosition = shadowWidth;
        	vPosition = 0;
        } else if(type == BOTTOM_LEFT) {
        	hPosition = 0;
        	vPosition = shadowWidth;
        } else if(type == BOTTOM_RIGHT) {
        	hPosition = shadowWidth;
        	vPosition = shadowWidth;
        }
    	width = width - 1;
    	height = height - 1;
        Graphics2D gg = (Graphics2D) g;
        // backup old color to be able to restore it at the end of paint
        Color oldColor = gg.getColor();
    	// Translate position
        gg.translate(x, y);
        // paint shadow
        paintShadow(c, gg, getShadowColor(c), hPosition, vPosition, width-shadowWidth, height-shadowWidth, shadowWidth);

        // Fill with background color the panel
        gg.setColor(getBackground((Container) c));
        gg.fillRect(shadowWidth - hPosition, shadowWidth - vPosition, width-shadowWidth, height-shadowWidth);
        // Draw line border
        if (lineBorderColor != null) {
	        gg.setColor(lineBorderColor);
	        gg.drawRect(shadowWidth - hPosition, shadowWidth - vPosition, width-shadowWidth, height-shadowWidth);
        }

        // Reset position
        gg.translate(-x, -y);
        // Re put oldColor
        gg.setColor(oldColor);
        gg.dispose();
    }
    
    public static Color getBackground(Container container) {
        while(!container.isOpaque() || container == null) {
            container = container.getParent();
        }
        if (container == null) return null;
        return container.getBackground();
    }
    
    /**
     * 
     * @param c           the component for which this border is being painted
     * @param gg          the paint graphics
     * @param shadowColor The color of the shadow
     * @param x           the x position of the painted border
     * @param y           the y position of the painted border
     * @param width       the width of the painted border
     * @param height      the height of the painted border
     * @param shadowWidth The shadow width 
     */
    public static void paintShadow(Component c, Graphics2D gg, Color shadowColor, int x, int y, int width, int height, int shadowWidth) {
        // Set anti aliasing
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw shadow
        AlphaComposite ac = null;
        gg.setColor(shadowColor);
        // Initial shadow width:
        int sw = width - 2*shadowWidth;
        // Initial shadow height:
        int sh = height - 2*shadowWidth;
        // Draw shadow
        for(int i=0; i<shadowWidth; ++i) {
        	ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f/(i+1));
            gg.setComposite(ac);
        	gg.fillRoundRect(x+shadowWidth-i, y+shadowWidth-i, sw + 2*i, sh + 2*i, i*2, i*2);
        }
        // Reset transparency
        ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        gg.setComposite(ac);
    }
    
	/**
	 * @return the inset
	 */
	public int getInset() {
		return inset;
	}

	/**
	 * @return the lineBorderColor
	 */
	public Color getLineBorderColor() {
		return lineBorderColor;
	}

	/**
	 * @return the shadowWidth
	 */
	public int getShadowWidth() {
		return shadowWidth;
	}

	/**
	 * @return the type (BOTTOM_RIGHT, BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT)
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param inset the inset to set
	 */
	public void setInset(int inset) {
		this.inset = inset;
	}

	/**
	 * @param lineBorderColor the Border Color
	 */
	public void setLineBorderColor(Color lineBorderColor) {
		this.lineBorderColor = lineBorderColor;
	}

	/**
	 * @param shadowColor the shadow color
	 */
	public void setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
	}

	/**
	 * @param shadowWidth the shadow width
	 */
	public void setShadowWidth(int shadowWidth) {
		this.shadowWidth = shadowWidth;
	}

	/**
	 * @param type the type to set BOTTOM_RIGHT, BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT
	 */
	public void setType(int type) {
		this.type = type;
	}
}
