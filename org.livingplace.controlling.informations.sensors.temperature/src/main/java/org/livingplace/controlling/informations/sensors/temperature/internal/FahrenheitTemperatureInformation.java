package org.livingplace.controlling.informations.sensors.temperature.internal;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Information;
import org.livingplace.controlling.informations.api.providers.InformationQualifier;

import java.util.Random;

public class FahrenheitTemperatureInformation extends Information implements IInformation {
  public FahrenheitTemperatureInformation(ISensor source) {
    super(source, new InformationQualifier("temperature", "inFahrenheit", "1.0"));
    this.setInformation(this.getQualifier() + " - Temperature in Degrees: " +  (new Random().nextInt() % 25));
  }
}
