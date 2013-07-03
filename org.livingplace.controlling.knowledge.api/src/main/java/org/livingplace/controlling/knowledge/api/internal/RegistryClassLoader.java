package org.livingplace.controlling.knowledge.api.internal;

import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;

import java.util.List;

public class RegistryClassLoader extends ClassLoader {

    private Logger logger = Logger.getLogger(RegistryClassLoader.class);
    private IActionRegistry actionRegistry;
    private IInformationRegistry informationRegistry;

    public RegistryClassLoader(ClassLoader parent, IActionRegistry actionRegistry, IInformationRegistry informationRegistry) {
        super(parent);
        this.actionRegistry = actionRegistry;
        this.informationRegistry = informationRegistry;
        logger.info("This classloader is used to search in the Information and Action Registry. To find classes in there, "
                + "your class has to be in any child package of \"org.livingplave.controlling.*\".");
    }

    public synchronized Class loadClass(String name) throws java.lang.ClassNotFoundException {
        // prevent this from failing when no defaultpackage was defined, snip it!
        if (name.contains("defaultpkg.")) {
            debuggingClassloadingPrintouts("Removing \"defaultpkg.\" from FQN: " + name);
            name = name.replace("defaultpkg.", "");
        }

        // this class should not be loaded from registries.
        if (!name.contains("org.livingplace.controlling")) {
            debuggingClassloadingPrintouts("Searching \"" + name + "\" in parent.");
            return super.loadClass(name);
        }

        // search through the registries
        if (name.contains("org.livingplace.controlling.actions")) {
            List<IAction> actions = actionRegistry.getAllRegistered();
            for (IAction action : actions) {
                if (action.getClass().getName().equals(name)) {
                    debuggingClassloadingPrintouts("---> !FOUND IT! <---");
                    debuggingClassloadingPrintouts("action.getClass().getName(): " + action.getClass().getName());
                    return action.getClass();
                }
            }
        } else if (name.contains("org.livingplace.controlling.informations")) {
            List<IInformation> informations = informationRegistry.getAllRegistered();
            for (IInformation information : informations) {
                if (information.getClass().getName().equals(name)) {
                    debuggingClassloadingPrintouts("---> !FOUND IT! <---");
                    debuggingClassloadingPrintouts("information.getClass().getName(): " + information.getClass().getName());
                    return information.getClass();
                }
            }
        }
        throw new ClassNotFoundException("Class " + name + " couldn't be found in registries.");
    }

    private void debuggingClassloadingPrintouts(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }
}
