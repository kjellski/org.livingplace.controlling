package org.livingplace.controlling.actions.api;

import org.livingplace.controlling.api.IQualifiable;

import java.util.List;

public interface IActor extends IQualifiable {
  IActorQualifier getQualifier();
  List<IAction> getAllActions();
}
