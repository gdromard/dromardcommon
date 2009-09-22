package net.dromard.common.rss.feed;


public class Item {
	public static final String ITEM = "item";
	public static final String GUID = "guid";
	public static final String PUBLISHED = "pubDate";
	public static final String CATEGORY = "category";
	public static final String LINK = "link";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String AUTHOR = "author";
	
	private String guid;
	private String published;
	private String category;
	private String link;
	private String author;
	private String title;
	private String description;
	private Enclosure enclosure; 

	/**
	 * @return the id
	 */
	public String getGuid() {
		return guid;
	}


	/**
	 * @param guid the id to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}


	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}


	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the published
	 */
	public String getPublished() {
		return published;
	}

	/**
	 * @return the published
	 * @throws ParseException If the date is not well formatted
	public Date getPublishedDate() throws ParseException {
		return RSS.parseDate(published);
	}
	 */

	/**
	 * @param published the published to set
	 */
	public void setPublished(String published) {
		this.published = published;
	}

	/**
	 * @param published the published to set
	public void setPublished(Date published) {
		this.published = RSS.formatDate(published);
	}
	 */


	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}


	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}


	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setEnclosure(Enclosure enclosure) {
		this.enclosure = enclosure;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("-------------").append('\n');
		buf.append(GUID).append(":          ").append(guid).append('\n');
		buf.append(PUBLISHED).append(":   ").append(published).append('\n');
		buf.append(TITLE).append(":       ").append(title).append('\n');
		buf.append(LINK).append(":    ").append(link).append('\n');
		buf.append(CATEGORY).append(":    ").append(category).append('\n');
		buf.append(DESCRIPTION).append(":     ").append(description).append('\n');
		buf.append(AUTHOR).append(":     ").append(author).append('\n');
		
		return buf.toString();
	}


	public String toXML(final String tabs) {
		StringBuffer buf = new StringBuffer();
		buf.append(tabs).append("<").append(ITEM).append(">\n");
		buf.append(tabs).append("\t<").append(GUID).append(">").append(guid).append("</" + GUID + ">\n");
		buf.append(tabs).append("\t<").append(AUTHOR).append(">").append(author).append("</" + AUTHOR + ">\n");
		buf.append(tabs).append("\t<").append(PUBLISHED).append(">").append(published).append("</" + PUBLISHED + ">\n");
		buf.append(tabs).append("\t<").append(CATEGORY).append(">").append(category).append("</" + CATEGORY + ">\n");
		buf.append(tabs).append("\t<").append(TITLE).append(">").append(title).append("</" + TITLE + ">\n");
		buf.append(tabs).append("\t<").append(DESCRIPTION).append(">").append(description).append("</" + DESCRIPTION + ">\n");
		if (enclosure != null) {
			buf.append(enclosure.toXML(tabs + "\t"));
		}
		buf.append(tabs).append("</").append(ITEM).append(">\n");
		return buf.toString();
	}


	public Enclosure getEnclosure() {
		return enclosure;
	}
}
