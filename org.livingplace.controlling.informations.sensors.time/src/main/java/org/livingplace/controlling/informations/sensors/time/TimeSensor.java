package org.livingplace.controlling.informations.sensors.time;

import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.AbstractSensor;
import org.livingplace.controlling.informations.api.providers.SensorQualifier;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.informations.sensors.time.internal.UTCTimeInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component(immediate = true)
@Service
public class TimeSensor extends AbstractSensor implements ISensor
{
  List<IInformationListener> listeners = new ArrayList<IInformationListener>();
  Timer timer = new Timer();
  private static Logger logger = Logger.getLogger(TimeSensor.class);

  @Reference
  protected IInformationRegistryFactory informationRegistryFactory;

  public TimeSensor() {
    super(new SensorQualifier("time", "timer", "1.0"));
  }

  @Activate
  void start(){
    logger.info("Timer started");

    informations.add(new UTCTimeInformation(this));

    for (IInformation information : informations) {
      listeners.add(informationRegistryFactory.getInstance().register(information));
    }

    timer.schedule(new UTCTimeSensingTask(this), 1000, 10000);
  }

  private class UTCTimeSensingTask extends TimerTask {
    public final ISensor sensor;

    private UTCTimeSensingTask(ISensor sensor) {
      this.sensor = sensor;
    }

    @Override
    public void run() {
      for (IInformationListener listener : listeners) {
        listener.sensedInformation(new UTCTimeInformation(sensor));
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

