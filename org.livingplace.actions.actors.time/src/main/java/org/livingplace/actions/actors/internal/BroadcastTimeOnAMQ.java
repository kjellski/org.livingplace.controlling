package org.livingplace.actions.actors.internal;

import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.providers.AbstractAction;
import org.livingplace.actions.api.providers.ActionQualifier;

public class BroadcastTimeOnAMQ extends AbstractAction implements IAction {

  protected BroadcastTimeOnAMQ() {
    super(new ActionQualifier("time", "broadcastTime", "1.0"));
  }

  @Override
  public void run() {
    System.out.println("Time: " + System.currentTimeMillis());
  }
}
