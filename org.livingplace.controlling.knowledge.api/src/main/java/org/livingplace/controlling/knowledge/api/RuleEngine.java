package org.livingplace.controlling.knowledge.api;


import org.apache.felix.scr.annotations.*;
import org.livingplace.controlling.knowledge.api.internal.DroolsManager;
import org.osgi.service.log.LogService;

@Component(immediate = true)
@Service
public class RuleEngine implements IRuleEngine {

  @Reference
  LogService log;

  DroolsManager engineManager;

  @Activate
  public void start(){
    System.out.println("STARTING RULEENGINE..........");
    init();
    System.out.println("STARTED RULEENGINE!");
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " started.");
  }

  @Override
  public void print() {
    System.out.println("PRINT!");
  }

  private void init() {
//    Properties defaults = new Properties();
//    try {
//      defaults.load(new FileInputStream("droolsengine.properties"));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    log.log(LogService.LOG_INFO, "Default configuration for the RuleEngine: " + defaults.toString());

    try {
      engineManager = new DroolsManager(log);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  @Deactivate
  public void stop(){
    System.out.println("STOPPED RULEENGINE!");
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " stopped.");
  }
}
