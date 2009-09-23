package net.dromard.common.widget.zipviewer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.tree.DefaultTreeModel;

import net.dromard.common.util.StringHelper;

/**
 * A ZipEntreyTreeModel.
 * @author Gabriel Dromard
 */
public class ZipEntryTreeModel extends DefaultTreeModel {
    /** The serialVersionUID. */
    private static final long serialVersionUID = -1750530388684495121L;

    public ZipEntryTreeModel(final ZipFile zipFile) throws IOException {
        super(null);
        ZipNode root = new ZipNode(new File(zipFile.getName()).getName());
        load(root, zipFile.entries());
        zipFile.close();
    }

    private void load(final ZipNode root, final Enumeration<? extends ZipEntry> entries) {
        final Map<String, ZipNode> nodesByPath = new HashMap<String, ZipNode>();
        while (entries.hasMoreElements()) {
            load(nodesByPath, root, ZipEntryTreeModel.getZipEntryName(entries.nextElement().getName()));
        }
        // filter(nodesByPath);
        for (ZipNode node : nodesByPath.values()) {
            if (hasToBeFilter(node)) {
                filter(node);
            }
        }
        super.setRoot(root);
    }

    /**
     * This method is called at the end of model initialization, you can override it to remove a
     * zip node from the model.
     * Empty implementation.
     * @param zipNode A zip node.
     */
    protected boolean hasToBeFilter(final ZipNode zipNode) {
        return false;
    }

    /**
     * Used by {@link #filter(ZipNode)} method to remove a filtered node.
     * @param zipNode The zipNode to be filtered.
     */
    protected void filter(final ZipNode zipNode) {
        ZipNode parent = (ZipNode) zipNode.getParent();
        if (parent != null) {
            System.out.println("Removing " + zipNode.getEntryPath());
            parent.remove(zipNode);
            if (parent.getChildCount() == 0) {
                filter(parent);
            }
        }
    }

    private ZipNode load(final Map<String, ZipNode> nodesByPath, final ZipNode root, final String path) {
        String lPath = StringHelper.trim(path, "/");
        if (nodesByPath.containsKey(lPath)) {
            return nodesByPath.get(lPath);
        }
        int token = lPath.lastIndexOf("/");
        if (token == lPath.length() - 1) {
            token = -1;
        } else {
            if (token == -1) {
                token = lPath.lastIndexOf("\\");
            }
        }
        System.out.println("Threading " + lPath);
        ZipNode zipNode = new ZipNode(lPath);
        if (token > -1) {
            String parentFolder = lPath.substring(0, token);
            ZipNode parent = nodesByPath.get(parentFolder);
            if (parent == null) {
                parent = load(nodesByPath, root, parentFolder);
            }
            ZipEntryTreeModel.insert(parent, zipNode);
        } else {
            ZipEntryTreeModel.insert(root, zipNode);
        }
        nodesByPath.put(lPath, zipNode);
        return zipNode;
    }

    private static void insert(final ZipNode parent, final ZipNode child) {
        for (int i = 0; i < parent.getChildCount(); ++i) {
            ZipNode last = (ZipNode) parent.getChildAt(i);
            if (last.toString().compareTo(child.toString()) >= 0) {
                parent.insert(child, i);
                return;
            }
        }
        parent.add(child);
    }

    /**
     * As the ZIP entry name is in cp850 encoding, this method convert the string in valid string.
     * @param entryName The ZipEntry name.
     * @return The entry name converted
     */
    public static String getZipEntryName(final String entryName) {
        try {
            char[] characters = entryName.toCharArray();
            byte[] bytes = new byte[characters.length];
            for (int i = 0; i < characters.length; ++i) {
                bytes[i] = (byte) characters[i];
            }
            return new String(bytes, "cp850");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return entryName;
        }
    }
}