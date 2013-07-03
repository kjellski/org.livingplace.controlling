package org.livingplace.controlling.knowledge.api.internal;

import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;

public class FactInsertionListener implements IInformationListener {

    private static final Logger logger = Logger.getLogger(FactInsertionListener.class);

    private final DroolsManager droolsManager;

    FactInsertionListener(DroolsManager droolsManager) {
        this.droolsManager = droolsManager;
    }

    @Override
    public void sensedInformation(IInformation information) {

        if (logger.isDebugEnabled()) {
            logger.debug(information.toString());
        }
        try {
            if (!droolsManager.shutdown.get())
                droolsManager.addFact(information);
        } catch (Exception e) {
            logger.error("An Information was sensed and an exception occured while "
                    + "inserting it into the KnowledgeBase.", e);
        }
    }
}
