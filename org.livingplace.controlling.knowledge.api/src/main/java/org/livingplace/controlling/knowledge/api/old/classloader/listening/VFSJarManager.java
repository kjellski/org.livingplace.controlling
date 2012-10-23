package org.livingplace.controlling.knowledge.api.old.classloader.listening;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.apache.log4j.Logger;
import org.livingplace.controlling.knowledge.api.old.classloader.JarFileInspector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to retrieve all jar files form a given directory path. it also supports changes to these files in
 * order to re or unload them.
 * 
 * @author kjellski
 * 
 */
public class VFSJarManager extends Thread implements FileListener {

    /**
     * Path used to be monitored
     */
    private final String basePath;

    /**
     * FileMonitor to be used for the monitoring itself
     */
    private final DefaultFileMonitor fm;
    /**
     * Logger for this class
     */
    private Logger logger = Logger.getLogger(VFSJarManager.class.getName());

    /**
     * JarFile list to be used for marking changes to the existing files under the filder to be watched.
     */
    private List<FileObject> jarFiles = new ArrayList<FileObject>();

    /**
     * Adapted class from <see>org.apache.commons.vfs2.VFS</see> and modified to listen recursively for all jar files
     * under a given path.
     * 
     * @param path
     * @throws FileSystemException
     */
    public VFSJarManager(String path) throws FileSystemException {
        this.basePath = path;

        FileSystemManager fsManager = VFS.getManager();
        FileObject listendir = fsManager.resolveFile(path);

        fm = new DefaultFileMonitor(this);
        fm.setRecursive(true);
        fm.addFile(listendir);
        for (FileObject fo : Arrays.asList(listendir.findFiles(new RecursiveJarFileSelector()))) {
            jarFiles.add(fo);
        }
    }

    /**
     * Fired when a FileObject has been changes in the directory tree this monitor is watching
     */
    public void fileChanged(FileChangeEvent arg0) throws Exception {
        System.out.println(arg0.getFile().getURL().toURI().toString() + " changed.");

        FileObject file = arg0.getFile();
        if (isJarFile(file))
            handleJarFileChanged(file);
    }

    /**
     * Fired when a FileObject has been created in the directory tree this monitor is watching
     */
    public void fileCreated(FileChangeEvent arg0) throws Exception {
        System.out.println(arg0.getFile().getURL().toURI().toString() + " created.");

        FileObject file = arg0.getFile();
        if (isJarFile(file))
            handleJarFileCreated(file);
    }

    /**
     * Fired when a FileObject has been deleted in the directory tree this monitor is watching
     */
    public void fileDeleted(FileChangeEvent arg0) throws Exception {
        System.out.println(arg0.getFile().getURL().toURI().toString() + " deleted.");

        FileObject file = arg0.getFile();
        if (isJarFile(file))
            handleJarFileDeleted(file);
    }

    /**
     * handles the creation of a new jar file under the given directory structure.
     * 
     * @param jarFile
     *            created FileObject
     */
    private void handleJarFileCreated(FileObject jarFile) {
        this.logger.info("Jar file created: " + jarFile.toString());
        
        new JarFileInspector(jarFile);
        
        jarFiles.add(jarFile);
        logJarFileList();
    }

    /**
     * handles the change of a jar file under the given directory structure.
     * 
     * @param jarFile
     *            changed FileObject
     */
    private void handleJarFileChanged(FileObject jarFile) {
        jarFiles.remove(jarFile);
        jarFiles.add(jarFile);
        logJarFileList();
    }

    /**
     * handles the deletion of a new jar file under the given directory structure.
     * 
     * @param jarFile
     *            deleted FileObject
     */
    private void handleJarFileDeleted(FileObject jarFile) {
        jarFiles.remove(jarFile);
        logJarFileList();
    }

    /**
     * Helper method that logs all currently existing files under the given directory
     */
    private void logJarFileList() {
        StringBuffer sb = new StringBuffer("Files present: ");
        for (FileObject f : jarFiles) {
            sb.append(f.toString() + ", ");
        }
        this.logger.info(sb.toString());
    }

    /**
     * This method ensures that the given FileObject is a JarFile
     * 
     * @param file
     *            FileObject to be checked.
     * @return true if the file has the "jar" ending.
     */
    private boolean isJarFile(FileObject file) {
        FileName name = file.getName();
        if (name.getExtension().equals("jar"))
            return true;
        return false;
    }

    /**
     * Cancelles the execution of this thread per interrupt().
     * 
     * <see>Java Concurrency in practice, p. 137</see>
     */
    public void cancel() {
        this.interrupt();
    }

    /**
     * Run method for the thread implementation. Starts the FileMonitor and sleeps periodically until interrupted. If
     * interrupted, calls shutdown()
     */
    @Override
    public void run() {
        this.logger.info("Watching for *.jar files under directory: " + this.basePath);
        logJarFileList();

        fm.start();
        try {
            while (!this.isInterrupted()) {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
        } finally {
            this.shutdown();
        }
    }

    /**
     * Shuts down all necessary services for this class
     */
    private void shutdown() {
        logger.info("Shutting down " + this.getClass().getName() + "...");
        this.fm.stop();
        logger.info("... finished shutting down " + this.getClass().getName() + ".");
    }

    /**
     * Returns the constructor given path.
     * 
     * @return the given path to listen on.
     */
    public String getBasePath() {
        return basePath;
    }
}
