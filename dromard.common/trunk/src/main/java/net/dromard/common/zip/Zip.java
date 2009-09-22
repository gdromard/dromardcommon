package net.dromard.common.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.dromard.common.util.TimeWatch;

/**
 * Zip utility class that use ZipFile
 * See {@link ZipReader} implementation for direct inputstream management or for non UTF8 entry names.
 * @author Gabriel Dromard
 */
public class Zip {
	private ZipFile zipFile;
	private boolean isOpened = false;

	public Zip(final String zipFile) throws ZipException, IOException {
		this.zipFile = new ZipFile(zipFile);
	}

	public Zip(final File zipFile) throws ZipException, IOException {
		this.zipFile = new ZipFile(zipFile);
	}

	public List<String> getEntries() {
		isOpened = true;
		List<String> entries = new ArrayList<String>();
		for (ZipEntry entry : Collections.list(zipFile.entries())) {
			try {
				entries.add(getZipEntryName(entry.getName()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				entries.add(entry.getName());
			}
		}
		return entries;
	}

	public InputStream getInputStream(final String entry) throws IOException {
		return zipFile.getInputStream(zipFile.getEntry(entry));
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
	
	private void close() throws IOException {
		if (isOpened) {
			isOpened = false;
			zipFile.close();
		}
	}

	/**
	 * As the ZIP entry name is in cp850 encoding, this method convert the string in valid string.
	 * @param entryName The ZipEntry name.
	 * @return The entry name converted
	 * @throws UnsupportedEncodingException
	 */
	private static String getZipEntryName(String entryName) throws UnsupportedEncodingException {
		char[] characters = entryName.toCharArray();
		byte[] bytes = new byte[characters.length];
		for (int i = 0; i < characters.length; ++i) {
			bytes[i] = (byte) characters[i];
		}
		return new String(bytes, "cp850");
	}

	/**
	 * Static method that show the content of a ZIP.
	 * @param zipFilePath
	 * @throws IOException
	 */
	public static final void showZipContent(final String zipFilePath) throws IOException {
		Zip zip = new Zip(zipFilePath);
		for (String entry : zip.getEntries()) {
			System.out.println(entry);
		}
		zip.close();
	}
	
	public static void main(String[] args) throws IOException {
		Zip zip = new Zip(args[0]);
		TimeWatch watch = TimeWatch.start("test");
		for (String entry : zip.getEntries()) {
			System.out.println(entry);
		}
		System.out.println("---------------");
		watch.stop().record().print();
	}
}