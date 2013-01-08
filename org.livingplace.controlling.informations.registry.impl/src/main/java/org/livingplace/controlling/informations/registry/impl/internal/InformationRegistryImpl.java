package org.livingplace.controlling.informations.registry.impl.internal;

import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.osgi.service.log.LogService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that registeres known informations provided by sensors registering
 * by the
 */
public class InformationRegistryImpl implements IInformationRegistry {

  private final Map<String, Map.Entry<IInformation, IInformationListener>> registry
          = new ConcurrentHashMap<String, Map.Entry<IInformation, IInformationListener>>();

  protected LogService log;

  public InformationRegistryImpl(LogService log) {
    this.log = log;
    listeners.add(new PrintingInformationListener());
  }

  public List<IInformationListener> listeners = Collections.synchronizedList(new ArrayList<IInformationListener>());

  private IInformationListener BroadcasterListener  = new IInformationListener() {
    @Override
    public void sensedInformation(IInformation information) {
      for (IInformationListener listener : listeners) {
        listener.sensedInformation(information);
      }
    }
  };

  @Override
  public void addAllInformationsListener(IInformationListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeAllInformationsListener(IInformationListener listener) {
    this.listeners.remove(listener);
  }

  @Override
  public IInformationListener register(IInformation toBeRegistered) {

    Map.Entry<IInformation, IInformationListener> old = registry.put(toBeRegistered.getQualifier().getFullQualifier(),
            new AbstractMap.SimpleEntry<IInformation, IInformationListener>(toBeRegistered, BroadcasterListener));

    if (old != null)
      log.log(LogService.LOG_INFO, "Replaced " + old.getKey().getQualifier().getFullQualifier() +
              " with new " + toBeRegistered.getQualifier().getFullQualifier() + " in InformationRegistry.");

    log.log(LogService.LOG_INFO, "Added " + toBeRegistered.getQualifier().getFullQualifier() + " to InformationRegistry.");
    return BroadcasterListener;
  }

  @Override
  public void unregister(IInformation toBeUnregistered) {
    registry.remove(toBeUnregistered.getQualifier().getFullQualifier());
    log.log(LogService.LOG_INFO, "Removed " + toBeUnregistered.getQualifier().getFullQualifier() + " from InformationRegistry.");
  }

  @Override
  public List<IInformation> getAllRegistered() {
    List<Map.Entry<IInformation, IInformationListener>> l =
            new ArrayList<Map.Entry<IInformation, IInformationListener>>(this.registry.values());

    List<IInformation> res = new ArrayList<IInformation>();

    for (Map.Entry<IInformation, IInformationListener> informationListenerEntry : l) {
      res.add(informationListenerEntry.getKey());
    }

    log.log(LogService.LOG_INFO, "Returning " + res.size() + " Entries from InformationRegistry.");

    return res;
  }

  @Override
  public IInformation get(IQualifier qualifier) {
    log.log(LogService.LOG_INFO, "Returning " + qualifier.getFullQualifier() + " from InformationRegistry.");
    return registry.get(qualifier.getFullQualifier()).getKey();
  }

  private class PrintingInformationListener implements IInformationListener {
    @Override
    public void sensedInformation(IInformation information) {
      String out = "Sensed: " + information.toString();
      log.log(LogService.LOG_INFO, out);
    }
  }
}
