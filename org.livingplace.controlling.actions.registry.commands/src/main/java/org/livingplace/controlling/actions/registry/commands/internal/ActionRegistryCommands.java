package org.livingplace.controlling.actions.registry.commands.internal;

import org.apache.felix.service.command.Descriptor;
import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;
import org.livingplace.controlling.actions.api.providers.ActionQualifierParser;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;

import java.util.List;

public class ActionRegistryCommands {
  private IActionRegistry actionRegistry;
  private static Logger logger = Logger.getLogger(ActionRegistryCommands.class);
  
  /**
   * Functions available to the gogo shell from this registry
   */
  public static String[] commands = {"show", "execute"};

  public ActionRegistryCommands(IActionRegistryFactory actionRegistryFactory) {
    this.actionRegistry = actionRegistryFactory.getInstance();

    for (String command : commands) {
      logger.info(command + "() added.");
    }
  }

  @Descriptor("executes an action qualified by the parameters")
  public void execute(
          @Descriptor("full qualified action in the form: \"<namespace>.<name>:<version>\"")
          String fullQualifier) {

    IActionQualifier qualifier = new ActionQualifier(ActionQualifierParser.parseActionQualifier(fullQualifier));
    StringBuilder b = new StringBuilder("Executing IAction with qualifier: " + qualifier.getFullQualifier());

    actionRegistry.executeAction(qualifier);

    logger.info(b.toString());
    System.out.println(b.toString());
  }

  @Descriptor("shows all registered actions")
  public void show() {
    StringBuilder b = new StringBuilder("Looking up registered actions in Registry... ");
    b.append(" found " + actionRegistry.getAllRegistered().size() + " IActions.\n");

    List<IAction> actions = actionRegistry.getAllRegistered();
    if (actions.size() > 0) {
      for (IAction action : actions) {
        b.append(action.toString() + "\n");
      }
    } else {
      b.append("No Actions Registered.\n");
    }

    logger.info(b.toString());
    System.out.println(b.toString());
  }
}
