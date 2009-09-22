/*
 * Created on 8 oct. 2004
 * By Gabriel Dromard
 */
package net.dromard.common.swing;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;

/**
 * @author Gabriel Dromard
 */
public class FileChooser extends JFileChooser {
    
	/**
	 * Open frame that let the user be able to choose a file.
	 * It manage filter. 
	 * @param parent            The caller frame
	 * @param title             The FileChooser
	 * @param currentDirectory  The currentDirectory of the file chooser
	 * @param filterExtentions  Extentions that you want to filter
	 * @param filterDescription Filter description
	 * @return The file objet corresponding to the user choose.
	 * @throws IOException No comment
	 */
	public File showFileChooserOpenDialog(Component parent, String title, String currentDirectory, String[] filterExtentions, String filterDescription) {
        return showChooserOpenDialog(parent, title, currentDirectory, filterExtentions, filterDescription, JFileChooser.FILES_ONLY);
	}
    
    /**
     * Open frame that let the user be able to choose a file.
     * It manage filter. 
     * @param parent            The caller frame
     * @param title             The FileChooser
     * @param currentDirectory  The currentDirectory of the file chooser
     * @param filterExtentions  Extentions that you want to filter
     * @param filterDescription Filter description
     * @return The file objet corresponding to the user choose.
     * @throws IOException No comment
     */
    public File showFolderChooserOpenDialog(Component parent, String title, String currentDirectory, String[] filterExtentions, String filterDescription) {
        return showChooserOpenDialog(parent, title, currentDirectory, filterExtentions, filterDescription, JFileChooser.DIRECTORIES_ONLY);
    }
    
    public File showChooserOpenDialog(Component parent, String title, String currentDirectory, String[] filterExtentions, String filterDescription, int fileSelectionMode) {
        if(currentDirectory == null || currentDirectory.length() == 0) currentDirectory = ".";
        File directory = new File(currentDirectory);
        System.out.println(directory.getAbsolutePath());
        setCurrentDirectory(directory);
        if(filterExtentions != null) {
            ExtentionFileFilter filter = new ExtentionFileFilter(filterExtentions, filterDescription);
            addChoosableFileFilter(filter);
        }
        
        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(fileSelectionMode);
        setDialogTitle(title);
        
        // Récupération du nom du fichier
        if (JFileChooser.APPROVE_OPTION == showOpenDialog(parent)) {
            return getSelectedFile();
        }
        return null;
    }
}
