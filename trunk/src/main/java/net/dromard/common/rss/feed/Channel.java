package net.dromard.common.rss.feed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Channel {
	public static final String CHANNEL = "channel";
	public static final String LINK = "link";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String AUTHOR = "author";
	
	private String link;
	private String title;
	private String description;
	private String author;
	private List<Item> items;

	/**
	 * @return the rss link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param id the id to set
	 */
	public void setLink(String link) {
		this.link = link;
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
	 * @return the subtitle
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
	 * @return the entries
	 */
	public List<Item> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}

	/**
	 * @param items the entries to set
	 */
	public void addItem(Item item) {
		if (items == null) {
			items = new ArrayList<Item>();
		}
		items.add(item);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(LINK).append(":        ").append(link).append('\n');
		buf.append(TITLE).append(":       ").append(title).append('\n');
		buf.append(DESCRIPTION).append(": ").append(description).append('\n');
		buf.append(AUTHOR).append(":      ").append(author).append('\n');
		if (items != null) {
			for(Item entry : items) {
				buf.append(entry);
			}
		}
		return buf.toString();
	}
	

	public String toXML(final String tabs) {
		StringBuffer buf = new StringBuffer();
		buf.append(tabs).append("<").append(CHANNEL).append(">\n");
		buf.append(tabs).append("\t<").append(LINK).append(">").append(link).append("</").append(LINK).append(">\n");
		buf.append(tabs).append("\t<").append(TITLE).append(">").append(title).append("</").append(TITLE).append(">\n");
		buf.append(tabs).append("\t<").append(DESCRIPTION).append(">").append(description).append("</").append(DESCRIPTION).append(">\n");
		buf.append(tabs).append("\t<").append(AUTHOR).append(">").append(author).append("</").append(AUTHOR).append(">\n");
		if (items != null) {
			for(Item entry : items) {
				buf.append(entry.toXML(tabs + "\t"));
			}
		}
		buf.append(tabs).append("</").append(CHANNEL).append(">").append('\n');
		return buf.toString();
	}

}
