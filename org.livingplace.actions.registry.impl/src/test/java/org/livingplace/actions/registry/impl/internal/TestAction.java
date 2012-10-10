package org.livingplace.actions.registry.impl.internal;

import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionResult;
import org.livingplace.actions.api.IActionStatus;
import org.livingplace.actions.api.providers.AbstractAction;
import org.livingplace.actions.api.providers.ActionQualifier;
import org.livingplace.actions.api.providers.SimpleActionStatus;
import org.livingplace.actions.api.providers.SimpleActionResult;

/**
 * This class is just for testing purposes and is unusable in production. It just shows how to implement
 * the concrete type.
 */
public class TestAction extends AbstractAction implements IAction {

  private String prefix;

  public TestAction(String prefix) {
    super(new ActionQualifier(prefix + "testnamespace", prefix + "testname", prefix + "testversion"));
    this.prefix = prefix;
  }

  @Override
  public void run() {

    IActionResult result = new SimpleActionResult();
    result.setResult("TestAction " + prefix + " ran!");

    IActionStatus status = new SimpleActionStatus();

    status.setActionResult(result);
    status.setActionState(IActionStatus.EActionState.SUCCESSED);

    setStatus(status);
  }
}

