package org.livingplace.controlling.api;

import java.util.List;

public interface IRegistry<T> {
  void register(T action);

  void unregister(T action);

  List<T> getAllRegistered();

  T get(IQualifier qualifier);
}
