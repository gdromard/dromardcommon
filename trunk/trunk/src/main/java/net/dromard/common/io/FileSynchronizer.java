package net.dromard.common.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import net.dromard.common.util.ReflectHelper;

/**
 * File Synchronizer. <BR>
 * Used to synchronized a target file with the source one. No modification is performed on the source directory.
 * 
 * @author Gabriel Dromard
 * @date 15/11/08
 */
public class FileSynchronizer {
	/** synchronization status */
	public static char[] SYNCHRONIZATION_STATUS = new char[] { ' ', '!', '=', '-', '+', '>' };
	public static final int SYNCHRONIZATION_RESET = 0;
	public static final int SYNCHRONIZATION_ERROR = 1;
	public static final int SYNCHRONIZATION_FILES_EQUALS = 2;
	public static final int SYNCHRONIZATION_SOURCE_DELETED = 3;
	public static final int SYNCHRONIZATION_SOURCE_ADDED = 4;
	public static final int SYNCHRONIZATION_SOURCE_CHANGED = 5;

	/** The source to compare */
	private File m_source;
	/** The target to synchronized */
	private File m_target;
	/** The synchronization status */
	private int m_synchStatus = SYNCHRONIZATION_RESET;
	/** The sub files to synchronized in case of directory */
	private ArrayList<FileSynchronizer> m_childs;
	/** The target relative path for display */
	private String m_relativePath = null;
	/** The target absolute path for file creation */
	private String m_absolutePath = null;

	// Constructor
	/**
	 * Default constructor. <BR>
	 * It will create the whole directory, sub directory and file tree and launch the light synchronization (only the synchronization status is filled, no file is created, deleted or updated).
	 * 
	 * @param source The source to compare
	 * @param target The target to synchronized
	 */
	public FileSynchronizer(File source, File target) {
		m_absolutePath = target.getParent() + "/";
		initialize(source, target, "./");
	}

	/**
	 * Intern constructor used for the file tree construction
	 * 
	 * @param source The source to compare
	 * @param target The target to synchronized
	 * @param relativePath The current relative path
	 * @param absolutePath The current absolute path
	 */
	private FileSynchronizer(File source, File target, String relativePath, String absolutePath) {
		m_absolutePath = absolutePath;
		initialize(source, target, relativePath);
	}

	/**
	 * Initialize the current file synchronizer
	 * 
	 * @param source The source to compare
	 * @param target The target to synchronized
	 * @param relativePath The current relative path
	 */
	private void initialize(File source, File target, String relativePath) {
		m_source = source;
		m_target = target;
		m_relativePath = relativePath;
		m_synchStatus = synchronize();
		m_childs = new ArrayList<FileSynchronizer>();
		initChilds();
	}

	/**
	 * Initialize the childs of the current file synchronizer.<BR>
	 * This method with the addChilds, the constructor and the initialize one are recursive to get all the sub files and sub directory to synchronized.
	 */
	private void initChilds() {
		HashMap<String, File> destinationFiles = new HashMap<String, File>();
		if (getTarget() != null) {
			File[] tmp = getTarget().listFiles();
			if (tmp != null) {
				for (int i = 0; i < tmp.length; ++i) {
					destinationFiles.put(tmp[i].getName(), tmp[i]);
				}
			}
		}
		m_childs = new ArrayList<FileSynchronizer>();
		if (getSource() != null) {
			File[] tmp = getSource().listFiles();
			if (tmp != null && tmp.length > 0) {
				for (int i = 0; i < tmp.length; ++i) {
					File dest = destinationFiles.get(tmp[i].getName());
					addChild(tmp[i], dest);
					if (dest != null) {
						destinationFiles.remove(tmp[i].getName());
					}
				}
				for (String key : destinationFiles.keySet()) {
					addChild(null, destinationFiles.get(key));
				}
			}
		} else if (getTarget() != null) {
			File[] tmp = getTarget().listFiles();
			if (tmp != null && tmp.length > 0) {
				for (int i = 0; i < tmp.length; ++i) {
					addChild(null, tmp[i]);
				}
			}
		}
	}

