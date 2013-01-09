package org.livingplace.controlling.knowledge.api.internal;


import org.apache.felix.scr.annotations.*;
import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.livingplace.controlling.knowledge.api.IRuleEngine;

@Component(immediate = true)
public class RuleEngine implements IRuleEngine {

  @Reference
  IActionRegistryFactory actionRegistryFactory;

  @Reference
  IInformationRegistryFactory informationRegistryFactory;

  private DroolsManager engineManager;
  private static final Logger logger = Logger.getLogger(RuleEngine.class);

  @Activate
  public void start() {
    logger.info("STARTING RULEENGINE...");
    init();
    logger.info("STARTED RULEENGINE.");
  }

  @Deactivate
  public void stop() {
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

      // bind information listener!
      IInformationListener factInsetionListener = new IInformationListener() {
        @Override
        public void sensedInformation(IInformation information) {
          if (logger.isDebugEnabled()){
            logger.debug(information.toString());
          }
          engineManager.addFact(information);
        }
      };

      informationRegistry.addAllInformationsListener(factInsetionListener);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
}