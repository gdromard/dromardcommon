package net.dromard.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.dromard.common.treenode.DefaultTreeNodeVisitor;
import net.dromard.common.treenode.FileTreeNode;
import net.dromard.common.visitable.Visitor;

/**
 * Personnal grep implementation.
 *
 * @author Gabriel Dromard
 */
public class Grep extends DefaultTreeNodeVisitor implements Visitor {  
	protected String grepRegexp;
	protected String fileRegexp;
	
    /**
     * Visit implementation.
     * @param node The element object of the tree.
     * @throws Exception Any exception can occured during visit.
     */
    public final void visit(final Object node) throws Exception {
        if (!(node instanceof FileTreeNode)) {
            throw new ClassCastException("FileTreeNode expected");
        }

        File file = ((FileTreeNode) node).getFile();
        // Grep the file
        if (file.isFile()) grep(file, grepRegexp, fileRegexp);
        // Continue the folder content
        else acceptChilds(node);
    }

    /**
     * Grep a file: check if the file match the given fileRegexp, than return the number of times the file contains the grepRegexp. 
     * @param source The file to be tested
     * @param grepRegexp The grepExpression to be found in the file
     * @param fileRegexp The file name filter
     * @return The count of found entries in the file that match the regexp.
     * @throws IOException If something wrong occurred while parsing files.
     */
    public static int grep(File source, String grepRegexp, String fileRegexp) throws IOException {
    	int count = 0;
    	if (source.getName().matches(fileRegexp)) {
            BufferedReader buf = null;  
    		try {
	            buf = new BufferedReader(new FileReader(source));  
	            String line = buf.readLine();  
	            while(line != null) {
	            	if (line.matches(grepRegexp)) {
	            		++count;
	            		System.out.println(line);  
	            	}
	                line = buf.readLine();  
	            }
    		} finally {
    			if (buf != null) buf.close();
    		}
    	}
    	return count;
    }
    
	public static void main(String[] argv) {  
        try {  
            if(argv.length == 2 && new File(argv[1]).exists()) {
            	File source = new File(argv[1]);
            	Grep grep = new Grep();
            	grep.visit(new FileTreeNode(source));
            } else {  
                System.out.println("Usage: grep regexp file");  
            }  
        } catch(Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  