/*
 * Created on 13 oct. 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.io;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * This class is a clone of the tail command in UNIX environnement.
 * It can be usefull on windows computer to visualize logs ...
 * 
 * First parameter is the fileName.
 * The second parameter (optional) is the last number of line to be displayed
 * 
 * @author Gabriel DROMARD
 */
public class Tail {
    public static void main(String[] args) throws Exception {
        if(args.length == 1) tail(args[0]);
        else if (args.length == 2) tail(args[0], Integer.parseInt(args[1]));
        else printUsage();
    }

    /**
     * This is a clone of tail command from UNIX environnement.
     * @param fileName The file name to tail
     * @param nbLines  If negative, the first nbLines will be skipped.
     *                 If positive, keep only the last nbLines.
     */
    public static final void tail(String fileName, int nbLines) throws Exception {
        File fileToTail = new File(fileName);
        // Verifications
        if (!fileToTail.exists()) {
            System.out.println("File '" + fileName + "' does not exist.");
            return;
        }

        // Get reader
        BufferedReader tailed = new BufferedReader(new InputStreamReader(fileToTail.toURI().toURL().openStream()));
        String line;
        
        // Print the last nbLines ELSE print entire file
        // Initialize
        LinkedQueue queue = new LinkedQueue();
        int count = 0; // number of lines stored so far

        // Read all input lines into the queue and keep the last nlines
        while ((line = tailed.readLine()) != null) {
            // Add new line to the queue
            queue.enqueue(line);
            // Drop the oldest entry if there are already nbLines lines in the queue
            // and increment counter
            if((nbLines<0 && count < -nbLines) || (nbLines>=0 && count >= nbLines)) queue.dequeue();
            ++count;
        }

        // Print out contents of queue
        while(!queue.empty()) {
            line = (String)queue.dequeue();
            System.out.println(line);
        }
        
        // Get lines from system.in 
        Thread th = new Thread() {
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    String line = null;
                    while((line = in.readLine()) != null) System.out.print(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
        
        // Continue read of inputstream while process is killed
        while(true) {
            line = tailed.readLine();
            // Print line
            if(line != null) System.out.println(line);
            // Wait
            else Thread.sleep(500);
        }
    }

    /**
     * Alias to tail(String fileName, int nbLines) using 10 by default for nbLines.
     * @param fileName The file name to tail
     * @throws Exception
     */
    public static void tail(String fileName) throws Exception {
        tail(fileName, 10);
    }

    /**
     * Print the usage of the main.
     */
    private static final void printUsage() {
        System.out.println("usage: java " + Tail.class.getName() + " [file name] ["+(char)241+"nb line]");
    }
}

/**
 * This class provides a container for objects that is accessed in FIFO
 * (first in first out) discipline. The implementation uses a LinkedList
 * instance.
 */
class LinkedQueue {
    private LinkedList<Object> list;

    /**
     * Create a queue.
     */
    public LinkedQueue() {
        list = new LinkedList<Object>();
    }

    /**
     * Test a queue for being empty.
     * @return true if the queue is empty.
     */
    public boolean empty() {
        return list.size() == 0;
    }

    /**
     * Test a queue for being full.
     * @return true if the queue is full.
     */
    public boolean full() {
        return false;
    }

    /**
     * Add an object to the queue.
     * @param object object to be appended to the queue.
     */
    public void enqueue(Object object) {
        list.addLast(object);
    }

    /**
     * Fetch and remove the next object from the queue. Precondition: Queue must not be empty.
     * @return the next object.
     */
    public Object dequeue() {
        return list.removeFirst();
    }
}
