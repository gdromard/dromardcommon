/*
 * Created on 22 mars 2006
 * By Gabriel DROMARD
 */
package net.dromard.common.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class StreamGobbler extends Thread {
    InputStream is;
    
    public StreamGobbler(InputStream is) {
        this.is = is;
    }
    
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ((line = br.readLine()) != null) printLine(line);    
            flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    abstract protected void flush();
    
    abstract protected void printLine(String line);
    
    public static final StreamGobbler getRedirectorInstance(InputStream is, OutputStream redirect) {
        return new Redirector(is, redirect);
    }
    
}

class Redirector extends StreamGobbler {
    PrintWriter pw;
    
    public Redirector(InputStream is, OutputStream redirect) {
        super(is);
        pw = new PrintWriter(redirect);
        
    }
    protected void flush() {
        if (pw != null) pw.flush();
    }
    
    protected void printLine(String line) {
        if (pw != null) pw.println(line);
    }
}

