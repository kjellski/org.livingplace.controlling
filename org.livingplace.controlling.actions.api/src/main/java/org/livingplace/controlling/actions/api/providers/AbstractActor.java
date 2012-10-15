package org.livingplace.controlling.actions.api.providers;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.api.IActorQualifier;

import java.util.ArrayList;
import java.util.List;

public class AbstractActor implements IActor {
  protected IActorQualifier qualifier;
  protected List<IAction> actions = new ArrayList<IAction>();

  protected AbstractActor(IActorQualifier qualifier) {
    this.qualifier = qualifier;
  }

  /*
     * this one should never ever be used.
     */
  @SuppressWarnings("unused")
  private AbstractActor() {
    throw new IllegalAccessError("A childclass of the " + AbstractActor.class.getName()
            + " was enforced to be instantiated in the wrong way.");
  }

  @Override
  public IActorQualifier getActorQualifer() {
    return qualifier;
  }

  @Override
  public List<IAction> getAllActions() {
    return actions;
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append("[" + this.qualifier.getFullQualifier() + "]: ");
    if (this.actions.size() > 0)
      for (IAction action : actions) {
        b.append(action.toString());
      }
    else
      b.append("  No actions for this IActor.");

    return b.toString();
  }
}
