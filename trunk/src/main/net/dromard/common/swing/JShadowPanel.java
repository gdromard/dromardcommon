/*
 * Created on 17 oct. 06
 * By Gabriel DROMARD
 */
package net.dromard.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A class which implements a shadow border.
 * 
 * <p>Code Example: </p>
 * <pre>
 *        JPanel root = new JPanel();
 *        // Generate 4 example for each TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT and BOTTOM_RIGHT configurations
 *        for(int i=0; i<4; ++i) {
 *            JShadowPanel test = new JShadowPanel(new BorderLayout());
 *            test.setPreferredSize(new Dimension(100, 100));
 *            test.setBackground(Color.WHITE);
 *            root.add(test, BorderLayout.CENTER);
 *            test.setType(i);
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
public class JShadowPanel extends JPanel {
    private int shadowWidth = 10;
    protected Color lineBorderColor = null;
    protected Color shadowColor = Color.GRAY;
    protected int type = 3;
    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_LEFT = 2;
    public static final int BOTTOM_RIGHT = 3;
    
    /**
     * Auto-generated main method to display this
     * JPanel inside a new JFrame.
     */
    public static void main(String[] args) {
        JPanel root = new JCachedPanel(new FlowLayout());
        for(int i=0; i<4; ++i) {
            JShadowPanel test = new JShadowPanel(new FlowLayout());
            test.setType(i);
            test.setPreferredSize(new Dimension(100, 100));
            test.setBackground(Color.WHITE);
            test.add(new JLabel(""+i));
            root.add(test);
        }
        SwingHelper.openInFrame(root);
    }

    /**
     * Creates a shadow border with the specified shadow width and whose
     * colors will be derived from the background color of the
     * component passed into the paintBorder method.
     * @param manager The Panel layout.
     */
    public JShadowPanel(LayoutManager manager) {
        super(manager);
        //setBorder(BorderFactory.createEmptyBorder(getInsets().top, getInsets().left, getInsets().bottom, getInsets().right));
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
    public JShadowPanel(Color shadowColor, Color lineBorderColor, int shadowWidth, int type) {
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
    public JShadowPanel(Color shadowColor) {
        super();
        this.shadowWidth = 5;
        this.shadowColor = shadowColor;
        this.lineBorderColor = null;
        this.type = BOTTOM_RIGHT;
    }
    
    

    /**
     * Returns the insets of the border.
     */
    @Override
    public Insets getInsets() {
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
        return new Insets((shadowWidth - hPosition)+getInset(), (shadowWidth - vPosition)+getInset(), hPosition+getInset(), vPosition+getInset());
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
     * Paints the border for the specified component with the specified position and size.
     * @param g the paint graphics
     */
    @Override
    public void paintComponent(Graphics g) {
    	int width = getWidth();
    	int height = getHeight();
    	
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
        
        // Fill with background color of the entire panel
        gg.setColor(getBackground(this.getParent()));
        gg.fillRect(0, 0, getWidth(), getHeight());
        // paint shadow
        paintShadow(this, gg, getShadowColor(this), hPosition, vPosition, width-shadowWidth, height-shadowWidth, shadowWidth);
        
        // Fill with background color of the inner panel
        gg.setColor(getBackground(this));
        gg.fillRect(shadowWidth - hPosition, shadowWidth - vPosition, width-shadowWidth, height-shadowWidth);

        // Draw line border
        if (lineBorderColor != null) {
	        gg.setColor(lineBorderColor);
	        gg.drawRect(shadowWidth - hPosition, shadowWidth - vPosition, width-shadowWidth, height-shadowWidth);
        }

        // Re put oldColor
        gg.setColor(oldColor);
    }

    /**
     * Retreive the background color of a container.
     * If the given container is opaque, the parent's one is taken (recursivly)
     * @param container The container on which to retreive the background color.
     * @return The background color of the given container.
     */
	public static Color getBackground(Container container) {
        while(!container.isOpaque() || container == null) {
            container = container.getParent();
        }
        if (container == null) return null;
        return container.getBackground();
    }
    
    /**
     * Paint shadow of the given component.
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
        gg.setColor(shadowColor);
        // Initial shadow width:
        int sw = width - 2*shadowWidth;
        // Initial shadow height:
        int sh = height - 2*shadowWidth;
        // Draw shadow
        for(int i=0; i<=shadowWidth; ++i) {
        	int opacity = (int) (255*1f/(i+1));
        	gg.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), opacity));
        	//System.out.println("opacity: "+opacity);
        	gg.fillRoundRect(x+shadowWidth-i, y+shadowWidth-i, sw + 2*i, sh + 2*i, i*2, i*2);
        }
    }

	/**
	 * @return the inset
	 */
	public int getInset() {
		return 1;
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
;