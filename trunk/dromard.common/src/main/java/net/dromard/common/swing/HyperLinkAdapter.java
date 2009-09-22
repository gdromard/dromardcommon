package net.dromard.common.swing;

import java.awt.Cursor;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * Frame that allow visualisation of a HTML document.
 * @author Gabriel Dromard
 */
public class HyperLinkAdapter implements HyperlinkListener {
    /** The listener. */
    private HyperlinkListener listener = null;

    public HyperLinkAdapter() {
    }

    /**
     * @param listener The listener who wants to be informed that a link was processed successfully.
     */
    public HyperLinkAdapter(final HyperlinkListener listener) {
        this.listener = listener;
    }

    @Override
    public final void hyperlinkUpdate(final HyperlinkEvent event) {
        JEditorPane editorPane = null;
        if (event.getSource() instanceof JEditorPane) {
            editorPane = (JEditorPane) event.getSource();
        }
        if (editorPane != null && event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (event instanceof HTMLFrameHyperlinkEvent) {
                editorPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                HTMLDocument doc = (HTMLDocument) editorPane.getDocument();
                doc.processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent) event);
                if (listener != null) {
                    listener.hyperlinkUpdate(event);
                }
                editorPane.setCursor(Cursor.getDefaultCursor());
            } else {
                try {
                    editorPane.setPage(event.getURL());
                    if (listener != null) {
                        listener.hyperlinkUpdate(event);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