	/**
	 * Add a new file synchronizer as a child of the current one.
	 * 
	 * @param source The source to compare
	 * @param target The target to synchronized
	 */
	private void addChild(File source, File target) {
		String relativePath = this.getRelativePath() + this.getName() + "/";
		String absolutePath = this.getAbsolutePath() + this.getName() + "/";
		FileSynchronizer subTree = new FileSynchronizer(source, target, relativePath, absolutePath);
		m_childs.add(subTree);
	}

	/**
	 * Launch the light synchronization.<BR>
	 * It will calculate the status of the synchronization for the current source and target file.
	 * 
	 * @return the synchronization status.
	 */
	private int synchronize() {
		int synchronizeStatus = SYNCHRONIZATION_RESET;

		if (!checkFileExist(this.getSource()) && !checkFileExist(this.getTarget())) {
			synchronizeStatus = SYNCHRONIZATION_FILES_EQUALS;
		} else if (!checkFileExist(this.getSource())) {
			synchronizeStatus = SYNCHRONIZATION_SOURCE_DELETED;
		} else if (!checkFileExist(this.getTarget())) {
			synchronizeStatus = SYNCHRONIZATION_SOURCE_ADDED;
		} else if (getSource().isDirectory() != getTarget().isDirectory() || getSource().isFile() != getTarget().isFile()) {
			synchronizeStatus = SYNCHRONIZATION_ERROR;
			// setSynchronizationErrorMessage("Files types are differents !!");
		} else {
			if ((getSource().isDirectory() && getTarget().isDirectory()) || (getSource().length() == getTarget().length() && getSource().lastModified() <= getTarget().lastModified())) {
				synchronizeStatus = SYNCHRONIZATION_FILES_EQUALS;
			} else {
				synchronizeStatus = SYNCHRONIZATION_SOURCE_CHANGED;
			}
		}
		return synchronizeStatus;
	}

	/**
	 * Check if the specified file (source or target) exist.
	 * 
	 * @param f The file to check
	 * @return true if it exist
	 */
	private boolean checkFileExist(File f) {
		return f != null && f.exists();
	}

	// getter
	/**
	 * Get the file source of synchronization
	 * 
	 * @return the file source of synchronization
	 */
	public File getSource() {
		return m_source;
	}

	/**
	 * Get the file target of synchronization
	 * 
	 * @return the file target of synchronization
	 */
	public File getTarget() {
		return m_target;
	}

	/**
	 * Get the status of synchronization
	 * 
	 * @return the status of synchronization
	 */
	public int getSynchStatus() {
		return m_synchStatus;
	}

	/**
	 * Get the childs of the current file synchronizer
	 * 
	 * @return the childs of the current file synchronizer
	 */
	public Iterator<FileSynchronizer> getChilds() {
		return m_childs.iterator();
	}

	/**
	 * Get the relative path
	 * 
	 * @return the relative path
	 */
	public String getRelativePath() {
		return m_relativePath;
	}

	/**
	 * Get the absolute path
	 * 
	 * @return the absolute path
	 */
	public String getAbsolutePath() {
		return m_absolutePath;
	}

	/**
	 * Get the name of the of the current file.<BR>
	 * It is calculate from the target file if exist or the source one if exist.
	 * 
	 * @return the name of the of the current file
	 */
	public String getName() {
		String result = "<no_name>";
		if (this.getTarget() != null) {
			result = this.getTarget().getName();
		} else if (this.getSource() != null) {
			result = this.getSource().getName();
		}
		return result;
	}

