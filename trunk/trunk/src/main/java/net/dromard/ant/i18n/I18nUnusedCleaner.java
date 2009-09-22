package net.dromard.ant.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.dromard.common.properties.WritablePropertyFile;

import org.apache.tools.ant.Task;

/**
 * A properties duplicated value finder class.
 * @author Gabriel Dromard
 */
public class I18nUnusedCleaner extends Task {
    /** The resourcesFolder. */
    private File sourceFolder;
    /** The unusedKeysFile. */
    private File unusedKeysFile;

    /**
     * @param sourceFolder the sourceFolder to set
     */
    public final void setSourceFolder(final String sourceFolder) {
        this.sourceFolder = new File(sourceFolder);
    }

    /**
     * @param unusedKeysFile
     */
    public final void setUnusedKeysFile(final String unusedKeysFile) {
        this.unusedKeysFile = new File(unusedKeysFile);
    }

    @Override
    public void execute() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(unusedKeysFile));
            String line = null;
            String currentBundle = null;
            List<WritablePropertyFile> current = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--")) {
                    if (current != null) {
                        for (WritablePropertyFile prop : current) {
                            prop.save();
                        }
                        current = null;
                        currentBundle = null;
                    }
                } else {

                    String bundle = line.substring(0, line.lastIndexOf(':'));
                    if (current == null) {
                        // Start dealing with a new bundle
                        current = getFiles(bundle);
                        currentBundle = bundle;
                    }

                    if (!currentBundle.equals(bundle)) {
                        throw new IOException("Current bundle does not match (" + currentBundle + " <> " + bundle + ")");
                    }

                    // Remove unused key
                    String key = line.substring(line.lastIndexOf(':') + 1);
                    for (WritablePropertyFile prop : current) {
                        System.out.println("Removing key: " + key + " from " + prop.getFile().getAbsolutePath());
                        prop.remove(key);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            super.log(e, 0);
        } catch (IOException e) {
            super.log(e, 0);
        }
    }

    private List<WritablePropertyFile> getFiles(final String bundle) throws FileNotFoundException, IOException {
        File parent = new File(sourceFolder.getAbsolutePath() + File.separator + bundle.substring(0, bundle.lastIndexOf(".")).replace('.', File.separatorChar));
        String bundleName = bundle.substring(bundle.lastIndexOf(".") + 1);
        List<WritablePropertyFile> files = new ArrayList<WritablePropertyFile>();
        for (String f : parent.list()) {
            if (f.startsWith(bundleName)) {
                File file = new File(parent.getAbsolutePath() + File.separator + f);
                files.add(new WritablePropertyFile(file));
            }
        }
        return files;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            args = new String[] { "./src/", "./tmp/unusedkeys.log" };
        }
        I18nUnusedCleaner cleaner = new I18nUnusedCleaner();
        cleaner.setSourceFolder(args[0]);
        cleaner.setUnusedKeysFile(args[1]);
        cleaner.execute();
    }
}
