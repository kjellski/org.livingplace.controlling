package org.livingplace.controlling.informations.api;

import org.livingplace.controlling.api.IQualifiable;

/**
 * Information event that is used by
 * @see ISensor
 * to provide informations. When a sensor detects something, it creates a new instance of this information
 * in order to inform the registry provided listeners about this new fact.
 */
public interface IInformation extends IQualifiable {
  /**
   * setter for the information contained
   * @param information
   */
  void setInformation(Object information);

  /**
   * getter for the information contained
   * @return
   */
  Object getInformation();
}
