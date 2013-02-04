package org.livingplace.controlling.informations.sensors.temperature;

import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Sensor;
import org.livingplace.controlling.informations.api.providers.SensorQualifier;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.informations.sensors.temperature.internal.DegreeTemperatureInformation;
import org.livingplace.controlling.informations.sensors.temperature.internal.FahrenheitTemperatureInformation;

import java.util.*;

@Component(immediate = true)
@Service
public class TemperatureSensor extends Sensor implements ISensor
{
  List<IInformationListener> listeners = new ArrayList<IInformationListener>();
  Timer timer = new Timer();
  private static Logger logger = Logger.getLogger(TemperatureSensor.class);

  @Reference
  protected IInformationRegistryFactory informationRegistryFactory;

  public TemperatureSensor() {
    super(new SensorQualifier("time", "timer", "1.0"));
  }

  @Activate
  void start(){
    logger.info("Timer started");

    informations.add(new DegreeTemperatureInformation(this));
    informations.add(new FahrenheitTemperatureInformation(this));

    for (IInformation information : informations) {
      listeners.add(informationRegistryFactory.getInstance().registerOnListener(information));
    }

    timer.schedule(new UTCTimeSensingTask(this), 1000,  ( (new Random().nextInt() % 25) + 1 )* 1000);
  }

  private class UTCTimeSensingTask extends TimerTask {
    public final ISensor sensor;

    private UTCTimeSensingTask(ISensor sensor) {
      this.sensor = sensor;
    }

    @Override
    public void run() {
      for (IInformationListener listener : listeners) {
        listener.sensedInformation(new DegreeTemperatureInformation(sensor));
        listener.sensedInformation(new FahrenheitTemperatureInformation(sensor));
      }
    }
  }

  @Deactivate
  void stop(){
    for (IInformation information : informations) {
      informationRegistryFactory.getInstance().unregister(information);
    }
    timer.cancel();
    logger.info("Timer stopped");
  }
}

