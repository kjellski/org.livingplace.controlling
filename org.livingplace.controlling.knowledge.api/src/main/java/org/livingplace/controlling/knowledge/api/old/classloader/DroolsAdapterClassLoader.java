package org.livingplace.controlling.knowledge.api.old.classloader;

public class DroolsAdapterClassLoader extends MultiClassLoader {

    private JarResources jarResources;

    public DroolsAdapterClassLoader(String jarName)
    {
        // Create the JarResource and suck in the jar file.
        jarResources = new JarResources(jarName);
    }

    @Override
    protected byte[] loadClassBytes(String className) {
        // Support the MultiClassLoader's class name munging facility.
        className = formatClassName(className);
        // Attempt to get the class data from the JarResource.
        return (jarResources.getResource(className));
    }

}
