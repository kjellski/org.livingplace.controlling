package org.livingplace.controlling.informations.registry.api;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.IRegistry;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;

import java.util.List;

/**
 * Informations registry
 */
public interface IInformationRegistry extends IRegistry<IInformation> {

  /**
   * Adds a listener to be notified whenever any information is sensed.
   */
  void addAllInformationsListener(IInformationListener listener);

  /**
   * Adds a listener to be notified whenever any information is sensed.
   */
  void removeAllInformationsListener(IInformationListener listener);

  /**
   * Method to register a new Information to be provided by a sensor
   * @param toBeRegistered information to be registered
   * @return listener to be informed when a new information of that kind is sensed
   */
  IInformationListener registerOnListener(IInformation toBeRegistered);

  /**
   * shuts down all listeners and removes the information form the registry
   * @param toBeUnregistered
   */
  void unregister(IInformation toBeUnregistered);

  /**
   * Returns a list of all registered informations
   * @return
   */
  List<IInformation> getAllRegistered();

  /**
   * Returns an information object corresponding to the qualifier
   * @param qualifier
   * @return
   */
  IInformation get(IQualifier qualifier);
}