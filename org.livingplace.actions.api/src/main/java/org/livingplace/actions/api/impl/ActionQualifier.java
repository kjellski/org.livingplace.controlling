package org.livingplace.actions.api.impl;

import org.livingplace.actions.api.IActionQualifier;

/*
* This class is representing the actions identifier
*
* @author kjellski
*/
public class ActionQualifier implements IActionQualifier {
    private final String actionName;
    private final String namespaceName;

    public ActionQualifier(String actionName, String namespaceName) {
        if(actionName == null || namespaceName == null)
            throw new IllegalArgumentException("An ActionQualifier may never be initialized with empty qualification strings.");

        this.actionName = actionName;
        this.namespaceName = namespaceName;
    }

    @Override
    public String getFullQualifier() {
        return toString();
    }

    @Override
    public String getActionName() {
        return actionName;
    }

    @Override
    public String getActionNamespace() {
        return namespaceName;
    }

    @Override
    public String toString() {
        return this.getActionNamespace() + "." + this.getActionName();
    }

    /*
      * private to ensure that the qualifier couldn't be instantiated useless
      */
    @SuppressWarnings("unused")
    private ActionQualifier() {
        this("undefined", "undefined");
    }
}
