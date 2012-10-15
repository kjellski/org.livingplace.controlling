package org.livingplace.controlling.actions.actors.time;

import org.apache.felix.scr.annotations.*;
import org.livingplace.controlling.actions.actors.time.internal.BroadcastTimeOnAMQ;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.api.providers.AbstractActor;
import org.livingplace.controlling.actions.api.providers.ActorQualifier;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.osgi.service.log.LogService;

@Component(immediate = true)
@Service
public class TimeActor extends AbstractActor implements IActor {

  @Reference
  protected LogService log;

  @Reference
  protected IActionRegistry registry;

  public TimeActor() {
    super(new ActorQualifier("time", "timer", "1.0"));
  }

  @Activate
  void start(){
    log.log(LogService.LOG_INFO, "Timer started");
    actions.add(new BroadcastTimeOnAMQ());

    for (IAction action : actions) {
      registry.registerAction(action);
    }
  }

  @Deactivate
  void stop(){
    log.log(LogService.LOG_INFO, "Timer stopped");

    for (IAction action : actions) {
      registry.unregisterAction(action);
    }
  }
}
