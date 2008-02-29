package net.dromard.common.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This layout is the layout commonly used for displaying images preview.
 * The Flow layout is the less far to this implementation.
 * The main differences with FlowLayout is :
 * <ul>
 *  <li>All the components are displayed in a box that takes the size of the bigger one.</li>
 *  <li>When the panel that use this layout is inner a scrollPane than only the vertical scroll is displayed.</li>
 * </ul>
 * 
 * @todo Add posibility to choose positionning alignent
 * @author Gabriel Dromard
 */
public class CellFlowLayout implements LayoutManager2 {
    /** Horizontal alignment. */
	public static final float HORIZONTAL_LEFT   = 0;
    /** Horizontal alignment. */
	public static final float HORIZONTAL_CENTER = 0.5f;
    /** Horizontal alignment. */
	public static final float HORIZONTAL_RIGTH  = 1;

    /** Vertical alignment. */
	public static final float VERTICAL_TOP     = 0;
    /** Vertical alignment. */
	public static final float VERTICAL_MIDDLE  = 0.5f;
    /** Vertical alignment. */
	public static final float VERTICAL_BOTTOM  = 1;
	
    /** Horizontal alignment. */
	private float hAlignement = HORIZONTAL_CENTER;
	/** Vertical alignment. */
	private float vAlignement = VERTICAL_MIDDLE;
    /** Horizontal alignment of component in its box. */
	private float hInnerAlignement = HORIZONTAL_CENTER;
    /** Vertical alignment of component in its box. */
	private float vInnerAlignement = VERTICAL_MIDDLE;
    /** Layout horizontal Gap. */
	private int hGap = 0;
    /** Layout vertical Gap. */
	private int vGap = 0;
    /** Box width. */
	private int width;
    /** Box height. */
	private int height;
    /** Number of boxes that can be displayed in a line. */
	private int nbBoxPerLine;
    /** Number of lines needed to display all the components. */
	private int nbLines;
	/** Is the prefered size has been computed ? (It is used for handling the frame.pack() event so as to propose the more optimized size.) */
	private boolean preferedSizeComputed = false;
	
	/**
	 * Constructor.
	 * @param hgap The horizontal gap
	 * @param vgap The vertical gap
	 */
	public CellFlowLayout(final int hgap, final int vgap) {
		this.hGap = hgap;
		this.vGap = vgap;
	}
	
	/**
	 * Constructor.
	 * @param hgap        The horizontal gap
	 * @param vgap        The vertical gap
	 * @param hAlignement The horizontal alignement
	 * @param vAlignement The vertical alignement
	 */
	public CellFlowLayout(final int hgap, final int vgap, final float hAlignement, final float vAlignement) {
		this(hgap, vgap);
		this.hAlignement = hAlignement;
		this.vAlignement = vAlignement;
	}
	
	/**
	 * Default Constructor.
	 */
	public CellFlowLayout() {
		// Default initialization
	}

	/**
	 * @param alignement the HorizontalAlignement to set
	 */
	public void setHorizontalAlignement(float alignement) {
		hAlignement = alignement;
	}

	/**
	 * @param innerAlignement the HorizontalInnerAlignement to set
	 */
	public void setHorizontalInnerAlignement(float innerAlignement) {
		hInnerAlignement = innerAlignement;
	}

	/**
	 * @param alignement the VerticalAlignement to set
	 */
	public void setVerticalAlignement(float alignement) {
		vAlignement = alignement;
	}

	/**
	 * @param innerAlignement the VerticalInnerAlignement to set
	 */
	public void setVerticalInnerAlignement(float innerAlignement) {
		vInnerAlignement = innerAlignement;
	}

