package org.livingplace.controlling.actions.actors.doorcontrol.internal;

import com.google.gson.Gson;
import org.apache.felix.scr.annotations.Reference;
import org.livingplace.bundles.messagingdefines.messages.Messages;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.providers.AbstractAction;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;
import org.livingplace.messaging.activemq.api.ILPConnectionSettings;
import org.livingplace.messaging.activemq.api.ILPMessagingFactory;
import org.livingplace.messaging.activemq.api.ILPProducer;


import javax.jms.JMSException;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: sven
 * Date: 01.07.13
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public class DeliverToDoor extends AbstractAction implements IAction {


    @Reference
    protected ILPMessagingFactory messagingFactory;

    public DeliverToDoor() {
        super(new ActionQualifier("Door", "DevilverToDoorAction", "1.0"));
    }

    /**
     * executes the action in the registrys executor poolw
     */
    @Override
    public void execute() {
        ILPConnectionSettings settings = messagingFactory.createLPConnectionSettings();
        //set ips


        // set data to send
//        String door =  this.getActionProperties().getDefaultProperties().getProperty("LPResName");
//        String operation = this.getActionProperties().getDefaultProperties().getProperty("Operation");

        Gson gson = new Gson();
        try {
            ILPProducer producer = messagingFactory.createLPProducer("door" ,settings);
            Messages msg = new Messages("2.0", "");
            msg.setOperation("OPEN");

            producer.produce(gson.toJson(msg));
            producer.disconnect();

        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
