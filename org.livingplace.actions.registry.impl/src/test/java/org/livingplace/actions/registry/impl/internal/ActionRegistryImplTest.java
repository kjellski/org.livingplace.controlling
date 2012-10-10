package org.livingplace.actions.registry.impl.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionStatus;
import org.livingplace.actions.api.IActor;
import org.livingplace.actions.registry.api.IActionRegistry;
import org.mockito.Mockito;
import org.osgi.service.log.LogService;

import java.util.List;

public class ActionRegistryImplTest {
  IActor actor;
  IActionRegistry registry;

  @Before
  public void setUp() throws Exception {
    actor = new TestActor();
    registry = new ActionRegistryImpl();
    ((ActionRegistryImpl) registry).setLog(Mockito.mock(LogService.class));
  }

  @Test
  public void testExecuteActionWithActionProperties() throws Exception {

  }

  @Test
  public void testExecuteAction() throws Exception {
    IAction actionToExecute = actor.getAllActions().get(0);
    registry.registerAction(actionToExecute);

    registry.executeAction(actionToExecute.getActionQualifier());

    Assert.assertTrue(actionToExecute.getStatus() != null);
    Assert.assertTrue(actionToExecute.getStatus().getActionResult() != null);

    while (actionToExecute.getStatus().getActionsState() == IActionStatus.EActionState.PROCESSING ||
            actionToExecute.getStatus().getActionsState() == IActionStatus.EActionState.INITIALIZING){
      Thread.sleep(100);
    }

    Assert.assertNotNull(actionToExecute.getStatus().getActionResult().getResult());
  }

  @Test
  public void testRegisterAndUnregisterActions() throws Exception {
    List<IAction> registryActions = registry.getAllRegisteredActions();
    List<IAction> actorsActions = actor.getAllActions();
    int registeredActions = registryActions.size();
    int actorActions = actorsActions.size();

    for (IAction actorsAction : actorsActions) {
      registry.registerAction(actorsAction);
    }
    Assert.assertEquals(registeredActions + actorActions, registry.getAllRegisteredActions().size());

    for (IAction actorsAction : actorsActions) {
      registry.unregisterAction(actorsAction);
    }

    Assert.assertEquals(registeredActions, registry.getAllRegisteredActions().size());
  }

  @Test
  public void testGetAllRegisteredActions() throws Exception {
    List<IAction> registryActions = registry.getAllRegisteredActions();
    List<IAction> actorsActions = actor.getAllActions();
    int registeredActions = registryActions.size();
    int actorActions = actorsActions.size();

    for (IAction actorsAction : actorsActions) {
      registry.registerAction(actorsAction);
    }

    Assert.assertTrue(registry.getAllRegisteredActions().size() > 0);

    if (registeredActions > 0) {
      for (IAction iAction : registry.getAllRegisteredActions()) {
        Assert.assertTrue(iAction != null);
        Assert.assertTrue(iAction.getActionQualifier() != null);
        actorsActions.contains(iAction);
      }
    }

    for (IAction actorsAction : actorsActions) {
      registry.unregisterAction(actorsAction);
    }
  }
}

//  @Test
//  public void testRegisterActor() throws Exception {
//    int actionCount = registry.getAllRegisteredActions().size();
//    registry.registerActor(actor);
//    Assert.assertEquals(actionCount + TestActor.ACTION_COUNT, registry.getAllRegisteredActions().size());
//  }

//  @Test
//  public void testUnregisterActor() throws Exception {
//    int actionCount = registry.getAllRegisteredActions().size();
//    registry.registerActor(actor);
//    Assert.assertEquals(actionCount + TestActor.ACTION_COUNT, registry.getAllRegisteredActions().size());
//    registry.unregisterActor(actor);
//    Assert.assertEquals(actionCount, registry.getAllRegisteredActions().size());
//  }
//
//  @Test
//  public void testExecuteActionOnActorWithActionProperties() throws Exception {
//
//  }
//
//  @Test
//  public void testExecuteActionOnActor() throws Exception {
//
//  }
//
//  @Test
//  public void testGetAllRegisteredActors() throws Exception {
//
//  }