package net.dromard.common.rss.atom;

import net.dromard.common.util.StringEscapeHelper;

public class TypedText {
	public String TYPE_TEXT = "text";
	public String TYPE_HTML = "html";

	private String tag = "";
	private String text = "";
	private String type = TYPE_TEXT;
	
	public TypedText(final String tag) {
		this.tag = tag;
	}
	
	public TypedText(final String tag, final String type, final String text) {
		this.tag = tag;
		setType(type);
		setText(text);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return (type.equalsIgnoreCase(TYPE_HTML)) ? StringEscapeHelper.unescapeXML(text) : text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getText();
	}

	public String toXML(final String tabs) {
		if (text.length() == 0) return text;
		return tabs + "<" + tag + " type=\"" + type + "\">" + text + "</" + tag + ">\n";
	}
}
