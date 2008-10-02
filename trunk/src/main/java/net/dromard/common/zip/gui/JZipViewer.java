package net.dromard.common.zip.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipFile;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class JZipViewer extends JTree {
	private static final long serialVersionUID = 1862149776227045012L;

	public JZipViewer(String zipFilePath, TreeSelectionHandler treeHandler) throws IOException {
		setModel(new ZipEntryTreeModel(new ZipFile(zipFilePath)));
		addKeyListener(treeHandler);
		addMouseListener(treeHandler);
	}
	
	public static final void main(String[] args) throws IOException {
		String zipfile = args[0];
		JTree tree = new JZipViewer(zipfile, new TreeSelectionHandler() {
			@Override
			public void select(List<ZipNode> selectedZipEntries) {
				System.out.println("---------------------");
				for (ZipNode entry : selectedZipEntries) {
					System.out.println(entry.getEntryPath());
				}
			}
		});
		openInFrame(tree, "Zip Viewer");
	}

	/**
	 * Open the given panel in a default frame.
	 * @param component The inner component of the frame.
	 * @param title The frame title.
	 */
	public static JFrame openInFrame(JComponent component, String title) {
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