package org.livingplace.controlling.actions.api;

/*
 * Change Listener interface to be used when listening on changes at the action result of an Action
 *
 */
public interface IActionStateChangedListener {
  /**
   * called when the state of an action has changed.
   */
  void actionStateChanged(IActionStatus.EActionState state);
}
