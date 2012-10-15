package org.livingplace.controlling.actions.registry.commands.internal;

import org.apache.felix.service.command.Descriptor;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;
import org.livingplace.controlling.actions.api.providers.ActionQualifierParser;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.osgi.service.log.LogService;

import java.util.List;

public class ActionRegistryCommands {
  private IActionRegistry registry;
  private LogService log;

  /**
   * Functions available to the gogo shell from this registry
   */
  public static String[] commands = {"show", "execute"};

  public ActionRegistryCommands(IActionRegistry registry, LogService log) {
    this.registry = registry;
    this.log = log;

    for (String command : commands) {
      log.log(LogService.LOG_INFO, command + "() added.");
    }
  }

  @Descriptor("executes an action qualified by the parameters")
  public void execute(
          @Descriptor("full qualified action in the form: \"<namespace>.<name>:<version>\"")
          String fullQualifier) {

    IActionQualifier qualifier = new ActionQualifier(ActionQualifierParser.parseActionQualifier(fullQualifier));
    StringBuilder b = new StringBuilder("Executing IAction with qualifier: " + qualifier.getFullQualifier());

    registry.executeAction(qualifier);

    log.log(LogService.LOG_INFO, b.toString());
    System.out.println(b.toString());
  }

  @Descriptor("shows all registered actions")
  public void show() {
    StringBuilder b = new StringBuilder("Looking up registered actions in Registry... ");
    b.append(" found " + registry.getAllRegistered().size() + " IActions.\n");

    List<IAction> actions = registry.getAllRegistered();
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
