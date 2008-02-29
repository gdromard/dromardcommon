package net.dromard.common.ant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * This class represent an ear application.xml file.
 * It makes ant capable to generate an application.xml file
 * <br>
 * @author    Pingus
 */
public class EarApplicationTask extends XmlAntTask {
    // ----------------------------------------------

    /**
     * Task constructor.
     */
    public EarApplicationTask() {
        super("application");
    }

    /**
     * Create the child element.
     * @return A new child instance.
     */
    public final DisplayName createDisplayName() {
        DisplayName displayName = new DisplayName();
        addChild(displayName);
        return displayName;
    }

    /**
     * Child class definition.
     */
    public class DisplayName extends XmlAntTask {
        /**
         * Constructor.
         */
        public DisplayName() {
            super("display-name");
        }

        /**
         * Attribute setter.
         * @param displayName The attribute value.
         */
        public final void setDisplayName(final String displayName) {
            addText(displayName);
        }
    }

    // ----------------------------------------------

    /**
     * Create the child element.
     * @return A new child instance.
     */
    public final Module createModule() {
        Module module = new Module();
        addChild(module);
        return module;
    }

    /**
     * Child class definition.
     */
    public class Module extends XmlAntTask {
        /**
         * Constructor.
         */
        public Module() {
            super("module");
        }

        /**
         * Create the child element.
         * @return A new child instance.
         */
        public final Web createWeb() {
            Web web = new Web();
            addChild(web);
            return web;
        }

        /**
         * Child class definition.
         */
        public class Web extends XmlAntTask {
            /**
             * Constructor.
             */
            public Web() {
                super("web");
            }

            /**
             * Create the child element.
             * @return A new child instance.
             */
            public final WebUri createWebUri() {
                WebUri webUri = new WebUri();
                addChild(webUri);
                return webUri;
            }

            /**
             * Child class definition.
             */
            public class WebUri extends XmlAntTask {
                /**
                 * Constructor.
                 */
                public WebUri() {
                    super("web-uri");
                }

                /**
                 * Attribute setter.
                 * @param webUri The attribute value.
                 */
                public final void setWebUri(final String webUri) {
                    addText(webUri);
                }
            }

            /**
             * Create the child element.
             * @return A new child instance.
             */
            public final ContextRoot createContextRoot() {
                ContextRoot contextRoot = new ContextRoot();
                addChild(contextRoot);
                return contextRoot;
            }

            /**
             * Child class definition.
             */
             public class ContextRoot extends XmlAntTask {
                 /**
                  * Constructor.
                  */
                public ContextRoot() {
                    super("context-root");
                }

                /**
                 * Attribute setter.
                 * @param contextRoot The attribute value.
                 */
                public final void setContextroot(final String contextRoot) {
                    addText(contextRoot);
                }
            }
        }
    }

    // ----------------------------------------------

    /**
     * The file destination.
     */
    private File toFile = null;

    /**
     * Execute the Ant Task.
     * @throws BuildException if toFile attribute is not present.
     */
    public final void execute() throws BuildException {
        if (toFile == null) {
            throw new BuildException("Attribute toFile undefined");
        }

        String s = toString();
        log(s, Project.MSG_INFO);
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(toFile));
            pw.print(s);
            pw.close();
        } catch (IOException ex) {
            throw new BuildException("Error writing application XML to " + toFile.getAbsolutePath(), ex);
        }
    }

    /**
     * Set file destination.
     * @param file The file in which the XML string representing this task has to be stored.
     */
    public final void setToFile(final File file) {
        toFile = file;
    }
}
