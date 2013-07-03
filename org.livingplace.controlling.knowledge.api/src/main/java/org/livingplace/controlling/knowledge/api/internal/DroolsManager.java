package org.livingplace.controlling.knowledge.api.internal;

import org.apache.log4j.Logger;
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
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;

import java.util.concurrent.atomic.AtomicBoolean;

public class DroolsManager extends Thread {

    private static final Logger logger = Logger.getLogger(DroolsManager.class);

    /* This one is used for the agend, should point to a folder that will be asked for changes */
    private static final String RULES_CHANGESET = "org/livingplace/controlling/knowledge/changeset.xml";

    private static final int EXECUTION_INTERVAL = 500;

    private KnowledgeBase kbase;
    private KnowledgeAgent kagent;
    private StatefulKnowledgeSession ksession;

    private boolean newFact = false;
    public AtomicBoolean shutdown = new AtomicBoolean(true);

    public DroolsManager(IActionRegistry actionRegistry, ClassLoader classLoader) {

        this.kagent = getConfiguredKnowledgeAgent(classLoader);
        this.kagent.applyChangeSet(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()));
        if (this.kagent == null) throw new IllegalStateException("KnowledgeAgent was null.");

        this.kbase = this.kagent.getKnowledgeBase();
        if (this.kbase == null) throw new IllegalStateException("KnowledgeBase was null.");

        this.ksession = getConfiguredKnowledgeSession(this.kbase);
        if (this.ksession == null) throw new IllegalStateException("StatefulKnowledgeSession was null.");

        this.ksession.setGlobal("Actions", actionRegistry);

        ResourceFactory.getResourceChangeNotifierService().start();
        ResourceFactory.getResourceChangeScannerService().start();

        this.setDaemon(true);
        this.start();
        // we're not shut down any more.
        this.shutdown.set(false);
        logger.info("RuleEngine Deamon Thread started.");
    }

    /**
     * inserts a fact into the knowledge base
     *
     * @param o
     */
    public void addFact(Object o) {
        if (!this.shutdown.get()) {
            this.newFact = true;
            logger.debug("Add Fact: \n\t" + o.toString());
            this.ksession.insert(o);
        } else {
            logger.error("The KnowledgeBase was shut down, using it after that is illegal.");
        }
    }

    /**
     * method that gets the ruleengine evaluate on all known rules
     */
    public void reason() {
        if (!this.shutdown.get()) {
            if (this.newFact) {
                this.newFact = false;
                logger.debug("Reasoning with " + this.ksession.getFactCount() + " Facts.");
            }
            this.ksession.fireAllRules();
        } else {
            logger.error("The KnowledgeBase was shut down, using it after that is illegal.");
        }
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
            logger.warn("Interrupted the CEP Machine, action interrupted: " + e.getMessage() + ".");
        } catch (Exception e) {
            logger.error("An error occured in the reasoning cycle of the CEP Machine, printing Exception: " + e.toString(), e);
        } finally {
            this.shutdown();
        }
    }

    private void shutdown() {
        /**
         * From the JavaSE docs:
         *
         * public final boolean compareAndSet(boolean expect, boolean update)
         * Atomically set the value to the given updated value if the current value == the expected value.
         * Parameters:
         *     expect - the expected value
         *     update - the new value
         * Returns:
         *     true if successful. False return indicates that the actual value was not equal to the expected value.
         */
        if (!this.shutdown.compareAndSet(false, true)) {
            logger.info("Shutting down the " + this.getClass().getName() + " ...");
            ResourceFactory.getResourceChangeNotifierService().stop();
            ResourceFactory.getResourceChangeScannerService().stop();

            this.ksession.halt();
            this.ksession.dispose();
            logger.info("... finished shutdown of " + this.getClass().getName() + ".");
        }
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


    private KnowledgeAgentConfiguration getKnowledgeAgentConfiguration() {
        KnowledgeAgentConfiguration agentConfiguration = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();

        // we do not want to scan directories, just files
        agentConfiguration.setProperty("drools.agent.newInstance", "false");
        agentConfiguration.setProperty("drools.agent.scanDirectories", "true");
        agentConfiguration.setProperty("drools.agent.scanResources", "true");
        agentConfiguration.setProperty("drools.agent.useKBaseClassLoaderForCompiling", "true");

        return agentConfiguration;
    }

    private KnowledgeAgent getConfiguredKnowledgeAgent(ClassLoader classLoader) {
        int loadingRetry = 0;
        try {
            KnowledgeBuilderConfiguration kbconf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(null, classLoader);
            KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbconf);

            // Add the changeset in order to get the right classes loaded.
            knowledgeBuilder.add(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()), ResourceType.CHANGE_SET);

            if (knowledgeBuilder.hasErrors()) {
                String errors = "There are errors in the rules: " + knowledgeBuilder.getErrors();
//                System.out.println(errors);
                throw new IllegalRuleEvaluationException(errors);
            }

            KnowledgeBaseConfiguration kbaseconfig = getConfiguredKnowledgeBaseConfiguration(classLoader);

            KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseconfig);
            kbase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());

        } catch (IllegalRuleEvaluationException ruleEvalException) {
            // retry to evaluate the rules, just in case we've not yet loaded
            // all informations that are needed to create the correct classes
            // in bindings of the information or action registry
            // we'll retry for 5 minutes, and then stop
            if (loadingRetry < 30) {// 6 are a minute; 30 are 5 minutes
                logger.warn("There were errors in the rule evaluation. Retrying in 10s. (more info in DEBUG logging level)");
                logger.debug(ruleEvalException.getMessage());
                try {
                    Thread.sleep(10000L); // 10 seconds
                    loadingRetry++;
                } catch (InterruptedException e) {
                    // if interrupted, just throw the last really interesting error...
                    throw ruleEvalException;
                }
            } else {
                logger.error(ruleEvalException.getMessage());
            }
        }

        KnowledgeAgentConfiguration kagentConfiguration = getKnowledgeAgentConfiguration();
        return KnowledgeAgentFactory.newKnowledgeAgent("KnowledgeBaseAgent", kbase, kagentConfiguration);
    }

    private KnowledgeBaseConfiguration getConfiguredKnowledgeBaseConfiguration(ClassLoader classLoader) {
        KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration(null, classLoader);

        kbaseConfig.setOption(EventProcessingOption.STREAM);

        return kbaseConfig;
    }

}
