package net.dromard.ant.i18n;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.dromard.common.properties.WritablePropertyFile;

import org.apache.tools.ant.Task;

/**
 * A properties duplicated value finder class.
 * @author Gabriel Dromard
 */
public class I18nSorterTask extends Task {
    /** The resourcesFolder. */
    private File resourcesFolder;

    /**
     * @param resourcesFolder the sourceFolder to set
     */
    public final void setResourcesFolder(final String resourcesFolder) {
        this.resourcesFolder = new File(resourcesFolder);
    }

    @Override
    public void execute() {
        for (String f : resourcesFolder.list()) {
            if (f.toLowerCase().endsWith("properties")) {
                File file = new File(resourcesFolder.getAbsolutePath() + File.separator + f);
                try {
                    new WritablePropertyFile(file).save();
                } catch (FileNotFoundException e) {
                    super.log(e, 0);
                } catch (IOException e) {
                    super.log(e, 0);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            args = new String[] { "./tmp/resources" };
        }
        I18nSorterTask cleaner = new I18nSorterTask();
        cleaner.setResourcesFolder(args[0]);
        cleaner.execute();
    }
}
