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
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.UrlResource;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionPseudoClock;
import org.osgi.service.log.LogService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DroolsManager extends Thread {

  private final String knowledgeLoggerLogFilePath = "./log/org.drools.KnowledgeRuntimeLogger.log";
  /* This one is used for the agend, should point to a folder that will be asked for changes */
  private static final String RULES_CHANGESET = "org/livingplace/controlling/knowledge/changeset.xml";

  private static final int EXECUTION_INTERVAL = 500;

  private KnowledgeRuntimeLogger klogger;
  private KnowledgeBuilder kbuilder;
  private KnowledgeBase kbase;
  private KnowledgeAgent kagent;
  private KnowledgeAgentConfiguration kagentConfiguration;
  private StatefulKnowledgeSession ksession;
  private SessionPseudoClock clock;

  private WorkingMemoryEntryPoint entryPoint;

  private LogService log;

  public DroolsManager(LogService log, ClassLoader classLoader) {
    this.log = log;

    ResourceFactory.getResourceChangeNotifierService().start();
    ResourceFactory.getResourceChangeScannerService().start();

    kbuilder = getConfiguredKnowledgeBuilder(classLoader);
    kbase = getConfiguredKnowledgeBase(kbuilder, classLoader);
    ksession = getConfiguredKnowledgeSession(kbase);

    klogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, knowledgeLoggerLogFilePath);
  }


  public Date getClockTime() {
    Date d = new Date(clock.getCurrentTime());
    return d;
  }

  public void advance(long arg0, TimeUnit arg1) {
    clock.advanceTime(arg0, arg1);
  }

  public void addFact(Object o) {
    this.entryPoint.insert(o);
  }

  public void reason() {
    ksession.fireAllRules();
  }

  public void cancel() {
    interrupt();
  }

  public Collection<KnowledgePackage> getKnowledgePackages() {
    return this.kbase.getKnowledgePackages();
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

    this.entryPoint = ksession.getWorkingMemoryEntryPoint("entryone");
    this.clock = ksession.getSessionClock();

    return ksession;
  }

  private KnowledgeSessionConfiguration getConfiguredKnowledgeSessionConfiguration() {
    KnowledgeSessionConfiguration ksessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
    ksessionConfig.setOption(ClockTypeOption.get("pseudo"));

    return ksessionConfig;
  }

  private KnowledgeBaseConfiguration getConfiguredKnowledgeBaseConfiguration(ClassLoader classLoader) {
    KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, classLoader);

    kbaseConfig.setOption(EventProcessingOption.STREAM);

    return kbaseConfig;
  }

  /**
   * Using the custom Classlaoder in order to get the right classes from registries loaded.
   * @param classLoader
   * @return
   */
  private KnowledgeBuilder getConfiguredKnowledgeBuilder(ClassLoader classLoader) {
    KnowledgeBuilderConfiguration kbconf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(null, classLoader);
    KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbconf);

    // Add the changeset in order to get the right classes loaded.
    kbuilder.add(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()), ResourceType.CHANGE_SET);

    if (kbuilder.hasErrors()) {
      String errors = "There are errors in the rules: " + kbuilder.getErrors();
      System.out.println(errors);
      log.log(LogService.LOG_ERROR, errors);
      return null;
    }

    return kbuilder;
  }

  private KnowledgeBase getConfiguredKnowledgeBase(KnowledgeBuilder kbuilder, ClassLoader classLoader) {
    KnowledgeBaseConfiguration kbaseconfig = getConfiguredKnowledgeBaseConfiguration(classLoader);

    KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseconfig);
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

    this.kagentConfiguration = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();

    // we do not want to scan directories, just files
    this.kagentConfiguration.setProperty("drools.agent.newInstance", "false");
    this.kagentConfiguration.setProperty("drools.agent.scanDirectories", "true");
    this.kagentConfiguration.setProperty("drools.agent.scanResources", "true");
    this.kagentConfiguration.setProperty("drools.agent.newInstance", "false");
    this.kagentConfiguration.setProperty("drools.agent.useKBaseClassLoaderForCompiling", "true");

    // the name of the agent
    this.kagent = KnowledgeAgentFactory.newKnowledgeAgent("KnowledgeBaseAgent", this.kagentConfiguration);

    String path = "config/change-set.xml";

    URL url = null;
    try {
      url = new URL("file:" + path);
    } catch (MalformedURLException e) {
      System.out.println("[ERROR]: The URL for this path was malformed: " + path);
      e.printStackTrace(System.out);
      return null;
    }

    UrlResource urlResource = (UrlResource) ResourceFactory.newUrlResource(url);

//    this.kagent.applyChangeSet(urlResource);

    return this.kagent.getKnowledgeBase();
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
