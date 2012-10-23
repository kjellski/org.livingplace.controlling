package org.livingplace.controlling.knowledge.api.old.interfaces;

import java.lang.reflect.Type;

/**
 * This interface has to be implemented in a class that should be available 
 * for reasoning.
 * @author kjellski
 */
public interface FactReceiver {
    /**
     * Creates the instance to listen from.
     * @return
     */
    FactReceiver newInstance();

    /**
     * Type of the objects inserted into the KnowledgeBase
     * @return
     */
    Type getType();
    
    /** 
     * Returns an instance of the fact that should be
     * inserted into the KnowledgeBase. 
     * 
     *  @warning: This Method needs to be blocking.
     * @return
     */
    Object receiveFact();   
}