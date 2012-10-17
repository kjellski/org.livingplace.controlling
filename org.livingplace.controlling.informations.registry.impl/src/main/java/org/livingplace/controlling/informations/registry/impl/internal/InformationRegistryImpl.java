package org.livingplace.controlling.informations.registry.impl.internal;

import org.apache.felix.scr.annotations.*;
import org.livingplace.controlling.api.IQualifier;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.osgi.service.log.LogService;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that registeres known informations provided by sensors registering
 * by the
 */
@Component
@Service
public class InformationRegistryImpl implements IInformationRegistry {

  private final Map<String, Map.Entry<IInformation, IInformationListener>> registry
          = new ConcurrentHashMap<String, Map.Entry<IInformation, IInformationListener>>();

  @Reference
  protected LogService log;

  public void setLog(LogService log) {
    this.log = log;
    log.log(LogService.LOG_WARNING, InformationRegistryImpl.class.getName() + ".log overidden.");
  }

  @Activate
  void start() {
    log.log(LogService.LOG_INFO, InformationRegistryImpl.class.getName() + " started.");
  }

  @Deactivate
  void stop() {
    log.log(LogService.LOG_INFO, InformationRegistryImpl.class.getName() + " stopped.");
  }

  @Override
  public IInformationListener register(IInformation toBeRegistered) {

    PrintingInformationListener tmp = new PrintingInformationListener();

    Map.Entry<IInformation, IInformationListener> old = registry.put(toBeRegistered.getQualifier().getFullQualifier(),
            new AbstractMap.SimpleEntry<IInformation, IInformationListener>(toBeRegistered, tmp));

    if (old != null)
      log.log(LogService.LOG_INFO, "Replaced " + old.getKey().getQualifier().getFullQualifier() +
              " with new " + toBeRegistered.getQualifier().getFullQualifier() + " in InformationRegistry.");

    log.log(LogService.LOG_INFO, "Added " + toBeRegistered.getQualifier().getFullQualifier() + " to InformationRegistry.");
    return tmp;
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
      String out = "\nSensed: " + information.toString();
      log.log(LogService.LOG_INFO, out);
      System.out.println(out);
    }
  }
}
