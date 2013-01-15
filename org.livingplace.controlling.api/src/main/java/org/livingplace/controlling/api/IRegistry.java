package org.livingplace.controlling.api;

import java.util.List;

/**
 * General Registry interface, this is the interface to be used when
 * providing a registry for actions or informations. to be extended
 * by the concrete interface.
 * @param <T>
 */
public interface IRegistry<T extends IQualifiable> {
  /**
   * Registers a new T Type in the registry
   * @param toBeRegistered
   */
  void register(T toBeRegistered);

  /**
   * Unregisters type T in the registry
   * @param toBeUnregistered
   */
  void unregister(T toBeUnregistered);

  /**
   * Returns all actually known entries of the Registry.
   * @return all known registry entries
   */
  List<T> getAllRegistered();

  /**
   * Returns one qualified entry of the registry
   * @param qualifier
   * @return
   */
  T get(IQualifier qualifier);
}
