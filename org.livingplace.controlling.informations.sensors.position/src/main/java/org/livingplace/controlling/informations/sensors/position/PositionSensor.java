package org.livingplace.controlling.informations.sensors.position;

import com.google.gson.Gson;
import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Sensor;
import org.livingplace.controlling.informations.api.providers.SensorQualifier;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.informations.sensors.position.internal.PositionInformation;
import org.livingplace.controlling.informations.sensors.position.internal.UbisensePositionMessage;
import org.livingplace.messaging.activemq.api.ILPConnectionSettings;
import org.livingplace.messaging.activemq.api.ILPMessagingFactory;
import org.livingplace.messaging.activemq.api.ILPSubscriber;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

@Component(immediate = true)
@Service
public class PositionSensor extends Sensor implements ISensor {

    List<IInformationListener> listeners = new ArrayList<IInformationListener>();

    private static Logger logger = Logger.getLogger(PositionSensor.class);

    @Reference
    protected IInformationRegistryFactory informationRegistryFactory;

    @Reference
    protected ILPMessagingFactory messagingFactory;

    public PositionSensor() {
        super(new SensorQualifier("position", "positioner", "1.0"));
    }

    @Activate
    void start() {
        logger.info("PositionSensor started");

        informations.add(new PositionInformation(this, new UbisensePositionMessage()));

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
                logger.info("Sensed UbisenseTracking information: " + msg);
                UbisensePositionMessage pos = gson.fromJson(msg, UbisensePositionMessage.class);
                for (IInformationListener listener : listeners) {
                    listener.sensedInformation(new PositionInformation(this, pos));
                }
            }
        } catch (JMSException e) {
            logger.error(this.getQualifier() + ": error while getting positions from Topic \"UbisenseTracking\"", e);
            e.printStackTrace();
        }
    }

    @Deactivate
    void stop() {
        for (IInformation information : informations) {
            informationRegistryFactory.getInstance().unregister(information);
        }
        logger.info("PositionSensor stopped");
    }

}
