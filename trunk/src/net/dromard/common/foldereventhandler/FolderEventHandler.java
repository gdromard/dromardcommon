/*
 * Created on 9 mars 2005
 * By Gabriel Dromard
 */
package net.dromard.common.foldereventhandler;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This is an implementation of FolderLookUp that listen on one folder.
 *
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [15/04/2005] by Gabriel Dromard
 *   - Manage File object instead of String.
 *
 * [09/03/2005] by Gabriel Dromard
 *   - Creation.
 * </pre><br/>
 * 
 * @author Gabriel Dromard
 */
public class FolderEventHandler extends FolderLookUp {
	public FolderEventHandler(File folder, long timeIntervalInMilliSecond) throws FileNotFoundException {
		super(folder, timeIntervalInMilliSecond);
	}

	public FolderEventHandler(String folderPath) throws FileNotFoundException {
		super(new File(folderPath));
	}

	public void addFolderEventListener(FolderEventListener folderEventListener) {
		addListener(folderEventListener);
		if(!isRunning()) new Thread(this).start();
	}

	/*
	 * ----------------------------------------------------------------------
	 * Thread ...
	 * ----------------------------------------------------------------------
	 */

	public static void main(String[] args) {
		try {
			FolderEventHandler handler = new FolderEventHandler(new File("mail/tosend"), 60000*5);
			handler.addFolderEventListener(new FolderEventListener() {
				public void newFile(FileEvent event) {
					System.out.println("newFile event was thrown for file: '"+event.getFileName()+"'");
				}
				public void existingFile(FileEvent newFile) {
					System.out.println("existingFile event was thrown for file: '"+newFile.getFileName()+"' with extention '"+newFile.getFileExtention()+"'");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
