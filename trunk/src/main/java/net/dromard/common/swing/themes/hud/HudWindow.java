package net.dromard.common.swing.themes.hud;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class HudWindow {

    private final JFrame fFrame = new JFrame();
    private JComponent fContentPane;
    private final TitlePanel fTitlePanel;
    private final HudPanel fHudPanel = new HudPanel();
    private static final int ROUNDED_RECT_DIAMETER = 16;

    public HudWindow() {
        this("");
    }

    public HudWindow(String title) {
        fFrame.setTitle(title);
        fTitlePanel = new TitlePanel(title);
        init();
    }

    private void init() {
        // indicate that this frame should not make all the content draggable. by default, when you
        // set the opacity to 0 (like we do below) this property automatically gets set to true.
        // also note that this client property must be set *before* changing the opacity (not sure
        // why).
        fFrame.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);

        fFrame.setUndecorated(true);
        fFrame.setAlwaysOnTop(true);
        fFrame.setVisible(true);
        fFrame.setBackground(new Color(0,0,0,0));

        fHudPanel.add(fTitlePanel, BorderLayout.NORTH);

        // set the backing frame's content pane.
        fFrame.setContentPane(fHudPanel);
        // set the HUD panel's content pane to a blank JPanel by default.
        setContentPane(new JPanel());

        // listen to the frame's title property so that we can update the label rendering the title.
        fFrame.addPropertyChangeListener("title", createTitlePropertyChangeListener());
        // listen for window focus changes so that we can repaint the entire window.
        fFrame.addWindowFocusListener(createWindowFocusRepaintListener());
        // listen for focus changes in the window so that we can update the focus state of the title
        // panel (e.g. the font color).
        fTitlePanel.addPropertyChangeListener("Frame.active", createFrameFocusPropertyChangeListener());
        new WindowDragger(fFrame, fTitlePanel);
    }

    public JFrame getJFrame() {
        return fFrame;
    }

    public JComponent getContentPane() {
        return fContentPane;
    }

    public void setContentPane(JComponent contentPane) {
        // remove the old content pane if there was one.
        if (fContentPane != null) {
            fHudPanel.remove(fContentPane);
        }
        fContentPane = contentPane;
        fHudPanel.add(fContentPane, BorderLayout.CENTER);
    }

    private PropertyChangeListener createTitlePropertyChangeListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fTitlePanel.setTitle(fFrame.getTitle());
            }
        };
    }

    private PropertyChangeListener createFrameFocusPropertyChangeListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                fTitlePanel.updateFocusState();
            }
        };
    }

    private WindowFocusListener createWindowFocusRepaintListener() {
        return new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                fFrame.repaint();
            }
            public void windowLostFocus(WindowEvent e) {
                fFrame.repaint();
            }
        };
    }

    private static class TitlePanel extends JPanel {

        private static final Color FONT_COLOR = new Color(255,255,255,255);
        private static final Color UNFOCUSED_FONT_COLOR = new Color(0xcccccc);
        private static final Color HIGHLIGHT = new Color(255,255,255,25);
        private static final Color TOP_BACKGROUND_TOP = new Color(255,255,255,59);
        private static final Color TOP_BACKGROUND_BOTTOM = new Color(196,196,196,59);
        private static final Color BOTTOM_BACKGROUND = new Color(255,255,255,30);
        private static final Color UNFOCUSED_BACKGROUND = new Color(0,0,0,10);
        private static final Icon CLOSE_ICON = new ImageIcon(TitlePanel.class.getResource("close.png"));
        private static final Icon CLOSE_HOVER_ICON = new ImageIcon(TitlePanel.class.getResource("close_hover.png"));
        private static final Icon CLOSE_PRESSED_ICON = new ImageIcon(TitlePanel.class.getResource("close_pressed.png"));
        private final JButton fCloseButton = new JButton(CLOSE_ICON);
        private final JLabel fLabel;

        private TitlePanel(String title) {
            fLabel = new JLabel(title, SwingConstants.CENTER);
            fLabel.setFont(fLabel.getFont().deriveFont(Font.BOLD, 11.0f));

            fCloseButton.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
            fCloseButton.setVerticalAlignment(SwingConstants.CENTER);
            fCloseButton.setOpaque(false);
            fCloseButton.setFocusable(false);
            fCloseButton.setBorderPainted(false);
            fCloseButton.setContentAreaFilled(false);
            fCloseButton.setRolloverIcon(CLOSE_HOVER_ICON);
            fCloseButton.setPressedIcon(CLOSE_PRESSED_ICON);

            setOpaque(false);
            setPreferredSize(new Dimension(-1,20));
            updateFocusState();

            setLayout(new BorderLayout());
            add(fCloseButton, BorderLayout.WEST);
            add(fLabel, BorderLayout.CENTER);
            add(Box.createHorizontalStrut(fCloseButton.getPreferredSize().width), BorderLayout.EAST);
        }

        private void setTitle(String title) {
            fLabel.setText(title);
        }

        private void updateFocusState() {
            Boolean focused = (Boolean) getClientProperty("Frame.active");
            fLabel.setForeground(focused == null || focused ? FONT_COLOR : UNFOCUSED_FONT_COLOR);
        }

        @Override
        protected void paintComponent(Graphics g) {
            // create a copy of the graphics object and turn on anti-aliasing.
            Graphics2D graphics2d = (Graphics2D) g.create();
            graphics2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // calculate the point in the title bar at which to change the background color.
            int midPointY = ROUNDED_RECT_DIAMETER/2 + 3;

            // if the window has focus, draw a shiny title bar.
            // else draw a flat background.
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow != null && parentWindow.hasFocus()) {
                // 1. The top half --------------------------------------------------------------//
                // create and set the shiny paint.
                GradientPaint paint =
                        new GradientPaint(0,0, TOP_BACKGROUND_TOP,0,midPointY,TOP_BACKGROUND_BOTTOM);
                graphics2d.setPaint(paint);
                // create a rounded rectangle area as big as the entire title bar, then subtract
                // off the bottom half (roughly) in order to have perfectly square edges.
                Area titleArea = new Area(new Area(new RoundRectangle2D.Double(
                        0,0,getWidth(),getHeight(),ROUNDED_RECT_DIAMETER,ROUNDED_RECT_DIAMETER)));
                titleArea.subtract(new Area(new Rectangle(0,midPointY,getWidth(),midPointY)));
                // draw the top half of the title bar (the shine).
                graphics2d.fill(titleArea);
                // 2. The bottom half -----------------------------------------------------------//
                // draw the bottom half of the title bar.
                int bottomHeight = getHeight() - midPointY;
                graphics2d.setColor(BOTTOM_BACKGROUND);
                graphics2d.fillRect(0,midPointY,getWidth(),bottomHeight);
            } else {
                // create an area that has rounded corners at the top and square corners at the
                // bottom.
                graphics2d.setColor(UNFOCUSED_BACKGROUND);
                Area titleArea = new Area(new Area(new RoundRectangle2D.Double(
                        0,0,getWidth(),getHeight(),ROUNDED_RECT_DIAMETER,ROUNDED_RECT_DIAMETER)));
                titleArea.subtract(new Area(
                        new Rectangle(0,midPointY,getWidth(),midPointY)));
                graphics2d.fill(titleArea);
                graphics2d.setColor(HIGHLIGHT);
                graphics2d.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }

            graphics2d.dispose();
        }

    }

    private static class HudPanel extends JPanel {

        private static final Color HIGHLIGHT = new Color(255,255,255,59);
        private static final Color HIGHLIGHT_BOTTOM = new Color(255,255,255,25);
        private static final Color BACKGROUND = new Color(30,30,30,216);

        private HudPanel() {
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintBorder(Graphics g) {
            // create a copy of the graphics object and turn on anti-aliasing.
            Graphics2D graphics2d = (Graphics2D) g.create();
            graphics2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // paint a border around the window that fades slightly to give a more pleasnt highlight
            // to the window edges.
            GradientPaint paint = new GradientPaint(0,0,HIGHLIGHT,0,getHeight(),HIGHLIGHT_BOTTOM);
            graphics2d.setPaint(paint);
            graphics2d.drawRoundRect(0,0,getWidth()-1,getHeight()-1,ROUNDED_RECT_DIAMETER,
                    ROUNDED_RECT_DIAMETER);

            graphics2d.dispose();
        }

        @Override
        protected void paintComponent(Graphics g) {
            // create a copy of the graphics object and turn on anti-aliasing.
            Graphics2D graphics2d = (Graphics2D) g.create();
            graphics2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics2d.setComposite(AlphaComposite.Src);

            // draw the rounded rectangle background of the window.
            graphics2d.setColor(BACKGROUND);
            graphics2d.fillRoundRect(0,0,getWidth(), getHeight(),
                    ROUNDED_RECT_DIAMETER, ROUNDED_RECT_DIAMETER);
            // tell the shadow to revalidate.
            getRootPane().putClientProperty("apple.awt.windowShadow.revalidateNow", new Object());

            graphics2d.dispose();
        }
    }

    private static class WindowDragger {

        private Window fWindow;
        private Component fComponent;
        private int dX;
        private int dY;

        public WindowDragger(Window window, Component component) {
            fWindow = window;
            fComponent = component;
            fComponent.addMouseListener(createMouseListener());
            fComponent.addMouseMotionListener(createMouseMotionListener());
        }

        private MouseListener createMouseListener() {
            return new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    Point clickPoint = new Point(e.getPoint());
                    SwingUtilities.convertPointToScreen(clickPoint, fComponent);
                    dX = clickPoint.x - fWindow.getX();
                    dY = clickPoint.y - fWindow.getY();
                }
            };
        }

        private MouseMotionAdapter createMouseMotionListener() {
            return new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point dragPoint = new Point(e.getPoint());
                    SwingUtilities.convertPointToScreen(dragPoint, fComponent);
                    fWindow.setLocation(dragPoint.x - dX, dragPoint.y - dY);
                }
            };
        }
    }
}
