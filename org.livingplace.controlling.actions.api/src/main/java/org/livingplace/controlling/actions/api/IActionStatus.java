package org.livingplace.controlling.actions.api;

import java.util.List;

/*
 * class to be used for retrieving the actions status and eventually the result.
 * @author kjellski
 */
public interface IActionStatus {

  enum EActionState {
    /* to be set when collecting informations for an action to be started*/
    INITIALIZING,
    /* to be set when an action is actually in progress */
    PROCESSING,
    /* to be set when an action was run(), CAUTION: HAS NO MEANING IN REGARDS OF SUCCESS OR FAILURE */
    FINISHED
  }

  EActionState getActionsState();

  void setActionState(EActionState state);

  void addActionStateChangedListener(IActionStateChangedListener listener);

  void addAllActionStateChangedListener(List<IActionStateChangedListener> listeners);

  List<IActionStateChangedListener> getAllActionStateChangedListeners();

  String getMessage();

  void setMessage(String msg);

  IActionResult getActionResult();

  void setActionResult(IActionResult result);

  abstract String toString();
}
