/*
 * Created on 29 juin 2005
 * By Gabriel Dromard
 */
package net.dromard.common.events;

import java.awt.Event;
import java.util.ArrayList;
import java.util.EventListener;

/**
 * This is an event hadler class.
 * To use it simply extends it.
 *
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [29/06/2005] by Gabriel Dromard
 *   - Creation.
 * </pre><br/>
 * 
 * @author Gabriel Dromard
 */
public abstract class EventHandler {
	protected ArrayList<EventListener> eventListeners = new ArrayList<EventListener>();

	public void addEventListener(EventListener eventListener) {
		addListener(eventListener);
	}
	
	public void addListener(EventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public void removeListener(EventListener eventListener) {
		eventListeners.remove(eventListener);
	}

	public ArrayList getListeners() {
		return eventListeners;
	}

	public void getClearListeners() {
		eventListeners.clear();
	}
	
	protected abstract void processEvent(Event event);
}
