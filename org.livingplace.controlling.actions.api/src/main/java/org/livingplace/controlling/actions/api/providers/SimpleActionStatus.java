package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IActionResult;
import org.livingplace.controlling.actions.api.IActionStateChangedListener;
import org.livingplace.controlling.actions.api.IActionStatus;

import java.util.ArrayList;
import java.util.List;

public class SimpleActionStatus implements IActionStatus {

  // to be modified from within the actions act and other methods, not from
  // outside.
  protected EActionState state = EActionState.INITIALIZING;
  protected IActionResult result = new SimpleActionResult();
  protected String message = null;

  // list of listeners to be notified for state changes
  private List<IActionStateChangedListener> listeners = new ArrayList<IActionStateChangedListener>();

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
    b.append("[" + SimpleActionStatus.class.getName() + "]: (" + this.state.name() + ") Message: \"" + this.message + "\"");
    if (this.result.getResult() != null) {
      b.append("  [Result]: " + this.result.getResult().toString());
    }
    if (this.listeners.size() > 0) {
      b.append("  [IActionStateChangedListener]: ");
      for (IActionStateChangedListener l : this.listeners) {
        b.append(l.toString() + ", ");
      }
    }
    return b.toString();
  }
}