	// process methods
	/**
	 * Update the target file from the source one
	 * 
	 * @param source The source to compare
	 * @param target The target to synchronized
	 * @return The success of the operation
	 */
	protected boolean update(final File source, final File target) {
		if (source.isDirectory()) {
			return setLastModified(source, target);
		}
		if (source.isFile()) {
			 if (!target.canWrite()) {
				try {
					if (!((Boolean) ReflectHelper.invokeMethod(target, "setWritable", new Object[] { Boolean.TRUE })).booleanValue()) {
						System.out.println("[WARNING] Source file '" + target.getPath() + "' is read only.");
						return false;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("[WARNING] Source file '" + target.getPath() + "' is read only.");
					return false;
				}
			}
			try {
				FileHelper.copy(source, target);
				setLastModified(source, target);
				return true;
			} catch (IOException e) {
				System.out.println("[WARNING] Unable to copy '" + source.getPath() + "' to '" + target.getPath() + "'.");
				FileHelper.delete(target);
				// e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * Create the target file from the source one.
	 * 
	 * @param source The source to compare
	 * @param target The target to synchronized
	 * @return The success of the operation
	 */
	protected boolean create(final File source, final File target) {
		if (source.isDirectory()) {
			return target.mkdir();
		}
		if (source.isFile()) {
			try {
				if (!target.getParentFile().exists()) {
					if (!target.getParentFile().mkdirs()) {
						return false;
					}
				}
				if (target.createNewFile()) {
					FileHelper.copy(source, target);
					setLastModified(source, target);
					return true;
				}
			} catch (IOException e) {
				System.out.println("[WARNING] Unable to copy '" + source.getPath() + "' to '" + target.getPath() + "'.");
				FileHelper.delete(target);
				// e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * Change the last modified date of the target file to the source one
	 * 
	 * @param source The source to compare
	 * @param target The target to synchronized
	 * @return The success of the operation
	 */
	protected boolean setLastModified(final File source, final File target) {
		if (!target.setLastModified(source.lastModified()) || target.lastModified() != source.lastModified()) {
			if (!target.setLastModified(new Date().getTime())) {
				System.out.println("[WARNING] Unable to set last modification time on '" + target.getPath() + "'.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Launch the process of file creation, modification or deletion.
	 * 
	 * @param onlyDisplay verbose mode, the process is bypass, only the action to do is display.
	 */
	public void process(boolean onlyDisplay) {
		System.out.println(this.toString());
		if (this.getSynchStatus() == SYNCHRONIZATION_SOURCE_ADDED)
			System.out.println(this.getAbsolutePath() + this.getSource().getName());
		if (!onlyDisplay) {
			boolean result = true;
			switch (this.getSynchStatus()) {
			case SYNCHRONIZATION_FILES_EQUALS:
				break;
			case SYNCHRONIZATION_SOURCE_DELETED:
				result = FileHelper.delete(this.getTarget());
				break;
			case SYNCHRONIZATION_SOURCE_ADDED:
				result = create(this.getSource(), new File(this.getAbsolutePath() + this.getSource().getName()));
				break;
			case SYNCHRONIZATION_SOURCE_CHANGED:
				result = update(this.getSource(), this.getTarget());
				break;
			default:
			}
			if (!result) {
				System.out.println("  Error processing " + this.toString());
			}
		}
		for (Iterator<FileSynchronizer> it = this.getChilds(); it.hasNext();) {
			it.next().process(onlyDisplay);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(SYNCHRONIZATION_STATUS[this.getSynchStatus()]).append("]");
		for (int i = 0; i < getRelativePath().split("/").length; i++) {
			sb.append(" ");
		}
		sb.append(getRelativePath());
		sb.append(this.getName());
		return sb.toString();
	}

	/**
	 * Test method
	 * 
	 * @param args nothing
	 */
	public static void main(String[] args) {
		FileSynchronizer tree = new FileSynchronizer(new File("C:/Home/ST09747/projetLocal/forges/tester/test/source"), new File("C:/Home/ST09747/projetLocal/forges/tester/test/destination"));
		tree.process(false);
	}
}
