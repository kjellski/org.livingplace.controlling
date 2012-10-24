package org.livingplace.controlling.knowledge.api;


import org.apache.felix.scr.annotations.*;
import org.osgi.service.log.LogService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
@Service
public class RuleEngine implements IRuleEngine{

  @Reference
  LogService log;

  @Activate
  public void start(){
    log.log(LogService.LOG_DEBUG, "Starting the RuleEngine.");

    Properties defaults = new Properties();
    try {
      defaults.load(new FileInputStream("droolsengine.properties"));
    } catch (IOException e) {
      log.log(LogService.LOG_ERROR, "Couldn't read the default configuration for the RuleEngine.");
    }
    log.log(LogService.LOG_INFO, "Default configuration for the RuleEngine: " + defaults.toString());
  }

  @Deactivate
  public void stop(){
    log.log(LogService.LOG_DEBUG, "Stopping the RuleEngine.");
  }
}
