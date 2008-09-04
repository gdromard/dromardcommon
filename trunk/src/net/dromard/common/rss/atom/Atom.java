package net.dromard.common.rss.atom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Atom {
	public static final String FEED = "feed";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String SUBTITLE = "subtitle";
	public static final String AUTHOR = "author";
	public static final String CATEGORY = "category";
	public static final String UPDATED = "updated";
	
	private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss.");
	private String id;
	private TypedText title = new TypedText("title");
	private TypedText subtitle = new TypedText("subtitle");
	private String updated;
	private String category = "";
	private Author author;
	private List<AtomEntry> entries;

	/**
	 * @param date The string to be parsed
	 * @return The date corresponding to the string.
	 * @throws ParseException If the string is not well formatted
	 */
	public static Date parseDate(final String date) throws ParseException {
		return (date != null && date.length() > 0) ? DATE_FORMATTER.parse(date.replace('T', '.').replace('Z', '.')) : null;
	}

	/**
	 * @param date The date to be formatted into string
	 * @return The string corresponding to the date.
	 */
	public static String formatDate(final Date date) {
		return (date != null) ? DATE_FORMATTER.format(date).replaceFirst("\\.([^\\.]*)\\.", "T$1Z") : null;
	}
	
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
		if (subtitle != null) {
			this.subtitle = subtitle;
		}
	}

	/**
	 * @return the updated
	 */
	public Date getUpdatedDate() throws ParseException {
		return parseDate(updated);
	}

	/**
	 * @return the updated
	 */
	public String getUpdated() {
		return updated;
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
		this.updated = formatDate(updated);
	}

	/**
	 * @return the entries
	 */
	public List<AtomEntry> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<AtomEntry> entries) {
		this.entries = entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void addEntry(AtomEntry entry) {
		if (entries == null) {
			entries = new ArrayList<AtomEntry>();
		}
		entries.add(entry);
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
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(ID).append(":       ").append(id).append('\n');
		buf.append(TITLE).append(":    ").append(title).append('\n');
		buf.append(SUBTITLE).append(": ").append(subtitle).append('\n');
		buf.append(UPDATED).append(":  ").append(updated).append('\n');
		buf.append(CATEGORY).append(": ").append(category).append('\n');
		if (author != null) buf.append(author);
		if (entries != null) {
			for(AtomEntry entry : entries) {
				buf.append(entry);
			}
		}
		return buf.toString();
	}
	

	public String toXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<").append(FEED).append(" xmlns=\"http://www.w3.org/2005/Atom\">\n");
		buf.append("\t<").append(ID).append(">").append(id).append("</").append(ID).append(">\n");
		buf.append("\t<").append(UPDATED).append(">").append(updated).append("</").append(UPDATED).append(">\n");
		buf.append(title.toXML("\t"));
		if (subtitle != null) buf.append(subtitle.toXML("\t"));
		if (author != null) buf.append(author.toXML("\t"));
		if (entries != null) {
			for(AtomEntry entry : entries) {
				buf.append(entry.toXML("\t"));
			}
		}
		buf.append("</").append(FEED).append(">").append('\n');
		return buf.toString();
	}

}
