/*
 * Created on 2 févr. 2005
 * By Gabriel Dromard
 */
package net.dromard.common.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import net.dromard.common.util.ResourceFileBundle;


/**
 * This is a property manager that manage 4 languages, but can be grown.
 * 
 * You must implement a getDefault() static method that set the singleton object if not already done, and return it.
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [10/03/2005] by Gabriel Dromard
 *   - Separate reusable code from specific.
 *
 * [06/03/2005] by Gabriel Dromard
 *   - Rename to increase reusability.
 *
 * [25/02/2005] by Gabriel Dromard
 *   - Add a test on file date to check if we have to reload properties from files.
 *   - Adapt implementation to clean duplicated code (use of array)
 *
 * [02/02/2005] by Gabriel Dromard
 *   - Creation.
 * </pre><br/>
 *
 * @author Gabriel Dromard
 */
public abstract class PropertyManager {
	//  The languages Keys
	protected static final int DEFAULT_KEY = 0;
	protected static final int ENGLISH_KEY = 1;
	protected static final int FRENCH_KEY  = 2;
	protected static final int SPANISH_KEY = 3;
	protected static final int GERMAN_KEY  = 4;
	// The loading date
	protected static long[] propertyDates = new long[] {-1, -1, -1, -1, -1 };
	// The property Objects
	protected static Properties[] properties = new Properties[] {null, null, null, null, null };
	// The files names
	protected static String[] propertyFileNames = null;// = new String[] {"webapp.properties", "webapp_en.properties", "webapp_fr.properties", "webapp_es.properties", "webapp_de.properties" };
	// Singleton
	protected static PropertyManager singleton = null;

	protected PropertyManager() { setPropertyFileNames(); }

	/**
	 * This method must set the property 'propertyFileNames' as an array of 5 string corresponding to the 5 property files.
	 * In this order: default, english, french, spanish, german)
	 */
	protected abstract void setPropertyFileNames();

	/**
	 * Get the the value of a property comming from the required version depending on the language.
	 * Can be en, fr, es, de.
	 * @return Returns the the value of a property comming from the english version of the property.
	 */
	public final String getProperty(String language, String key) throws FileNotFoundException, IOException {
		if(language.startsWith("en"))      return getProperty(ENGLISH_KEY, key);
		else if(language.startsWith("fr")) return getProperty(FRENCH_KEY, key);
		else if(language.startsWith("es")) return getProperty(SPANISH_KEY, key);
		else if(language.startsWith("de")) return getProperty(GERMAN_KEY, key);
		return getProperty(DEFAULT_KEY, key);
	}

	/**
	 * Get the the value of a property comming from the English version of the property.
	 * @return Returns the the value of a property comming from the english version of the property.
	 */
	public final String getProperty(Locale locale, String key) throws FileNotFoundException, IOException {
		return getProperty(locale.getLanguage(), key);
	}

	/**
	 * Get the the value of a property comming from the required version depending on the language.
	 * Can be en, fr, es, de.
	 * @return Returns the the value of a property comming from the english version of the property.
	 */
	protected final String getProperty(int language, String key) throws FileNotFoundException, IOException {
		String toReturn = null;

		File f = new File(propertyFileNames[language]);
		try {
			// WARNING : Do not call logger methods here it will generate a StackOverFlow error.
			if(properties[language] == null || propertyDates[language] < f.lastModified()) {
				properties[language] = ResourceFileBundle.getResourceFileBundle(f);
				propertyDates[language] = f.lastModified();
			}
			toReturn = properties[language].getProperty(key);
		} catch (FileNotFoundException ex) {
			if(language == DEFAULT_KEY) {
				properties[language] = null;
				propertyDates[language] = -1;
				throw new FileNotFoundException("A FileNotFoundException occured while trying to load property file '"+f.getAbsolutePath()+"' (Original message is: "+ex.getMessage()+")");
			}
		}
		// If ne values have been found in specific language, search it in default one !!
		return (toReturn==null && language != DEFAULT_KEY)?getProperty(DEFAULT_KEY, key):toReturn;
	}

	/**
	 * Get the the value of a property comming from the English version of the property.
	 * @param key The key property to retreive. 
	 * @return Returns the the value of a property comming from the english version of the property.
	 */
	public final String getDefaultProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(DEFAULT_KEY, key);
	}

	/**
	 * Get the the value of a property comming from your system language version of the property.
	 * @param key The key property to retreive. 
	 * @return Returns the the value of a property comming from your system language version of the property.
	 */
	public final String getProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(Locale.getDefault(), key);
	}

	/**
	 * Get the the value of a property comming from the french version of the property.
	 * If no value is found it will return the one from the default version (no language)
	 * @return Returns the the value of a property comming from the french version of the property.
	 */
	public final String getEnglishProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(ENGLISH_KEY, key);
	}

	/**
	 * Get the the value of a property comming from the french version of the property.
	 * If no value is found it will return the one from the default version (no language)
	 * @return Returns the the value of a property comming from the french version of the property.
	 */
	public final String getFrenchProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(FRENCH_KEY,  key);
	}

	/**
	 * Get the the value of a property comming from the spanish version of the property.
	 * If no value is found it will return the one from the default version (no language)
	 * @return Returns the the value of a property comming from the spanish version of the property.
	 */
	public final String getSpanishProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(SPANISH_KEY,  key);
	}

	/**
	 * Get the the value of a property comming from the german version of the property.
	 * If no value is found it will return the one from the default version (no language)
	 * @return Returns the the value of a property comming from the german version of the property.
	 */
	public final String getGermanProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(GERMAN_KEY,  key);
	}
}
