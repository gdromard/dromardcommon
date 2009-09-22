package net.dromard.common.swing.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * An abstract TransferHandler for FileSystem.
 * @author Gabriel Dromard
 */
public abstract class FileTransferHandler extends TransferHandler {
    /** The serialVersionUID. */
    private static final long serialVersionUID = 3964989347574863242L;

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(final JComponent comp, final Transferable t) {
        if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }

        // Grab the tree, its model and the root node
        try {
            List<File> data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
            return importFiles(data);
        } catch (UnsupportedFlavorException ufe) {
            System.err.println("Ack! we should not be here.\nBad Flavor.");
        } catch (IOException ioe) {
            System.out.println("Something failed during import:\n" + ioe);
        }
        return false;
    }

    /**
     * You must implement the import of files.
     * If the import succeed, the return must be true.
     * @param files The files that are dropped
     * @return True if the drop was successful
     */
    protected abstract boolean importFiles(final List<File> files);

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
     */
    @Override
    public boolean canImport(final JComponent comp, final DataFlavor[] transferFlavors) {
        for (int i = 0; i < transferFlavors.length; i++) {
            if (!transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) {
                return false;
            }
        }
        return true;
    }
}