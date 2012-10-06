package org.livingplace.actions.api.internal;

import org.livingplace.actions.api.*;

import java.util.ArrayList;
import java.util.List;

public class AbstractActor implements IActor {
  protected IActorQualifier qualifier;
  protected List<IAction> actions = new ArrayList<IAction>();

  protected AbstractActor(IActorQualifier qualifier){
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
}
