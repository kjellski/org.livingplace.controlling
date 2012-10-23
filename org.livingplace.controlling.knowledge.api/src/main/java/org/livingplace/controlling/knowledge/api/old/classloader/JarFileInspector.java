package org.livingplace.controlling.knowledge.api.old.classloader;

import org.apache.commons.vfs2.FileObject;
import org.clapper.util.classutil.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JarFileInspector {

    public JarFileInspector(FileObject fo) {
        try {
            File file = new File(fo.getURL().toURI());
            ClassFinder classFinder = new ClassFinder();
            classFinder.add(file);

            List<ClassInfo> classes = new ArrayList<ClassInfo>();
            ClassFilter interfaceFilter = new InterfaceOnlyClassFilter();
            ClassFilter regexFilter = new RegexClassFilter("");

            int classCount = classFinder.findClasses(classes, interfaceFilter);

            System.out.println("Found " + classCount + " classes:");
            for (ClassInfo classInfo : classes) {
                System.out.println(classInfo.getClassName() + ":");
                for (MethodInfo methodInfo : classInfo.getMethods()) {
                    System.out.println("\t" + methodInfo.getSignature());
                }
            }
            
            JarResources jar = new JarResources(file.getAbsolutePath());
            System.out.println(jar.ResourcesToString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
