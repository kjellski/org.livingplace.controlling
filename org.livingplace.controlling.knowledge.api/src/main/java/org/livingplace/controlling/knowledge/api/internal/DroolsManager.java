package org.livingplace.controlling.knowledge.api.internal;

import org.apache.felix.scr.annotations.Reference;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.*;
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
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DroolsManager extends Thread {

  private final String knowledgeLoggerLogFilePath = "./log/org.drools.KnowledgeRuntimeLogger.log";

  private KnowledgeRuntimeLogger klogger;
  private KnowledgeBuilder kbuilder;
  private KnowledgeBase kbase;
  private KnowledgeAgent kagent;
  private KnowledgeAgentConfiguration kagentConfiguration;
  private StatefulKnowledgeSession ksession;
  private SessionPseudoClock clock;

  private WorkingMemoryEntryPoint entryPoint;

  @Reference
  LogService log;

  public DroolsManager() throws MalformedURLException {
    this(null);
  }

  public DroolsManager(List<SimpleEntry<String, ResourceType>> resources) throws MalformedURLException {
    ResourceFactory.getResourceChangeNotifierService().start();
    ResourceFactory.getResourceChangeScannerService().start();

    this.kbuilder = getConfiguredKnowledgeBuilder(resources);
    this.kbase = getConfiguredKnowledgeBase(kbuilder);
    this.ksession = getConfiguredKnowledgeSession(kbase);

    this.klogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, knowledgeLoggerLogFilePath);
  }

  public void getClockTime() {
    Date d = new Date(this.clock.getCurrentTime());
  }

  public void advance(long arg0, TimeUnit arg1) {
    this.clock.advanceTime(arg0, arg1);
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

  private KnowledgeBaseConfiguration getConfiguredKnowledgeBaseConfiguration() {
    KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();

    kbaseConfig.setOption(EventProcessingOption.STREAM);

    return kbaseConfig;
  }

  private KnowledgeBuilder getConfiguredKnowledgeBuilder(List<SimpleEntry<String, ResourceType>> toBeLoaded) {
    KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

    if (toBeLoaded != null)
      addKnowledgeToKnowledgeBaseBuilder(toBeLoaded, kbuilder);

    return kbuilder;
  }

  public void addKnowledgeToKnowledgeBaseBuilder(List<SimpleEntry<String, ResourceType>> toBeLoaded) {
    addKnowledgeToKnowledgeBaseBuilder(toBeLoaded, this.kbuilder);
  }

  private void addKnowledgeToKnowledgeBaseBuilder(List<SimpleEntry<String, ResourceType>> toBeLoaded,
                                                  KnowledgeBuilder kbuilder) {

    for (SimpleEntry<String, ResourceType> resourceToLoad : toBeLoaded) {
      kbuilder.add(ResourceFactory.newClassPathResource(resourceToLoad.getKey()), resourceToLoad.getValue());
    }

    KnowledgeBuilderErrors errors = kbuilder.getErrors();
    if (errors.size() > 0) {
      for (KnowledgeBuilderError error : errors) {
        System.err.println(error);
      }
      log.log(LogService.LOG_ERROR, "Could not parse knowledge.");
      throw new IllegalArgumentException("Could not parse knowledge.");
    }
  }

  private KnowledgeBase getConfiguredKnowledgeBase(KnowledgeBuilder kbuilder) throws MalformedURLException {
    KnowledgeBaseConfiguration kbaseconfig = getConfiguredKnowledgeBaseConfiguration();

    KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseconfig);
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

    this.kagentConfiguration = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();

    // we do not want to scan directories, just files
    this.kagentConfiguration.setProperty("drools.agent.scanDirectories", "true");
    this.kagentConfiguration.setProperty("drools.agent.scanResources", "true");
    this.kagentConfiguration.setProperty("drools.agent.newInstance", "false");
    this.kagentConfiguration.setProperty("drools.agent.useKBaseClassLoaderForCompiling", "true");

    // the name of the agent
    this.kagent = KnowledgeAgentFactory.newKnowledgeAgent("KnowledgeBaseAgent", this.kagentConfiguration);

    String path = "config/change-set.xml";
    URL url = new URL("file:" + path);

    UrlResource urlResource = (UrlResource) ResourceFactory.newUrlResource(url);

//    this.kagent.applyChangeSet(urlResource);

    return this.kagent.getKnowledgeBase();
  }

  public void run() {
    // StopWatch s = new StopWatch();
    try {
      while (!Thread.currentThread().isInterrupted()) {
        Thread.sleep(1000);
        //
        // s.start();
        // this.reason();
        // s.stop();
        // logger.info("fireAllRules() took " + s.toString());
        // s.reset();
        // this.getClockTime();
      }
    } catch (InterruptedException e) {
      log.log(LogService.LOG_INFO, "Interrupted the " + this.getClass().getName() + ", action interrupted: " + e.getMessage()
              + ".");
    } finally {
      this.shutdown();
    }
  }
}
