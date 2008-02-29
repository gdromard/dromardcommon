/*
 * Created on 9 mars 2005
 * By Gabriel Dromard
 */
package net.dromard.common.foldereventhandler;

import java.io.File;

/**
 * This event is used in by the folder event listener when an event is thrown.
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
public class FileEvent {
	private File file = null;

	/**
	 * Construct a new FileEvent
	 * @param file the file on which the event is thrown
	 */
	public FileEvent(File file) {
		this.file = file;
	}

	/**
	 * Retreive the file name of the file (without the path)
	 * @return the file name of the file (without the path)
	 */
	public String getFileName() {
		return file.getName();
	}

	/**
	 * Retreive the file extention (whithout the dot)
	 * @return the file extention (whithout the dot)
	 */
	public String getFileExtention() {
		String fileName = getFileName();
		int index = fileName.lastIndexOf('.');
		if(index <= 0) return null;
		return fileName.substring(index+1, fileName.length());
	}

	/**
	 * Retreive the file name without extention
	 * @return the file name without extention
	 */
	public String getFileNameWithoutExtention() {
		String fileName = getFileName();
		int index = fileName.lastIndexOf('.');
		if(index <= 0) return fileName;
		return fileName.substring(0, index);
	}

	/**
	 * retreive the file thrown by the event
	 * @return the file thrown by the event
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Retreive the folder absolute path of file.
	 * @return The folder absolute path of file.
	 */
	public String getFolderPath() {
		return file.getParentFile().getAbsolutePath();
	}
}
