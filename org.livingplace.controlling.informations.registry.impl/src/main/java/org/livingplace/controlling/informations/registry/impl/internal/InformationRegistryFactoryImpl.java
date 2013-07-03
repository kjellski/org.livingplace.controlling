package org.livingplace.controlling.informations.registry.impl.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;

@Service(serviceFactory = true)
@Component
public class InformationRegistryFactoryImpl implements IInformationRegistryFactory {

  private static volatile IInformationRegistry instance = null;
  private static Logger logger = Logger.getLogger(InformationRegistryFactoryImpl.class);

  private static IInformationRegistry createInstance() {
    if (instance == null) {
      synchronized (InformationRegistryFactoryImpl.class){
        if (instance == null) {
          logger.info("Instantiated the singleton InformationRegistry.");
          instance = new InformationRegistryImpl();
        }
      }
    }
    return instance;
  }

  @Override
  public IInformationRegistry getInstance() {
    return createInstance();
  }
}
