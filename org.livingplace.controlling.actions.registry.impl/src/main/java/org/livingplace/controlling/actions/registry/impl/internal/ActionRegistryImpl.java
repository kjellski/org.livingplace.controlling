package org.livingplace.controlling.actions.registry.impl.internal;

import org.apache.felix.scr.annotations.*;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionProperties;
import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.Registry;
import org.osgi.service.log.LogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@Service
public class ActionRegistryImpl implements IActionRegistry {
  private static final int THREADPOOL_SIZE = 100;
  private static final Executor executor = Executors.newFixedThreadPool(THREADPOOL_SIZE);
  protected final Map<String, IAction> registry = new ConcurrentHashMap<String, IAction>();

  @Reference
  protected LogService log;

  public void setLog(LogService log) {
    this.log = log;
    log.log(LogService.LOG_WARNING, Registry.class.getName() + ".log overidden.");
  }

  @Activate
  void start() {
    log.log(LogService.LOG_INFO, ActionRegistryImpl.class.getName() + " started.");
  }

  @Deactivate
  void stop() {
    log.log(LogService.LOG_INFO, ActionRegistryImpl.class.getName() + " stopped.");
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
      log.log(LogService.LOG_INFO, "Replaced IAction: " + old.toString() + " with new IAction: " + action.toString());
    }
    log.log(LogService.LOG_INFO, "Added IAction to ActionRegistryImpl: " + action.getQualifier().getFullQualifier());
  }

  @Override
  public void unregister(IAction action) {
    this.registry.remove(action.getQualifier().getFullQualifier());
  }

  @Override
  public List<IAction> getAllRegistered() {
    return new ArrayList<IAction>(this.registry.values());
  }

  @Override
  public IAction get(IQualifier actionQualifier) {
    return registry.get(actionQualifier.getFullQualifier());
  }

}