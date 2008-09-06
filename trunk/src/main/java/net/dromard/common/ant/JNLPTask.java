package net.dromard.common.ant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * @version 1.0
 * @since 03.09.2003
 * @author alvaro.diaz@roxes.com
 * 
 * (c) Copyright 2003 ROXES Technologies (www.roxes.com).
 */

interface JNLPTaskMember {
    public void toString(StringBuffer sb, int depth);
    public void execute() throws BuildException;
}

/**
 * 
 * Usage exemple:
 * 
 * <pre>
 *   &lt;taskdef name=&quot;jnlp&quot; classname=&quot;net.dromard.common.util.JNLPTask&quot; classpath=&quot;${home.dir}/classes&quot; /&gt;
 *   
 *   &lt;target name=&quot;jnlp&quot;&gt;
 *       &lt;jnlp spec=&quot;1.0+&quot; 
 *            version=&quot;0.3&quot; 
 *            description=&quot;Application Description&quot;
 *            codebase=&quot;http://www.webserver.com/ApplicationHome/&quot; 
 *            href=&quot;Application.jnlp&quot; 
 *            tofile=&quot;Application.jnlp&quot;&gt;
 *            &lt;information&gt;
 *                &lt;title&gt;Application Name&lt;/title&gt;
 *                &lt;vendor&gt;Vendor Name&lt;/vendor&gt; 
 *                &lt;homepage href=&quot;http://www.webserver.com/ApplicationHome&quot;/&gt;
 *                &lt;description&gt;Application Name&lt;/description&gt;
 *                &lt;description kind=&quot;short&quot;&gt;Application short description.&lt;/description&gt;
 *                &lt;icon href=&quot;images/icon.gif&quot;/&gt;
 *                &lt;icon width=&quot;64&quot; height=&quot;64&quot; href=&quot;images/icon64x64.gif&quot;/&gt;
 *                &lt;icon width=&quot;32&quot; height=&quot;32&quot; href=&quot;images/icon32x32.gif&quot;/&gt;
 *                &lt;offline_allowed/&gt;
 *                &lt;shortcut online=&quot;false&quot;&gt;
 *                  &lt;desktop/&gt;
 *                  &lt;menu submenu=&quot;yFiles Demo Applications&quot;/&gt;
 *                &lt;/shortcut&gt;
 *            &lt;/information&gt;
 *            &lt;security&gt;
 *                &lt;all_permissions/&gt;
 *            &lt;/security&gt;
 *            &lt;resources&gt;
 *                &lt;j2se version=&quot;1.4+&quot; java-vm-args=&quot;-esa -Xnoclassgc&quot;/&gt;
 *                &lt;jar href=&quot;Application.jar&quot;/&gt;
 *            &lt;/resources&gt;
 *            &lt;application_desc main_class=&quot;com.vendor.application.MainClass&quot; /&gt;
 *        &lt;/jnlp&gt;
 *     &lt;/target&gt;
 *  
 * </pre>
 */
public class JNLPTask extends Task implements JNLPTaskMember {
    public class Information implements JNLPTaskMember {
        String locale = null;

        public void execute() throws BuildException {
        //
        }
        public void toString(StringBuffer sb, int depth) {
            appendTabs(sb, depth);
            sb.append("<information");
            if (locale != null)
                sb.append(" locale=\"").append(locale).append('\"');
            sb.append('>').append(br());

            title.toString(sb, depth + 1);
            vendor.toString(sb, depth + 1);
            homepage.toString(sb, depth + 1);

            for (int i = 0; i < descriptions.size(); i++) {
                Description description = (Description) descriptions.get(i);
                description.toString(sb, depth + 1);
            }

            for (int i = 0; i < shortcuts.size(); i++) {
                Shortcut shortcut = (Shortcut) shortcuts.get(i);
                shortcut.toString(sb, depth + 1);
            }

            for (int i = 0; i < icons.size(); i++) {
                Icon icon = (Icon) icons.get(i);
                icon.toString(sb, depth + 1);
            }

            if (offlineAllowed != null && offlineAllowed.booleanValue()) {
                appendTabs(sb, depth + 1);
                sb.append("<offline-allowed/>").append(br());
            }

            appendTabs(sb, depth);
            sb.append("</information>").append(br());
        }

