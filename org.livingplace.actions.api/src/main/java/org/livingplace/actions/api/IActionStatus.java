package org.livingplace.actions.api;

import org.livingplace.actions.api.impl.EActionState;

import java.util.List;

/*
 * class to be used for retrieving the actions status and eventually the result.
 * @author kjellski
 */
public interface IActionStatus {
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
