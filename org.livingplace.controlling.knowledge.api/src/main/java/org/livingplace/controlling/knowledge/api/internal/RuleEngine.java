package org.livingplace.controlling.knowledge.api.internal;


import org.apache.felix.scr.annotations.*;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.knowledge.api.IRuleEngine;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

import java.util.Dictionary;

@Component(immediate = true)
public class RuleEngine implements IRuleEngine, ManagedService {

  @Reference
  IActionRegistryFactory actionRegistryFactory;

  @Reference
  IInformationRegistryFactory informationRegistryFactory;

  @Reference
  LogService log;

  DroolsManager engineManager;

  @Property(value="default value given by annotation")
  static final String CONSTANT_NAME = "property.name";

  @Activate
  public void start(){
    log.log(LogService.LOG_INFO, "\nSTARTING RULEENGINE...");
    init();
    log.log(LogService.LOG_INFO, "STARTED RULEENGINE.");
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " started.");
  }

  @Override
  public void updated(Dictionary<String, ?> stringDictionary) throws ConfigurationException {

  }

  @Deactivate
  public void stop(){
    engineManager.interrupt();
    log.log(LogService.LOG_INFO, "STOPPED RULEENGINE!");
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " stopped.");
  }

  private void init() {
    try {
      log.log(LogService.LOG_INFO, "Registered Actions:      " + actionRegistryFactory.getInstance().getAllRegistered());
      log.log(LogService.LOG_INFO, "Registered Informations: " + informationRegistryFactory.getInstance().getAllRegistered());
      engineManager = new DroolsManager(log);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }



}
