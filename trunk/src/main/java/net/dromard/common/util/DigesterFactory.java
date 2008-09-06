package net.dromard.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.SAXException;

/**
 * Digester Factory
 * This load a Java object from a XML file using Digester framework
 *
 * @author Gabriel Dromard
 */
public class DigesterFactory {
	/*
	public static Root loadRoot(File input) throws SAXException, Exception {
		// Create the digester instance
		Digester digester = new Digester();

		// Activate or not the DTD checking
		digester.setValidating( false );

		// This is needed because digester is sending errors directly to output without raising exceptions othervise
		//digester.setErrorHandler(new DefaultSAXErrorHandler());
		digester.setErrorHandler(new ErrorHandler() {
			public void error(SAXParseException exception) throws SAXException { throw exception; }
			public void fatalError(SAXParseException exception) throws SAXException { throw exception; }
			public void warning(SAXParseException exception) throws SAXException { throw exception; }
		});

		// Parsing rules
		digester.addObjectCreate( "myxml", Root.class );
		//digester.addSetProperties( "descriptor", "type", "type" );
		//digester.addSetProperties( "myxml/a/b", new String [] {"type", "target", "header"} , new String [] {"outputType", "outputTarget", "header"} );

		digester.addObjectCreate( "myxml/a", A.class );
		digester.addSetProperties( "myxml/a", "name", "name" );
		digester.addSetProperties( "myxml/a/b", "name" , "bname" );
		digester.addSetNext( "myxml/a", "setA");

		digester.addObjectCreate( "myxml/a/c", C.class );
		digester.addSetProperties( "myxml/a/c", new String [] {"name", "value"} , new String [] {"name", "value"} );
		digester.addBeanPropertySetter("myxml/a/c", "description");
		digester.addSetNext( "myxml/a/c", "addC");

		return (Root)digester.parse( input );
	}
*/

	/**
	 * Load an object from a XML file unsing digester framwork
	 * @param input The data input file defining the Java Object
	 * @param rules The rules input file defininf the digester rules for loading this Java Object
	 * @return Return an instance of the a Java Object (defined in rule file)
	 * @throws SAXException Can be thrown while parsing files.
	 * @throws Exception Can be MalformedURLException or IOException for the input or rules files.
	 * @see DigesterFactory#load(File, URL)
	 * @see DigesterFactory#load(URL, URL)
	 */
	public static Object load(File input, File rules) throws SAXException, IOException {
		Digester digester = DigesterLoader.createDigester(rules.toURI().toURL());
		return digester.parse(input);
	}

	/**
	 * Load an object from a XML file unsing digester framwork
	 * @param input The data input URL defining the Java Object
	 * @param rules The rules input URL defining the digester rules for loading this Java Object
	 * @return Return an instance of the a Java Object (defined in rule file)
	 * @throws SAXException Can be thrown while parsing files.
	 * @throws Exception Can be MalformedURLException or IOException for the input or rules files.
	 * @see DigesterFactory#load(File, URL)
	 * @see DigesterFactory#load(File, File)
	 */
	public static Object load(URL input, URL rules) throws SAXException, IOException {
		Digester digester = DigesterLoader.createDigester(rules);
		return digester.parse(input.openStream());
	}

	/**
	 * Load an object from a XML file unsing digester framwork
	 * @param input The data input file defining the Java Object
	 * @param rules The rules input URL defining the digester rules for loading this Java Object
	 * @return Return an instance of the a Java Object (defined in rule file)
	 * @throws SAXException Can be thrown while parsing files.
	 * @throws Exception Can be MalformedURLException or IOException for the input or rules files.
	 * @see DigesterFactory#load(URL, URL)
	 * @see DigesterFactory#load(File, File)
	 */
	public static Object load(File input, URL rules) throws SAXException, IOException {
		Digester digester = DigesterLoader.createDigester(rules);
		return digester.parse(input);
	}

	public static void main(String[] args) {
		if (args.length == 2) {
			try {
				//Root o = loadRoot(new File( "input.xml" ));
				//Object o = load(new File("input.xml"), new File("digester-input-rules.xml"));
				Object o = load(new File(args[0]), new File(args[1]));
				/*Display resulting beans*/
				System.out.println("=================================");
				System.out.println(o.toString());
				System.out.println("=================================");
			} catch( SAXException saxEx) {
				System.err.println("Error in XML parsing: "+saxEx.getMessage());
			} catch( Exception exc ) {
				exc.printStackTrace(System.err);
			}
		} else {
			System.out.println("Usage: java "+DigesterFactory.class.getName()+" [XML input file] [XML rule]");
		}
	}
}
