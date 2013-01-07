package org.livingplace.controlling.knowledge.api.internal;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionPseudoClock;
import org.osgi.service.log.LogService;

public class DroolsManager extends Thread {

  private final String knowledgeLoggerLogFilePath = "./log/org.drools.KnowledgeRuntimeLogger.log";
  /* This one is used for the agend, should point to a folder that will be asked for changes */
  private static final String RULES_CHANGESET = "org/livingplace/controlling/knowledge/changeset.xml";

  private static final int EXECUTION_INTERVAL = 500;

  private KnowledgeRuntimeLogger klogger;
  private StatefulKnowledgeSession ksession;
  private SessionPseudoClock clock;

  private WorkingMemoryEntryPoint entryPoint;

  private LogService log;

  public DroolsManager(LogService log) {
    this.log = log;

    // part of this configuration is lend from openhab! :)
    // Builder
    KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    kbuilder.add(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()), ResourceType.CHANGE_SET);

    if (kbuilder.hasErrors()) {
      log.log(LogService.LOG_ERROR, "There are errors in the rules: " + kbuilder.getErrors());
      return;
    }

    // Base
    KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

    // Agent
    KnowledgeAgentConfiguration aconf = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
    aconf.setProperty("drools.agent.newInstance", "false");
    aconf.setProperty("drools.agent.scanDirectories", "true");
    aconf.setProperty("drools.agent.scanResources", "true");
    aconf.setProperty("drools.agent.newInstance", "false");
    aconf.setProperty("drools.agent.useKBaseClassLoaderForCompiling", "true");

    KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("RuleAgent", kbase, aconf);
    kagent.applyChangeSet(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()));

    // Mixed
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
    ksession = kbase.newStatefulKnowledgeSession();

    // activate notifications
    ResourceFactory.getResourceChangeNotifierService().start();
    ResourceFactory.getResourceChangeScannerService().start();

    // activate this for extensive logging
    KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

    // set the scan interval to 20 secs
    ResourceChangeScannerConfiguration sconf = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
    sconf.setProperty("drools.resource.scanner.interval", "20");
    ResourceFactory.getResourceChangeScannerService().configure(sconf);

    // activate this for extensive logging
    KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

    klogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, knowledgeLoggerLogFilePath);

    // threading
    this.setDaemon(true);
    this.start();
  }
  private void shutdown() {
    log.log(LogService.LOG_INFO, "Shutting down the " + this.getClass().getName() + " ...");
    ResourceFactory.getResourceChangeNotifierService().stop();
    ResourceFactory.getResourceChangeScannerService().stop();

    this.klogger.close();
    this.ksession.halt();
    this.ksession.dispose();
    log.log(LogService.LOG_INFO, "... finished shutdown of " + this.getClass().getName() + ".");
  }

  public void reason() {
    ksession.fireAllRules();
  }

  /**
   * executes the resoning of the ruleengine all interval
   */
  public void run() {

    try {
      while (!Thread.currentThread().isInterrupted()) {
        Thread.sleep(EXECUTION_INTERVAL);
        this.reason();
      }
    } catch (InterruptedException e) {
      log.log(LogService.LOG_INFO, "Interrupted the " + this.getClass().getName() + ", action interrupted: " + e.getMessage()
              + ".");
    } finally {
      this.shutdown();
    }
  }
}
