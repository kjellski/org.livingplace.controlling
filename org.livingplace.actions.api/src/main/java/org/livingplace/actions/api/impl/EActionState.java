package org.livingplace.actions.api.impl;

/*
 * enumeration to show be used for what the actual state of the 
 * action is
 * @author kjellski
 */
public enum EActionState {
	/* to be set when collecting informations for an action to be started*/
	INTIALIZING,
	/* to be set when an action is in any setup phase */
	STARTED,
	/* to be set when an action is actually in progress */
	PROCESSING,
	/* to be set and never changed when the action has finished in a successful state */
	SUCCESSED,
	/* to be set and never changed when the action has finished in a erroneous state */
	FAILED
}
