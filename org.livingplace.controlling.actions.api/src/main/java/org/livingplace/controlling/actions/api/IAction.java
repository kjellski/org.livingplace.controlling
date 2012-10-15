package org.livingplace.controlling.actions.api;

import org.livingplace.controlling.api.IQualifiable;

/*
* The general interface for all ations to be processed by actors.
* Also adapter interface for future actions to be implemented.
*
* @author kjellski
*/
public interface IAction extends IQualifiable, Runnable{
    IActionQualifier getQualifier();

    void setActionProperties(IActionProperties operationProperties);

    IActionProperties getActionProperties();

    IActionStatus getStatus();

    void setStatus(IActionStatus status);

    void execute();
}
