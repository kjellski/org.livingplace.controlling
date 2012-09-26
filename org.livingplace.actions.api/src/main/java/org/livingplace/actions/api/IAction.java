package org.livingplace.actions.api;

import org.livingplace.actions.api.impl.ActionProperties;
import org.livingplace.actions.api.impl.ActionQualifier;

/*
* The general interface for all ations to be processed by actors.
* Also adapter interface for future actions to be implemented.
*
* @author kjellski
*/
public interface IAction extends Runnable{
    ActionQualifier getActionQualifier();

    void setActionProperties(ActionProperties operationProperties);

    ActionProperties getActionProperties();

    IActionStatus getStatus();

    void setStatus(IActionStatus status);

    void execute();
    
    String toString();
}
