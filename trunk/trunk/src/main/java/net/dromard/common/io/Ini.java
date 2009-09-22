package net.dromard.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that is able to read a ini file.
 * This implementation handle comments with ';' or '#' characters.
 *
 * @author Gabriel Dromard
 */
public class Ini {
	private static String EOL = System.getProperty("line.separator");

	private Map<String, Map<String, String>> valueByKeyBySection;
	private ByteBuffer buf;

	public Ini() throws IOException {
	}

	public void load(final String file) throws IOException {
		load(new FileInputStream(new File(file)).getChannel());
	}
	public void load(final File file) throws IOException {
		load(new FileInputStream(file).getChannel());
	}
	public void load(final FileInputStream inputStream) throws IOException {
		load(inputStream.getChannel());
	}
	public void load(final FileChannel fileChannel) throws IOException {
		valueByKeyBySection = new HashMap<String, Map<String,String>>();
		buf = ByteBuffer.allocateDirect((int) fileChannel.size());
		int rd = fileChannel.read(buf);
		buf.rewind();
		if (rd != (int) fileChannel.size()) {
			throw new IOException("[ERROR] There is an inconsistency error between the file length and the read number of bytes.");
		}
		fileChannel.close();

		String currentSection = "";
		String[] keyValuePair = new String[2];
		while ((currentSection = readNextLine(currentSection, keyValuePair)) != null) {
			Map<String, String> section = valueByKeyBySection.get(currentSection);
			if (section == null) {
				section = new HashMap<String, String>();
				valueByKeyBySection.put(currentSection, section);
			}
			section.put(keyValuePair[0], keyValuePair[1]);
		}
		
		buf.clear();
		buf = null;
	}		

	/**
	 * Read the next line in the file.
	 * @param currentSection The current section.
	 * @param keyValuePair   The keyValuePair array to be filled.
	 * @return The new current section if changed else the currentSection.
	 */
	private String readNextLine(String currentSection, String[] keyValuePair) {
		String line = "";
		while (buf.hasRemaining() || line.length() != 0) {
			if (buf.hasRemaining()) line += (char) buf.get();
			if (!buf.hasRemaining() || line.endsWith(EOL)) {
				if (buf.hasRemaining()) line = line.substring(0, line.length() - EOL.length());
				if (line.indexOf(';') > -1 && (line.indexOf(';') == 0 || line.charAt(line.indexOf(';')-1) != '\\')) line = line.substring(0, line.indexOf(';'));
				if (line.indexOf('#') > -1 && (line.indexOf('#') == 0 || line.charAt(line.indexOf('#')-1) != '\\')) line = line.substring(0, line.indexOf('#'));

				if (line.trim().length() > 0) {
					// Section
					if (line.trim().charAt(0) == '[') {
						currentSection = line.substring(line.indexOf('[') +1 , line.lastIndexOf(']'));
					} else {
						// Key value pair
						int index = line.indexOf('=');
						while (index > 0 && line.charAt(index-1) == '\\') {
							index = line.indexOf('=', index + 1);
						}
						if (index > 0) {
							keyValuePair[0] = line.substring(0, index).trim().replaceAll("\\n", "\n").replaceAll("\\\\=", "=").replaceAll("\\\\t", "\t").replaceAll("\\\\;", ";").replaceAll("\\\\#", "#");
							keyValuePair[1] = line.substring(index + 1).trim().replaceAll("\\n", "\n").replaceAll("\\\\=", "=").replaceAll("\\\\t", "\t").replaceAll("\\\\;", ";").replaceAll("\\\\#", "#");
							return currentSection;
						}
					}
				}
				line = "";
			}
		}
		return null;
	}

	/**
	 * Retrieve the value of the given key that belong in the given section.
	 * @param section The key section
	 * @param key     The key
	 * @return The value corresponding or null if the key does not exist.
	 */
	public String getValue(String section, String key) {
		Map<String, String> map = valueByKeyBySection.get(section);
		if (map == null) return null;
		return map.get(key);
	}

	/**
	 * Retrieve the entire key/value pair of a section.
	 * @param section The section
	 * @return the section's key/value pair or null if it does not exist.
	 */
	public Map<String, String> getSection(String section) {
		return valueByKeyBySection.get(section);
	}
}