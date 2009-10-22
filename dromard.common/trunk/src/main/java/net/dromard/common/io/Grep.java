package net.dromard.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dromard.common.nio.FileChannelHandler;
import net.dromard.common.treenode.DefaultTreeNodeVisitor;
import net.dromard.common.treenode.FileTreeNode;
import net.dromard.common.visitable.Visitor;

/**
 * Personnal grep implementation.
 *
 * @author Gabriel Dromard
 */
public class Grep {
    private final GrepVisitor visitor;
    private final String encoding;
    private int nbMatches = 0;
    private final Map<File, List<String>> matches = new HashMap<File, List<String>>();

    public Grep(final File source, final String grepRegexp, final String fileRegexp) throws Exception {
        this(source, grepRegexp, fileRegexp, "8859_1");
    }

    public Grep(final File source, final String grepRegexp, final String fileRegexp, final String encoding) throws Exception {
        visitor = new GrepVisitor(grepRegexp, fileRegexp);
        new FileTreeNode(source).accept(visitor);
        this.encoding = encoding;
    }

    /**
     * @return the found
     */
    public final int getNbMatches() {
        return nbMatches;
    }

    /**
     * @return the matches
     */
    public final Map<File, List<String>> getMatches() {
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
    private int grep(final File source, final String grepRegexp, final String fileRegexp) throws IOException {
        int count = 0;
        if (fileRegexp == null || source.getName().matches(fileRegexp)) {
            if (source.getName().equals("Grep.java")) {
                System.out.println("");
            }

            BufferedReader buf = null;
            FileChannelHandler channel;
            try {
                channel = new FileChannelHandler(new FileInputStream(source).getChannel());
                channel.readString((int) channel.getSize(), encoding);
                buf = new BufferedReader(new FileReader(source));
                String line = buf.readLine();
                while (line != null) {
                    if (line.matches(grepRegexp)) {
                        ++count;
                        List<String> lines = matches.get(source);
                        if (lines == null) {
                            lines = new ArrayList<String>();
                        }
                        lines.add(line);
                        matches.put(source, lines);
                        ++nbMatches;
                    }
                    line = buf.readLine();
                }
            } finally {
                if (buf != null) {
                    buf.close();
                }
            }
        }
        return count;
    }

    /**
     * Internal class done to encapsulate visitor visibility.
     * @author Gabriel Dromard
     */
    private class GrepVisitor extends DefaultTreeNodeVisitor implements Visitor {
        private final String grepRegexp;
        private final String fileRegexp;
        private int found = 0;

        public GrepVisitor(String grepRegexp, String fileRegexp) {
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
         * @throws IOException 
         * @throws Exception Any exception can occurred during visit.
         */
        @Override
        public final void visit(final Object node) throws Exception {
            if (!(node instanceof FileTreeNode)) {
                throw new ClassCastException("FileTreeNode expected");
            }

            File file = ((FileTreeNode) node).getFile();
            // Grep the file
            if (file.isFile()) {
                found += grep(file, grepRegexp, fileRegexp);
                // Continue the folder content
            } else {
                acceptChilds(node);
            }
        }
    }

    public static void main(final String[] argv) {
        try {
            if (argv.length >= 2 && new File(argv[1]).exists()) {
                File source = new File(argv[1]);
                long start = System.currentTimeMillis();
                Grep grep = new Grep(source, argv[0], (argv.length == 3 ? argv[2] : null));
                System.out.println("Grep of '" + argv[0] + "' on folder '" + source.getAbsolutePath() + "'" + (argv.length == 3 ? " with file filter '" + argv[2] + "'" : "") + " Found " + grep.getNbMatches() + " matches in " + (System.currentTimeMillis() - start) + "ms");
                for (File match : grep.getMatches().keySet()) {
                    System.out.println("Found " + grep.getMatches().get(match).size() + " in " + match.getAbsolutePath());
                    for (String found : grep.getMatches().get(match)) {
                        System.out.println("\t" + found.replaceAll("^[ \\t]*", ""));
                    }
                }
            } else {
                System.out.println("Usage: grep regexp file");
                System.out.println("       grep regexp folder [file extension regexp filter]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}