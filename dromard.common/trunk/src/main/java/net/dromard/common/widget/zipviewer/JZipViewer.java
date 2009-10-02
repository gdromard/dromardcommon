package net.dromard.common.widget.zipviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * A swing component that show a zip content.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class JZipViewer extends JTree {
    /** The serialVersionUID. */
    private static final long serialVersionUID = 1862149776227045012L;

    public JZipViewer(final ZipEntryTreeModel model, final TreeSelectionHandler treeHandler) throws IOException {
        setModel(model);
        addKeyListener(treeHandler);
        addMouseListener(treeHandler);
        setRootVisible(false);
        DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
        cellRenderer.setLeafIcon(new ImageIcon(JZipViewer.class.getResource("document.gif")));
        setCellRenderer(cellRenderer);
    }

    /** The tree. */
    private static JTree tree;

    public static final void main(final String[] args) throws IOException {
        ZipEntryTreeModel zipEntryTreeModel = new ZipEntryTreeModel(new ZipFile(args[0]));
        JZipViewer.tree = new JZipViewer(zipEntryTreeModel, new TreeSelectionHandler() {
            @Override
            public void selectionDone() {
                List<ZipNode> selectedZipEntries = new ArrayList<ZipNode>();
                // Returns the last path element of the selection.
                // This method is useful only when the selection model allows a single selection.
                TreePath[] selectionPaths = JZipViewer.tree.getSelectionPaths();
                for (TreePath selectionPath : selectionPaths) {
                    DefaultMutableTreeNode zipNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
                    if (zipNode instanceof ZipNode && (zipNode.isLeaf())) {
                        selectedZipEntries.add((ZipNode) zipNode);
                    }
                }
                System.out.println("---------------------");
                for (ZipNode entry : selectedZipEntries) {
                    System.out.println(entry.getEntryPath());
                }
            }
        });
        JZipViewer.openInFrame(JZipViewer.tree, "Zip Viewer");
    }

    /**
     * Open the given panel in a default frame.
     * @param component The inner component of the frame.
     * @param title The frame title.
     */
    public static JFrame openInFrame(final JComponent component, final String title) {
        final JFrame frame = new JFrame(title);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
        });
        return frame;
    }
}