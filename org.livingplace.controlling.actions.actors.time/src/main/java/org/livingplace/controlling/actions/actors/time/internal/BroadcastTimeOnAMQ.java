package org.livingplace.controlling.actions.actors.time.internal;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.providers.AbstractAction;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;

public class BroadcastTimeOnAMQ extends AbstractAction implements IAction {

  public BroadcastTimeOnAMQ() {
    super(new ActionQualifier("time", "broadcastTime", "1.0"));
  }

  @Override
  public void run() {
    System.out.println("Time: " + System.currentTimeMillis());
  }
}