package org.livingplace.controlling.actions.actors.time.internal;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.providers.AbstractAction;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;

public class BroadcastTimeOnAMQ extends AbstractAction implements IAction {

  public BroadcastTimeOnAMQ() {
    super(new ActionQualifier("time", "broadcastTime", "1.0"));
  }

  @Override
  public void execute() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    System.out.println(this.getQualifier() + " - Time: " + System.currentTimeMillis());
  }
}
