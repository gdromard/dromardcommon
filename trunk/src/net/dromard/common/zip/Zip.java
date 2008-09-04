package net.dromard.common.zip;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Zip {
	public static final void showZipContent(final String zipFilePath) throws IOException {
		Enumeration<? extends ZipEntry> entries;
		ZipFile zipFile = new ZipFile(zipFilePath);

		entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			System.err.println(entry.getName());
		}

		zipFile.close();
	}
}