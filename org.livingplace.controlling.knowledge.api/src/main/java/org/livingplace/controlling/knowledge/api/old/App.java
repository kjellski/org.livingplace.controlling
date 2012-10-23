package org.livingplace.controlling.knowledge.api.old;

import org.apache.commons.vfs2.FileSystemException;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.rule.Global;
import org.drools.definition.rule.Rule;
import org.drools.definition.type.FactField;
import org.drools.definition.type.FactType;
import org.livingplace.controlling.knowledge.api.old.drools.DroolsManager;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This is the main class for starting the 
 * server that loads and unloads the rules and classes
 * to add functionality in the reactive rulebased AI 
 * system for the livingplace.org
 */
public class App
{
//    public static final String path = "/Users/kjellski/tmp/";
    public static final String path = "/Users/kjellski/Projects/eclipse-workspaces/java/org.livingplace.controlling/plugins";
    private static Logger logger = Logger.getLogger(App.class.getName());
    
    /**
     * 
     * @param args
     * @throws InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException
    {
        logger.info("Starting org.livingplace.controlling.App...");
        initializeServers();
        logger.info("... started org.livingplace.controlling.App.");
    }

    private static void initializeServers() throws FileSystemException, IOException, InterruptedException {
//        VFSJarManager jarManager = new VFSJarManager(path);
//        jarManager.start();

//        List<SimpleEntry<String, ResourceType>> resources = new ArrayList<AbstractMap.SimpleEntry<String, ResourceType>>();
//        resources.add(new SimpleEntry<String, ResourceType>("rules/_Sample.drl", ResourceType.DRL));

        DroolsManager droolsManager = new DroolsManager();
        droolsManager.start();
        
        Thread.sleep(3000); 
        
        for (KnowledgePackage pkg : droolsManager.getKnowledgePackages()) {
            System.out.println(pkg.toString());
            
            for (Global global : pkg.getGlobalVariables()) {
                System.out.println(global.getName() + ":" + global.getType());
            }
            
            for (String fkt : pkg.getFunctionNames()) {
                System.out.println(fkt);
            }
            
            for (FactType ft : pkg.getFactTypes()) {
                System.out.println("FactType: " + ft.getName());
                for (FactField ff : ft.getFields()) {
                    System.out.println("\tFactField: " + ff.getName());
                }
            }
            
            for (Rule rule : pkg.getRules()) {
                System.out.println(rule.getNamespace() + ":" + rule.getName() + "");
                Map<String, Object> m = rule.getMetaData();
                
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    System.out.println("\t" + entry.getKey() + ":" + entry.getValue());
                }
            }
        }
//        
//        System.out.println("Press <Enter> to exit...");
//        System.in.read();
//        jarManager.cancel();
        droolsManager.cancel();
    }
}
