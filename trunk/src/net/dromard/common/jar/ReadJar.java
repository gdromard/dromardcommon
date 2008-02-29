/*
 * Created on 2 mai 07
 * By Gabriel DROMARD
 */
package net.dromard.common.jar;

import java.io.*;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

class ReadJar {
    public static void main (String[] args) {
       try {
          JarInputStream jis = new JarInputStream(new FileInputStream(args[0]));
 
          printManifest (jis.getManifest());
 
          // Print the attributes for each jar entry.
          while (true) {
             final JarEntry je = jis.getNextJarEntry();
             if (je==null) break;
             System.out.println("++++++++++ " + je);
             printAttributes(je.getAttributes());
          }
          jis.close();
       } catch (Exception e) {
          e.printStackTrace();
       }
    }
 
    static void printManifest (Manifest man) {
       if (man==null) { System.out.println ("No manifest"); return; }
       System.out.println("---------- Main Attributes");
       printAttributes(man.getMainAttributes());
       for (Iterator it=man.getEntries().keySet().iterator(); it.hasNext(); ) {
          final String ename = (String)it.next();
          System.out.println("---------- " + ename);
          printAttributes(man.getAttributes(ename));
       }
    }
 
    static void printAttributes (Attributes attr) {
       if (attr==null) { return; }
       for (Iterator it=attr.keySet().iterator(); it.hasNext(); ) {
          final Object key = it.next();
          System.out.println (key+": "+attr.get(key));
       }
    }
}