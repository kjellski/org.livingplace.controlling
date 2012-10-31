package org.livingplace.controlling.actions.registry.impl.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;
import org.osgi.service.log.LogService;

@Service(serviceFactory = true)
@Component
public class ActionRegistryFactory implements IActionRegistryFactory {

  @Reference
  LogService log;

  private static volatile IActionRegistry instance = null;

  private static IActionRegistry createInstance(LogService log) {
    if (instance == null) {
      synchronized (ActionRegistryImpl.class){
        if (instance == null) {
          log.log(LogService.LOG_INFO, "Instantiated the singleton ActionRegistry.");
          instance = new ActionRegistryImpl(log);
        }
      }
    }
    return instance;
  }

  @Override
  public IActionRegistry getInstance() {
    return createInstance(log);
  }
}
