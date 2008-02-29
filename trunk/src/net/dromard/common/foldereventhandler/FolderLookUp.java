/*
 * Created on 9 mars 2005
 * By Gabriel Dromard
 */
package net.dromard.common.foldereventhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This is a Folder look up that throws events.
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
class FolderLookUp extends Thread {
	private long      timeIntervalInMilliSecond = 5000;
	private File      folder = null;
	private boolean   isRunning  = false;
	private ArrayList<FolderEventListener> folderEventListeners = new ArrayList<FolderEventListener>();

	public FolderLookUp(File folder) throws FileNotFoundException {
		super("FolderLookUp");
		if(!folder.exists()) throw new FileNotFoundException("Folder '"+folder.getAbsolutePath()+"' not found.");
		this.folder = folder;
	}

	public FolderLookUp(File folderPath, long timeIntervalInMilliSecond) throws FileNotFoundException {
		this(folderPath);
		if(timeIntervalInMilliSecond > 0) this.timeIntervalInMilliSecond = timeIntervalInMilliSecond;
		super.setDaemon(true);
	}

	public void addListener(FolderEventListener folderEventListener) {
		folderEventListeners.add(folderEventListener);
	}

	public void removeListener(FolderEventListener folderEventListener) {
		folderEventListeners.remove(folderEventListener);
	}

	public ArrayList getListeners() {
		return folderEventListeners;
	}

	public void getClearListeners() {
		folderEventListeners.clear();
	}

	public void stopLookUp() {
		isRunning = false;
	}

	@SuppressWarnings("unchecked")
    public void run() {
		super.run();
		isRunning = true;
		ArrayList<String> filesNames = new ArrayList<String>();
		while(isRunning()) {
			String[] list = getFolder().list();
			Arrays.sort(list, new Comparator() {
				public boolean equals(Object obj) { return false; }
				public int compare(Object o1, Object o2) {
					Long f1 = new Long(new File(getFolder().getAbsolutePath()+File.separatorChar+o1.toString()).lastModified());
					Long f2 = new Long(new File(getFolder().getAbsolutePath()+File.separatorChar+o2.toString()).lastModified());
					return (f1.compareTo(f2));
				}
			});

			// List folder content.
			for(int i=0; i<list.length; ++i) {
				// Construct event
				FileEvent event = new FileEvent(new File(getFolder().getAbsolutePath()+File.separatorChar+list[i]));

				for(int listener=0; listener<folderEventListeners.size(); ++listener) {
					// New file event
					if(!filesNames.contains(list[i])) {
						// Add file in list
						filesNames.add(list[i]);
						// Throws event to all listeners
						folderEventListeners.get(listener).newFile(event);
					} else {
						folderEventListeners.get(listener).existingFile(event);
					}
				}
			}

			// Sleep with the given interval
			try {
				sleep(timeIntervalInMilliSecond);
			} catch(InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
    public void destroy() {
		super.destroy();
		isRunning = false;
	}

	public void interrupt() {
		super.interrupt();
		isRunning = false;
	}

    public String getFolderPath() {
        return folder.getAbsolutePath();
    }

    public File getFolder() {
        return folder;
    }

	public boolean isRunning() {
		return isRunning && folderEventListeners.size() > 0;
	}
}
