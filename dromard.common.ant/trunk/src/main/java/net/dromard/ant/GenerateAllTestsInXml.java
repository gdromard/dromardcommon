/**
 * 
 */
package net.dromard.ant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Generate build.xml to make possible to devloppers to execute all tests.
 * <br><br>
 * Project name : NDTKit eads  <br><br>
 * Class created by : Kameneff Ivan <br>
 * Creation date : 21 avr. 2009
 */
public class GenerateAllTestsInXml extends Task {

    /** Field name : javaTestSrcDir. */
    private String javaTestSrcDir;
    /** Field name : destFile. */
    private String destFile;

    /**
     * Set java test src files directory to analyse.
     * @param javaTestSrcDir java src files directory.
     */
    public final void setJavaTestSrcDir(final String javaTestSrcDir) {
        this.javaTestSrcDir = javaTestSrcDir;
    }

    /**
     * Set java test src files directory to analyse.
     * @param theDestFile java src files directory.
     */
    public final void setXmlDestFile(final String theDestFile) {
        destFile = theDestFile;
    }

    /* (non-Javadoc)
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public final void execute() {
        File dir = new File(javaTestSrcDir);
        ArrayList<File> parseFiles = parseFiles(dir, new ArrayList<File>());
        String line = "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >\n";
        line += "<suite name=\"Suite\" verbose=\"2\" >\n";
        line += "<test name=\"Simple example\">\n";
        line += "<classes>\n";
        for (File file : parseFiles) {
            String path = file.getPath();
            String substring = path.substring(0, path.length() - ".java".length());

            path = substring.replaceAll("[\\\\]+", ".");

            line += "\t<class name=\"" + path.substring("tests.src.".length(), path.length()) + "\"/>\n";
        }

        line += "</classes></test></suite>\n";
        FileWriter out = null;
        try {
            out = new FileWriter(destFile);
            out.write(line);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new BuildException(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    throw new BuildException(e.getMessage());
                }
            }

        }

    }

    /**
     * @param fileDir fileDir.
     * @param allFileResult allFileResult.
     * @return liste of files.
     */
    private ArrayList<File> parseFiles(final File fileDir, final ArrayList<File> allFileResult) {
        // This filter only returns directories
        ArrayList<File> result = new ArrayList<File>();
        File[] files = fileDir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(parseFiles(file, allFileResult));
            } else {
                if (file.getName().endsWith("Test.java")) {
                    result.add(file);
                }
            }
        }
        return result;

    }
    /**
     * @param args
     */
    public static void main(final String[] args) {
        GenerateAllTestsInXml xmlGenerator = new GenerateAllTestsInXml();
        xmlGenerator.setJavaTestSrcDir("tests//src");

        xmlGenerator.setXmlDestFile("tests//build//testng.xml");
        xmlGenerator.execute();
    }
}
