package net.dromard.ant;

import java.io.File;
import java.util.List;
import java.util.Map;

import net.dromard.common.io.Grep;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Ant Task to check if all bundle messages are used. <br>
 * <br>
 * Project name : NDTKit eads <br>
 * <br>
 * Class created by : 30000054 <br>
 * Creation date : 16 mars 2009
 */
public class CheckXMLMappingTask extends Task {
    private String xmlFileRegexp = "";
    private String dir = "";
    private boolean buildFailed = false;

    /**
     * @param xmlFileRegExp bundle class.
     */
    public void setFileRegexp(final String xmlFileRegExp) {
        xmlFileRegexp = xmlFileRegExp;
    }

    /**
     * @param xmlFileRegexp bundle class.
     */
    public void setDir(final String dir) {
        this.dir = dir;
    }

    @Override
    public void execute() {
        try {
            System.out.println("**************************************" + "\nStart analysis " + " \n\t XML to analyse :" + xmlFileRegexp + "\n -- ");
            checkXMLMapping();
            if (buildFailed) {
                throw new BuildException("Some invalid class definitions have been detected.");
            }
        } catch (Exception ex) {
            throw new BuildException(ex.getMessage());
        }
    }

    protected void checkXMLMapping() throws Exception {
        Grep grep = new Grep(new File(dir), "<class[ ]*name=\"([^\"]*)\"", xmlFileRegexp);
        if (grep.getNbMatches() > 0) {
            Map<File, List<String>> matches = grep.getMatches();
            for (File file : matches.keySet()) {
                for (String line : matches.get(file)) {
                    String className = line.replaceAll(".*name=\"([^\"]*)\".*", "$1");
                    try {
                        Class.forName(className);
                    } catch (java.lang.ClassNotFoundException e) {
                        handleErrorOutput("Defined class " + className + " does not exists in file " + file.getAbsoluteFile() + " !");
                        buildFailed = true;
                    }
                }
            }
        }
        grep = new Grep(new File(dir), "<field.*type=\"([^\"]*)\"", xmlFileRegexp);
        if (grep.getNbMatches() > 0) {
            Map<File, List<String>> matches = grep.getMatches();
            for (File file : matches.keySet()) {
                for (String line : matches.get(file)) {
                    String className = line.replaceAll(".*type=\"([^\"]*)\".*", "$1");
                    try {
                        Class.forName(className);
                    } catch (ClassNotFoundException e1) {
                        if (!className.equals("int") && !className.equals("integer") && !className.equals("float") && !className.equals("double") && !className.equals("boolean") && !className.equals("string")) {
                            handleErrorOutput("Field reference " + className + " does not exists in file " + file.getAbsoluteFile() + " !");
                            buildFailed = true;
                        }
                    }
                }
            }
        }
    }

    public static void main(final String[] args) {
        String dir = "C:/Projects/NDTKit/src/mapping";
        CheckXMLMappingTask mappingTask = new CheckXMLMappingTask();
        mappingTask.setDir(dir);
        mappingTask.setFileRegexp("*.xml");
        mappingTask.execute();
    }
}
