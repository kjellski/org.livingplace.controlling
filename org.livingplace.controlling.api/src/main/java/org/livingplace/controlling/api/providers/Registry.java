package org.livingplace.controlling.api.providers;

import org.livingplace.controlling.api.IQualifiable;
import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.api.IRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry<T extends IQualifiable> implements IRegistry<T> {

  private final Map<String, T> registry = new ConcurrentHashMap<String, T>();

  @Override
  public void register(T toBeRegistered) {

    T old = registry.put(toBeRegistered.getQualifier().getFullQualifier(), toBeRegistered);
    if (old != null) {
      System.out.println("Replaced " + old.getQualifier().getFullQualifier() + " with new : " + toBeRegistered.getQualifier().getFullQualifier());
    }
    System.out.println("Added to " + toBeRegistered.getQualifier().getFullQualifier() + " Registry.");
  }


  @Override
  public void unregister(T toBeUnregistered) {
    this.registry.remove(toBeUnregistered.getQualifier().getFullQualifier());
    System.out.println("Removed " + toBeUnregistered.getQualifier().getFullQualifier() + " from Registry.");
  }

  @Override
  public List<T> getAllRegistered() {
      return new ArrayList<T>(this.registry.values());
  }

  @Override
  public T get(IQualifier qualifier) {
    return this.registry.get(qualifier.getFullQualifier());
  }

}
