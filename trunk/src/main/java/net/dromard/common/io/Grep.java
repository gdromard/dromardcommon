package net.dromard.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.dromard.common.treenode.DefaultTreeNodeVisitor;
import net.dromard.common.treenode.FileTreeNode;
import net.dromard.common.visitable.Visitor;

/**
 * Personnal grep implementation.
 *
 * @author Gabriel Dromard
 */
public class Grep {  
	private GrepVisitor visitor;
	private Map<File, String> matches = new HashMap<File, String>();

	public Grep(File source, String grepRegexp, String fileRegexp) throws Exception {
		visitor = new GrepVisitor(grepRegexp, fileRegexp);
		new FileTreeNode(source).accept(visitor);
		System.out.println("Found " + getNbMatches() + " for '" + grepRegexp + "'");
	}

	/**
	 * @return the found
	 */
	public final int getNbMatches() {
		return matches.size();
	}
	
    /**
	 * @return the matches
	 */
	public final Map<File, String> getMatches() {
		return matches;
	}

	/**
     * Grep a file: check if the file match the given fileRegexp, than return the number of times the file contains the grepRegexp. 
     * @param source The file to be tested
     * @param grepRegexp The grepExpression to be found in the file
     * @param fileRegexp The file name filter
     * @return The count of found entries in the file that match the regexp.
     * @throws IOException If something wrong occurred while parsing files.
     */
    private int grep(File source, String grepRegexp, String fileRegexp) throws IOException {
    	int count = 0;
    	if (fileRegexp == null || source.getName().matches(fileRegexp)) {
            BufferedReader buf = null;  
    		try {
	            buf = new BufferedReader(new FileReader(source));  
	            String line = buf.readLine();  
	            while(line != null) {
	            	if (line.matches(grepRegexp)) {
	            		++count;
	            		matches.put(source, line);
	            	}
	                line = buf.readLine();  
	            }
    		} finally {
    			if (buf != null) buf.close();
    		}
    	}
    	return count;
    }

    /**
     * Internal class done to encapsulate visitor visibility.
     * @author Gabriel Dromard
     */
    private class GrepVisitor extends DefaultTreeNodeVisitor implements Visitor {
    	private String grepRegexp;
		private String fileRegexp;
		private int found = 0;
		
		public GrepVisitor(String grepRegexp, String fileRegexp) throws Exception {
			if (fileRegexp.startsWith("\\*")) {
				fileRegexp = "." + fileRegexp.substring(1);
			} else if (fileRegexp.startsWith("*")) {
				fileRegexp = "." + fileRegexp;
			}
			if (!grepRegexp.startsWith("^")) {
				grepRegexp = ".*" + grepRegexp;
			}
			if (!grepRegexp.endsWith("$")) {
				grepRegexp = grepRegexp + ".*";
			}
			this.grepRegexp = grepRegexp;
			this.fileRegexp = fileRegexp;
		}
		/**
		 * Visit implementation.
		 * @param node The element object of the tree.
		 * @throws Exception Any exception can occurred during visit.
		 */
    	@Override
		public final void visit(final Object node) throws Exception {
			if (!(node instanceof FileTreeNode)) {
				throw new ClassCastException("FileTreeNode expected");
			}
			
			File file = ((FileTreeNode) node).getFile();
			// Grep the file
			if (file.isFile()) found += grep(file, grepRegexp, fileRegexp);
			// Continue the folder content
			else acceptChilds(node);
		}
    }
    
	public static void main(String[] argv) {  
        try {  
            if(argv.length >= 2 && new File(argv[1]).exists()) {
            	File source = new File(argv[1]);
            	new Grep(source, argv[0], (argv.length == 3 ? argv[2] : null));
            } else {  
                System.out.println("Usage: grep regexp file");  
                System.out.println("       grep regexp folder [file extension regexp filter]");  
            }  
        } catch(Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  