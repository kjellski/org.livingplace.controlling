package org.livingplace.actions.api.providers;

import org.livingplace.actions.api.IActionResult;

public class SimpleActionResult implements IActionResult {
  private Object result = null;

  @Override
  public Object getResult() {
    return result;
  }

  @Override
  public void setResult(Object result) {
    this.result = result;
  }
}
