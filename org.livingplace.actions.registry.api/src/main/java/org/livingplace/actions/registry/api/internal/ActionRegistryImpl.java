package org.livingplace.actions.registry.api.internal;

import org.apache.felix.scr.annotations.*;
import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionProperties;
import org.livingplace.actions.api.IActionQualifier;
import org.livingplace.actions.registry.api.IActionRegistry;
import org.osgi.service.log.LogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component(name = "IActionRegistry")
@Service
public class ActionRegistryImpl implements IActionRegistry {
  private static final int THREADPOOL_SIZE = 100;
  private final Map<String, IAction> actions = new ConcurrentHashMap<String, IAction>();
  private static final Executor executor = Executors.newFixedThreadPool(THREADPOOL_SIZE);

  @Activate
  void start() {
    log.log(LogService.LOG_INFO, ActionRegistryImpl.class.getName() + " started.");
  }

  @Reference
  private LogService log;

  public void setLog(LogService log) {
    this.log = log;
    log.log(LogService.LOG_WARNING, ActionRegistryImpl.class.getName() + ".log overidden.");
  }


  @Deactivate
  void stop() {
    log.log(LogService.LOG_INFO, ActionRegistryImpl.class.getName() + " stopped.");
  }


  @Override
  public void executeAction(IActionQualifier qualifier, IActionProperties properties) {
    IAction action = this.actions.get(qualifier.toString());
    if (action != null) {
      executor.execute(action);
    } else {
      log.log(LogService.LOG_WARNING, "Action not found: " + qualifier);
    }
  }

  @Override
  public void registerActions(IAction action) {
    IAction old = actions.put(action.getActionQualifier().getFullQualifier(), action);
    if (old != null) {
      log.log(LogService.LOG_INFO, "Replaced IAction: " + old.toString() + " with new IAction: " + action.toString());
    }
    log.log(LogService.LOG_INFO, "Added IAction to ActionRegistryImpl: " + action.toString());
  }

  @Override
  public void unregisterActions(IAction action) {
    this.actions.remove(action.getActionQualifier().getFullQualifier());
  }

  @Override
  public List<IAction> getAllRegisteredActions() {
    return new ArrayList<IAction>(this.actions.values());
  }
}
