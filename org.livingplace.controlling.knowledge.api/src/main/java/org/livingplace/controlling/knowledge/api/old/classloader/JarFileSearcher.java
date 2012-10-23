package org.livingplace.controlling.knowledge.api.old.classloader;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileSearcher {
    public static List<FileObject> search(String searchPattern, File file) throws IOException {
        JarFile jarFile = new JarFile(file);

        List<FileObject> result = new ArrayList<FileObject>();

        FileSystemManager fsManager = VFS.getManager();
        fsManager.createVirtualFileSystem("ram:///");

        Enumeration e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();
            if (je.isDirectory()) {
                continue;
            }

            // this is a drl jarFile...
            if (je.getName().matches(searchPattern)) {

                System.out.println(je.getName());

                fsManager.resolveFile("ram:///" + je.getName()).createFile();
                FileObject fo = fsManager.resolveFile("ram:///" + file.getPath() + File.pathSeparator + je.getName());

                InputStream is = jarFile.getInputStream(je);
                OutputStream os = fo.getContent().getOutputStream();
                while (is.available() > 0)
                    os.write(is.read());
                os.close();
                is.close();

                result.add(fo);
            }
        }

        return result;
    }

    public static List<FileObject> searchDRLs(File fileToSearchThrough) throws IOException {
        return search(".*\\.drl", fileToSearchThrough);
    }
}
