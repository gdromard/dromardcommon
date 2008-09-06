package net.dromard.common.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;
import java.util.Vector;

import net.dromard.common.util.CSVLineTokenizer;

/**
 * This is THE class that you need for importing properties from CSV to properties.
 * You must set your CSV file properly by putting the columns names on the first 
 * column with the good names (expect for the first one)
 * For example the CSV :<br>
 * <code>"name","secondname","thirdname",'fourthname"</code><br>
 * <code>"toto","tata","titi","tutu"</code><br>
 * Will be generated into a property file like : <br>
 * <code>toto.secondname=tata</code><br>
 * <code>toto.thirdname=titi</code><br>
 * <code>toto.fourthname=tutu</code><br>
 * <br>
 * After having parse your CSV into Properties you can :<br>
 * <code><pre>
 * // Initialisation du flux de sortie
 * FileOutputStream oStream=new FileOutputStream(propertyFileName);
 * // Enregistrement des données
 * prop.store(oStream, propertyFileName);
 * // Fermeture du flux de sortie
 * oStream.close();
 * </pre></code>
 */
public class CSV2Properties {
	/**
	 * It will generate a properties name with the same name of your CSV file
	 * @param args First Arg is the CSV file name
	 */
	public static void main(String[] args) {
		File file = new File(args[0]);
		CSV2Properties sourcingUsers = new CSV2Properties();
		try {
			Properties prop = sourcingUsers.parse(file);
			
			// Initialisation du flux de sortie
			FileOutputStream oStream=new FileOutputStream(args[0]+".properties");

			// Enregistrement des données
			prop.store(oStream, args[0]+".properties");

			// Fermeture du flux de sortie
			oStream.close();

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Generate Propery Object from a CSV file
	 * @param file The CSV File
	 * @return A property Object
	 * @throws Exception Thrown if IOException Occured or any thing else !!
	 */
	public Properties parse(File file) throws Exception {
		Properties csv = new Properties();
		System.out.println(file.getAbsolutePath());
		Vector v = parseCSVFile(file, ',', '"', false);
		Vector firstLine = (Vector)v.get(0);
		
		Vector line = null;
		for(int i=0; i<v.size(); ++i) {
			line = (Vector)v.get(i);
			for(int l=1; l<line.size(); ++l) {
				csv.put(line.get(0)+"."+firstLine.get(l), line.get(l));
			}
		}
		return csv;
	}
    
    /**
     * To parse the csv file 
     * @param file The file to parse
     * @param token fields tokenizer 
     * @param quote field delimiters
     * @param keepQuotes Does the parse have to keep quotes or not ?
     * @return Return a Vector of Vector containing all columns (<code>(String)((Vector)v.get(line)).get(column);</code>)
     */
    public static Vector parseCSVFile(File file, char token, char quote, boolean keepQuotes) throws Exception {
        Vector<Vector> lines = new Vector<Vector>();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        CSVLineTokenizer csvParser;
        // Process lines
        while((line = br.readLine()) != null) {
            csvParser = new CSVLineTokenizer(line, token, quote, keepQuotes);
            Vector<String> columns = new Vector<String>();
            // Process columns
            while(csvParser.hasMoreElements()) columns.add((String) csvParser.nextElement());
            lines.add(columns);
        }
        return lines;
    }
}