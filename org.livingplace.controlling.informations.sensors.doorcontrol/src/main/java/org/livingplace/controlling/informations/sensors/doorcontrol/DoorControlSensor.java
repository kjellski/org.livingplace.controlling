package org.livingplace.controlling.informations.sensors.doorcontrol;

import com.google.gson.Gson;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Sensor;
import org.livingplace.controlling.informations.api.providers.SensorQualifier;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.informations.sensors.doorcontrol.internal.DoorInformations;
import org.livingplace.messaging.activemq.api.ILPConnectionSettings;
import org.livingplace.messaging.activemq.api.ILPMessagingFactory;
import org.livingplace.messaging.activemq.api.ILPSubscriber;

import org.haw.door.messages.*;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sven
 * Date: 01.07.13
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class DoorControlSensor extends Sensor implements ISensor {

    private static Logger logger = Logger.getLogger(DoorControlSensor.class);
    List<IInformationListener> listeners = new ArrayList<IInformationListener>();

    @Reference
    protected IInformationRegistryFactory informationRegistryFactory;

    @Reference
    protected ILPMessagingFactory messagingFactory;


    public DoorControlSensor (){
        super(new SensorQualifier("Door", "DoorControlSensor", "1.0"));
    }

    @Activate
    void start(){
        logger.info("DoorControl Sensor started");

       // informations.add(new PositionInformation(this, new Position()));

        for (IInformation information : informations) {
            listeners.add(informationRegistryFactory.getInstance().registerOnListener(information));
        }


        ILPConnectionSettings cs = messagingFactory.createLPConnectionSettings();
//        cs.setActiveMQIp("172.16.0.200");
//        cs.setMongoDBIp("172.16.0.200");

        try {
            ILPSubscriber subscriber = messagingFactory.createLPSubscriberInstance("UbisenseTracking", cs);
            Gson gson = new Gson();
            for (; ; ) {

                String msg = subscriber.subscribeBlocking();
                logger.info("#######Sensed Message: " + msg);
                Messages pos = gson.fromJson(msg, Messages.class);
                for (IInformationListener listener : listeners)
                    listener.sensedInformation(new DoorInformations(this, pos.getLpRes()));
            }
        } catch (JMSException e) {
            logger.error(this.getQualifier() + ": " + e);
            e.printStackTrace();
        }


    }

    @Deactivate
    void stop(){
        for (IInformation information : informations) {
            informationRegistryFactory.getInstance().unregister(information);
        }

        logger.info("DoorControl Sensor stopped");
    }

}
