package org.livingplace.controlling.knowledge.api.old.classloader.listening;

import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;

/**
 * Finds all files with a "jar" ending.
 * @author kjellski
 *
 */
public class RecursiveJarFileSelector implements FileSelector {

    public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {
        return true;
    }

    public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
        return fileInfo.getFile().getName().getExtension().equals("jar");
    }
}