	/**
     * This is the methods that handle the location of each components.
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	public void layoutContainer(final Container parent) {
		// Initialize grid size
		for (int i=0; i<parent.getComponentCount(); ++i) {
			width  = Math.max(Math.max(parent.getComponent(i).getPreferredSize().width, parent.getComponent(i).getSize().width), width);
			height = Math.max(Math.max(parent.getComponent(i).getPreferredSize().height, parent.getComponent(i).getSize().height), height);
		}
		
		//    The '-1' is here so as to rearange component when the width of the panel is exactly the width need. (just less than the limit)
		//    without it when the panel is reduced the components are not put at the next line so orizontal scroll bar is shown (what is not wanted)
		float tmp = (parent.getWidth()-1) / (width+vGap);
		nbBoxPerLine = Math.max(Math.round(tmp), 1);
		int count = parent.getComponentCount();
		// The minimal line is 1
		nbLines = Math.max(Math.round((float) count / nbBoxPerLine + 0.49f), 1);
		//    The 'Math.min' is here so as to handle the case in which there is less components that the nomber of components that can be displayed.
		int preferedWidth = Math.min(count, nbBoxPerLine) * (width+hGap);
		int preferedHeight = Math.min(count, nbLines) * (height+vGap);
		int wOffset = (int) ((parent.getWidth()  - preferedWidth)  * hAlignement);
		int hOffset = (int) ((parent.getHeight() - preferedHeight) * vAlignement);
		
		// Locate the components
		for (int i=0; i<count; ++i) {
			int wInnerOffset = (int) ((width  - parent.getComponent(i).getWidth())  * hInnerAlignement);
			int hInnerOffset = (int) ((height - parent.getComponent(i).getHeight()) * vInnerAlignement);
            parent.getComponent(i).setLocation((width+hGap)*(i%nbBoxPerLine)+wInnerOffset+wOffset, (height+vGap)*(i/nbBoxPerLine)+hInnerOffset+hOffset);
		}
        //System.out.println("[DEBUG] "+(Math.round((float) count / nbBoxPerLine  + 0.49f)) + " ~ " + ((float) count / nbBoxPerLine  + 0.49f));
	}

	/**
     * Retreive the prefered size of the container using this layout.
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	public Dimension preferredLayoutSize(final Container parent) {
		if (!preferedSizeComputed) {
			double sqrt = Math.sqrt(parent.getComponentCount());
			preferedSizeComputed = (width > 0 && height > 0);
			//System.out.println("[DEBUG] "+parent.getComponentCount()+" > "+Math.round((float) sqrt+0.5f) * (width+hGap) + " ~ " + Math.round((float) sqrt) * (height+vGap));
			// The '+1' is here so as to let enought place for component to take place as wanted (just more than the limit)
			return new Dimension(Math.round((float) sqrt+0.5f) * (width+hGap) + 1, Math.round((float) sqrt) * (height+vGap));
		}
		// The 'Math.min' is here so as to handle the case in which there is less components that the nomber of components that can be displayed.
		int count = parent.getComponentCount();
		int preferedWidth = Math.min(count, nbBoxPerLine) * (width+hGap);
		int preferedHeight = Math.min(count, nbLines) * (height+vGap);
		return new Dimension(preferedWidth, preferedHeight);
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	public Dimension minimumLayoutSize(final Container parent) {
		return new Dimension(width+vGap*2, height+hGap*2);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
	 */
	public void addLayoutComponent(final String name, final Component comp) {
		// Needed for LayoutManager 
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	public void removeLayoutComponent(final Component comp) {
		// Needed for LayoutManager 
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
	 */
	public void addLayoutComponent(final Component comp, final Object arg1) {
		// Needed for LayoutManager2
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
	 */
	public float getLayoutAlignmentX(final Container parent) {
		// Needed for LayoutManager2
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
	 */
	public float getLayoutAlignmentY(final Container parent) {
		// Needed for LayoutManager2
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
	 */
	public void invalidateLayout(final Container parent) {
		// Needed for LayoutManager2
	}

	/* (non-Javadoc)
	 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 */
	public Dimension maximumLayoutSize(final Container parent) {
		// Needed for LayoutManager2
		return null;
	}
	
	/**
	 * This is an exemple of Layout.
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			final JFrame frame = new JFrame("CellFlowLayout demo");
	        frame.getContentPane().setLayout(new CellFlowLayout(2, 2));
	        for(int i=0; i<=10; ++i) {
	        	JLabel lbl = new JLabel("Demo "+i);
	        	lbl.setSize(new Dimension(50 + 10 * i, 50 + 10 * i));
	        	lbl.setPreferredSize(lbl.getSize());
	        	lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        	frame.getContentPane().add(lbl);
	        }
	        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                frame.setVisible(true);
	                frame.pack();
	                frame.setLocationRelativeTo(null);
	            }
	        });        
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
