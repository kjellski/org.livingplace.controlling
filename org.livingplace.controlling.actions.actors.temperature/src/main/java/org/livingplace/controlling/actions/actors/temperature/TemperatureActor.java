package org.livingplace.controlling.actions.actors.temperature;

import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.actors.temperature.internal.TemperatureAction;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.api.providers.Actor;
import org.livingplace.controlling.actions.api.providers.ActorQualifier;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;

@Component(immediate = true)
@Service
public class TemperatureActor extends Actor implements IActor {

  private static Logger logger = Logger.getLogger(TemperatureActor.class);

  @Reference
  protected IActionRegistryFactory actionRegistryFactory;

  public TemperatureActor() {
    super(new ActorQualifier("time", "timer", "1.0"));
  }

  @Activate
  void start(){
    logger.info("Starting TimerActor ...");

    actions.add(new TemperatureAction());

    logger.info("TimerActor started with " + actions.size() + " actions: ");
    for (IAction action : actions) {
      logger.info("Registering " + action);
      actionRegistryFactory.getInstance().register(action);
    }

    logger.info("TimerActor started: " + this.toString());
  }

  @Deactivate
  void stop(){
    for (IAction action : actions) {
      actionRegistryFactory.getInstance().unregister(action);
    }
    logger.info("TimerActor stopped.");
  }
}
