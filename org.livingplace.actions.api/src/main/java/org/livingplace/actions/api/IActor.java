package org.livingplace.actions.api;

import java.util.List;

public interface IActor {
  IActorQualifier getActorQualifer();
  List<IAction> getAllActions();
}
