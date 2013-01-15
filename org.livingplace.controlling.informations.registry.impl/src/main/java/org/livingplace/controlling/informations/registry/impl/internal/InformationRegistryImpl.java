package org.livingplace.controlling.informations.registry.impl.internal;

import org.apache.log4j.Logger;
import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that registeres known informations provided by sensors registering
 * by the
 */
public class InformationRegistryImpl implements IInformationRegistry {

  private final Map<String, Map.Entry<IInformation, IInformationListener>> registry
          = new ConcurrentHashMap<String, Map.Entry<IInformation, IInformationListener>>();
  private static Logger logger = Logger.getLogger(InformationRegistryImpl.class);

  public InformationRegistryImpl() {
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
  public IInformationListener registerOnListener(IInformation toBeRegistered) {

    Map.Entry<IInformation, IInformationListener> old = registry.put(toBeRegistered.getQualifier().getFullQualifier(),
            new AbstractMap.SimpleEntry<IInformation, IInformationListener>(toBeRegistered, BroadcasterListener));

    if (old != null)
      logger.info("Replaced " + old.getKey().getQualifier().getFullQualifier() +
              " with new " + toBeRegistered.getQualifier().getFullQualifier() + " in InformationRegistry.");

    logger.info("Added " + toBeRegistered.getQualifier().getFullQualifier() + " to InformationRegistry.");
    return BroadcasterListener;
  }

  /**
   * Registers an Information type to be known by the Knowledgebase.
   * WARNING: When this method is used, you can't create this Information outside the KnowledgeBase since you don't
   * get a Listener to be notified by the sensor. This is normally not what you want; use registerOnListener instead.
   * @param toBeRegistered
   */
  @Override
  public void register(IInformation toBeRegistered) {
    Map.Entry<IInformation, IInformationListener> old = registry.put(toBeRegistered.getQualifier().getFullQualifier(),
            new AbstractMap.SimpleEntry<IInformation, IInformationListener>(toBeRegistered, BroadcasterListener));

    if (old != null)
      logger.info("Replaced " + old.getKey().getQualifier().getFullQualifier() +
              " with new " + toBeRegistered.getQualifier().getFullQualifier() + " in InformationRegistry.");

    logger.info("Added " + toBeRegistered.getQualifier().getFullQualifier() + " to InformationRegistry.");
  }

  @Override
  public void unregister(IInformation toBeUnregistered) {
    registry.remove(toBeUnregistered.getQualifier().getFullQualifier());
    logger.info("Removed " + toBeUnregistered.getQualifier().getFullQualifier() + " from InformationRegistry.");
  }

  @Override
  public List<IInformation> getAllRegistered() {
    List<Map.Entry<IInformation, IInformationListener>> l =
            new ArrayList<Map.Entry<IInformation, IInformationListener>>(this.registry.values());

    List<IInformation> res = new ArrayList<IInformation>();

    for (Map.Entry<IInformation, IInformationListener> informationListenerEntry : l) {
      res.add(informationListenerEntry.getKey());
    }

    logger.info("Returning " + res.size() + " Entries from InformationRegistry.");

    return res;
  }

  @Override
  public IInformation get(IQualifier qualifier) {
    logger.info("Returning " + qualifier.getFullQualifier() + " from InformationRegistry.");
    return registry.get(qualifier.getFullQualifier()).getKey();
  }

  private class PrintingInformationListener implements IInformationListener {
    @Override
    public void sensedInformation(IInformation information) {
      String out = "Sensed: " + information.toString();
      logger.info(out);
    }
  }
}
