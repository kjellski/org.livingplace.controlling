package org.livingplace.actions.registry.api;

import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionProperties;
import org.livingplace.actions.api.IActionQualifier;

import java.util.List;

public interface IActionRegistry {
    void registerActions(IAction action);

    void unregisterActions(IAction action);

    void executeAction(IActionQualifier qualifier, IActionProperties properties);
    
    List<IAction> getAllRegisteredActions();
}
