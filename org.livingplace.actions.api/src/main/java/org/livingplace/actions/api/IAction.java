package org.livingplace.actions.api;

/*
* The general interface for all ations to be processed by actors.
* Also adapter interface for future actions to be implemented.
*
* @author kjellski
*/
public interface IAction extends Runnable{
    IActionQualifier getActionQualifier();

    void setActionProperties(IActionProperties operationProperties);

    IActionProperties getActionProperties();

    IActionStatus getStatus();

    void setStatus(IActionStatus status);

    void execute();
    
    String toString();
}
