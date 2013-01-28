package org.livingplace.controlling.knowledge.api.internal;


import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.knowledge.api.IRuleEngine;

@Component(immediate = true)
public class RuleEngine implements IRuleEngine {

  @Reference
  IActionRegistryFactory actionRegistryFactory;

  @Reference
  IInformationRegistryFactory informationRegistryFactory;

  private static final Logger logger = Logger.getLogger(RuleEngine.class);
  private DroolsManager engineManager;
  private FactInsertionListener factInsetionListener;

  @Activate
  public void start() {
    logger.info("STARTING RULEENGINE...");
    init();
    logger.info("STARTED RULEENGINE.");
  }

  @Deactivate
  public void stop() {
    informationRegistryFactory.getInstance().removeAllInformationsListener(factInsetionListener);
    engineManager.interrupt();
    logger.info("STOPPED RULEENGINE!");
  }

  private void init() {
    try {
      IActionRegistry actionRegistry = actionRegistryFactory.getInstance();
      IInformationRegistry informationRegistry = informationRegistryFactory.getInstance();

      // show what's there:
      logger.info("Registered Actions:      " + actionRegistry.getAllRegistered());
      logger.info("Registered Informations: " + informationRegistry.getAllRegistered());

      engineManager = new DroolsManager(actionRegistry, new RegistryClassLoader(
              this.getClass().getClassLoader(),
              actionRegistry,
              informationRegistry));
      factInsetionListener = new FactInsertionListener(engineManager);

      informationRegistry.addAllInformationsListener(factInsetionListener);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
}