package org.livingplace.controlling.knowledge.api.internal;


import org.apache.felix.scr.annotations.*;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
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

  @Property(value = "default value given by annotation")
  static final String CONSTANT_NAME = "property.name";

  @Activate
  public void start() {
    log.log(LogService.LOG_INFO, "\nSTARTING RULEENGINE...");
    init();
    log.log(LogService.LOG_INFO, "STARTED RULEENGINE.");
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " started.");
  }

  @Deactivate
  public void stop() {
    engineManager.interrupt();
    log.log(LogService.LOG_INFO, "STOPPED RULEENGINE!");
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " stopped.");
  }

  private void init() {
    try {
      IActionRegistry actionRegistry = actionRegistryFactory.getInstance();
      IInformationRegistry informationRegistry = informationRegistryFactory.getInstance();

      // show what's there:
      log.log(LogService.LOG_INFO, "Registered Actions:      " + actionRegistry.getAllRegistered());
      log.log(LogService.LOG_INFO, "Registered Informations: " + informationRegistry.getAllRegistered());

      engineManager = new DroolsManager(log, new RegistryClassLoader(
              this.getClass().getClassLoader(),
              actionRegistry,
              informationRegistry));

      // bind information listener!
      IInformationListener factInsetionListener = new IInformationListener() {
        @Override
        public void sensedInformation(IInformation information) {
          engineManager.addFact(information);
        }
      };

      informationRegistry.addAllInformationsListener(factInsetionListener);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
}