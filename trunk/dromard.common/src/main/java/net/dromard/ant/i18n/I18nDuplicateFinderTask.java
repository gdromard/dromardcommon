package net.dromard.ant.i18n;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.Task;

/**
 * A properties duplicated value finder class.
 * @author Gabriel Dromard
 */
public class I18nDuplicateFinderTask extends Task {
    /** The resourcesFolder. */
    private File resourcesFolder;
    /** The locale. */
    private String locale;
    /** The duplicates Value by Key by File. */
    private final Map<File, Map<String, String>> duplicatesValueByKeyByFile = new HashMap<File, Map<String, String>>();

    /**
     * @param resourcesFolder the resourcesFolder to set
     */
    public final void setResourcesFolder(final String resourcesFolder) {
        this.resourcesFolder = new File(resourcesFolder);
    }

    /**
     * @param locale the locale to set
     */
    public final void setLocale(final String locale) {
        this.locale = locale;
    }

    @Override
    public void execute() {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return (file.getName().toLowerCase().endsWith((locale.length() > 0 ? "_" + locale : locale) + ".properties"));
            }
        };
        try {
            for (File file : resourcesFolder.listFiles(filter)) {
                check(file);
            }
            printResults();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printResults() {
        for (File file : duplicatesValueByKeyByFile.keySet()) {
            System.out.println("Duplicates found in " + file.getAbsolutePath());
            for (String key : duplicatesValueByKeyByFile.get(file).keySet()) {
                System.out.println("\t" + key + "=" + duplicatesValueByKeyByFile.get(file).get(key));
            }
        }
    }

    private void check(final File file) throws FileNotFoundException, IOException {
        Map<String, String> duplicatesValueByKey = new HashMap<String, String>();
        Properties prop = new Properties();
        prop.load(new FileInputStream(file));

        Map<String, String> tmp = new HashMap<String, String>();
        for (Object key : prop.keySet()) {
            if (!tmp.containsValue(prop.get(key))) {
                tmp.put((String) key, (String) prop.get(key));
            } else {
                String validKey = getKeyForValue(tmp, (String) prop.get(key));
                duplicatesValueByKey.put((String) key, (String) prop.get(key) + "|" + validKey);
            }
        }

        if (duplicatesValueByKey.size() > 0) {
            duplicatesValueByKeyByFile.put(file, duplicatesValueByKey);
        }
    }

    private <T, T2> T getKeyForValue(final Map<T, T2> map, final T2 value) {
        for (T key : map.keySet()) {
            if (map.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            args = new String[] { "./src/resources", "" };
        }
        I18nDuplicateFinderTask finder = new I18nDuplicateFinderTask();
        finder.setResourcesFolder(args[0]);
        finder.setLocale(args[1]);
        finder.execute();
    }
}
