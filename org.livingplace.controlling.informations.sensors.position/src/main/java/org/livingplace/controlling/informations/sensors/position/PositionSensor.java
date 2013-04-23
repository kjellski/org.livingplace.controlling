package org.livingplace.controlling.informations.sensors.position;

import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Sensor;
import org.livingplace.controlling.informations.api.providers.SensorQualifier;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.informations.sensors.position.internal.PositionInformation;

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

    informations.add(new PositionInformation(this));

    for (IInformation information : informations) {
      listeners.add(informationRegistryFactory.getInstance().registerOnListener(information));
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
