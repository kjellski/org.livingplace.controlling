package org.livingplace.controlling.actions.api;

import org.livingplace.controlling.api.IQualifiable;

import java.util.List;

/**
 * The action is used to bundle actions into one context and make them available to the IActionRegistry.
 */
public interface IActor extends IQualifiable {
  @Override
  IActorQualifier getQualifier();

  /**
   * Returns a list of all actions the IActor is capable of.
   *
   * @return actions provided by the actor
   */
  List<IAction> getAllActions();
}