        public class Vendor implements JNLPTaskMember {
            String text;

            public void setValue(String text) {
                this.text = text;
            }

            public void addText(String text) {
                this.text = text;
            }
            
            public String getText() {
                return text;
            }

            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                if (vendor != null) {
                    appendTabs(sb, depth);
                    sb.append("<vendor>").append(getText()).append("</vendor>").append(br());
                }
            }
        }

        public class Title implements JNLPTaskMember {
            String text;

            public void setValue(String text) {
                this.text = text;
            }

            public void addText(String text) {
                this.text = text;
            }
            
            public String getText() {
                return text;
            }

            public void execute() throws BuildException {
            //
            }

            public void toString(StringBuffer sb, int depth) {
                if (title != null) {
                    appendTabs(sb, depth);
                    sb.append("<title>").append(getText()).append("</title>").append(br());
                }
            }

        }


        Title title = new Title();
        Vendor vendor = new Vendor();
        Homepage homepage = new Homepage();

        public class Homepage implements JNLPTaskMember {
            String href = null;

            public void setHref(String string) {
                href = string;
            }

            public void execute() throws BuildException {
            //
            }

            public void toString(StringBuffer sb, int depth) {
                if (href != null) {
                    appendTabs(sb, depth);
                    sb.append("<homepage href=\"").append(href).append("\"/>").append(br());
                }
            }
        }


        public class Description implements JNLPTaskMember {
            String text;

            public void setValue(String text) {
                this.text = text;
            }

            public void addText(String text) {
                this.text = text;
            }
            
            public String getText() {
                return text;
            }

            String kind = null;

