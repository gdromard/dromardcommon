package net.dromard.common.xml;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * This class represent a XML member and all its sub childs.
 * @since 29.04.2007
 * @author Gabriel Dromard
 */
public class XmlMember {
	protected ArrayList<XmlMember> childs = new ArrayList<XmlMember>();
	protected HashMap<String, String> attributes = new HashMap<String, String>();
	protected String xmlTagName = "simple-xmlTagName";
	protected String text = null;
	protected XmlMember parent = null;
	
	/**
	 * Constructor that instantiate a new XmlMember.
	 * @param xmlTagName The name of the xml tag.
	 */
	protected XmlMember(String xmlTagName) {
		this.xmlTagName = xmlTagName;
	}

	/**
	 * Retreive the root elemnt.
	 * @return The root image.
	 */
	protected XmlMember getRoot() {
		XmlMember tmp1 = parent;
		XmlMember tmp2 = this;
		while (tmp1 != null) {
			tmp2 = tmp1;
			tmp1 = tmp2.getParent();
		}
		return tmp2;
	}

	/**
	 * Retreive the parent image.
	 * @return the parent image.
	 */
	public XmlMember getParent() {
		return parent;
	}

	/**
	 * Set parent image.
	 * @param parent The parent image.
	 */
	protected void setParent(XmlMember parent) {
		this.parent = parent;
	}
	
	/**
	 * Add text content to this XML tag
	 * @param text
	 */
	public void setText(String text) {
		if (text == null) return;
		this.text = text.trim();
		if (this.text.length() == 0) {
			this.text = null;
		}
	}
	
	/**
	 * Retreive the text content.
	 * @return The text content.
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Add an IXmlMember to this instance.
	 * @param child An IXmlMember instance
	 */
	protected void addChild(Object child) {
		childs.add((XmlMember) child);
		((XmlMember) child).setParent(this);
	}
	
	/**
	 * Retreive he childs
	 */
	public List<XmlMember> getChilds() {
		return childs;
	}
	
	/**
	 * Add an attribute to this instance.
	 * @param name  The attribute name.
	 * @param value The attribute value.
	 */
	protected void addAttribute(String name, String value) {
		attributes.put(name, value);
	}
	
	/**
	 * Retreive an attribute.
	 * @param name  The attribute name.
	 */
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	/**
	 * Retreive the attributes.
	 * @return The element's attributes.
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	/**
	 * This print the XML structure of the object into the String Buffer. 
	 */
	protected void toXML(StringBuffer sb, int depth) {
    	appendTabs(sb, depth);
		sb.append("<"+xmlTagName);
		// Handle attributes
		if (attributes.size() > 0) {
			Iterator<String> it = attributes.keySet().iterator();
			while (it.hasNext()) {
				String name = (String) it.next();
				String value = getAttribute(name);
				if (value != null) sb.append(" "+name+"=\"").append(value).append("\"");
			}
		}
		if (childs.size() > 0 || text != null) {
			sb.append(">").append(br());
			if (text != null) {
				appendTabs(sb, depth+1);
				sb.append(text).append(br());
			}
	        for (int i = 0; i < childs.size(); i++) {
	        	XmlMember child = this.childs.get(i);
	        	child.toXML(sb, depth+1);
	        }
			appendTabs(sb, depth);
			sb.append("</"+xmlTagName+">").append(br());
		} else {
			sb.append("/>").append(br());
		}
	}
	
	/**
	 * Store this xml member to file.
	 * @param xmlFile The 
	 * @throws IOException
	 */
    public void toFile(File xmlFile) throws IOException {
        if (xmlFile == null) throw new FileNotFoundException("file is null");

        PrintWriter pw = new PrintWriter(new FileWriter(xmlFile));
        pw.print(toString());
        pw.close();
    }

    /**
     * toString implementation that return the XML string representing a XmlMember instance.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br());
        toXML(sb, 0);
        return sb.toString();
    }

	/**
	 * Util method that handle identation.
	 * @param sb    The String Buffer.
	 * @param depth The identation.
	 */
    protected static void appendTabs(StringBuffer sb, int depth) {
        while (depth-- > 0) {
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