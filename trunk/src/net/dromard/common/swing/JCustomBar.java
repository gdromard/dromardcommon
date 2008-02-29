package net.dromard.common.swing;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class JCustomBar extends JPanel {
	private static final long serialVersionUID = 664415987125837437L;
	private Vector<CustomBarButton> buttons = new Vector<CustomBarButton>();
	
    public JCustomBar() {
    	super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setPreferredSize(new Dimension(10, 30));
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
    }
    
    public CustomBarButton addButton(String name, Action action) {
    	CustomBarButton button = new CustomBarButton(this, name, action);
    	return addButton(button);
    }
    
    public CustomBarButton addButton(CustomBarButton button) {
    	add(button);
    	buttons.add(button);
    	SwingUtilities.updateComponentTreeUI(this);
    	return button;
    }
    
    public JComponent removeButton() {
    	System.out.println("remove "+(buttons.size()-1));
    	CustomBarButton button = buttons.get(buttons.size()-1);
    	remove(button);
    	buttons.remove(buttons.size()-1);
    	SwingUtilities.updateComponentTreeUI(this);
    	return button;
    }
    
    public JComponent removeButtonsAfterMe(JComponent button) {
    	return removeButtons(buttons.lastIndexOf(button)+1);
    }
    
    public JComponent removeButtonsIncludingMe(JComponent button) {
    	return removeButtons(buttons.lastIndexOf(button));
    }
    
    public JComponent removeButtons(int index) {
    	JComponent button = null;
    	for (int i=buttons.size()-1; i>= index; --i) button = removeButton();
    	return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;
        
        // Paint background
        g2.setColor(getBackground());
        g2.fillRect(x, y, width, height);
        
        g2.setFont(getFont().deriveFont(Font.BOLD, height / 1.8f));

        // top gradient
        g2.setPaint(new GradientPaint(0.0f, y,
                                      new Color(220, 220, 220, 140),
                                      0.0f, y + height / 2.0f,
                                      new Color(220, 220, 220, 80)));
        g2.fillRect(x, y, width, height / 2);

        // bottom gradient
        g2.setPaint(new GradientPaint(0.0f, y + height / 2.0f + 5.0f,
                                      new Color(255, 255, 255, 0),
                                      0.0f, y + height,
                                      new Color(255, 255, 255, 70)));
        g2.fillRect(x, y + height / 2, width, height / 2);
    }
    
    
    @SuppressWarnings("serial")
	public static void main(String[] args) {
        //JCachedPanel panel = new JCachedPanel(new BorderLayout());
    	JCachedPanel panel = new JCachedPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        final JCustomBar bar = new JCustomBar();
        CustomLogoLabel logo = new CustomLogoLabel("Fait Main");
        bar.add(logo);
        bar.addButton("Add", new AbstractAction() {
        	public void actionPerformed(ActionEvent e) {
				bar.addButton("Remove", new AbstractAction() {
		        	public void actionPerformed(ActionEvent e) {
						bar.removeButtons(2);
					}
		        });
			}
        });
        JCustomHeader header = new JCustomHeader("Fait Main", "Réutiliser ses magazine ...", Color.GRAY, Color.WHITE, Color.BLACK);
        header.setFont(Font.decode("Haettenschweiler-PLAIN-60"));
        header.setSubTitleFont(Font.decode("Monotype Corsiva BOLD 20"));
        panel.add(header, BorderLayout.NORTH);
        JForm form = new JForm(0, 0);
        form.setPreferredSize(new Dimension(400, 400));
        form.addLine(null, bar, null);
        panel.add(form, BorderLayout.CENTER);
        SwingHelper.openInFrame(panel, "JCustomBar demo");
    }
    
    /**
     * 
     * @author Gabriel Dromard
     */
    public static class CustomBarButton extends JLabel implements MouseListener {
		private static final long serialVersionUID = 1228047528275064633L;
		protected Action action;
		protected JCustomBar parent;
		protected Color hover;
		
		public CustomBarButton(JCustomBar parent, final String name) {
    		super(name);
    		this.parent = parent;
    		setBackground(parent.getBackground());
    		setForeground(parent.getForeground());
    		setFont(parent.getFont().deriveFont(Font.BOLD, 20));
    		setPreferredSize(new Dimension(SwingHelper.getUIStringWidth(this, name, getFont())+15, 30));
    		addMouseListener(this);
			int blue  = (getForeground().getBlue()  + getBackground().getBlue())/2;
			int green = (getForeground().getGreen() + getBackground().getGreen())/2;
			int red   = (getForeground().getRed()   + getBackground().getRed())/2;
			hover = new Color(red, green, blue);
    	}

		public CustomBarButton(JCustomBar parent, final String name, final Action action) {
    		this(parent, name);
    		this.action = action;
    	}
    	
    	@Override
    	protected void paintComponent(Graphics g) {
    		Graphics2D g2 = (Graphics2D) g.create();
    		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    		
    		Insets insets = getInsets();
    		int x = insets.left;
    		int y = insets.top;
    		int width = getWidth() - insets.left - insets.right;
    		int height = getHeight() - insets.top - insets.bottom;
    		
    		g2.setColor(getForeground());
    		g2.setFont(getFont().deriveFont(Font.BOLD, height / 1.8f));
    		g2.drawString(getText(), x + 5, y + (height / 2) + g2.getFontMetrics().getAscent() / 3);
    		
    		Color color = getParent().getBackground();
    		if (color.equals(Color.BLACK)) color = Color.GRAY.darker().darker();
    		
    		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
    		
    		g2.setColor(color.brighter());
    		g2.drawLine(width-0, 0, width-(height/3)-0, height);
    		g2.setColor(color.brighter().brighter());
    		g2.drawLine(width-1, 0, width-(height/3)-1, height);
    		g2.setColor(color);
    		g2.drawLine(width-2, 0, width-(height/3)-2, height);
    		
    		g2.dispose();
    	}

		public void mouseClicked(MouseEvent arg0) {
		    // Empty block
        }
		public void mouseEntered(MouseEvent arg0) {
			setForeground(hover);
		}
		public void mouseExited(MouseEvent arg0) {
    		//setBackground(parent.getBackground());
    		setForeground(parent.getForeground());
		}
		public void mousePressed(MouseEvent arg0) {
            // Empty block
        }
		public void mouseReleased(MouseEvent arg0) {
			if (action != null) action.actionPerformed(null);
		}
    }
    
    /**
     * 
     * @author Gabriel Dromard
     */
    public static class CustomLogoLabel extends JLabel {
		private static final long serialVersionUID = 4369034983958289597L;

		public CustomLogoLabel(final String name) {
    		super(name);
    		setBackground(Color.BLACK);
    		setForeground(Color.WHITE);
    		setFont(getFont().deriveFont(Font.BOLD, 20));
    	}
    	
    	@Override
    	protected void paintComponent(Graphics g) {
    		Graphics2D g2 = (Graphics2D) g.create();
    		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    		
    		Insets insets = getInsets();
    		//int x = insets.left;
    		//int y = insets.top;
    		//int width = getWidth() - insets.left - insets.right;
    		int height = getHeight() - insets.top - insets.bottom;
    		
    		g2.setColor(getForeground());
    		int px = 8;
    		g2.setFont(getFont().deriveFont(Font.BOLD, px));
    		//int len = SwingHelper.getUIStringWidth(this, getText(), g2.getFont());
    		/**/
    		
    		while (SwingHelper.getUIStringWidth(this, getText(), g2.getFont()) > height)
    			g2.setFont(getFont().deriveFont(Font.BOLD, --px));
    		/**/
    		AffineTransform at = new AffineTransform();
    		at.setToRotation(-Math.PI/2.0, height/2, height/2);
    		g2.setTransform(at);
    		
    		//System.out.println(len + " + " + px);
    		g2.drawString(getText(), 0, 8);
    		g2.dispose();
    	}
    }
}