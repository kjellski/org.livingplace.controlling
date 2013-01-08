package org.livingplace.controlling.knowledge.api.internal;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.osgi.service.log.LogService;

public class DroolsManager extends Thread {

  private final String knowledgeLoggerLogFilePath = "./log/org.drools.KnowledgeRuntimeLogger.log";
  /* This one is used for the agend, should point to a folder that will be asked for changes */
  private static final String RULES_CHANGESET = "org/livingplace/controlling/knowledge/changeset.xml";

  private static final int EXECUTION_INTERVAL = 500;

  private KnowledgeRuntimeLogger klogger;
  private KnowledgeBuilder kbuilder;
  private KnowledgeBase kbase;
  private KnowledgeAgent kagent;
  private StatefulKnowledgeSession ksession;

//  private WorkingMemoryEntryPoint entryPoint;

  private LogService log;
  private boolean consolePrinting = true;
  private boolean newFact;

  public DroolsManager(LogService log, IActionRegistry actionRegistry, ClassLoader classLoader) {
    this.log = log;

    this.kbuilder = getConfiguredKnowledgeBuilder(classLoader);
    if (this.kbuilder == null) throw new IllegalStateException("KnowledgeBuilder was null.");

    this.kagent = getConfiguredKnowledgeAgent(this.kbuilder, classLoader);
    this.kagent.applyChangeSet(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()));
    if (this.kagent == null) throw new IllegalStateException("KnowledgeAgent was null.");

    this.kbase = this.kagent.getKnowledgeBase();
    this.kbase.addKnowledgePackages(this.kbuilder.getKnowledgePackages());
    if (this.kbase == null) throw new IllegalStateException("KnowledgeBase was null.");

    this.ksession = getConfiguredKnowledgeSession(this.kbase);
    if (this.ksession == null) throw new IllegalStateException("StatefulKnowledgeSession was null.");

    this.ksession.setGlobal("action", actionRegistry);

//    this.entryPoint = this.ksession.getWorkingMemoryEntryPoint("entryone");
//    if (this.entryPoint == null ) throw new IllegalStateException("WorkingMemoryEntryPoint was null.");

    this.klogger = KnowledgeRuntimeLoggerFactory.newFileLogger(this.ksession, this.knowledgeLoggerLogFilePath);
    if (this.klogger == null) throw new IllegalStateException("KnowledgeRuntimeLogger was null.");

    ResourceFactory.getResourceChangeNotifierService().start();
    ResourceFactory.getResourceChangeScannerService().start();

    this.setDaemon(true);
    this.start();
  }

  public void addFact(Object o) {
    this.newFact = true;
    log(LogService.LOG_DEBUG, "Add Fact: \n\t" + o.toString());
    this.ksession.insert(o);
  }

  public void reason() {
    if (this.newFact) {
      this.newFact = false;
      log(LogService.LOG_DEBUG, "Reasoning with " + this.ksession.getFactCount() + " Facts.");
    }
    this.ksession.fireAllRules();
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

  private StatefulKnowledgeSession getConfiguredKnowledgeSession(KnowledgeBase kbase) {
    KnowledgeSessionConfiguration ksessionConfig = getConfiguredKnowledgeSessionConfiguration();
    StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(ksessionConfig, null);

    return ksession;
  }

  private KnowledgeSessionConfiguration getConfiguredKnowledgeSessionConfiguration() {
    KnowledgeSessionConfiguration ksessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();

    ksessionConfig.setOption(ClockTypeOption.get("realtime"));

    return ksessionConfig;
  }

  private KnowledgeBaseConfiguration getConfiguredKnowledgeBaseConfiguration(ClassLoader classLoader) {
    KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, classLoader);

    kbaseConfig.setOption(EventProcessingOption.STREAM);

    return kbaseConfig;
  }

  /**
   * Using the custom Classlaoder in order to get the right classes from registries loaded.
   *
   * @param classLoader
   * @return
   */
  private KnowledgeBuilder getConfiguredKnowledgeBuilder(ClassLoader classLoader) {
    KnowledgeBuilderConfiguration kbconf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(null, classLoader);
    KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbconf);

    // Add the changeset in order to get the right classes loaded.
    knowledgeBuilder.add(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()), ResourceType.CHANGE_SET);

    if (knowledgeBuilder.hasErrors()) {
      String errors = "There are errors in the rules: " + knowledgeBuilder.getErrors();
      System.out.println(errors);
      log.log(LogService.LOG_ERROR, errors);
      throw new IllegalRuleEvaluationException(errors);
    }

    return knowledgeBuilder;
  }

  private KnowledgeAgentConfiguration getKnowledgeAgentConfiguration() {
    KnowledgeAgentConfiguration agentConfiguration = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();

    // we do not want to scan directories, just files
    agentConfiguration.setProperty("drools.agent.newInstance", "false");
    agentConfiguration.setProperty("drools.agent.scanDirectories", "true");
    agentConfiguration.setProperty("drools.agent.scanResources", "true");
    agentConfiguration.setProperty("drools.agent.newInstance", "false");
    agentConfiguration.setProperty("drools.agent.useKBaseClassLoaderForCompiling", "true");

    return agentConfiguration;
  }

  private KnowledgeAgent getConfiguredKnowledgeAgent(KnowledgeBuilder kbuilder, ClassLoader classLoader) {
    KnowledgeBaseConfiguration kbaseconfig = getConfiguredKnowledgeBaseConfiguration(classLoader);

    KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseconfig);
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

    KnowledgeAgentConfiguration kagentConfiguration = getKnowledgeAgentConfiguration();

    return KnowledgeAgentFactory.newKnowledgeAgent("KnowledgeBaseAgent", kbase, kagentConfiguration);
  }

  private void log(int LogLevel, String msg) {
    this.log.log(LogLevel, msg);
    if (this.consolePrinting)
      System.out.println(msg);
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
