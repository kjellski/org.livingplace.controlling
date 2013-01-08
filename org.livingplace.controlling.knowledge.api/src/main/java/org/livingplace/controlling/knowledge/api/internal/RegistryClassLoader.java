package org.livingplace.controlling.knowledge.api.internal;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;

import java.util.List;

public class RegistryClassLoader extends ClassLoader {

  private IActionRegistry actionRegistry;
  private IInformationRegistry informationRegistry;

  public RegistryClassLoader(ClassLoader parent, IActionRegistry actionRegistry, IInformationRegistry informationRegistry) {
    super(parent);
    this.actionRegistry = actionRegistry;
    this.informationRegistry = informationRegistry;
  }

  public synchronized Class loadClass(String name) throws java.lang.ClassNotFoundException {
    // prevent this from failing when no defaultpackage was defined, snip it!
    if (name.contains("defaultpkg.")) {
      name = name.replace("defaultpkg.", "");
    }

    // this class should not be loaded from registries.
    if (!name.contains("org.livingplace.controlling")) {
      return super.loadClass(name);
    }

    // search through the registries
    if (name.contains("org.livingplace.controlling.actions")) {
      List<IAction> actions = actionRegistry.getAllRegistered();
      for (IAction action : actions) {
        if (action.getClass().getName().equals(name)) {
          System.out.println("---> !FOUND IT! <---");
          System.out.println("action.getClass().getName(): " + action.getClass().getName());
          return action.getClass();
        }
      }
    } else if (name.contains("org.livingplace.controlling.informations")) {
      List<IInformation> informations = informationRegistry.getAllRegistered();
      for (IInformation information : informations) {
        if (information.getClass().getName().equals(name)) {
          System.out.println("---> !FOUND IT! <---");
          System.out.println("information.getClass().getName(): " + information.getClass().getName());
          return information.getClass();
        }
      }
    }

    throw new ClassNotFoundException("Class " + name + " couldn't be found in registries.");
  }
}
