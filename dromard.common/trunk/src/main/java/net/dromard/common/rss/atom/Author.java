package net.dromard.common.rss.atom;

public class Author {
	public static final String AUTHOR = "author";
	public static final String NAME = "name";
	public static final String URI = "uri";
	public static final String EMAIL = "email";

	private String name;
	private String uri;
	private String email;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (uri != null) buf.append("author uri:  ").append(uri).append('\n');
		if (name != null) buf.append("author name: ").append(name).append('\n');
		if (email != null) buf.append("author mail: ").append(email).append('\n');
		return buf.toString();
	}
	
	public String toXML(final String tabs) {
		StringBuffer buf = new StringBuffer();
		buf.append(tabs).append("<").append(AUTHOR).append(">\n");
		buf.append(tabs).append("\t<").append(NAME).append(">").append(name).append("</" + NAME + ">\n");
		buf.append(tabs).append("\t<").append(URI).append(">").append(uri).append("</" + URI + ">\n");
		buf.append(tabs).append("\t<").append(EMAIL).append(">").append(email).append("</" + EMAIL + ">\n");
		buf.append(tabs).append("</").append(AUTHOR).append(">").append('\n');

		return buf.toString();
	}
}
