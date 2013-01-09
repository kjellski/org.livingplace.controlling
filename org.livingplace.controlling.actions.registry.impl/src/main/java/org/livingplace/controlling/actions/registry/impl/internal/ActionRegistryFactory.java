package org.livingplace.controlling.actions.registry.impl.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;

@Service(serviceFactory = true)
@Component
public class ActionRegistryFactory implements IActionRegistryFactory {

  private static Logger logger = Logger.getLogger(ActionRegistryFactory.class);

  private static volatile IActionRegistry instance = null;

  private static IActionRegistry createInstance() {
    if (instance == null) {
      synchronized (ActionRegistryImpl.class){
        if (instance == null) {
          logger.info("Instantiated the singleton ActionRegistry.");
          instance = new ActionRegistryImpl();
        }
      }
    }
    return instance;
  }

  @Override
  public IActionRegistry getInstance() {
    return createInstance();
  }
}
