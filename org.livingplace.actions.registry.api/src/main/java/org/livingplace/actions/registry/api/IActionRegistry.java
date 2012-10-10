package org.livingplace.actions.registry.api;

import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionProperties;
import org.livingplace.actions.api.IActionQualifier;

import java.util.List;

public interface IActionRegistry {
  void registerAction(IAction action);

  void unregisterAction(IAction action);

  void executeAction(IActionQualifier actionQualifier, IActionProperties actionProperties);

  void executeAction(IActionQualifier actionQualifier);

  List<IAction> getAllRegisteredActions();

  IAction getAction(IActionQualifier actionQualifier);
//
//  void registerActor(IActor actor);
//
//  void unregisterActor(IActor actor);
// 
//  void executeActionOnActor(IActorQualifier actorQualifier, IActionQualifier actionQualifier, IActionProperties properties);
//
//  void executeActionOnActor(IActorQualifier actorQualifier, IActionQualifier actionQualifier);
//
//  List<IActor> getAllRegisteredActors();
}
