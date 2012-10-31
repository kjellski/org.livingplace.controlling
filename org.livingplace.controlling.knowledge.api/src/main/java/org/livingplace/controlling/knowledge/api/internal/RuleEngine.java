package org.livingplace.controlling.knowledge.api.internal;


import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.knowledge.api.IRuleEngine;
import org.osgi.service.log.LogService;

@Component(immediate = true)
public class RuleEngine implements IRuleEngine {

  @Reference
  IActionRegistryFactory actionRegistryFactory;

  @Reference
  IInformationRegistryFactory informationRegistryFactory;

  @Reference
  LogService log;

  DroolsManager engineManager;

  @Activate
  public void start(){
    System.out.println("\nSTARTING RULEENGINE...");
    init();
    System.out.println();
    System.out.println("STARTED RULEENGINE.");
    System.out.println();
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " started.");
  }

  private void init() {
    try {
      System.out.println();
      System.out.println("Registered Actions:      " + actionRegistryFactory.getInstance().getAllRegistered());
      System.out.println("Registered Informations: " + informationRegistryFactory.getInstance().getAllRegistered());
      engineManager = new DroolsManager(log);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  @Deactivate
  public void stop(){
    engineManager.interrupt();
    System.out.println("STOPPED RULEENGINE!");
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " stopped.");
  }
}
