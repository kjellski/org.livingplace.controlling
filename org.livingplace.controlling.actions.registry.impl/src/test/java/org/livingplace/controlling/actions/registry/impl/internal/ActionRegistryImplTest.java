package org.livingplace.controlling.actions.registry.impl.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.IActionStatus;
import org.livingplace.controlling.actions.api.IActor;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;

import java.util.List;

public class ActionRegistryImplTest {
  IActor actor;
  IActionRegistry registry;

  @Before
  public void setUp() throws Exception {
    actor = new TestActor();
    registry = new ActionRegistryImpl();
  }

  @Test
  public void testExecuteActionWithActionProperties() throws Exception {

  }

  @Test
  public void testExecuteAction() throws Exception {
    IAction actionToExecute = actor.getAllActions().get(0);
    registry.register(actionToExecute);

    registry.executeAction(actionToExecute.getQualifier());

    Assert.assertTrue(actionToExecute.getStatus() != null);
    Assert.assertTrue(actionToExecute.getStatus().getActionResult() != null);

    while (actionToExecute.getStatus().getActionsState() != IActionStatus.EActionState.FINISHED){
      Thread.sleep(100);
    }

    Assert.assertNotNull(actionToExecute.getStatus().getActionResult().getResult());
  }

  @Test
  public void testRegisterAndUnregisterActions() throws Exception {
    List<IAction> registryActions = registry.getAllRegistered();
    List<IAction> actorsActions = actor.getAllActions();
    int registeredActions = registryActions.size();
    int actorActions = actorsActions.size();

    for (IAction actorsAction : actorsActions) {
      registry.register(actorsAction);
    }
    Assert.assertEquals(registeredActions + actorActions, registry.getAllRegistered().size());

    for (IAction actorsAction : actorsActions) {
      registry.unregister(actorsAction);
    }

    Assert.assertEquals(registeredActions, registry.getAllRegistered().size());
  }

  @Test
  public void testGetAllRegisteredActions() throws Exception {
    List<IAction> registryActions = registry.getAllRegistered();
    List<IAction> actorsActions = actor.getAllActions();
    int registeredActions = registryActions.size();
    int actorActions = actorsActions.size();

    for (IAction actorsAction : actorsActions) {
      registry.register(actorsAction);
    }

    Assert.assertTrue(registry.getAllRegistered().size() > 0);

    if (registeredActions > 0) {
      for (IAction iAction : registry.getAllRegistered()) {
        Assert.assertTrue(iAction != null);
        Assert.assertTrue(iAction.getQualifier() != null);
        actorsActions.contains(iAction);
      }
    }

    for (IAction actorsAction : actorsActions) {
      registry.unregister(actorsAction);
    }
  }
}