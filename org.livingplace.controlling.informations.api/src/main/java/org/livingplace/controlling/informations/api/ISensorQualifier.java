package org.livingplace.controlling.informations.api;

import org.livingplace.controlling.api.IQualifier;

/**
 * Sensor specific qualifier
 */
public interface ISensorQualifier extends IQualifier {
  /**
   * Prefix to be combined with the full qualifier in order to
   * make the qualifier distinguishable
   */
  public static final String PREFIX = "SENSOR";
}