            public void setKind(String string) {
                kind = string;
            }

            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<description");
                if (kind != null) sb.append(" kind=\"").append(kind).append('\"');
                sb.append('>');
                sb.append(getText());
                sb.append("</description>").append(br());
            }

        }

        public Homepage createHomepage() {
            return homepage;
        }

        public Vendor createVendor() {
            return vendor;
        }

        public Title createTitle() {
            return title;
        }

        ArrayList descriptions = new ArrayList();

        public Description createDescription() {
            Description description = new Description();
            descriptions.add(description);
            return description;
        }

        Boolean offlineAllowed = null;

        public Boolean createOffline_Allowed() {
            return (offlineAllowed = Boolean.TRUE);
        }

        ArrayList shortcuts = new ArrayList();

        public Shortcut createShortcut() {
            Shortcut shortcut = new Shortcut();
            shortcuts.add(shortcut);
            return shortcut;
        }

        public class Icon implements JNLPTaskMember {
            String href = null, kind = null, version = null, width = null, height = null, depth = null, size = null;

            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<icon");
                if (href != null)
                    sb.append(" href=\"").append(href).append('\"');

                if (version != null)
                    sb.append(" version=\"").append(version).append('\"');

                if (width != null)
                    sb.append(" width=\"").append(width).append('\"');

                if (height != null)
                    sb.append(" height=\"").append(height).append('\"');

                if (kind != null)
                    sb.append(" kind=\"").append(kind).append('\"');

                if (this.depth != null)
                    sb.append(" depth=\"").append(this.depth).append('\"');

                if (size != null)
                    sb.append(" size=\"").append(size).append('\"');

                sb.append("/>").append(br());
            }

            public void setHref(String s) {
                href = s;
            }

            public void setKind(String s) {
                kind = s;
            }

            public void setHeight(String string) {
                height = string;
            }

            public void setSize(String string) {
                size = string;
            }

            public void setVersion(String string) {
                version = string;
            }

            public void setWidth(String string) {
                width = string;
            }
        }

        ArrayList icons = new ArrayList();

        public Icon createIcon() {
            Icon icon = new Icon();
            icons.add(icon);

            return icon;
        }

        public void setLocale(String string) {
            locale = string;
        }
    }
    // ----------------------------------------------
    
    /**
     * Shortcut class.
     *
     * <br/>
     * <pre>
     *    +---------+
     *    | History |
     *    +---------+
     *
     *  [11/10/2006] by Gabriel Dromard
     *     - Add class.
     * </pre>
     * <br/>
     * @author Gabriel Dromard
     */
    public class Shortcut implements JNLPTaskMember {
        String online = null;
        Desktop desktop = null;
        Menu menu = null;

        public void execute() throws BuildException {}
        
        public void setOnline(String string) {
            online = string;
        }

        public void toString(StringBuffer sb, int depth) {
            appendTabs(sb, depth);
            sb.append("<shortcut>").append(br());
            if (desktop == null)
                throw new RuntimeException("<shortcut> requires either <desktop/> or/and <menu submennu=\"SubMenu\"/> as child element.");
            else
                desktop.toString(sb, depth + 1);
            
            if (online != null)
                sb.append(" online=\"").append(online).append('\"');

            appendTabs(sb, depth);
            sb.append("</security>").append(br());
        }

        public Desktop createDesktop() {
            return desktop = new Desktop();
        }

        public Menu createMenu() {
            return menu = new Menu();
        }

        public class Desktop implements JNLPTaskMember {
            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append('<').append("desktop").append("/>").append(br());
            }
        }

        public class Menu implements JNLPTaskMember {
            String submenu = null;
            
            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append('<').append("menu");
                if (submenu != null) sb.append(" submenu=\"").append(submenu).append('\"');
                sb.append("/>").append(br());
            }
        }
    }
    
    // -----------------------------------------------
    
    public class Security implements JNLPTaskMember {
        Permission permission = null;

        public void execute() throws BuildException {}

        public void toString(StringBuffer sb, int depth) {
            appendTabs(sb, depth);
            sb.append("<security>").append(br());
            if (permission == null)
                throw new RuntimeException("<security> requires either <all_permissions/> or <j2ee_application_client_permissions/> as child element.");
            else
                permission.toString(sb, depth + 1);

            appendTabs(sb, depth);
            sb.append("</security>").append(br());
        }

        public class Permission implements JNLPTaskMember {
            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append('<').append(name).append("/>").append(br());
            }

            String name;

            public Permission(String s) {
                name = s;
            }
        }

        public Permission createAll_Permissions() {
            return permission = new Permission("all-permissions");
        }

        public Permission createJ2ee_Application_Client_Permissions() {
            return permission = new Permission("j2ee-application-client-permissions");
        }
    }

    Security security = null;

    public Security createSecurity() {
        return security = new Security();
    }

    public class ApplicationDesc implements JNLPTaskMember {
        String mainClass = null;

        ArrayList arguments = new ArrayList();

        public void execute() throws BuildException {}

        public void toString(StringBuffer sb, int depth) {
            appendTabs(sb, depth);
            sb.append("<application-desc");

            if (mainClass == null)
                throw new RuntimeException("<application_desc> requires attribute main_class");
            else
                sb.append(" main-class=\"").append(mainClass).append('"');

            if (arguments.size() == 0)
                sb.append("/>").append(br());
            else
                sb.append('>').append(br());

            for (int i = 0; i < arguments.size(); i++) {
                Argument arg = (Argument) arguments.get(i);
                arg.toString(sb, depth + 1);
            }

            if (arguments.size() > 0) {
                appendTabs(sb, depth);
                sb.append("</application-desc>").append(br());
            }
        }

        public class Argument implements JNLPTaskMember {
            String text = null;

            public void addText(String s) {
                text = s;
            }

            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<argument>");

                if (text == null)
                    throw new RuntimeException("<argument> requires #pcdata content");
                else
                    sb.append(text);

                sb.append("</argument>").append(br());
            }
        };

        public void setMain_Class(String string) {
            mainClass = string;
        }

        public Argument createArgument() {
            Argument argument = new Argument();
            arguments.add(argument);
            return argument;
        }
    }

    ApplicationDesc applicationDesc = null;

    public ApplicationDesc createApplication_Desc() {
        return applicationDesc = new ApplicationDesc();
    }

    public class AppletDesc implements JNLPTaskMember {
        ArrayList params = new ArrayList();

        String documentBase = null, mainClass = null, name = null, width = null, height = null;

        public class Param implements JNLPTaskMember {
            String name = null, value = null;

            public void setName(String string) {
                name = string;
            }

            public void setValue(String string) {
                value = string;
            }

            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<param");

                if (name == null)
                    throw new RuntimeException("<param> requires attribute name");
                else
                    sb.append(" name=\"").append(name).append('"');

                if (value == null)
                    throw new RuntimeException("<param> requires attribute value");
                else
                    sb.append(" value=\"").append(value).append('"');

                sb.append("/>").append(br());
            }
        };

        public Param createArgument() {
            Param param = new Param();
            params.add(param);
            return param;
        }

        public void setHeight(String string) {
            height = string;
        }

        public void setMainClass(String string) {
            mainClass = string;
        }

        public void setName(String string) {
            name = string;
        }

        public void setWidth(String string) {
            width = string;
        }

        public void execute() throws BuildException {}

        public void toString(StringBuffer sb, int depth) {
            appendTabs(sb, depth);
            sb.append("<applet-desc");

            if (name == null)
                throw new RuntimeException("<applet_desc> requires attribute name");
            else
                sb.append(" name=\"").append(name).append('"');

            if (mainClass == null)
                throw new RuntimeException("<applet_desc> requires attribute main_class");
            else
                sb.append(" mainClass=\"").append(mainClass).append('"');

            if (width == null)
                throw new RuntimeException("<applet_desc> requires attribute width");
            else
                sb.append(" width=\"").append(width).append('"');

            if (height == null)
                throw new RuntimeException("<applet_desc> requires attribute height");
            else
                sb.append(" height=\"").append(height).append('"');

            if (documentBase != null)
                sb.append(" documentbase=\"").append(documentBase).append('"');

            if (params.size() == 0)
                sb.append("\">").append(br());
            else
                sb.append('>').append(br());

            for (int i = 0; i < params.size(); i++) {
                Param param = (Param) params.get(i);
                param.toString(sb, depth + 1);
            }

            if (params.size() > 0) {
                appendTabs(sb, depth);
                sb.append("</applet-desc>").append(br());
            }
        }

        public void setDocumentBase(String string) {
            documentBase = string;
        }

    }

    File toFile = null;

    AppletDesc appletDesc = null;

    public AppletDesc createApplet_Desc() {
        return appletDesc = new AppletDesc();
    }

    public class ComponentDesc implements JNLPTaskMember {
        public void execute() throws BuildException {}

        public void toString(StringBuffer sb, int depth) {
            appendTabs(sb, depth);
            sb.append("<component-desc/>").append(br());
        }
    }

    ComponentDesc componentDesc = null;

    public ComponentDesc createComponent_Desc() {
        return componentDesc = new ComponentDesc();
    }

    public class InstallerDesc implements JNLPTaskMember {
        String mainClass = null;

        public void setMain_Class(String string) {
            mainClass = string;
        }

        public void execute() throws BuildException {}

        public void toString(StringBuffer sb, int depth) {
            appendTabs(sb, depth);
            sb.append("<installer-desc");

            if (mainClass == null)
                throw new RuntimeException("<installer_desc> requires attribute main_class");
            else
                sb.append(" main-class=\"").append(mainClass).append('"');

            sb.append("</installer-desc>").append(br());
        }
    }

    InstallerDesc installerDesc = null;

    public InstallerDesc createInstaller_Desc() {
        return installerDesc = new InstallerDesc();
    }

    String spec = "1.0+";

    String codebase = null;

    String href = null;

    String version = null;

    ArrayList informations = new ArrayList();

    public class Resources implements JNLPTaskMember {
        ArrayList filesets = new ArrayList();

        ArrayList jars = new ArrayList();

        ArrayList nativeLibs = new ArrayList();

        ArrayList extensions = new ArrayList();

        ArrayList packages = new ArrayList();

        ArrayList properties = new ArrayList();

        ArrayList j2ses = new ArrayList();

        String os = null, arch = null, locale = null;

        public void execute() throws BuildException {}

        public void toString(StringBuffer sb, int depth) {
            appendTabs(sb, depth);
            sb.append("<resources");
            if (os != null)
                sb.append(" os=\"").append(os).append('"');
            if (arch != null)
                sb.append(" arch=\"").append(arch).append('"');
            if (locale != null)
                sb.append(" locale=\"").append(locale).append('"');
            sb.append('>').append(br());

            for (int i = 0; i < j2ses.size(); i++) {
                J2SE j2se = (J2SE) j2ses.get(i++);
                j2se.toString(sb, depth + 1);
            }

            // lookup filesets and
            // insert the fileset resources
            // into jars | nativeLibs
            for (int i = 0; i < filesets.size(); i++) {
                FileSet fileSet = (FileSet) filesets.get(i);
                DirectoryScanner directoryscanner = fileSet.getDirectoryScanner(getProject());
                String files[] = directoryscanner.getIncludedFiles();

                for (int j = 0; j < files.length; j++) {
                    String string = files[j];
                    if (string.endsWith(".dll") || string.endsWith(".so")) {
                        NativeLib nativeLib = createNativeLib();
                        nativeLib.href = string.replace('\\', '/').replace(' ', '+');
                    } else {
                        Jar jar = createJar();
                        jar.href = string.replace('\\', '/').replace(' ', '+');
                    }
                }
            }
            // --

            for (int i = 0; i < jars.size(); i++) {
                Jar jar = (Jar) jars.get(i);
                jar.toString(sb, depth + 1);
            }

            for (int i = 0; i < nativeLibs.size(); i++) {
                NativeLib nativeLib = (NativeLib) nativeLibs.get(i);
                nativeLib.toString(sb, depth + 1);
            }

            for (int i = 0; i < extensions.size(); i++) {
                Extension extension = (Extension) extensions.get(i);
                extension.toString(sb, depth + 1);
            }

            for (int i = 0; i < properties.size(); i++) {
                Property property = (Property) properties.get(i);
                property.toString(sb, depth + 1);
            }

            for (int i = 0; i < packages.size(); i++) {
                Package _package = (Package) packages.get(i);
                _package.toString(sb, depth + 1);
            }

            appendTabs(sb, depth);
            sb.append("</resources>").append(br());
        }

        public void setArch(String string) {
            arch = string;
        }

        public void setLocale(String string) {
            locale = string;
        }

        public void setOs(String string) {
            os = string;
        }

        public void addFileset(FileSet fileset) {
            filesets.add(fileset);
        }

        public class NativeLib implements JNLPTaskMember {
            String href = null, part = null, download = null, version = null, size = null;

            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<nativelib");

                if (href == null)
                    throw new RuntimeException("<nativelib> requires attribute href");
                else
                    sb.append(" href=\"").append(href).append('"');

                if (version != null)
                    sb.append(" version=\"").append(version).append('"');

                if (download != null)
                    sb.append(" download=\"").append(download).append('"');

                if (size != null)
                    sb.append(" size=\"").append(size).append('"');

                if (part != null)
                    sb.append(" part=\"").append(part).append('"');

                sb.append("/>").append(br());
            }

            public void setDownload(String string) {
                download = string;
            }

            public void setHref(String string) {
                href = string;
            }

            public void setPart(String string) {
                part = string;
            }

            public void setSize(String string) {
                size = string;
            }

            public void setVersion(String string) {
                version = string;
            }
        }

        public class Jar implements JNLPTaskMember {
            String href = null, part = null, download = null, size = null, main = null, version = null;

            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<jar");

                if (href == null)
                    throw new RuntimeException("<jar> requires attribute href");
                else
                    sb.append(" href=\"").append(href).append('"');

                if (version != null)
                    sb.append(" version=\"").append(version).append('"');

                if (main != null)
                    sb.append(" main=\"").append(main).append('"');

                if (download != null)
                    sb.append(" download=\"").append(download).append('"');

                if (size != null)
                    sb.append(" size=\"").append(size).append('"');

                if (part != null)
                    sb.append(" part=\"").append(part).append('"');

                sb.append("/>").append(br());
            }

            public void setDownload(String string) {
                download = string;
            }

            public void setHref(String string) {
                href = string;
            }

            public void setPart(String string) {
                part = string;
            }

            public void setMain(String string) {
                main = string;
            }

            public void setSize(String string) {
                size = string;
            }

            public void setVersion(String string) {
                version = string;
            }
        };

        public class Extension implements JNLPTaskMember {
            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<extension");

                if (href == null)
                    throw new RuntimeException("<extension> requires attribute href");
                else
                    sb.append(" href=\"").append(href).append('"');

                if (version != null)
                    sb.append(" version=\"").append(version).append('"');

                if (name != null)
                    sb.append(" name=\"").append(name).append('"');

                if (extDownloads.size() == 0)
                    sb.append("\">").append(br());
                else
                    sb.append('>').append(br());

                for (int i = 0; i < resources.size(); i++) {
                    ExtDownload extDownload = (ExtDownload) extDownloads.get(i);
                    extDownload.toString(sb, depth + 1);
                }

                if (extDownloads.size() > 0) {
                    appendTabs(sb, depth);
                    sb.append("</extension>").append(br());
                }
            }

            public class ExtDownload implements JNLPTaskMember {
                public void execute() throws BuildException {}

                public void toString(StringBuffer sb, int depth) {
                    appendTabs(sb, depth);
                    sb.append("<ext-download");

                    if (extPart == null)
                        throw new RuntimeException("<ext-download> requires attribute ext-part");
                    else
                        sb.append(" ext-part=\"").append(extPart).append('"');

                    if (download != null)
                        sb.append(" download=\"").append(download).append('"');

                    if (part != null)
                        sb.append(" part=\"").append(part).append('"');

                    sb.append("/>").append(br());
                }

                String extPart = null, download = null, part = null;

                public void setDownload(String string) {
                    download = string;
                }

                public void setExt_Part(String string) {
                    extPart = string;
                }

                public void setPart(String string) {
                    part = string;
                }
            }

            ArrayList extDownloads = new ArrayList();

            public ExtDownload createExtDownload() {
                ExtDownload extDownload = new ExtDownload();
                extDownloads.add(extDownloads);
                return extDownload;
            }

            String name = null, version = null, href = null;

            public void setHref(String string) {
                href = string;
            }

            public void setName(String string) {
                name = string;
            }

            public void setVersion(String string) {
                version = string;
            }
        }

        public class Property implements JNLPTaskMember {
            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<property");

                if (name == null)
                    throw new RuntimeException("<property> requires attribute name");
                else
                    sb.append(" name=\"").append(name).append('"');

                if (value == null)
                    throw new RuntimeException("<property> requires attribute value");
                else
                    sb.append(" value=\"").append(value).append('"');

                sb.append("/>").append(br());
            }

            String name = null, value = null;

            public void setName(String string) {
                name = string;
            }

            public void setValue(String string) {
                value = string;
            }
        };

        public class Package implements JNLPTaskMember {
            String name = null, part = null, recursive = null;

            public void setName(String string) {
                name = string;
            }

            public void setPart(String string) {
                part = string;
            }

            public void setRecursive(String string) {
                recursive = string;
            }

            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<package");

                if (name == null)
                    throw new RuntimeException("<package> requires attribute name");
                else
                    sb.append(" name=\"").append(name).append('"');

                if (part == null)
                    throw new RuntimeException("<package> requires attribute part");
                else
                    sb.append(" part=\"").append(part).append('"');

                if (recursive != null)
                    sb.append(" recursive=\"").append(recursive).append('"');

                sb.append("/>").append(br());
            }
        };

        /**
         * 
         * J2SE class.
         *
         * <br/>
         * <pre>
         *    +---------+
         *    | History |
         *    +---------+
         *
         *  [11/10/2006] by Gabriel Dromard
         *     - Add java-vm-args="-esa -Xnoclassgc".
         * </pre>
         * <br/>
         * @author Gabriel Dromard
         */
        public class J2SE implements JNLPTaskMember {
            public void execute() throws BuildException {}

            public void toString(StringBuffer sb, int depth) {
                appendTabs(sb, depth);
                sb.append("<j2se");
                if (version == null)
                    throw new RuntimeException("<j2se> requires attribute version");
                else
                    sb.append(" version=\"").append(version).append('"');

                if (href != null)
                    sb.append(" href=\"").append(href).append('"');

                if (initialHeapSize != null)
                    sb.append(" initial-heap-size=\"").append(initialHeapSize).append('"');

                if (maxHeapSize != null)
                    sb.append(" max-heap-size=\"").append(maxHeapSize).append('"');

                if (javaVmArgs != null)
                    sb.append(" java-vm-args=\"").append(javaVmArgs).append('"');

                if (this.resources.size() == 0)
                    sb.append("/>").append(br());
                else
                    sb.append('>').append(br());

                for (int i = 0; i < resources.size(); i++) {
                    JNLPTask.Resources resources = (JNLPTask.Resources) this.resources.get(i);
                    resources.toString(sb, depth + 1);
                }

                if (this.resources.size() > 0) {
                    appendTabs(sb, depth);
                    sb.append("</j2se>").append(br());
                }
            }

            ArrayList resources = new ArrayList();

            public Resources createResources() {
                Resources _resources = new Resources();
                resources.add(_resources);
                return _resources;
            }

            String version = null, href = null, initialHeapSize = null, maxHeapSize = null, javaVmArgs = null;

            public void setHref(String string) {
                href = string;
            }

            public void setInitial_Heap_Size(String string) {
                initialHeapSize = string;
            }

            public void setMax_Heap_Size(String string) {
                maxHeapSize = string;
            }

            public void setJava_Vm_Args(String string) {
                javaVmArgs = string;
            }

            public void setVersion(String string) {
                version = string;
            }
        };

        public Jar createJar() {
            Jar jar = new Jar();
            jars.add(jar);
            return jar;
        }

        public J2SE createJ2SE() {
            J2SE j2se = new J2SE();
            j2ses.add(j2se);
            return j2se;
        }

        public Extension createExtension() {
            Extension extension = new Extension();
            extensions.add(extension);
            return extension;
        }

        public Property createProperty() {
            Property property = new Property();
            properties.add(property);
            return property;
        }

        public Package createPackage() {
            Package _package = new Package();
            packages.add(_package);
            return _package;
        }

        public NativeLib createNativeLib() {
            NativeLib nativeLib = new NativeLib();
            nativeLibs.add(nativeLib);
            return nativeLib;
        }
    }

    ArrayList resources = new ArrayList();

    public Resources createResources() {
        Resources _resources = new Resources();
        resources.add(_resources);
        return _resources;
    }

    public void execute() throws BuildException {
        if (toFile == null)
            throw new BuildException("Attribute toFile undefined");

        String s = toString();
        log(s, Project.MSG_INFO);
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(toFile));
            pw.print(s);
            pw.close();
        } catch (IOException ex) {
            throw new BuildException("Error writing created jndi signature to " + toFile.getAbsolutePath(), ex);
        }
    }

    public Information createInformation() {
        Information information = new Information();
        informations.add(information);
        return information;
    }

    public void setCodebase(String string) {
        codebase = string;
    }

    public void setVersion(String string) {
        version = string;
    }

    public void setSpec(String string) {
        spec = string;
    }

    public void setHref(String string) {
        href = string;
    }

    public void toString(StringBuffer sb, int depth) {
        sb.append("<?xml version=\"1.0\"?>").append(br());

        sb.append("<jnlp");
        if (spec != null)
            sb.append(" spec=\"").append(spec).append("\"");
        if (version != null)
            sb.append(" version=\"").append(version).append("\"");
        if (codebase != null)
            sb.append(" codebase=\"").append(codebase).append("\"");
        if (href != null)
            sb.append(" href=\"").append(href).append("\"");
        sb.append('>').append(br());

        for (int i = 0; i < informations.size(); i++) {
            Information information = (Information) informations.get(i);
            information.toString(sb, 1);
        }

        if (security != null)
            security.toString(sb, depth + 1);

        for (int i = 0; i < resources.size(); i++) {
            JNLPTask.Resources resources = (JNLPTask.Resources) this.resources.get(i);
            resources.toString(sb, 1);
        }

        int choice = 0;
        if (applicationDesc != null)
            choice++;
        if (appletDesc != null)
            choice++;
        if (componentDesc != null)
            choice++;
        if (installerDesc != null)
            choice++;

        if (choice == 0)
            throw new RuntimeException("<jndi> : Sub element missing ( application-desc | applet-desc | component-desc | installer-desc)");

        if (choice > 1)
            throw new RuntimeException("<jndi> : Only one of application-desc | applet-desc | component-desc | installer-desc expected");

        if (applicationDesc != null)
            applicationDesc.toString(sb, depth + 1);

        if (appletDesc != null)
            appletDesc.toString(sb, depth + 1);

        if (componentDesc != null)
            componentDesc.toString(sb, depth + 1);

        if (installerDesc != null)
            installerDesc.toString(sb, depth + 1);

        sb.append("</jnlp>").append(br());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        toString(sb, 0);
        return sb.toString();
    }

    void appendTabs(StringBuffer sb, int depth) {
        while (depth-- > 0) {
            sb.append('\t');
        }
    }

    String br() {
        return "\r\n";
    }

    public void setToFile(File file) {
        toFile = file;
    }
}
