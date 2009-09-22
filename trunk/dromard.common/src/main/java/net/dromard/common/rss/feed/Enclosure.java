package net.dromard.common.rss.feed;

public class Enclosure {
	public static final String ENCLOSURE = "enclosure";
	public static final String TYPE = "type";
	public static final String URL = "url";
	public static final String LENGTH = "length";
	
	private String type;
	private String url;
	private int length = 0;
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
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("-------------").append('\n');
		buf.append(TYPE).append(":          ").append(type).append('\n');
		buf.append(URL).append(":   ").append(url).append('\n');
		buf.append(LENGTH).append(":       ").append(length).append('\n');
		
		return buf.toString();
	}


	public String toXML(final String tabs) {
		StringBuffer buf = new StringBuffer();
		buf.append(tabs).append("<").append(ENCLOSURE).append(">\n");
		buf.append(tabs).append("\t<").append(TYPE).append(">").append(type).append("</" + TYPE + ">\n");
		buf.append(tabs).append("\t<").append(URL).append(">").append(url).append("</" + URL + ">\n");
		buf.append(tabs).append("\t<").append(LENGTH).append(">").append(length).append("</" + LENGTH + ">\n");
		buf.append(tabs).append("</").append(ENCLOSURE).append(">\n");
		return buf.toString();
	}
	
}
