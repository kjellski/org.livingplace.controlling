package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.QualifierParser;
import org.livingplace.controlling.informations.api.ISensorQualifier;

public class SensorQualifierParser extends QualifierParser {

  public static ISensorQualifier parseActorQualifier(String fullSensorQualifier) {
    IQualifier qualifier = parseQualifier(fullSensorQualifier);
    return new SensorQualifier(qualifier);
  }
}
