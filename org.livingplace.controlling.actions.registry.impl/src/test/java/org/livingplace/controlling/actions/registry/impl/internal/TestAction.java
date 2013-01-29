package org.livingplace.controlling.actions.registry.impl.internal;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionResult;
import org.livingplace.controlling.actions.api.providers.AbstractAction;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;
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
  public void execute() {
    IActionResult result = new SimpleActionResult();
    result.setResult("TestAction " + prefix + " ran!");

    this.status.setActionResult(result);
  }
}

