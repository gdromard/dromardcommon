package net.dromard.common.swing;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Frame that allow visualisation of a HTML document.
 * @author Gabriel Dromard
 */
public class JHTMLViewer extends JFrame implements ActionListener, HyperlinkListener {
    /** Composant Swing permettant de visualiser un document. */
    private final JEditorPane viewer = new JEditorPane();
    /** Champ de saisie de l'URL ?  visualiser. */
    private final JTextField urlTextField = new JTextField();

    public JHTMLViewer() {
        // Construction de l'Interface Graphique
        // Panel en haut avec un label et le champ de saisie
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("URL : ");
        inputPanel.add(label, BorderLayout.WEST);
        inputPanel.add(urlTextField, BorderLayout.CENTER);
        // Zone scroll??e au centre avec le document
        JScrollPane scrollPane = new JScrollPane(viewer);
        // Ajout des composants ?  la fen??tre
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Mode non editable pour recevoir les clics sur les
        // liens hypertexte
        viewer.setEditable(false);
        // Ajout du listener de clic sur lien
        viewer.addHyperlinkListener(new HyperLinkAdapter(this));
        // Ajout du listener de modification de la saisie
        urlTextField.addActionListener(this);
    }

    public final void hyperlinkUpdate(final HyperlinkEvent event) {
        urlTextField.setText(event.getURL().toString());
    }

    public final void actionPerformed(final ActionEvent event) {
        loadPage(urlTextField.getText());
    }

    public final void loadPage(final String url) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            viewer.setPage(url);
            urlTextField.setText(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    public static void main(final String[] args) throws IOException {
        JHTMLViewer viewerFrame = new JHTMLViewer();
        viewerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewerFrame.loadPage(ClassLoader.getSystemResource("resources/copyright.html").toString());
        viewerFrame.setSize(400, 500);
        viewerFrame.setVisible(true);
    }
}
