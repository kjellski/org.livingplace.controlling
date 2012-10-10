package org.livingplace.actions.registry.commands.internal;

import org.apache.felix.service.command.Descriptor;
import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionQualifier;
import org.livingplace.actions.api.providers.ActionQualifier;
import org.livingplace.actions.registry.api.IActionRegistry;
import org.osgi.service.log.LogService;

import java.util.List;

public class ActionRegistryCommands {
  private IActionRegistry registry;
  private LogService log;

  /**
   * Functions available to the gogo shell from this registry
   */
  public static String[] commands = {"show", "execute"};


  public ActionRegistryCommands(IActionRegistry registry, LogService log){
    this.registry = registry;
    this.log = log;

    for (String command : commands) {
      log.log(LogService.LOG_INFO, command + "() added.");
    }
  }

  @Descriptor("executes an action qualified by the parameters")
  public void execute(
          @Descriptor("action qualifiers namespace") String namespace,
          @Descriptor("action qualifiers name") String name,
          @Descriptor("action qualifiers version") String version){
    IActionQualifier qualifier = new ActionQualifier(namespace, name, version);
    StringBuilder b = new StringBuilder("Searching IAction with qualifier: " + qualifier.getFullQualifier());

    registry.executeAction(qualifier);

    log.log(LogService.LOG_INFO, b.toString());
    System.out.println(b.toString());
  }

  @Descriptor("shows all registered actions")
  public void show(){
    StringBuilder b = new StringBuilder("Looking up registered actions in Registry...");
    b.append("...found " + registry.getAllRegisteredActions().size() + " IActions.\n");

    List<IAction> actions = registry.getAllRegisteredActions();
    if (actions.size() > 0) {
      for (IAction action : actions) {
        b.append(action.toString() + "\n");
      }
    } else {
      b.append("No Actions Registered.\n");
    }

    log.log(LogService.LOG_INFO, b.toString());
    System.out.println(b.toString());
  }
}
