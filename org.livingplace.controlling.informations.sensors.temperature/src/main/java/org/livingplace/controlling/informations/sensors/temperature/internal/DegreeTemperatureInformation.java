package org.livingplace.controlling.informations.sensors.temperature.internal;

import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.ISensor;
import org.livingplace.controlling.informations.api.providers.Information;
import org.livingplace.controlling.informations.api.providers.InformationQualifier;

import java.util.Random;

public class DegreeTemperatureInformation extends Information implements IInformation {
  public DegreeTemperatureInformation(ISensor source) {
    super(source, new InformationQualifier("temperature", "inDegrees", "1.0"));
    this.setInformation(this.getQualifier() + " - Temperature in Degrees: " +  (new Random().nextInt() % 25));
  }
}
