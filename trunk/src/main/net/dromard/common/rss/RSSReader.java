package net.dromard.common.rss;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class RSSReader {

	public RSSReader() {
		super();
	}

	private String getCharacterDataFromElement(Element e) {
		try {
			if (e != null) {
				Node child = e.getFirstChild();
				if (child instanceof CharacterData) {
					CharacterData cd = (CharacterData) child;
					return cd.getData().replaceAll("^[ \\t\\n]*(.*)[ \\t\\n]*$", "$1");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected float getFloat(String value) {
		if (value != null && !value.equals("")) {
			return Float.parseFloat(value);
		}
		return 0;
	}

	protected String getElementValue(Element parent, String label) {
		NodeList element = parent.getElementsByTagName(label);
		return getCharacterDataFromElement((Element) element.item(0));
	}

	protected String getElementAttribute(Element parent, String label, String attribute) {
		return ((Element) parent.getElementsByTagName(label).item(0)).getAttribute(attribute);
	}
}