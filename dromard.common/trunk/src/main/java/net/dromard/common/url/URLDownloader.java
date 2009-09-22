package net.dromard.common.url;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import net.dromard.common.io.StreamHelper;

import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

/**
 * Downloader class
 * @author Gabriel Dromard
 * @version 1.0
 */
public class URLDownloader {

	public static void main(String[] args) {
		try {
			if(args.length>1) {
				if(args[0].equalsIgnoreCase("download")) {
					if(args.length==3) {
						download(args[1], args[2], null, -1);
					} else if(args.length==5) {
						download(args[1], args[2], args[3], Integer.parseInt(args[4]));
					} else {
						System.out.println("java net.url.URLDownloader download url fileName [proxyHost ProxyPortNumber]");
					}
				} else if(args[0].equalsIgnoreCase("upload")) {
					if(args.length==6) {
						upload(args[1], args[2], args[3], args[4], args[5]);
					} else {
						System.out.println("java net.url.URLDownloader upload ftpHostName ftpUserName ftpPassword, directory, fileName");
					}
				} else {
					System.out.println("java net.url.URLDownloader download url fileName [proxyHost ProxyPortNumber]");
					System.out.println("   or   ");
					System.out.println("java net.url.URLDownloader upload ftpHostName ftpUserName ftpPassword, directory, fileName");
				}
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Download a file from the WWW
	 * @param sURL      URL to download
	 * @param fileName  File name of downloaded file
	 * @param proxyHost Proxy hostname
	 * @param proxyPort Proxy port number
	 * @throws IOException
	 */
	public static void download(String sURL, String fileName, String proxyHost, int proxyPort) throws IOException {
		File fichier=null;
		HttpURLConnectionViaProxy urlConnection = null;

		URL url=new URL(sURL);
		urlConnection = new HttpURLConnectionViaProxy(url, proxyHost, proxyPort);
		urlConnection.connect();

		// Open file
		fichier=new File(fileName);
		if(!fichier.exists()) {
			fichier.createNewFile();
		} else {
			File save=new File(fileName+".save");
			if(save.exists()) save.delete();
			fichier.renameTo(save);
			System.out.println("File '"+fichier.getName()+"' renamed to '"+save.getName()+"'");
			fichier.createNewFile();
		}

		// download url
		urlConnection.download(fichier);
		System.out.println("Download of file '"+fichier.getAbsoluteFile()+"' Done");
	}

	/**
	 * Upload a file to an FTP server
	 * @param file        The file to download
	 * @param ftpHostName FTP hostname
	 * @param userName    FTP user name
	 * @param password    FTP password
	 * @param directory   Remote directory, where to put save file.
	 * @throws IOException
	 */
	public static void upload(String ftpHostName, String userName, String password, String directory, String fileName) throws IOException {
		File file = new File(fileName);
		FtpClient ftpClient = new FtpClient(ftpHostName);
		ftpClient.login(userName, password);
		ftpClient.cd(directory);
		TelnetOutputStream out = ftpClient.put(file.getName());
		FileInputStream in = new FileInputStream(file);
		StreamHelper.streamCopier(in, out);
		out.close();
		in.close();
		System.out.println("Upload of file '"+fileName+"' to FTP server '"+ftpHostName+"' Done");
	}
}

