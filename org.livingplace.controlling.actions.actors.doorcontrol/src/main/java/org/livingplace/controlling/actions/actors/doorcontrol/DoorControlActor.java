package org.livingplace.controlling.actions.actors.doorcontrol;

import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.actors.doorcontrol.internal.DeliverToDoor;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.api.IActorQualifier;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;
import org.livingplace.controlling.actions.api.providers.Actor;
import org.livingplace.controlling.actions.api.providers.ActorQualifier;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;

/**
 * Created with IntelliJ IDEA.
 * User: sven
 * Date: 01.07.13
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
@Component(immediate = true)
@Service
public class DoorControlActor extends Actor implements IActor {


    private static Logger logger = Logger.getLogger(DoorControlActor.class);

    @Reference
    protected IActionRegistryFactory actionRegistryFactory;

    public DoorControlActor() {
        super(new ActorQualifier("Door", "DoorControlAction", "1.0"));
    }

    @Activate
    void start(){
        logger.info("Starting TimerActor ...");

        actions.add(new DeliverToDoor());

        logger.info("DoorControl Actor started with " + actions.size() + " actions: ");
        for (IAction action : actions) {
            logger.info("Registering " + action);
            actionRegistryFactory.getInstance().register(action);
        }

        logger.info("DoorControl Actor started: " + this.toString());
    }

    @Deactivate
    void stop(){
        for (IAction action : actions) {
            actionRegistryFactory.getInstance().unregister(action);
        }
        logger.info("DoorControl Actor stopped.");
    }

}
