package org.livingplace.actions.registry.impl.internal;

import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.internal.AbstractAction;
import org.livingplace.actions.api.internal.ActionQualifier;

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
    System.out.println(prefix + "TestAction ran!");
  }
}

