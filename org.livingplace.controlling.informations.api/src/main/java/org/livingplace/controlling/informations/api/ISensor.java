package org.livingplace.controlling.informations.api;

import org.livingplace.controlling.api.IQualifiable;

import java.util.List;

/**
 * Sensors are the system components that generate the
 * @see IInformation informations
 * as Events.
 * The sensor senses new informations and notifies the listeners retrieved by the IInformationRegistry
 * that the new information is now known.
 *
 * This interface should be used to implement new sensors.
 * It basically ensured that sensors are identifyable and
 * are able to tell the user what informations they provide.
 */
public interface ISensor extends IQualifiable {
  /**
   * All informations this sensor provides
   * @return List of Informations the sensor provides
   */
  List<IInformation> getAllInformations();
}
