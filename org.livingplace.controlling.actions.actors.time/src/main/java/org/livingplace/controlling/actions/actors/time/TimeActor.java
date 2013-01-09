package org.livingplace.controlling.actions.actors.time;

import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.actors.time.internal.BroadcastTimeOnAMQ;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.api.providers.AbstractActor;
import org.livingplace.controlling.actions.api.providers.ActorQualifier;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;

@Component(immediate = true)
@Service
public class TimeActor extends AbstractActor implements IActor {

  private static Logger logger = Logger.getLogger(TimeActor.class);

  @Reference
  protected IActionRegistryFactory actionRegistryFactory;

  public TimeActor() {
    super(new ActorQualifier("time", "timer", "1.0"));
  }

  @Activate
  void start(){
    logger.info("TimerActor started.");
    actions.add(new BroadcastTimeOnAMQ());

    for (IAction action : actions) {
      actionRegistryFactory.getInstance().register(action);
    }
  }

  @Deactivate
  void stop(){
    for (IAction action : actions) {
      actionRegistryFactory.getInstance().unregister(action);
    }
    logger.info("TimerActor stopped.");
  }
}
