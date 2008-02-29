/*
 * Created on 21 oct. 2004
 * By Gabriel Dromard
 */
package net.dromard.common.jdbc;

import java.sql.*;

import net.dromard.common.logging.Logger;


/**
 * This is tool library for DataBase connection
 * @author Gabriel Dromard
 */
public class DbConnection {
	protected Connection connection;
	protected String driver, url, user, passwd;

	/**
	 * Initialisation de la connection
	 * @param driver Driver de la base de données
	 * @param url    Nom de la base de donnée
	 * @param user   Nom de l'utilisateur de la base de données
	 * @param passwd Mot de passe de l'utilisateur de la base de donnéees
	 */
	public DbConnection(String driver, String url, String user, String passwd) throws ClassNotFoundException, SQLException {
		this.driver = driver;
		this.url    = url;
		this.user   = user;
		this.passwd = passwd;
		connect();
	}
	
	/**
	 * Connect to the database 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected void connect() throws ClassNotFoundException, SQLException {
		// Récupération du driver
		Class.forName(driver);
		// Connection à la BDD
		this.connection = DriverManager.getConnection(url, user, passwd);
	}

	/**
	 * Retreive the JDBC connection
	 * @return A Connection object
	 */
	public Connection getConnection() { return connection; }
	
	/**
	 * Retreive the driver
	 * @return The driver class
	public String getDriver() { return driver; }
	 * Retreive the user name
	 * @return The user name
	public String getUserName() { return user; }
	 * Retreive the database URL
	 * @return The database URL
	public String getUrl() { return url; }
	 */

	/**
	 * Check the connection
	 * @return A boolean that says if the connection is up or not
	 */
	public boolean isConnected() {
		try {
			if(connection != null && !connection.isClosed()) return true;
			return false;
		} catch(Exception ex) {
			Logger.logDebug(this.getClass(), "Exception occured in methods isConnected()", ex);
			return false;
		}
	}
	
	/**
	 * Close the connection and reconnect
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void reconnect() throws ClassNotFoundException, SQLException {
		if(isConnected()) close();
		connect();
	}
	
	/**
	 * Close the connection
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if(isConnected()) connection.close();
		connection = null;
		Logger.logDebug(this.getClass(), "close()");
	}
	
	/**
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		if(isConnected()) connection.close();
		connection = null; 
		driver = url = user = passwd = null;
		Logger.logDebug(this.getClass(), "finalize()");
		super.finalize();
	}

	/**
	 * Get the status details of the connection into a string
	 * @return The details of the connection
	 */
	public String toString() {
		String toReturn  = "Driver   : " + driver + "\n";
		       toReturn += "URL      : " + url    + "\n";
		       toReturn += "User name: " + user   + "\n";
		       toReturn += "Status   : " + (isConnected()?"Connected":"DISCONNECTED");
		return toReturn;
	}

	/**
	 * Get the status details of the connection into a html string
	 * @return The details of the connection
	 */
	public String toHtml() {
		String toReturn = "<table>";
		toReturn += "<tr><td><b>Driver     : </b></td><td>" + driver + "</td></tr>";
		toReturn += "<tr><td><b>URL        : </b></td><td>" + url    + "</td></tr>";
		toReturn += "<tr><td><b>User name  : </b></td><td>" + user   + "</td></tr>";
		toReturn += "<tr><td><b>Status     : </b></td><td>" + (isConnected()?"Connected</td></tr>":"DISCONNECTED</td></tr>");
		toReturn += "</table>";
		return toReturn;
	}
	
	public static void main(String[] args) throws Exception, Throwable {
		DbConnection conn = new DbConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/SOURCE_CODE", "code", "source");
		conn.getConnection();
		conn.close();
	}
}