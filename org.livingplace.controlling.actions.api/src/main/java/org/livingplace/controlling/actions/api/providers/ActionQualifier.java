package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.actions.api.IQualifier;

/*
* This class is representing the actions identifier
*
* @author kjellski
*/
public class ActionQualifier extends Qualifier implements IActionQualifier {

  public ActionQualifier(IQualifier qualifier) {
    super(qualifier.getNamespace(), qualifier.getName(), qualifier.getVersion());
  }

  public ActionQualifier(String actionNamespaceName, String actionName, String actionVersion) {
    super(actionNamespaceName, actionName, actionVersion);
  }

  @Override
  public String getPrefix() {
    return PREFIX;
  }
}
