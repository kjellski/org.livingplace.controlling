package org.livingplace.actions.registry.api.internal;

import org.apache.felix.scr.annotations.*;
import org.livingplace.actions.api.*;
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
  private final Map<String, IActor> actors = new ConcurrentHashMap<String, IActor>();
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
      action.setActionProperties(properties);
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
  public void registerActor(IActor actor) {
    IActor old = actors.put(actor.getActorQualifer().getFullQualifier(), actor);
    if (old != null) {
      log.log(LogService.LOG_INFO, "Replaced IActor: " + old.toString() + " with new IActor: " + actor.toString());
    }
    log.log(LogService.LOG_INFO, "Added IActor to ActionRegistryImpl: " + actor.toString());
  }

  @Override
  public void unregisterActor(IActor actor) {
    this.actors.remove(actor.getActorQualifer().getFullQualifier());
    log.log(LogService.LOG_INFO, "Removed IActor from ActionRegistryImpl: " + actor.toString());
  }

  @Override
  public void executeActionOnActor(IActorQualifier actorQualifier, IActionQualifier actionQualifier, IActionProperties actionProperties) {
    IActor actorToExecuteAction = actors.get(actorQualifier.getFullQualifier());
    if (actorToExecuteAction == null) {
      // we're completely unable to perform the action
      log.log(LogService.LOG_WARNING, "There was no IActor that could handle the execution of the requested IAction: " + actionQualifier.getFullQualifier());
      IAction possibleAction = actions.get(actionQualifier.getFullQualifier());
      if (possibleAction == null) {
        log.log(LogService.LOG_WARNING, "The requested IAction was not even registered: " + actionQualifier.getFullQualifier());
      } else {
        log.log(LogService.LOG_DEBUG, "But the requested IAction is in the registry: " + actionQualifier.getFullQualifier());
      }
      return;
    }
    log.log(LogService.LOG_DEBUG, "Found IActor: " + actorToExecuteAction.getActorQualifer().getFullQualifier());

    IAction actionToBeExecuted = null;
    for (IAction tmp : actorToExecuteAction.getAllActions()) {
      if (tmp.getActionQualifier().getFullQualifier() == actionQualifier.getFullQualifier()) {
        if (actionToBeExecuted == null) {
          log.log(LogService.LOG_DEBUG, "Found IAction: " + actionQualifier.getFullQualifier());
          actionToBeExecuted = tmp;
        } else {
          log.log(LogService.LOG_WARNING, "Found more then one IAction(" + actionQualifier.getFullQualifier()
                  + ")on IActor: " + actorToExecuteAction.getActorQualifer().getFullQualifier() + " WARNING: This one will be used for execution of Action.");
          actionToBeExecuted = tmp;
        }
      }
    }

    if (actionToBeExecuted == null) {
      log.log(LogService.LOG_WARNING, "The requested IAction was not found on the requested IActor: " + actorToExecuteAction.getActorQualifer().getFullQualifier());
      return;
    }

    String logEntry = "Executing IAction: " + actionQualifier.getFullQualifier()
            + "        on IActor: " + actorToExecuteAction.getActorQualifer().getFullQualifier();
    if (actionProperties != null) {
      logEntry += "  with Properties: null";
    } else {
      logEntry += "  with Properties: " + actionProperties.toString();
    }
    log.log(LogService.LOG_DEBUG, logEntry);

    // actually execute the action in the executor thread pool.
    actionToBeExecuted.setActionProperties(actionProperties);
    executor.execute(actionToBeExecuted);
  }

  @Override
  public void executeActionOnActor(IActorQualifier actorQualifier, IActionQualifier actionQualifier) {
    executeActionOnActor(actorQualifier, actionQualifier, null);
  }

  @Override
  public List<IActor> getAllRegisteredActors() {
    return new ArrayList<IActor>(this.actors.values());
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
