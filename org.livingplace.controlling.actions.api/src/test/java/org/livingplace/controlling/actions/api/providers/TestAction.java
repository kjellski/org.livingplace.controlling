package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionResult;

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
  public void execute() {
    IActionResult result = new SimpleActionResult();

    result.setResult(prefix + " TestAction ran!");

    this.status.setActionResult(result);
  }
}

