package org.livingplace.controlling.api;

import java.util.List;

public interface IRegistry<T> {
  void register(T toBeRegistered);

  void unregister(T toBeUnregistered);

  List<T> getAllRegistered();

  T get(IQualifier qualifier);
}
