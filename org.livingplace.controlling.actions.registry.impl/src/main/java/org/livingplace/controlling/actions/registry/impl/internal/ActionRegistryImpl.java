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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@Service
public class ActionRegistryImpl extends Registry<IAction> implements IActionRegistry {
  private static final int THREADPOOL_SIZE = 100;
  private static final Executor executor = Executors.newFixedThreadPool(THREADPOOL_SIZE);

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
}