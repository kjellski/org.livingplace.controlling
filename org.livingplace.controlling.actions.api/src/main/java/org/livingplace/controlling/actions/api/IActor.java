package org.livingplace.controlling.actions.api;

import java.util.List;

public interface IActor {
  IActorQualifier getActorQualifer();
  List<IAction> getAllActions();
}
