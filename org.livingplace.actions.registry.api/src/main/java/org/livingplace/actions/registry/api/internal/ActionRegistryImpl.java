package org.livingplace.actions.registry.api.internal;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionProperties;
import org.livingplace.actions.api.IActionQualifier;
import org.livingplace.actions.api.IActor;
import org.livingplace.actions.registry.api.IActionRegistry;
import org.osgi.service.log.LogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component(name = "IActionRegistry")
public class ActionRegistryImpl implements IActionRegistry {
  private static final int THREADPOOL_SIZE = 100;
  private final Map<String, IAction> actions = new ConcurrentHashMap<String, IAction>();
  private final Map<String, IActor> actors = new ConcurrentHashMap<String, IActor>();
  private static final Executor executor = Executors.newFixedThreadPool(THREADPOOL_SIZE);

  // NOTE: only needed if actors are in the mix... should be removed when they're definitely out of the game...
  private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
  private final Lock readLock = rwLock.readLock();
  private final Lock writeLock = rwLock.writeLock();

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
    try {
      readLock.lock();

      IAction action = this.actions.get(qualifier.toString());
      if (action != null) {
        action.setActionProperties(properties);
        log.log(LogService.LOG_INFO, buildPrintoutFor(qualifier, properties, null));
        executor.execute(action);
      } else {
        log.log(LogService.LOG_WARNING, "Action not found: " + qualifier);
      }
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public void executeAction(IActionQualifier actionQualifier) {
    executeAction(actionQualifier, null);
  }


  private String buildPrintoutFor(IActionQualifier actionQualifier, IActionProperties actionProperties, IActor actorToExecuteAction) {
    try {
      readLock.lock();

      StringBuilder b = new StringBuilder();
      b.append("Executing IAction: ");
      if (actionQualifier != null)
        b.append(actionQualifier.getFullQualifier());
      else
        b.append("null");

      b.append("        on IActor: ");
      if (actorToExecuteAction != null && actorToExecuteAction.getActorQualifer() != null)
        b.append(actorToExecuteAction.getActorQualifer().getFullQualifier());
      else
        b.append("null");

      b.append("  with Properties: ");
      if (actionProperties != null) {
        b.append(actionProperties.toString());
      } else {
        b.append("null");
      }

      return b.toString();
    } finally {
      readLock.unlock();
    }
  }


  @Override
  public void registerAction(IAction action) {
    try {
      writeLock.lock();

      IAction old = actions.put(action.getActionQualifier().getFullQualifier(), action);
      if (old != null) {
        log.log(LogService.LOG_INFO, "Replaced IAction: " + old.toString() + " with new IAction: " + action.toString());
      }
      log.log(LogService.LOG_INFO, "Added IAction to ActionRegistryImpl: " + action.getActionQualifier().getFullQualifier());
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public void unregisterAction(IAction action) {
    try {
      writeLock.lock();

      this.actions.remove(action.getActionQualifier().getFullQualifier());
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public List<IAction> getAllRegisteredActions() {
    try {
      readLock.lock();

      return new ArrayList<IAction>(this.actions.values());
    } finally {
      readLock.unlock();
    }
  }
}
//  @Override
//  public void registerActor(IActor actor) {
//
//    IActor old = actors.put(actor.getActorQualifer().getFullQualifier(), actor);
//    if (old != null) {
//      log.log(LogService.LOG_WARNING, "Replaced IActor: " + old.toString() + " with new IActor: " + actor.toString());
//    }
//    log.log(LogService.LOG_INFO, "Added IActor to ActionRegistryImpl: " + actor.toString());
//    for (IAction action : actor.getAllActions()) {
//      registerActions(action);
//    }
//  }
//
//  @Override
//  public void unregisterActor(IActor actor) {
//    this.actors.remove(actor.getActorQualifer().getFullQualifier());
//    log.log(LogService.LOG_INFO, "Removed IActor from ActionRegistryImpl: " + actor.toString());
//  }
//
//  @Override
//  public void executeActionOnActor(IActorQualifier actorQualifier, IActionQualifier actionQualifier, IActionProperties actionProperties) {
//    try {
//      readLock.lock();
//
//      IActor actorToExecuteAction = actors.get(actorQualifier.getFullQualifier());
//      if (actorToExecuteAction == null) {
//        // we're completely unable to perform the action
//        log.log(LogService.LOG_WARNING, "There was no IActor that could handle the execution of the requested IAction: " + actionQualifier.getFullQualifier());
//        IAction possibleAction = actions.get(actionQualifier.getFullQualifier());
//        if (possibleAction == null) {
//          log.log(LogService.LOG_WARNING, "The requested IAction was not even registered: " + actionQualifier.getFullQualifier());
//        } else {
//          log.log(LogService.LOG_INFO, "But the requested IAction is in the registry: " + actionQualifier.getFullQualifier());
//        }
//        return;
//      }
//      log.log(LogService.LOG_INFO, "Found IActor: " + actorToExecuteAction.getActorQualifer().getFullQualifier());
//
//      IAction actionToBeExecuted = null;
//      for (IAction tmp : actorToExecuteAction.getAllActions()) {
//        if (tmp.getActionQualifier().getFullQualifier() == actionQualifier.getFullQualifier()) {
//          if (actionToBeExecuted == null) {
//            log.log(LogService.LOG_INFO, "Found IAction: " + actionQualifier.getFullQualifier());
//            actionToBeExecuted = tmp;
//          } else {
//            log.log(LogService.LOG_WARNING, "Found more then one IAction(" + actionQualifier.getFullQualifier()
//                    + ")on IActor: " + actorToExecuteAction.getActorQualifer().getFullQualifier() + " WARNING: This one will be used for execution of Action.");
//            actionToBeExecuted = tmp;
//          }
//        }
//      }
//
//      if (actionToBeExecuted == null) {
//        log.log(LogService.LOG_WARNING, "The requested IAction was not found on the requested IActor: " + actorToExecuteAction.getActorQualifer().getFullQualifier());
//        return;
//      }
//
//      String logEntry = buildPrintoutFor(actionQualifier, actionProperties, actorToExecuteAction);
//      log.log(LogService.LOG_INFO, logEntry);
//
//      // actually execute the action in the executor thread pool.
//      actionToBeExecuted.setActionProperties(actionProperties);
//      executor.execute(actionToBeExecuted);
//    } finally {
//      readLock.unlock();
//    }
//  }
//  @Override
//  public void executeActionOnActor(IActorQualifier actorQualifier, IActionQualifier actionQualifier) {
//    try {
//      writeLock.lock();
//
//    executeActionOnActor(actorQualifier, actionQualifier, null);
//    } finally {
//      writeLock.unlock();
//    }
//  }
//
//  @Override
//  public List<IActor> getAllRegisteredActors() {
//    try {
//      readLock.lock();
//
//      return new ArrayList<IActor>(this.actors.values());
//    } finally {
//      readLock.unlock();
//    }
//  }