/*
 * Created on 2 mai 07
 * By Gabriel DROMARD
 */
package net.dromard.common.jar;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class JarFiles {
    private static String file = "test.jar";
     
    public static void main (String args[]) throws IOException {
       JarOutputStream out = new JarOutputStream (new FileOutputStream (file));
 
       for (int i=0; i<args.length; i++) {
          InputStream in = new FileInputStream (args[i]);
          out.putNextEntry (new ZipEntry (args[i]));
          for (;;) {
             int ch = in.read();
             if (ch<0) break;
             out.write (ch);
          }
          in.close();
       }
       out.close();
    }
}