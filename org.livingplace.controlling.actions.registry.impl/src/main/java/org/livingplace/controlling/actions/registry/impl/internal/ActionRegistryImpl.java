package org.livingplace.controlling.actions.registry.impl.internal;

import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionProperties;
import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.actions.api.IActor;
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
    IAction action = (IAction) get((IQualifier) qualifier);
    if (action != null) {
      action.setActionProperties(properties);
      logger.info(buildPrintoutFor(qualifier, properties, null));
      executor.execute(action);
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
    logger.info(buildPrintoutFor(action.getQualifier(), action.getActionProperties(), null));
    executor.execute(action);
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
      logger.info("Replaced " + old.getQualifier().getFullQualifier() +
              " with new " + action.getQualifier().getFullQualifier() + " in ActionRegistry.");
    }
    logger.info("Added " + action.getQualifier().getFullQualifier() + " to ActionRegistry.");
  }

  @Override
  public void unregister(IAction action) {
    this.registry.remove(action.getQualifier().getFullQualifier());
    logger.info("Removed " + action.getQualifier().getFullQualifier() + " from ActionRegistry.");
  }

  @Override
  public List<IAction> getAllRegistered() {
    logger.info("Returning " + registry.size() + " Entries from ActionRegistry.");
    return new ArrayList<IAction>(this.registry.values());
  }

  @Override
  public IAction get(IQualifier actionQualifier) {
    logger.info("Returning " + actionQualifier.getFullQualifier() + " from ActionRegistry.");
    return registry.get(actionQualifier.getFullQualifier());
  }
}