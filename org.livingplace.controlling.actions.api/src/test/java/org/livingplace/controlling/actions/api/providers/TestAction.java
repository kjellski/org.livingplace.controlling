package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionResult;
import org.livingplace.controlling.actions.api.IActionStatus;
import org.livingplace.controlling.actions.api.providers.AbstractAction;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;
import org.livingplace.controlling.actions.api.providers.SimpleActionStatus;
import org.livingplace.controlling.actions.api.providers.SimpleActionResult;

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

    result.setResult(prefix + " TestAction ran!");

    IActionStatus status = new SimpleActionStatus();

    status.setActionResult(result);
    status.setActionState(IActionStatus.EActionState.SUCCESSED);

    setStatus(status);
  }
}
