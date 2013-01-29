package org.livingplace.controlling.actions.registry.api;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionProperties;
import org.livingplace.controlling.actions.api.IActionQualifier;
import org.livingplace.controlling.api.IRegistry;

/**
 * This registry interface is used to define that action registry which will independently execute the actions
 * from rules and other bundles.
 */
public interface IActionRegistry extends IRegistry<IAction> {
  /**
   * Executes the Action given by Identifier with given properties
   * @param actionQualifier Qualifier to find the action
   * @param actionProperties Properties to be used for the execution of the action.
   */
  void executeAction(IActionQualifier actionQualifier, IActionProperties actionProperties);

  /**
   * Executes the Action given by Identifier
   * @param actionQualifier Qualifier to find the action
   */
  void executeAction(IActionQualifier actionQualifier);

  /**
   * Executes the given action instance with this registry.
   * @param action
   */
  void executeAction(IAction action);

  /**
   * Executes the Action given by it's fullQualifier, therefor searches it in the registry first.
   * @param fullQualifier full action qualifier given by IActionQualifier.getFullQualifier() in the form:
   *                      "ACTION:time.broadcastTime:1.0"
   */
  void executeAction(String fullQualifier);

  /**
   * Executes the Action given by it's fullQualifier, therefor searches it in the registry first. Allows Parameter
   * usage.
   * @param fullQualifier
   * @param actionProperties
   */
  void executeAction(String fullQualifier, IActionProperties actionProperties);


}
