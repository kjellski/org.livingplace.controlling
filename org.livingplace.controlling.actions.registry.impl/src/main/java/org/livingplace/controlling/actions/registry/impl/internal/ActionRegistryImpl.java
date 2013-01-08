package org.livingplace.controlling.actions.registry.impl.internal;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionProperties;
import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.api.IQualifier;
import org.osgi.service.log.LogService;

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
  protected final Map<String, IAction> registry = new ConcurrentHashMap<String, IAction>();

//  @Reference
  protected LogService log;

  public ActionRegistryImpl(LogService log) {
    this.log = log;
  }

  @Override
  public void executeAction(IActionQualifier qualifier, IActionProperties properties) {
    IAction action = (IAction) get((IQualifier) qualifier);
    if (action != null) {
      action.setActionProperties(properties);
      log.log(LogService.LOG_INFO, buildPrintoutFor(qualifier, properties, null));
      executor.execute(action);
    } else {
      log.log(LogService.LOG_WARNING, "Action not found: " + qualifier);
    }
  }

  @Override
  public void executeAction(IActionQualifier actionQualifier) {
    executeAction(actionQualifier, null);
  }

  @Override
  public void executeAction(IAction action) {
    log.log(LogService.LOG_INFO, buildPrintoutFor(action.getQualifier(), action.getActionProperties(), null));
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
      log.log(LogService.LOG_INFO, "Replaced " + old.getQualifier().getFullQualifier() +
              " with new " + action.getQualifier().getFullQualifier() + " in ActionRegistry.");
    }
    log.log(LogService.LOG_INFO, "Added " + action.getQualifier().getFullQualifier() + " to ActionRegistry.");
  }

  @Override
  public void unregister(IAction action) {
    this.registry.remove(action.getQualifier().getFullQualifier());
    log.log(LogService.LOG_INFO, "Removed " + action.getQualifier().getFullQualifier() + " from ActionRegistry.");
  }

  @Override
  public List<IAction> getAllRegistered() {
    log.log(LogService.LOG_INFO, "Returning " + registry.size() + " Entries from ActionRegistry.");
    return new ArrayList<IAction>(this.registry.values());
  }

  @Override
  public IAction get(IQualifier actionQualifier) {
    log.log(LogService.LOG_INFO, "Returning " + actionQualifier.getFullQualifier() + " from ActionRegistry.");
    return registry.get(actionQualifier.getFullQualifier());
  }
}