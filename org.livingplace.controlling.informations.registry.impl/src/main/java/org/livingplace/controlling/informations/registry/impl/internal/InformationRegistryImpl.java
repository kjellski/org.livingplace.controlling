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
    registry.put(toBeRegistered.getQualifier().getFullQualifier(),
            new AbstractMap.SimpleEntry<IInformation, IInformationListener>(toBeRegistered, tmp));
    return tmp;
  }

  @Override
  public void unregister(IInformation toBeUnregistered) {
    registry.remove(toBeUnregistered.getQualifier().getFullQualifier());
  }

  @Override
  public List<IInformation> getAllRegistered() {
    List<Map.Entry<IInformation, IInformationListener>> l =
            new ArrayList<Map.Entry<IInformation, IInformationListener>>(this.registry.values());
    List<IInformation> res = new ArrayList<IInformation>();
    for (Map.Entry<IInformation, IInformationListener> informationListenerEntry : l) {
      res.add(informationListenerEntry.getKey());
    }

    return res;
  }

  @Override
  public IInformation get(IQualifier qualifier) {
    return registry.get(qualifier.getFullQualifier()).getKey();
  }

  private class PrintingInformationListener implements IInformationListener {
    @Override
    public void sensedInformation(IInformation information) {
      System.out.println(information.getQualifier().getFullQualifier() + ": " + information.toString());
    }
  }
}
