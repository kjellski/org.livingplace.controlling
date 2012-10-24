package org.livingplace.controlling.knowledge.api.internal;


import org.apache.felix.scr.annotations.*;
import org.livingplace.controlling.knowledge.api.IRuleEngine;
import org.osgi.service.log.LogService;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;


@Component(immediate = true)
@Service
public class RuleEngine implements IRuleEngine {

  @Reference
  LogService log;

  DroolsManager engineManager;

  @Activate
  public void start(){
    init();
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " started.");
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
      engineManager = new DroolsManager();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Deactivate
  public void stop(){
    log.log(LogService.LOG_INFO, RuleEngine.class.getName() + " stopped.");
  }
}
