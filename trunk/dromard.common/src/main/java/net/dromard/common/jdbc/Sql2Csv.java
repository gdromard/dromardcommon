/*
 * Created on 21 oct. 2004
 * By Gabriel Dromard
 */
package net.dromard.common.jdbc;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import net.dromard.common.io.FileHelper;
import net.dromard.common.logging.Logger;
import net.dromard.common.util.Helper;
import net.dromard.common.util.ResourceFileBundle;


/**
 * This class is design to be a little usefull help for extracting informations from DataBase
 * You just need to pass the parameters as a Properties object
 * @author Gabriel Dromard
 */
public class Sql2Csv extends Logger {
	protected String driver, url, user, passwd, outputFileNames[], queries[];
	Connection connection;
	
	/**
	 * Class constructor. It will get the parameters from the properties.
	 * With the key below :<br/>
	 * <ul>
	 *   <li>database.driver</li>
	 *   <li>database.url</li>
	 *   <li>database.user</li>
	 *   <li>database.passwd</li>
	 *   <li>number.of.queries</li>
	 *   <li>query.1</li>
	 *   <li>output.filename.1</li>
	 *   <li>query.2</li>
	 *   <li>output.filename.2</li>
	 *   <li>...</li>
	 * </il>
	 * @param sql2csvParameters A Properties object with all parameters set.
	 * @throws NullPointerException If a parameter doesn't exist or empty
	 * @throws ClassNotFoundException If the JDBC Driver is not found
	 * @throws SQLException If something wrong with DataBase Connection
	 * @see Sql2Csv#Sql2Csv(String, String, String, String, String[], String[])
	 */
	public Sql2Csv(Properties sql2csvParameters) throws SQLException, ClassNotFoundException, NullPointerException {
		driver = sql2csvParameters.getProperty("database.driver");
		url    = sql2csvParameters.getProperty("database.url");
		user   = sql2csvParameters.getProperty("database.user");
		passwd = sql2csvParameters.getProperty("database.passwd");
		
		queries = new String[Integer.parseInt(sql2csvParameters.getProperty("number.of.queries"))];
		outputFileNames = new String[queries.length];
		for(int i=1; i<=queries.length; ++i) {
			outputFileNames[i-1] = sql2csvParameters.getProperty("output.filename."+i);
			queries[i-1] = sql2csvParameters.getProperty("query."+i);
		}
		init();
	}
	
	/**
	 * Class constructor. It initialise object with all these parameters
	 * @param driver  The JDBC Driver class name
	 * @param url     The URL connection to the Data Base
	 * @param user    The database user name
	 * @param passwd  The database password
	 * @param queries The queries to execute
	 * @param outputFilesNames The output files in where all the results of each queries will be stored
	 * @throws ClassNotFoundException If the JDBC Driver is not found
	 * @throws SQLException If something wrong with DataBase Connection
	 */
	public Sql2Csv(String driver, String url, String user, String passwd, String[] queries, String[] outputFilesNames) throws SQLException, ClassNotFoundException {
		this.driver  = driver;
		this.url     = url;
		this.user    = user;
		this.passwd  = passwd;
		this.queries = queries;
		this.outputFileNames = outputFilesNames;
		init();
	}
	
	/**
	 * This method initialize the DataBase connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	protected void init() throws SQLException, ClassNotFoundException {
		DbConnection dbConnection = new DbConnection(driver, url, user, passwd);
		connection = dbConnection.getConnection();
	}

	/**
	 * This will process the result of the given query.
	 */
	public void run() throws Exception {
		Statement stmt = null;
		ResultSetMetaData meta = null;
		ResultSet result = null;
		StringBuffer buf = null;
		int bufferCapacity = 4000;
		
		try {
			for (int query=0; query<queries.length; ++query) {
				// Prepare out put file
				File f = new File(outputFileNames[query]);
				if(f.exists()) {
					String backUpFileName = outputFileNames[query]+"."+Helper.getCurrentTime().replace(' ', '_').replace(':','-');
					f.renameTo(new File(backUpFileName));
					if(f.exists()) throw new IOException("Enable to rename file '"+outputFileNames[query]+"' into '"+backUpFileName+"'");
				}
			
				// Get connection, and create Statement
				stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				
				// Execute Query
				result = stmt.executeQuery(queries[query]);
				
				// Prepare buffer & counters
				buf = new StringBuffer();
				buf.ensureCapacity(bufferCapacity);
				int line=0;
				int bufferSize=0;
				
				// Manage result
				if (result != null) {
					// If there is something ...
					if (result.next()) {
						line=bufferSize=0;
						// Get The MetaData
						meta = result.getMetaData();
						// Get column count
						int nbColumn = meta.getColumnCount();
						// Put Columns into buffer
						for (int col=1; col<=nbColumn; ++col) buf.append(meta.getColumnName(col)+";");
						buf.append("\n");
						// Put result of each columns into buffer
						do {
							++line;
							//logDebug(this.getClass(), "Getting line n°"+line+" - Buffer size: "+(bufferSize+buf.length())+"-"+buf.length());
							for (int col=1; col<=nbColumn; ++col) buf.append(result.getString(col)+";");
							buf.append("\n");
							// flush buffer each 'bufferCapacity' characters (this resolve a little bug when you pass the limit of 73 000 characters)
							if(buf.length() > bufferCapacity-300) {
								FileHelper.append2File(outputFileNames[query], buf.toString());
								bufferSize += buf.length();
								buf = new StringBuffer();
								buf.ensureCapacity(bufferCapacity);
							}
						} while (result.next());
						logDebug(this.getClass(), "Resultset finish with "+line+" lines (and "+bufferSize+" octets).");
					} else {
						logDebug(this.getClass(), "Result of query '"+this.queries[query]+"' is empty.");
					}
				} else {
					logDebug(this.getClass(), "Result of query '"+this.queries[query]+"' is null.");
				}
				
				logDebug(this.getClass(), "Storing result.");
				if(buf.length() > 0) {
					FileHelper.append2File(outputFileNames[query], buf.toString());
				}
			}
		} catch(SQLException ex) {
			throw ex;
		} finally {
			if(result != null) result.close();
			if(stmt != null) stmt.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			if(args.length == 1) {
				Sql2Csv sql2Csv = new Sql2Csv(ResourceFileBundle.getResourceFileBundle(new File(args[0])));
				sql2Csv.run();
			} else if(args.length == 6) {
				Sql2Csv sql2Csv = new Sql2Csv(args[0], args[1], args[2], args[3], new String[] {args[4]}, new String[] {args[5]});
				sql2Csv.run();
			} else {
				System.out.println("Usage: java -cp common.jar;yourJdbcDriver.jar net.dromard.common.jdbc.Sql2Csv");
				System.out.println(" * With parameter:");
				System.out.println("      Property File Name");
				System.out.println(" * Or with parameters: ");
				System.out.println("      driver           # eg: oracle.jdbc.OracleDriver");
				System.out.println("      url              # eg: jdbc:oracle:thin:@<hostname>:1521:<database>");
				System.out.println("      user             # The data base user");
				System.out.println("      password         # The database user's password");
				System.out.println("      query            # eg: SELECT * FROM TABS;");
				System.out.println("      output file name # eg: my_query_result.csv");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}