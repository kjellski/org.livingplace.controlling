package org.livingplace.controlling.actions.api;

import org.livingplace.controlling.api.IQualifiable;

/**
 * The general interface for all ations to be provided by actors.
 * Also adapter interface for future actions to be implemented.
 */
public interface IAction extends IQualifiable, Runnable {
  @Override
  IActionQualifier getQualifier();

  /**
   * sets the properties for this action. available in the action itself
   * @param operationProperties
   */
  void setActionProperties(IActionProperties operationProperties);

  /**
   * gets the properties for the action
   * @return
   */
  IActionProperties getActionProperties();

  /**
   * gets the actual status of the operation
   * @return
   */
  IActionStatus getStatus();

  /**
   * executes the action in the registrys executor poolw
   */
  void execute();
}
