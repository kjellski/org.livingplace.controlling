package org.livingplace.controlling.informations.registry.impl.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.osgi.service.log.LogService;

@Service(serviceFactory = true)
@Component
public class InformationRegistryFactoryImpl implements IInformationRegistryFactory {

  @Reference
  LogService log;

  private static volatile IInformationRegistry instance = null;

  private static IInformationRegistry createInstance(LogService log) {
    if (instance == null) {
      synchronized (InformationRegistryFactoryImpl.class){
        if (instance == null) {
          log.log(LogService.LOG_INFO, "Instantiated the singleton InformationRegistry.");
          instance = new InformationRegistryImpl(log);
        }
      }
    }
    return instance;
  }

  @Override
  public IInformationRegistry getInstance() {
    return createInstance(log);
  }
}
