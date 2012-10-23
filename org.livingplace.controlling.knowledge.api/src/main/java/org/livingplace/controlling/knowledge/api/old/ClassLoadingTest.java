package org.livingplace.controlling.knowledge.api.old;

import org.clapper.util.classutil.*;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.rule.Function;
import org.drools.rule.Rule;
import org.drools.runtime.StatefulKnowledgeSession;
import org.livingplace.controlling.knowledge.api.old.interfaces.FactReceiver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ClassLoadingTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            // testClassFinder();

            // Resource urlResource = ResourceFactory.newClassPathResource(path);
            String pathFireSystem = "rules/FireSystem/FireSystem";
            URL ruleUrlFireSystem = new URL("file:" + pathFireSystem + ".drl");
            URL jarUrlFireSystem = new URL("file:" + pathFireSystem + ".jar");

            String pathSampleProject = "plugins/Test002/Sample";
            URL ruleUrlSampleProject = new URL("file:" + pathSampleProject + ".drl");
            URL jarUrlSampleProject = new URL("file:" + pathSampleProject + ".jar");

            File ruleFileFireSystem = new File(ruleUrlFireSystem.getPath());
            File jarFileFireSystem = new File(jarUrlFireSystem.getPath());

            File ruleFileSampleProject = new File(ruleUrlSampleProject.getPath());
            File jarFileSampleProject = new File(jarUrlSampleProject.getPath());

            System.out.println(jarUrlFireSystem.toURI());
            System.out.println(jarUrlSampleProject.toURI());

            ClassLoaderBuilder clb = new ClassLoaderBuilder();
            clb.add(jarFileFireSystem);
            clb.add(jarFileSampleProject);
            ClassLoader classLoader = clb.createClassLoader();

            //findClassesThatImplementInterface(jarFileFireSystem);
            //findClassesThatImplementInterface(jarFileSampleProject);
            
            // Resource resourceFireSystem = ResourceFactory.newUrlResource(ruleUrlFireSystem);
            // Resource resourceSampleProject = ResourceFactory.newUrlResource(ruleUrlSampleProject);

            // inspectRuleAndJar(classLoader, resourceFireSystem);
            // inspectRuleAndJar(classLoader, resourceSampleProject);

            KnowledgeBuilderConfiguration kbconf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(null,
                    classLoader);
            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder(kbconf);

            kb.add(ResourceFactory.newUrlResource(ruleUrlFireSystem), ResourceType.DRL);
            kb.add(ResourceFactory.newUrlResource(ruleUrlSampleProject), ResourceType.DRL);

            if (kb.hasErrors())
            {
                System.err.println(kb.getErrors().toString());
                System.exit(-1);
            }

            KnowledgeBaseConfiguration kbaseConf =
                    KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, classLoader);

            KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConf);
            kbase.addKnowledgePackages(kb.getKnowledgePackages());

            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

            startBlindInjects(jarFileFireSystem, classLoader, ksession);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void inspectRuleAndJar(ClassLoader classLoader, Resource resource) throws DroolsParserException,
            IOException {
        PackageBuilderConfiguration pbc = new PackageBuilderConfiguration(classLoader);
        pbc.setClassLoaderCacheEnabled(false);

        PackageBuilder builder = new PackageBuilder(pbc);

        builder.addPackageFromDrl(resource);

        if (builder.hasErrors()) {
            for (KnowledgeBuilderError e : builder.getErrors()) {
                System.err.println(e);
                System.err.println("Exiting!");
                System.exit(-1);
            }
        }

        for (org.drools.rule.Package pkg : builder.getPackages()) {
            System.out.println(pkg.toString());

            System.out.println("Globals: ");
            for (Entry<String, String> global : pkg.getGlobals().entrySet()) {
                System.out.println(global.getKey() + ":" + global.getValue());
            }

            System.out.println("Functions: ");
            for (Entry<String, Function> fkt : pkg.getFunctions().entrySet()) {
                System.out.println("\t" + fkt.getKey() + "" + fkt.getValue().getName());
            }

            System.out.println("FactTypes: ");
            for (Entry<String, org.drools.definition.type.FactType> ft : pkg.getFactTypes().entrySet()) {
                System.out.println("FactType: " + ft.getKey());
                for (org.drools.definition.type.FactField ff : ft.getValue().getFields()) {
                    System.out.println("\tFactField: " + ff.getName());
                }
            }

            System.out.println("Rules: ");
            for (Rule rule : pkg.getRules()) {
                System.out.println(rule.getNamespace() + ":" + rule.getName() + "");
                Map<String, Object> m = rule.getMetaData();

                for (Entry<String, Object> entry : m.entrySet()) {
                    System.out.println("\t" + entry.getKey() + ":" + entry.getValue());
                }
            }
        }
    }

    private static void findClassesThatImplementInterface(File jarFile)
    {
        ClassFinder cf = new ClassFinder();
        cf.add(jarFile);
//        ClassFilter classFilter = new RegexClassFilter(interfaceName);

        ClassFilter classFilter = new SubclassClassFilter(FactReceiver.class);

        List<ClassInfo> cis = new ArrayList<ClassInfo>();
        int count = cf.findClasses(cis,classFilter);
        System.out.println("Found " + count + " classes.");

        for (ClassInfo classInfo : cis) {
            System.err.println("Found implementation(" + "FactReceiver.class" + "):" + classInfo.getClassName());

            for (String interf : classInfo.getInterfaces()) {
                System.out.println(interf);
            }
        }
    }

    private static void startBlindInjects(File jarFile, ClassLoader classLoader, StatefulKnowledgeSession ksession)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        String[] classFQNs = { "com.sample.firesystem.Room",
                "com.sample.firesystem.Fire",
                "com.sample.firesystem.Alarm",
                "com.sample.firesystem.Sprinkler" };

        ClassFinder cf = new ClassFinder();
        cf.add(jarFile);
        for (String string : classFQNs) {
            ClassFilter classFilter = new RegexClassFilter(string);
            List<ClassInfo> oneClass = new ArrayList<ClassInfo>();
            int count = cf.findClasses(oneClass, classFilter);
            if (count != 1) {
                for (ClassInfo classInfo : oneClass) {
                    System.err.println("Found for name(" + string + "):" + classInfo.getClassName());
                }
            }

            ClassInfo roomClassInfo = oneClass.get(0);
            System.out.println(roomClassInfo.getClassName());
        }

        Class[] classes = { Class.forName(classFQNs[0], true, classLoader),
                Class.forName(classFQNs[1], true, classLoader),
                Class.forName(classFQNs[2], true, classLoader),
                Class.forName(classFQNs[3], true, classLoader)
        };

        String[] names = new String[] { "kitchen", "bedroom", "office", "livingroom" };
        // Map<String, Room> name2room = new HashMap<String, Room>();
        Map<String, Object> name2room = new HashMap<String, Object>();
        for (String name : names) {
            // Room room = new Room(name);
            Constructor roomCtor = classes[0].getConstructor(String.class);
            Object room = roomCtor.newInstance(name);

            name2room.put(name, room);
            ksession.insert(room);

            // Sprinkler sprinkler = new Sprinkler(room);
            Constructor sprinklerCtor = classes[3].getConstructor(classes[0]);
            Object sprinkler = sprinklerCtor.newInstance(room);

            ksession.insert(sprinkler);
        }

        ksession.fireAllRules();

        Constructor fireCtor = classes[1].getConstructor(classes[0]);
        // Fire kitchenFire = new Fire(name2room.get("kitchen"));
        Object kitchenFire = fireCtor.newInstance(name2room.get("kitchen"));
        // Fire officeFire = new Fire(name2room.get("office"));
        Object officeFire = fireCtor.newInstance(name2room.get("office"));

        org.drools.runtime.rule.FactHandle kitchenFireHandle = ksession.insert(kitchenFire);
        org.drools.runtime.rule.FactHandle officeFireHandle = ksession.insert(officeFire);
        ksession.fireAllRules();

        ksession.retract(kitchenFireHandle);
        ksession.retract(officeFireHandle);
        ksession.fireAllRules();
    }

    private static void testClassFinder() {
        ClassFinder classFinder = new ClassFinder();
        classFinder.add(new File("/Users/kjellski/lib/java/gson-2.2.1.jar"));

        List<ClassInfo> classes = new ArrayList<ClassInfo>();
        ClassFilter classFilter = new RegexClassFilter("com.google.gson.GsonBuilder");

        int classCount = classFinder.findClasses(classes);

        System.out.println("Found " + classCount + " classes:");
        for (ClassInfo classInfo : classes) {
            System.out.println(classInfo.getClassName() + ":");
            for (MethodInfo methodInfo : classInfo.getMethods()) {
                System.out.println("\t" + methodInfo.getSignature());
            }
        }

        List<ClassInfo> oneClass = new ArrayList<ClassInfo>();
        int count = classFinder.findClasses(oneClass, classFilter);
        System.out.println("Found by filter (" + classFilter.toString() + "): ");
        for (int i = 0; i < count; i++) {
            System.out.println("(" + i + "): " + oneClass.get(i));
        }
    }
}
