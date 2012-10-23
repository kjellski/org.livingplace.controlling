package org.livingplace.controlling.knowledge.api.old.classloader.listening;

import org.apache.commons.vfs2.FileObject;

import java.awt.event.ActionListener;

/**
 * Used to interface the classes that will dynamically load, unload and reload the JarFileObjects 
 * @author kjellski
 *
 */
public interface JarFileActionListener extends ActionListener{
    
    /**
     * Loads the given JarFileOject 
     * @param fo JarFileObject to be laoded
     */
    public void load(FileObject fo);
    
    /**
     * Removes the given JarFileOject
     * @param fo JarFileObject to be removed
     */
    public void unload(FileObject fo);
    
    /**
     * Reloads the given JarFileOject
     * @param fo JarFileObject to be reloaded
     */
    public void reload(FileObject fo);
}
