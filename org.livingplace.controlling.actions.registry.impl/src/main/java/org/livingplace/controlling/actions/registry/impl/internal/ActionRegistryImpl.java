package org.livingplace.controlling.actions.registry.impl.internal;

import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionProperties;
import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.api.providers.ActionQualifierParser;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.api.IQualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 */
public class ActionRegistryImpl implements IActionRegistry {
  private static final int THREADPOOL_SIZE = 100;
  private static final Executor executor = Executors.newFixedThreadPool(THREADPOOL_SIZE);
  private static Logger logger = Logger.getLogger(ActionRegistryImpl.class);
  protected final Map<String, IAction> registry = new ConcurrentHashMap<String, IAction>();

  public ActionRegistryImpl() {
  }

  @Override
  public void executeAction(IActionQualifier qualifier, IActionProperties properties) {
    IAction action = get(qualifier);
    if (action != null) {
      action.setActionProperties(properties);
      exec(action);
    } else {
      logger.warn("Action not found: " + qualifier);
    }
  }

  @Override
  public void executeAction(IActionQualifier actionQualifier) {
    executeAction(actionQualifier, null);
  }

  @Override
  public void executeAction(IAction action) {
    if (this.registry.containsKey(action.getQualifier().getFullQualifier())) {
      exec(action);
    } else {
      logger.debug("Not executing action that was not in the registry. Have you stopped the Actor?");
    }
  }

  private void exec(IAction action){
    logger.debug(buildPrintoutFor(action.getQualifier(), action.getActionProperties(), null));
    executor.execute(action);
  }

  @Override
  public void executeAction(String fullQualifier) {
    IAction action = get(ActionQualifierParser.parseActionQualifier(fullQualifier));
    executeAction(action);
  }

  @Override
  public void executeAction(String fullQualifier, IActionProperties actionProperties) {
    executeAction(ActionQualifierParser.parseActionQualifier(fullQualifier), actionProperties);
  }

  private String buildPrintoutFor(IActionQualifier actionQualifier, IActionProperties actionProperties, IActor actorToExecuteAction) {

    StringBuilder b = new StringBuilder();
    b.append("Executing IAction: ");
    if (actionQualifier != null)
      b.append(actionQualifier.getFullQualifier());
    else
      b.append("null");

    b.append(" on IActor: ");
    if (actorToExecuteAction != null && actorToExecuteAction.getQualifier() != null)
      b.append(actorToExecuteAction.getQualifier().getFullQualifier());
    else
      b.append("null");

    b.append("  with Properties: ");
    if (actionProperties != null) {
      b.append(actionProperties.toString());
    } else {
      b.append("null");
    }

    return b.toString();
  }

  @Override
  public void register(IAction action) {
    IAction old = registry.put(action.getQualifier().getFullQualifier(), action);
    if (old != null) {
      logger.warn("Replaced " + old.getQualifier().getFullQualifier() +
              " with new " + action.getQualifier().getFullQualifier() + " in ActionRegistry.");
    }
    logger.debug("Added " + action.getQualifier().getFullQualifier() + " to ActionRegistry.");
  }

  @Override
  public void unregister(IAction action) {
    logger.debug("Removing " + this.registry.remove(action.getQualifier().getFullQualifier())
            .getQualifier().getFullQualifier() + " ...");
    logger.debug("Removed  " + action.getQualifier().getFullQualifier() + " from ActionRegistry.");
  }

  @Override
  public List<IAction> getAllRegistered() {
    logger.debug("Returning all " + registry.size() + " registered actions from ActionRegistry.");
    return new ArrayList<IAction>(this.registry.values());
  }

  @Override
  public IAction get(IQualifier actionQualifier) {
    logger.debug("Found Action for Qualifier \"" + actionQualifier.getFullQualifier() + "\".");
    return registry.get(actionQualifier.getFullQualifier());
  }
}