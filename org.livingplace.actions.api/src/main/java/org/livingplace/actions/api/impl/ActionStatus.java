package org.livingplace.actions.api.impl;

import org.livingplace.actions.api.IActionResult;
import org.livingplace.actions.api.IActionStateChangedListener;
import org.livingplace.actions.api.IActionStatus;

import java.util.ArrayList;
import java.util.List;

public class ActionStatus implements IActionStatus {

    // to be modified from within the actions act and other methods, not from
    // outside.
    protected EActionState state;
    protected IActionResult result;
    protected String message;

    // list of listeners to be notified for state changes
    private List<IActionStateChangedListener> listeners;

    public ActionStatus() {
        this.state = EActionState.INTIALIZING;
        this.listeners = new ArrayList<IActionStateChangedListener>();
    }

    @Override
    public EActionState getActionsState() {
        return state;
    }

    @Override
    public List<IActionStateChangedListener> getAllActionStateChangedListeners() {
        return this.listeners;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public IActionResult getActionResult() {
        return this.result;
    }

    @Override
    public void addActionStateChangedListener(IActionStateChangedListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void addAllActionStateChangedListener(List<IActionStateChangedListener> listeners) {
        this.listeners.addAll(listeners);
    }

    @Override
    public void setActionState(EActionState state) {
        this.state = state;
    }

    @Override
    public void setMessage(String msg) {
        this.message = msg;
    }

    @Override
    public void setActionResult(IActionResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[" + ActionStatus.class.getName() + "]: (" + this.state.name() + ")\" Message:" + this.message + "\"\n");
        if(this.result.getResult() != null)
        {
            b.append("  [Result]: " + this.result.getResult().toString());
        }
        if( this.listeners.size() > 0 )
        {
            b.append("  [IActionStateChangedListener]: ");
            for (IActionStateChangedListener l : this.listeners) {
                b.append(l.toString() + ", ");
            }
        }
        return b.toString();
    }
}
