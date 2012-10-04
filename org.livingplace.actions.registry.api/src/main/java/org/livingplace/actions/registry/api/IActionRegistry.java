package org.livingplace.actions.registry.api;

import org.livingplace.actions.api.*;

import java.util.List;

public interface IActionRegistry {
  void registerActions(IAction action);

  void unregisterActions(IAction action);

  void executeAction(IActionQualifier actionQualifier, IActionProperties actionProperties);

  void executeAction(IActionQualifier actionQualifier);

  List<IAction> getAllRegisteredActions();


  void registerActor(IActor actor);

  void unregisterActor(IActor actor);

  void executeActionOnActor(IActorQualifier actorQualifier, IActionQualifier actionQualifier, IActionProperties properties);

  void executeActionOnActor(IActorQualifier actorQualifier, IActionQualifier actionQualifier);

  List<IActor> getAllRegisteredActors();
}
