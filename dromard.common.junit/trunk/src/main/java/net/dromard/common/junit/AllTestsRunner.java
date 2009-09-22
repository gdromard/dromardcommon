/*
 * Created on 28 sept. 06
 * By Gabriel DROMARD
 */
package net.dromard.common.junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * JUnit tools class.
 * 
 * Limitation if the package exists in two jar only the first in 
 * the classpath is taken into account.
 *
 * <br/>
 * <pre>
 *    +---------+
 *    | History |
 *    +---------+
 *
 *  [02/06/2007] by Gabriel Dromard
 *     - Add Jar parsing.
 *
 *  [02/10/2006] by Gabriel Dromard
 *     - Creation.
 * </pre>
 * <br/>
 * @author Gabriel Dromard
 */
public class AllTestsRunner {
    
    private static String TEST_CASE_SUPER_CLASS = junit.framework.TestCase.class.getName();

    /**
     * Add all Test classes from current package and all childs recursivly.
     * 
     * @param pckg  The current package.
     * @param suite The suite in which to add all the test classes. 
     */
    public static void addAllTestsRecursivly(String pckg, TestSuite suite) {
        Class[] classes = new Class[0];
        if (pckg.charAt(0) != '/') {
            pckg = ("/" + pckg).replace('.', '/');
        }
        // Adding Test Classes from current packages
        try {
            classes = getTestClasses(pckg);
            for (int i=0; i<classes.length; ++i) {
                suite.addTestSuite(classes[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Parssing sub packages
        try {
            String[] packages = getSubPackages(pckg);
            System.out.println("Found "+classes.length+" Tests and "+packages.length+" packages (in "+pckg+")");
            for (int i=0; i<packages.length; ++i) {
                addAllTestsRecursivly(packages[i], suite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retreive all the sub packages from a given one.
     * @param pckg The current package from which you want to retreive all childs.
     * @return An array of package names
     * @throws IOException If any error occured while parsing packages.
     */
    public static String[] getSubPackages(String pckg) {
        // Translate the package name into an absolute path
        String name = pckg.replace('.','/');
        
        // Get a File object for the package
        URL url = AllTestsRunner.class.getResource(name);
    
        if (url != null) {
        try {
            File directory = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            ArrayList<String> pckgs = new ArrayList<String>();
            if (directory.exists()) {
                // Get the list of the files contained in the package
                File[] files = directory.listFiles();
                for (int i=0; i<files.length;++i) {
                    // we are only interested in .class files
                    if (files[i].isDirectory()) {
                        String sub = pckg;
                        if (sub != "/") sub += "/";
                        // removes the .class extension
                        pckgs.add(sub+files[i].getName());
                    }
                }
            } else if(!url.getProtocol().equals("jar")) {
                System.err.println("[P] Invalid Directory: "+url.getFile() + " (pckg: "+pckg+")");
            }
            
            String[] packages = new String[pckgs.size()];
            for (int i=0; i<pckgs.size();++i) packages[i] = pckgs.get(i);
            return packages;
        } catch (Exception ex) {
            System.err.println("Invalid URL: "+url + " (pckg: "+pckg+")");
            ex.printStackTrace();
        }
        }
        return new String[0];
    }
    
    /**
     * Rtreive all Test classes from current package.
     * @param pckg Current package.
     * @return An array of <b>Class</b>es that represent the Test classes found from current package.
     * @throws IOException If any IO exception occured while retreiving Class object
     * @throws ClassNotFoundException If a class not found exception occured while retreiving Class object.
     */
    public static Class[] getTestClasses(String pckg) {
        // Translate the package name into an absolute path
        String name = pckg.replace('.','/');
        
        // Get a File object for the package
        URL url = AllTestsRunner.class.getResource(name);
    
        if (url != null) {
        try {
            File directory = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            ArrayList<Class> vClasses = new ArrayList<Class>();
            if (directory.exists()) {
                // Get the list of the files contained in the package
                String[] files = directory.list();
                for (int i=0; i<files.length;++i) {
                    
                    // we are only interested in .class files
                    if (files[i].endsWith(".class")) {
        
                        String classname = files[i].substring(0,files[i].length()-6);
                        Class clazz = Class.forName((pckg+"/"+classname).replace('/','.').substring(1));
    	                if (isItATest(clazz)) {
                            // removes the .class extension
                            vClasses.add(clazz);
                        }
                    }
                }
            } else if(url.getProtocol().equals("jar") && url.getFile().indexOf('!') > 0) {
                String jarFile = URLDecoder.decode(url.getFile().substring(6, url.getFile().indexOf('!')), "UTF-8");
                JarInputStream jis = new JarInputStream(new FileInputStream(jarFile));
                // Print the attributes for each jar entry.
                JarEntry je = jis.getNextJarEntry();
                while (je != null) {
                    // we are only interested in .class files
                    if (!je.isDirectory() && ("/"+je.getName()).startsWith(pckg) && je.getName().endsWith(".class")) {
        
                        String classname = je.getName().substring(0, je.getName().length()-6);
                        try {
                            Class clazz = Class.forName(classname.replace('/','.'));
                            if (isItATest(clazz)) {
                                // removes the .class extension
                                vClasses.add(clazz);
                                //System.out.println("[DEBUG] <C> Adding test " + classname);
                            }
                        } catch (NoClassDefFoundError e) {
                            // We do not add this class as Test (is dependencies are not presents).
                        }
                    }
                    je = jis.getNextJarEntry();
                }
                jis.close();
            } else {
                System.err.println("[C] Invalid Directory: "+url.getFile() + " (pckg: "+pckg+")");
            }
            
            Class[] classes = new Class[vClasses.size()];
            for (int i=0; i<vClasses.size();++i) classes[i] = vClasses.get(i);
            return classes;
        } catch (Exception ex) {
            System.err.println("Invalid URL: "+url + " (pckg: "+pckg+")");
            ex.printStackTrace();
        }
        }
        return new Class[0];
    }
    
    /**
     * Say if the class given as parameter extends TestCase.
     * @param clazz the class to test 
     */
    private static boolean isItATest(final Class clazz) {
        Class tmpClazz = clazz;
        while((tmpClazz = tmpClazz.getSuperclass()) != null && !tmpClazz.equals(Object.class)) {
            if (tmpClazz.getName().equals(TEST_CASE_SUPER_CLASS)) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * This main run the <code>junit.textui.TestRunner.run()</code> methods by putting it the TestSuite to be ran. 
     * @param args Not used !
     */
    public static void main(String[] args) {
        if (args.length == 1) junit.textui.TestRunner.run(suite(args[0]));
        else junit.textui.TestRunner.run(suite());
    }

    /**
     * Retreive all the Test classes from a given package.
     * @param pckg The based package of the search 
     * @return a TestSuite object.
     */
    public static Test suite(String pckg) {
        TestSuite suite = new TestSuite("Entire tests");
        AllTestsRunner.addAllTestsRecursivly(pckg, suite);
        return suite;
    }

    /**
     * Retreive all the Test classes from the application.
     * @return a TestSuite object.
     */
    public static Test suite() {
        return suite(AllTestsRunner.class.getPackage().getName());
    }
}
