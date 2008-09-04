package net.dromard.common.rss.atom;

import java.text.ParseException;
import java.util.Date;

public class AtomEntry {
	public static final String ENTRY = "entry";
	public static final String ID = "id";
	public static final String UPDATED = "updated";
	public static final String PUBLISHED = "published";
	public static final String CATEGORY = "category";
	public static final String TITLE = "title";
	public static final String SUBTITLE = "subtitle";
	public static final String CONTENT = "content";
	public static final String SUMMARY = "summary";

	private String id;
	private String updated;
	private String published;
	private String category;
	private Author author;
	private TypedText title = new TypedText(TITLE);
	private TypedText subtitle = new TypedText(SUBTITLE);
	private TypedText summary = new TypedText(SUMMARY);
	private TypedText content = new TypedText(CONTENT);

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the authorUri
	 */
	public Author getAuthor() {
		return author;
	}


	/**
	 * @param authorUri the authorUri to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}


	/**
	 * @return the updated
	 */
	public String getUpdated() {
		return updated;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdatedDate() throws ParseException {
		return Atom.parseDate(updated);
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = Atom.formatDate(updated);
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
	 */
	public Date getPublishedDate() throws ParseException {
		return Atom.parseDate(published);
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(String published) {
		this.published = published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(Date published) {
		this.published = Atom.formatDate(published);
	}


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
	public TypedText getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(TypedText title) {
		if (title != null) {
			this.title = title;
		}
	}


	/**
	 * @return the subtitle
	 */
	public TypedText getSubtitle() {
		return subtitle;
	}


	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(TypedText subtitle) {
		this.subtitle = subtitle;
	}


	/**
	 * @return the summary
	 */
	public TypedText getSummary() {
		return summary;
	}


	/**
	 * @param summary the summary to set
	 */
	public void setSummary(TypedText summary) {
		if (summary != null) {
			this.summary = summary;
		}
	}


	/**
	 * @return the content
	 */
	public TypedText getContent() {
		return content;
	}


	/**
	 * @param content the content to set
	 */
	public void setContent(TypedText content) {
		if (content != null) {
			this.content = content;
		}
	}


	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("-------------").append('\n');
		buf.append(ID).append(":          ").append(id).append('\n');
		buf.append(UPDATED).append(":     ").append(updated).append('\n');
		buf.append(PUBLISHED).append(":   ").append(published).append('\n');
		buf.append(TITLE).append(":       ").append(title).append('\n');
		buf.append(SUBTITLE).append(":    ").append(subtitle).append('\n');
		buf.append(CATEGORY).append(":    ").append(category).append('\n');
		buf.append(SUMMARY).append(":     ").append(summary).append('\n');
		buf.append(CONTENT).append(":     ").append(content).append('\n');
		if(author != null) buf.append(author);
		
		return buf.toString();
	}


	public String toXML(final String tabs) {
		StringBuffer buf = new StringBuffer();
		buf.append(tabs).append("<").append(ENTRY).append(">\n");
		buf.append(tabs).append("\t<").append(ID).append("\n");
		buf.append(tabs).append("\t<").append(UPDATED).append(">").append(updated).append("</" + UPDATED + ">\n");
		buf.append(tabs).append("\t<").append(PUBLISHED).append(">").append(published).append("</" + PUBLISHED + ">\n");
		buf.append(tabs).append("\t<").append(CATEGORY).append(">").append(category).append("</" + CATEGORY + ">\n");
		buf.append(title.toXML(tabs + "\t"));
		buf.append(subtitle.toXML(tabs + "\t"));
		buf.append(summary.toXML(tabs + "\t"));
		buf.append(content.toXML(tabs + "\t"));
		if(author != null) buf.append(author.toXML(tabs + "\t"));
		buf.append(tabs).append("</").append(ENTRY).append(">\n");
		
		return buf.toString();
	}
}
