package net.dromard.ant.i18n;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.dromard.common.io.Grep;
import net.dromard.common.util.ReflectHelper;

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
public class I18nTask extends Task {
    /** The root source folder. */
    private String javaSrcDir = "";
    /** The bundle to be analyzed. */
    private String bundleToAnalyse = "";
    /** The container of missing keys. */
    private final List<String> missingKeys = new ArrayList<String>();
    /** The container of unused keys. */
    private final List<String> unusedKeys = new ArrayList<String>();
    /** The missing Keys Output File. */
    private FileWriter missingKeysOutputFile = null;
    /** The unused Keys Output File. */
    private FileWriter unusedKeysOutputFile = null;

    /**
     * @param missingKeysOutputFile the missingKeysOutputFile to set
     */
    public final void setMissingKeysOutputFile(final String missingKeysOutputFile) throws IOException {
        File file = new File(missingKeysOutputFile);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        this.missingKeysOutputFile = new FileWriter(file, true);
        this.missingKeysOutputFile.write("---------------- Missing Keys " + DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(System.currentTimeMillis())) + "---------------\n");
    }

    /**
     * @param unusedgKeysOutputFile the unusedgKeysOutputFile to set
     */
    public final void setUnusedKeysOutputFile(final String unusedKeysOutputFile) throws IOException {
        File file = new File(unusedKeysOutputFile);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        this.unusedKeysOutputFile = new FileWriter(file, true);
        this.unusedKeysOutputFile.write("---------------- Unused Keys " + DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(System.currentTimeMillis())) + "---------------\n");
    }

    /**
     * Set java src files directory to analyse.
     * @param javaSrcDir java src files directory.
     */
    public final void setJavaSrcDir(final String javaSrcDir) {
        this.javaSrcDir = javaSrcDir;
    }

    /**
     * @param bundleToAnalyse bundle class.
     */
    public final void setAnalyseBundle(final String bundleToAnalyse) {
        this.bundleToAnalyse = bundleToAnalyse;
    }

    @Override
    public void execute() {
        System.out.println("**************************************" + "\nStart analysis " + " \n\t Class bundle to analyse :" + bundleToAnalyse + " \n\t analyse java files from :" + javaSrcDir + "\n -- ");
        // verifyBundle(CoreI18n.class);
        try {
            verifyBundle(Class.forName(bundleToAnalyse), javaSrcDir);
        } catch (Exception e) {
            throw new BuildException(e);
        } finally {
            try {
                if (missingKeysOutputFile != null) {
                    missingKeysOutputFile.close();
                }
                if (unusedKeysOutputFile != null) {
                    unusedKeysOutputFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (missingKeys.size() > 0) {
            throw new BuildException("Several keys missing have been detected.");
        }
    }

    protected final void verifyBundle(final Class<?> clazz, final String srcDirToCheck) throws Exception {
        if (!new File(javaSrcDir).exists()) {
            System.out.println("Path " + "SRC_PATH" + " does not exists");
        }
        String bundleName = (String) ReflectHelper.getField(clazz, "BUNDLE_NAME").get(null);
        assert (bundleName.substring(0, bundleName.lastIndexOf(".")).equals(clazz.getPackage().getName()));
        verifyKeyUsage(clazz, bundleName);
        checkForMissingKeys(clazz, bundleName);
    }

    private void checkForMissingKeys(final Class<?> clazz, final String bundleName) throws Exception {
        Method retrievingField = clazz.getMethod("getString", new Class[] { String.class });
        File sourceFolder = new File(javaSrcDir);
        String regexp = ".*" + clazz.getSimpleName() + ".getString\\(\"[^\"]*\".*";
        Grep grep = new Grep(sourceFolder, regexp, "*.java");
        for (List<String> lines : grep.getMatches().values()) {
            for (String line : lines) {
                if (line.matches(regexp)) {
                    String str = clazz.getSimpleName() + ".getString(\"";
                    line = line.substring(line.indexOf(str) + str.length());
                    line = line.substring(0, line.indexOf("\""));
                    if (retrievingField.invoke(null, new Object[] { line }).toString().startsWith("!")) {
                        missingKeys.add(line);
                    }
                }
            }
        }
        if (missingKeys.size() > 0) {
            handleErrorOutput("Following keys are not existing (you must add them into '" + bundleName + "'):");
            for (String key : missingKeys) {
                handleErrorOutput(key);
                if (missingKeysOutputFile != null) {
                    missingKeysOutputFile.write(bundleName + ":" + key + "\n");
                }
            }
        }
    }

    private void verifyKeyUsage(final Class<?> clazz, final String bundleName) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
        File sourceFolder = new File(javaSrcDir);
        String fileRegexp = "*/*.java";
        for (Object key : bundle.keySet()) {
            Grep grep = new Grep(sourceFolder, clazz.getSimpleName() + ".getString\\(\"" + key.toString() + "\"", fileRegexp);
            if (grep.getNbMatches() == 0) {
                unusedKeys.add(key.toString());
            }
        }
        // Reset original output
        if (unusedKeys.size() > 0) {
            log("Following keys from '" + bundleName + "' are not used:");
            for (String key : unusedKeys) {
                log("\t" + key);
                if (unusedKeysOutputFile != null) {
                    unusedKeysOutputFile.write(bundleName + ":" + key + "\n");
                }
            }
        }
    }

    /**
     * @param srcDir fileDir.
     * @return The list of i18n files.
     */
    private static ArrayList<File> getI18NClasseFiles(final File srcDir) {
        // This filter only returns directories
        ArrayList<File> result = new ArrayList<File>();
        File[] files = srcDir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(I18nTask.getI18NClasseFiles(file));
            } else {
                if (file.getName().endsWith(".java") && (file.getName().toLowerCase().indexOf("i18n") > -1 || file.getName().toLowerCase().indexOf("messages") > -1)) {
                    System.out.println("[getI18NClasseFiles] Found " + file.getName());
                    result.add(file);
                }
            }
        }
        return result;

    }

    public static void main(final String[] args) {
        String dir = "./src";
        for (File file : I18nTask.getI18NClasseFiles(new File(dir))) {
            try {
                String className = file.getAbsolutePath().replaceAll("\\\\", ".").replaceAll("\\.java", "");
                className = className.substring(className.indexOf("cimpa"));
                System.out.println("------------ Analysing bundle " + className);
                I18nTask task = new I18nTask();
                task.setUnusedKeysOutputFile("./tmp/unusedkeys.log");
                task.setMissingKeysOutputFile("./tmp/missingkeys.log");
                task.setJavaSrcDir("./src/");
                task.setAnalyseBundle(className);
                task.execute();
            } catch (Exception e) {
                // continue
            }
        }
    }
}
