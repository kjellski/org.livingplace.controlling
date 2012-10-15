package org.livingplace.controlling.actions.registry.api;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionProperties;
import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.api.IRegistry;

public interface IActionRegistry extends IRegistry<IAction> {

  void executeAction(IActionQualifier actionQualifier, IActionProperties actionProperties);

  void executeAction(IActionQualifier actionQualifier);
}
