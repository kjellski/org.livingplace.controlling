package org.livingplace.controlling.informations.sensors.position;

import com.google.gson.Gson;
import org.livingplace.messaging.activemq.wrapper.ConnectionSettings;
import org.livingplace.messaging.activemq.wrapper.LPSubscriber;
import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Sensor;
import org.livingplace.controlling.informations.api.providers.SensorQualifier;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.informations.sensors.position.internal.Position;
import org.livingplace.controlling.informations.sensors.position.internal.PositionInformation;
import org.livingplace.controlling.informations.sensors.position.internal.UbisensePositionMessage;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Component(immediate = true)
@Service
public class PositionSensor extends Sensor implements ISensor {

  List<IInformationListener> listeners = new ArrayList<IInformationListener>();
  Timer timer = new Timer();
  private static Logger logger = Logger.getLogger(PositionSensor.class);

  @Reference
  protected IInformationRegistryFactory informationRegistryFactory;

  public PositionSensor() {
    super(new SensorQualifier("position", "positioner", "1.0"));
  }

  @Activate
  void start(){
    logger.info("PositionSensor started");

    informations.add(new PositionInformation(this, new Position()));

    for (IInformation information : informations) {
      listeners.add(informationRegistryFactory.getInstance().registerOnListener(information));
    }
  }

  {
    ConnectionSettings cs = new ConnectionSettings();
    cs.amq_ip = "172.16.0.200";
    cs.mongo_ip = "172.16.0.200";

    try {
      LPSubscriber subscriber = new LPSubscriber("UbisenseTracking", cs);

      for (;;) {
        Gson gson = new Gson();
        UbisensePositionMessage msg = gson.fromJson(subscriber.subscribeBlocking(), UbisensePositionMessage.class);
        for (IInformationListener listener : listeners) {
          listener.sensedInformation(new PositionInformation(this, msg.getNewPosition()));
        }
      }
    } catch (JMSException e) {
      logger.error(this.getQualifier() + ": error while getting positions from Topic \"UbisenseTracking\"", e);
      e.printStackTrace();
    }
  }

  @Deactivate
  void stop(){
    for (IInformation information : informations) {
      informationRegistryFactory.getInstance().unregister(information);
    }
    timer.cancel();
    logger.info("PositionSensor stopped");
  }

}
