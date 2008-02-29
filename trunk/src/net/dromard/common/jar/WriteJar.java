/*
 * Created on 2 mai 07
 * By Gabriel DROMARD
 */
package net.dromard.common.jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

class WriteJar {
    public static void main (String[] args) {
       if (args.length < 2) {
          System.out.println("Usage: java Main <jar-file> <file1>...");
          System.exit(1);
       }
 
       try {
          // Create the manifest.
          final Manifest man = new Manifest();
          man.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
 
          for (int i=1; i<args.length; i++) {
             final File f = new File(args[i]);
             final Attributes attr = new Attributes();
             attr.put (new Attributes.Name("Date"), new Date(f.lastModified()).toString());
             man.getEntries().put (entryName(f), attr);
          }
 
          man.write (System.out);   // Show manifest on standard out
         
          final JarOutputStream jos =
             new JarOutputStream (new FileOutputStream(args[0]), man);
 
          // Now save all the list files in the JAR.
          final byte[] buf = new byte[1024];
          for (int i=1; i<args.length; i++) {
             final File f = new File(args[i]);
             final FileInputStream fis = new FileInputStream(f);
             jos.putNextEntry(new JarEntry(entryName(f)));
 
             // Now read and write the jar entry data.
             int len;
             while ((len = fis.read(buf)) >= 0) {
                jos.write(buf, 0, len);
             }
             fis.close();
          }
          jos.close();
       } catch (Exception e) {
          e.printStackTrace();
       }
    }
 
    // Converts f's pathname to a form acceptable to ZIP files.
    // In particular, file separators are converted to forward slashes.
    static String entryName(File f) {
       return f.getName().replace(File.separatorChar, '/');
    }
}