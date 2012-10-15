package org.livingplace.controlling.informations.api.providers;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.providers.Qualifier;
import org.livingplace.controlling.informations.api.ISensorQualifier;

/**
 * This class represents a qualifier for sensors, any instance should identify the
 * sensor uniquely.
 */
public class SensorQualifier extends Qualifier implements ISensorQualifier {

  public SensorQualifier(IQualifier qualifier) {
    super(qualifier.getNamespace(), qualifier.getName(), qualifier.getVersion());
  }

  public SensorQualifier(String actorNamespaceName, String actorName, String actorVersion) {
    super(actorNamespaceName, actorName, actorVersion);
  }

  @Override
  public String getPrefix() {
    return PREFIX;
  }
}
