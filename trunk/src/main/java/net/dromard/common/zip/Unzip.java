package net.dromard.common.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

/**
 * Static unzip implementations.
 * @deprecated see {@link Zip} class
 * @author Gabriel Dromard
 */
public class Unzip {

	public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
		in.close();
		out.close();
	}

	public static final void unzip(final String zipFilePath, final String extractPath) throws IOException {
		Enumeration entries;
		ZipFile zipFile = new ZipFile(zipFilePath);

		entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();

			if (entry.isDirectory()) {
				// Assume directories are stored parents first then children.
				System.err.println("Extracting directory: " + entry.getName());
				// This is not robust, just for demonstration purposes.
				(new File(entry.getName())).mkdir();
				continue;
			}

			System.err.println("Extracting file: " + entry.getName());
			File zipEntryFile = new File(extractPath + "/" + entry.getName());
			zipEntryFile.getParentFile().mkdirs();
			copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(zipEntryFile)));
		}

		zipFile.close();
	}

	public static final void unzip(final String zipFilePath, final String destinationPath, final String subPath) throws IOException {
		Enumeration<? extends ZipEntry> entries;
		ZipFile zipFile = new ZipFile(zipFilePath);

		entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();

			if (entry.getName().startsWith(subPath)) {
				System.err.println("Extracting file: " + entry.getName());
				File extractedFile = new File(destinationPath + "/" + entry.getName());
				extractedFile.getParentFile().mkdirs();
				copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(extractedFile)));
			}
		}

		zipFile.close();
	}
	
	public static final void main(String[] args) throws IOException {
		String zipfile = args[0];
		unzip(zipfile, ".", "M2K - M2M - Multi2000");
		if (true) return;
		if (args.length == 1) {
			try {
				unzip(args[0], ".");
			} catch (IOException ioe) {
				System.err.println("Unhandled exception:");
				ioe.printStackTrace();
				return;
			}
			return;
		}
		System.err.println("Usage: Unzip zipfile");
	}
}