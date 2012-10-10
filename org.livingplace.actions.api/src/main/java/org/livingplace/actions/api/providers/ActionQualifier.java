package org.livingplace.actions.api.providers;

import org.livingplace.actions.api.IActionQualifier;

/*
* This class is representing the actions identifier
*
* @author kjellski
*/
public class ActionQualifier extends Qualifier implements IActionQualifier {

  public ActionQualifier(String actionNamespaceName, String actionName, String actionVersion) {
    super(actionNamespaceName, actionName, actionVersion);
  }

  @Override
  public String getFullQualifier() {
    return PREFIX + ":" + super.getNamespace() + "." + super.getName() + ":" + super.getVersion();
  }

  private static final String PREFIX = "ACTION";

  @Override
  public String getPrefix() {
    return PREFIX;
  }
}
