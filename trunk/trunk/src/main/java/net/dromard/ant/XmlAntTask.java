package net.dromard.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.tools.ant.Task;


/**
 * This class represent a XML member and all its sub childs.
 * @since 29.04.2007
 * @author Gabriel Dromard
 */
public class XmlAntTask extends Task {
	private ArrayList<XmlAntTask> childs = new ArrayList<XmlAntTask>();
    private HashMap<String, String> attributes = new HashMap<String, String>();
    private String xmlTagName = "simple-xmlTagName";
    private String text = null;
    private XmlAntTask parent = null;

	/**
     * Constructor that instantiate a new XmlObject.
     * @param xmlTagName The name of the xml tag.
     */
	protected XmlAntTask(final String xmlTagName) {
		this.xmlTagName = xmlTagName;
	}

	/**
     * Retreive the root elemnt.
     * @return The root element.
     */
	protected final XmlAntTask getRoot() {
		XmlAntTask tmp1 = parent;
		XmlAntTask tmp2 = this;
		while (tmp1 != null) {
			tmp2 = tmp1;
			tmp1 = tmp2.getParent();
		}
		return tmp2;
	}

	/**
     * Retreive the parent element.
     * @return the parent element.
     */
	public final XmlAntTask getParent() {
		return parent;
	}

	/**
     * Set parent element.
     * @param parent The parent element.
     */
	protected final void setParent(final XmlAntTask parent) {
		this.parent = parent;
	}

	/**
     * Add text content to this XML tag.
     * @param textContent The internal text.
     */
	public final void addText(final String textContent) {
		this.text = textContent.trim();
		if (this.text.length() == 0) {
			this.text = null;
		}
	}

	/**
     * Retreive the text content.
     * @return The text content.
     */
	public final String getText() {
		return text;
	}

	/**
     * Add an IXmlMember to this instance.
     * @param child An IXmlMember instance
     */
	protected final void addChild(final XmlAntTask child) {
		childs.add(child);
		child.setParent(this);
	}

	/**
     * Retreive he childs.
     * @return the childs.
     */
	public final ArrayList getChilds() {
		return childs;
	}

	/**
     * Add an attribute key/value pair to this instance. If a null is given as value the attribute is removed.
     * @param name The attribute name.
     * @param value The attribute value.
     */
	protected final void addAttribute(final String name, final String value) {
        if(value == null) attributes.remove(name);
        else attributes.put(name, value);
	}

	/**
     * Retreive an attribute.
     * @param name The attribute name.
     */
	protected final String getAttribute(final String name) {
		return attributes.get(name);
	}

	/**
     * This print the XML structure of the object into the String Buffer.
     * @param sb    The buffer on which the XML representation has to be append.
     * @param depth The depth of the current XML element (used for indentation purpose)
     */
	protected final void toXML(final StringBuffer sb, final int depth) {
    	appendTabs(sb, depth);
		sb.append("<" + xmlTagName);
		// Handle attributes
		if (attributes.size() > 0) {
			Iterator it = attributes.keySet().iterator();
			while (it.hasNext()) {
				String name = (String) it.next();
				String value = getAttribute(name);
				if (value != null) sb.append(" " + name + "=\"").append(value.replaceAll("\"", "\\\\\"")).append("\"");
			}
		}
		if (childs.size() > 0 || text != null) {
			sb.append(">").append(br());
			if (text != null) {
				appendTabs(sb, depth + 1);
				sb.append(text).append(br());
			}
	        for (int i = 0; i < childs.size(); i++) {
	        	XmlAntTask child = childs.get(i);
	        	child.toXML(sb, depth + 1);
	        }
			appendTabs(sb, depth);
			sb.append("</" + xmlTagName + ">").append(br());
		} else {
			sb.append("/>").append(br());
		}
	}

	/**
     * Store this xml member to file.
     * @param xmlFile The
     * @throws IOException If an error occure while saving XML string to file.
     */
    public final void toFile(final File xmlFile) throws IOException {
        if (xmlFile == null) throw new FileNotFoundException("file is null");

        PrintWriter pw = new PrintWriter(new FileWriter(xmlFile));
        pw.print(toString());
        pw.close();
    }

    /**
     * toString implementation that return the XML string representing a XmlObject instance.
     * @return The XML string representation of the instance.
     */
    public final String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\"?>").append(br());
        toXML(sb, 0);
        return sb.toString();
    }

    /**
     * Util method that handle identation.
     * @param sb The String Buffer.
     * @param depth The identation.
     */
    protected static void appendTabs(final StringBuffer sb, final int depth) {
        int tmp = depth;
        while (tmp-- > 0) {
            sb.append('\t');
        }
    }

    /**
     * Util method that add a new line to buffer.
     * @return The new line.
     */
    protected static String br() {
        return "\r\n";
    }
}