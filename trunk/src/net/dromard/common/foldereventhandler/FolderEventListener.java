/*
 * Created on 9 mars 2005
 * By Gabriel Dromard
 */
package net.dromard.common.foldereventhandler;

import java.util.EventListener;

/**
 * This is an abstract class used for listening folder events.
 *
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [9 mars 2005] by Gabriel Dromard
 *   - Creation.
 * </pre><br/>
 * 
 * @author Gabriel Dromard
 */
public abstract class FolderEventListener implements EventListener {
	public abstract void newFile(FileEvent newFile);
	public abstract void existingFile(FileEvent newFile);
}
